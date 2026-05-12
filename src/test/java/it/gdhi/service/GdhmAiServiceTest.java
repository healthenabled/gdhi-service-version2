package it.gdhi.service;

import it.gdhi.config.BedrockAgentProperties;
import it.gdhi.dto.AIRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockagentruntime.model.DependencyFailedException;
import software.amazon.awssdk.services.bedrockagentruntime.model.ApiInvocationInput;
import software.amazon.awssdk.services.bedrockagentruntime.model.ApiParameter;
import software.amazon.awssdk.services.bedrockagentruntime.model.ApiResult;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvocationInputMember;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvocationResultMember;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvokeAgentRequest;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvokeAgentResponse;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvokeAgentResponseHandler;
import software.amazon.awssdk.services.bedrockagentruntime.model.PayloadPart;
import software.amazon.awssdk.services.bedrockagentruntime.model.ResponseStream;
import software.amazon.awssdk.services.bedrockagentruntime.model.ReturnControlPayload;
import software.amazon.awssdk.services.bedrockagentruntime.model.SessionState;
import software.amazon.awssdk.services.bedrockagentruntime.model.Trace;
import software.amazon.awssdk.services.bedrockagentruntime.model.TracePart;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.async.SdkPublisher;
import org.reactivestreams.Subscription;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.CompletionException;

import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GdhmAiServiceTest {

    @Mock
    private BedrockAgentRuntimeAsyncClient client;

    @Mock
    private BedrockReturnControlService returnControlService;

    private GdhmAiService service;

    @BeforeEach
    public void setUp() {
        BedrockAgentProperties properties = new BedrockAgentProperties();
        properties.setAgents(Map.of(
                "en", new BedrockAgentProperties.Agent("english-agent", "english-alias"),
                "es", new BedrockAgentProperties.Agent("spanish-agent", "spanish-alias")
        ));
        properties.setEnableTrace(false);
        service = new GdhmAiService(client, returnControlService, properties);
    }

    @Test
    public void shouldRouteToLanguageSpecificBedrockAgentFromUserLanguageRequestBodyParam() {
        AIRequest request = new AIRequest();
        request.setResponseId("session-1");
        request.setQuery("Hola");
        request.setUserLanguage("es");

        Disposable disposable = service.streamChat(request).subscribe();
        disposable.dispose();

        InvokeAgentRequest bedrockRequest = captureInvokeAgentRequest();
        assertEquals("spanish-agent", bedrockRequest.agentId());
        assertEquals("spanish-alias", bedrockRequest.agentAliasId());
        assertEquals("es", bedrockRequest.sessionState().sessionAttributes().get(USER_LANGUAGE));
        assertEquals("es", bedrockRequest.sessionState().promptSessionAttributes().get(USER_LANGUAGE));
        assertTrue(bedrockRequest.inputText().contains("Preferred response language: spanish (es)."));
    }

    @Test
    public void shouldDefaultToEnglishWhenUserLanguageIsNotSupported() {
        AIRequest request = new AIRequest();
        request.setResponseId("session-1");
        request.setQuery("Hola");
        request.setUserLanguage("spanish");

        Disposable disposable = service.streamChat(request).subscribe();
        disposable.dispose();

        InvokeAgentRequest bedrockRequest = captureInvokeAgentRequest();
        assertEquals("english-agent", bedrockRequest.agentId());
        assertEquals("en", bedrockRequest.sessionState().sessionAttributes().get(USER_LANGUAGE));
    }

    @Test
    public void shouldDefaultToEnglishWhenLanguageIsMissing() {
        AIRequest request = new AIRequest();
        request.setResponseId("session-1");
        request.setQuery("Hello");

        Disposable disposable = service.streamChat(request).subscribe();
        disposable.dispose();

        InvokeAgentRequest bedrockRequest = captureInvokeAgentRequest();
        assertEquals("english-agent", bedrockRequest.agentId());
        assertEquals("english-alias", bedrockRequest.agentAliasId());
        assertEquals("en", bedrockRequest.sessionState().sessionAttributes().get(USER_LANGUAGE));
    }

    @Test
    public void shouldCompleteWithVisibleMessageForBedrockErrors() {
        AIRequest request = new AIRequest();
        request.setResponseId("session-1");
        request.setQuery("Hello");
        AtomicReference<String> output = new AtomicReference<>();
        AtomicReference<Throwable> error = new AtomicReference<>();

        Disposable disposable = service.streamChat(request).subscribe(output::set, error::set);
        InvokeAgentResponseHandler handler = captureInvokeAgentResponseHandler();
        handler.exceptionOccurred(new RuntimeException("Bedrock failed"));
        disposable.dispose();

        assertNull(error.get());
        assertEquals("I ran into a temporary problem while generating the answer. Please try again.", output.get());
    }

    @Test
    public void shouldRetryOnceOnDependencyFailedErrors() {
        AIRequest request = new AIRequest();
        request.setResponseId("session-1");
        request.setQuery("Hello");
        AtomicReference<String> output = new AtomicReference<>();
        AtomicReference<Throwable> error = new AtomicReference<>();
        ArrayList<InvokeAgentResponseHandler> handlers = new ArrayList<>();

        doAnswer(invocation -> {
            handlers.add(invocation.getArgument(1));
            return null;
        }).when(client).invokeAgent(any(InvokeAgentRequest.class), any(InvokeAgentResponseHandler.class));

        Disposable disposable = service.streamChat(request).subscribe(output::set, error::set);

        assertEquals(1, handlers.size());
        handlers.get(0).exceptionOccurred(DependencyFailedException.builder().message("Dependency failed").build());
        assertEquals(2, handlers.size());
        handlers.get(1).exceptionOccurred(DependencyFailedException.builder().message("Dependency failed").build());
        disposable.dispose();

        assertNull(error.get());
        assertEquals("I'm having trouble getting a response from Bedrock right now. Please try the same question again in a moment.",
                output.get());
    }

    @Test
    public void shouldUnwrapCompletionExceptionsWhenHandlingDependencyFailures() {
        AIRequest request = new AIRequest();
        request.setResponseId("session-1");
        request.setQuery("Hello");
        AtomicReference<String> output = new AtomicReference<>();
        AtomicReference<Throwable> error = new AtomicReference<>();
        ArrayList<InvokeAgentResponseHandler> handlers = new ArrayList<>();

        doAnswer(invocation -> {
            handlers.add(invocation.getArgument(1));
            return null;
        }).when(client).invokeAgent(any(InvokeAgentRequest.class), any(InvokeAgentResponseHandler.class));

        Disposable disposable = service.streamChat(request).subscribe(output::set, error::set);
        DependencyFailedException cause = DependencyFailedException.builder().message("Dependency failed").build();
        handlers.get(0).exceptionOccurred(new CompletionException(cause));
        handlers.get(1).exceptionOccurred(new CompletionException(cause));
        disposable.dispose();

        assertNull(error.get());
        assertEquals("I'm having trouble getting a response from Bedrock right now. Please try the same question again in a moment.",
                output.get());
    }

    @Test
    public void shouldHandleReturnControlAndStreamChunksFromFollowUpInvocation() {
        AIRequest request = new AIRequest();
        request.setResponseId("session-1");
        request.setQuery("Hello");

        // Return control will be produced by the first model call.
        ReturnControlPayload payload = ReturnControlPayload.builder()
                .invocationId("invocation-1")
                .invocationInputs(InvocationInputMember.fromApiInvocationInput(ApiInvocationInput.builder()
                        .actionGroup("GDHI")
                        .apiPath("/countries")
                        .httpMethod("GET")
                        .parameters(java.util.List.of(
                                ApiParameter.builder().name(USER_LANGUAGE).type("string").value("en").build()
                        ))
                        .build()))
                .build();

        // Simulate a SessionState that contains return-control results so GdhmAiService marks this as "after tools".
        InvocationResultMember dummyResult = InvocationResultMember.fromApiResult(ApiResult.builder()
                .actionGroup("GDHI")
                .apiPath("/countries")
                .httpMethod("GET")
                .httpStatusCode(200)
                .build());
        when(returnControlService.buildSessionState(any(ReturnControlPayload.class), any(String.class)))
                .thenReturn(SessionState.builder()
                        .invocationId("invocation-1")
                        .returnControlInvocationResults(java.util.List.of(dummyResult))
                        .sessionAttributes(Map.of(USER_LANGUAGE, "en"))
                        .promptSessionAttributes(Map.of(USER_LANGUAGE, "en"))
                        .build());

        ArrayList<InvokeAgentRequest> requests = new ArrayList<>();

        doAnswer(invocation -> {
            InvokeAgentRequest invokeReq = invocation.getArgument(0);
            InvokeAgentResponseHandler handler = invocation.getArgument(1);
            requests.add(invokeReq);

            if (requests.size() == 1) {
                ResponseStream rcEvent = new TestReturnControlEvent(payload);
                SdkPublisher<ResponseStream> pub = eagerPublisher(rcEvent);
                handler.onEventStream(pub);
                handler.responseReceived(InvokeAgentResponse.builder().sessionId("session-1").build());
                handler.complete();
            } else {
                PayloadPart chunk = PayloadPart.builder()
                        .bytes(SdkBytes.fromUtf8String("hello"))
                        .build();
                ResponseStream chunkEvent = new TestChunkEvent(chunk);
                SdkPublisher<ResponseStream> pub = eagerPublisher(chunkEvent);
                handler.onEventStream(pub);
                handler.responseReceived(InvokeAgentResponse.builder().sessionId("session-1").build());
                handler.complete();
            }
            return null;
        }).when(client).invokeAgent(any(InvokeAgentRequest.class), any(InvokeAgentResponseHandler.class));

        String out = service.streamChat(request).collectList().block().stream().reduce("", String::concat);

        assertEquals(2, requests.size());
        assertEquals("hello", out);
        assertEquals("session-1", requests.get(0).sessionId());
        assertEquals("session-1", requests.get(1).sessionId());
        assertTrue(requests.get(1).sessionState().hasReturnControlInvocationResults());
    }

    @Test
    public void shouldNotRetryDependencyFailuresAfterToolResultsAndShowTooBroadMessage() {
        AIRequest request = new AIRequest();
        request.setResponseId("session-1");
        request.setQuery("Hello");

        ReturnControlPayload payload = ReturnControlPayload.builder()
                .invocationId("invocation-1")
                .invocationInputs(InvocationInputMember.fromApiInvocationInput(ApiInvocationInput.builder()
                        .actionGroup("GDHI")
                        .apiPath("/countries")
                        .httpMethod("GET")
                        .parameters(java.util.List.of(
                                ApiParameter.builder().name(USER_LANGUAGE).type("string").value("en").build()
                        ))
                        .build()))
                .build();

        InvocationResultMember dummyResult = InvocationResultMember.fromApiResult(ApiResult.builder()
                .actionGroup("GDHI")
                .apiPath("/countries")
                .httpMethod("GET")
                .httpStatusCode(200)
                .build());
        when(returnControlService.buildSessionState(any(ReturnControlPayload.class), any(String.class)))
                .thenReturn(SessionState.builder()
                        .invocationId("invocation-1")
                        .returnControlInvocationResults(java.util.List.of(dummyResult))
                        .sessionAttributes(Map.of(USER_LANGUAGE, "en"))
                        .promptSessionAttributes(Map.of(USER_LANGUAGE, "en"))
                        .build());

        ArrayList<InvokeAgentRequest> requests = new ArrayList<>();
        doAnswer(invocation -> {
            InvokeAgentRequest invokeReq = invocation.getArgument(0);
            InvokeAgentResponseHandler handler = invocation.getArgument(1);
            requests.add(invokeReq);

            if (requests.size() == 1) {
                ResponseStream rcEvent = new TestReturnControlEvent(payload);
                handler.onEventStream(eagerPublisher(rcEvent));
                handler.responseReceived(InvokeAgentResponse.builder().sessionId("session-1").build());
                handler.complete();
                return null;
            }

            handler.responseReceived(InvokeAgentResponse.builder().sessionId("session-1").build());
            handler.exceptionOccurred(new CompletionException(
                    DependencyFailedException.builder().message("Dependency failed").build()));
            return null;
        }).when(client).invokeAgent(any(InvokeAgentRequest.class), any(InvokeAgentResponseHandler.class));

        String out = service.streamChat(request).collectList().block().stream().reduce("", String::concat);

        assertEquals(2, requests.size());
        assertEquals("That request was too broad for the AI to finish reliably after loading the GDHM data. Please narrow it by country, year, category, phase, or region and try again.",
                out);
    }

    @Test
    public void shouldInsertDoubleNewlineBeforeVisibleErrorWhenTextAlreadyEmitted() {
        AIRequest request = new AIRequest();
        request.setResponseId("session-1");
        request.setQuery("Hello");

        doAnswer(invocation -> {
            InvokeAgentResponseHandler handler = invocation.getArgument(1);
            PayloadPart chunk = PayloadPart.builder().bytes(SdkBytes.fromUtf8String("partial")).build();
            ResponseStream chunkEvent = new TestChunkEvent(chunk);
            handler.onEventStream(eagerPublisher(chunkEvent));
            handler.responseReceived(InvokeAgentResponse.builder().sessionId("session-1").build());
            handler.exceptionOccurred(new RuntimeException("Bedrock failed"));
            return null;
        }).when(client).invokeAgent(any(InvokeAgentRequest.class), any(InvokeAgentResponseHandler.class));

        String out = service.streamChat(request).collectList().block().stream().reduce("", String::concat);

        assertEquals("partial\n\nI ran into a temporary problem while generating the answer. Please try again.", out);
    }

    private static <T> SdkPublisher<T> eagerPublisher(T item) {
        return eagerPublisher(List.of(item));
    }

    private static <T> SdkPublisher<T> eagerPublisher(List<T> items) {
        return subscriber -> subscriber.onSubscribe(new Subscription() {
            boolean done = false;
            int idx = 0;

            @Override
            public void request(long n) {
                if (done || n <= 0) {
                    return;
                }
                while (!done && n-- > 0) {
                    if (idx >= items.size()) {
                        done = true;
                        subscriber.onComplete();
                        return;
                    }
                    subscriber.onNext(items.get(idx++));
                }
            }

            @Override
            public void cancel() {
                done = true;
            }
        });
    }

    @Test
    public void shouldHandleTraceEventsWithoutBreakingTheStream() {
        AIRequest request = new AIRequest();
        request.setResponseId("session-1");
        request.setQuery("Hello");

        TracePart nullTrace = TracePart.builder().sessionId("session-1").build();

        Trace failureTrace = Trace.fromFailureTrace(f -> f.traceId("t-fail").failureReason("failure"));
        TracePart failure = TracePart.builder().sessionId("session-1").trace(failureTrace).build();

        Trace preTrace = Trace.fromPreProcessingTrace(p -> p
                .modelInvocationOutput(o -> o.traceId("t-pre")));
        TracePart pre = TracePart.builder().sessionId("session-1").trace(preTrace).build();

        software.amazon.awssdk.services.bedrockagentruntime.model.InvocationInput invocationInput =
                software.amazon.awssdk.services.bedrockagentruntime.model.InvocationInput.builder()
                        .actionGroupInvocationInput(software.amazon.awssdk.services.bedrockagentruntime.model.ActionGroupInvocationInput
                                .builder()
                                .actionGroupName("GDHI")
                                .verb("GET")
                                .apiPath("/countries")
                                .build())
                        .build();
        software.amazon.awssdk.services.bedrockagentruntime.model.Observation observation =
                software.amazon.awssdk.services.bedrockagentruntime.model.Observation.builder()
                        .type("action_group")
                        .traceId("t-obs")
                        .build();
        Trace orchestrationTrace = Trace.fromOrchestrationTrace(o -> o
                .invocationInput(invocationInput)
                .observation(observation));
        TracePart orchestration = TracePart.builder().sessionId("session-1").trace(orchestrationTrace).build();

        Trace postTrace = Trace.fromPostProcessingTrace(p -> p.modelInvocationOutput(o -> o.traceId("t-post")));
        TracePart post = TracePart.builder().sessionId("session-1").trace(postTrace).build();

        Trace unclassifiedTrace = Trace.fromGuardrailTrace(
                software.amazon.awssdk.services.bedrockagentruntime.model.GuardrailTrace.builder().build());
        TracePart unclassified = TracePart.builder().sessionId("session-1").trace(unclassifiedTrace).build();

        doAnswer(invocation -> {
            InvokeAgentResponseHandler handler = invocation.getArgument(1);
            List<ResponseStream> events = List.of(
                    new TestTraceEvent(nullTrace),
                    new TestTraceEvent(failure),
                    new TestTraceEvent(pre),
                    new TestTraceEvent(orchestration),
                    new TestTraceEvent(post),
                    new TestTraceEvent(unclassified)
            );
            handler.onEventStream(eagerPublisher(events));
            handler.responseReceived(InvokeAgentResponse.builder().sessionId("session-1").build());
            handler.complete();
            return null;
        }).when(client).invokeAgent(any(InvokeAgentRequest.class), any(InvokeAgentResponseHandler.class));

        String out = service.streamChat(request).collectList().block().stream().reduce("", String::concat);
        assertEquals("", out);
    }

    private static final class TestReturnControlEvent implements ResponseStream {
        private final ReturnControlPayload payload;

        private TestReturnControlEvent(ReturnControlPayload payload) {
            this.payload = payload;
        }

        @Override
        public java.util.List<software.amazon.awssdk.core.SdkField<?>> sdkFields() {
            return java.util.Collections.emptyList();
        }

        @Override
        public void accept(InvokeAgentResponseHandler.Visitor visitor) {
            visitor.visitReturnControl(payload);
        }
    }

    private static final class TestChunkEvent implements ResponseStream {
        private final PayloadPart chunk;

        private TestChunkEvent(PayloadPart chunk) {
            this.chunk = chunk;
        }

        @Override
        public java.util.List<software.amazon.awssdk.core.SdkField<?>> sdkFields() {
            return java.util.Collections.emptyList();
        }

        @Override
        public void accept(InvokeAgentResponseHandler.Visitor visitor) {
            visitor.visitChunk(chunk);
        }
    }

    private static final class TestTraceEvent implements ResponseStream {
        private final TracePart tracePart;

        private TestTraceEvent(TracePart tracePart) {
            this.tracePart = tracePart;
        }

        @Override
        public java.util.List<software.amazon.awssdk.core.SdkField<?>> sdkFields() {
            return java.util.Collections.emptyList();
        }

        @Override
        public void accept(InvokeAgentResponseHandler.Visitor visitor) {
            visitor.visitTrace(tracePart);
        }
    }

    private InvokeAgentRequest captureInvokeAgentRequest() {
        ArgumentCaptor<InvokeAgentRequest> requestCaptor = ArgumentCaptor.forClass(InvokeAgentRequest.class);
        verify(client).invokeAgent(requestCaptor.capture(), any(InvokeAgentResponseHandler.class));
        return requestCaptor.getValue();
    }

    private InvokeAgentResponseHandler captureInvokeAgentResponseHandler() {
        ArgumentCaptor<InvokeAgentResponseHandler> handlerCaptor =
                ArgumentCaptor.forClass(InvokeAgentResponseHandler.class);
        verify(client).invokeAgent(any(InvokeAgentRequest.class), handlerCaptor.capture());
        return handlerCaptor.getValue();
    }
}
