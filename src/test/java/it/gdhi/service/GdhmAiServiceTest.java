package it.gdhi.service;

import it.gdhi.config.BedrockAgentProperties;
import it.gdhi.dto.AIRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.Disposable;
import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvokeAgentRequest;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvokeAgentResponseHandler;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
    public void shouldPropagateBedrockErrorsAsInternalServerErrors() {
        AIRequest request = new AIRequest();
        request.setResponseId("session-1");
        request.setQuery("Hello");
        AtomicReference<Throwable> error = new AtomicReference<>();

        Disposable disposable = service.streamChat(request).subscribe(ignored -> { }, error::set);
        InvokeAgentResponseHandler handler = captureInvokeAgentResponseHandler();
        handler.exceptionOccurred(new RuntimeException("Bedrock failed"));
        disposable.dispose();

        ResponseStatusException exception = assertInstanceOf(ResponseStatusException.class, error.get());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals("Bedrock agent invocation failed.", exception.getReason());
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
