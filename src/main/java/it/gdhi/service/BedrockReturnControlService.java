package it.gdhi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gdhi.ai.dto.BedrockToolResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.bedrockagentruntime.model.ApiInvocationInput;
import software.amazon.awssdk.services.bedrockagentruntime.model.ApiResult;
import software.amazon.awssdk.services.bedrockagentruntime.model.ApiParameter;
import software.amazon.awssdk.services.bedrockagentruntime.model.ContentBody;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvocationInputMember;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvocationResultMember;
import software.amazon.awssdk.services.bedrockagentruntime.model.ReturnControlPayload;
import software.amazon.awssdk.services.bedrockagentruntime.model.SessionState;
import software.amazon.awssdk.services.bedrockagentruntime.model.ResponseState;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class BedrockReturnControlService {

    private static final int LARGE_TOOL_RESPONSE_BYTES = 25_000;

    private final BedrockToolsService bedrockToolsService;
    private final ObjectMapper objectMapper;

    public SessionState buildSessionState(ReturnControlPayload payload, String userLanguage) {
        log.info("Processing Bedrock return control invocationId={} with {} input(s)",
                payload.invocationId(), payload.invocationInputs().size());
        List<InvocationResultMember> results = payload.invocationInputs().stream()
                .map(input -> executeInvocation(input, userLanguage))
                .toList();
        Map<String, String> languageAttributes = languageAttributes(userLanguage);

        return SessionState.builder()
                .invocationId(payload.invocationId())
                .sessionAttributes(languageAttributes)
                .promptSessionAttributes(languageAttributes)
                .returnControlInvocationResults(results)
                .build();
    }

    private Map<String, String> languageAttributes(String userLanguage) {
        return Map.of(USER_LANGUAGE, userLanguage);
    }

    private InvocationResultMember executeInvocation(InvocationInputMember inputMember, String userLanguage) {
        ApiInvocationInput apiInvocationInput = withUserLanguage(inputMember.apiInvocationInput(), userLanguage);
        if (apiInvocationInput == null) {
            throw new IllegalArgumentException("Only API-schema return control inputs are supported.");
        }

        log.info("Executing returned Bedrock API actionGroup={} method={} path={}",
                apiInvocationInput.actionGroup(), apiInvocationInput.httpMethod(), apiInvocationInput.apiPath());
        try {
            BedrockToolResponse<?> response = bedrockToolsService.executeApiInvocation(apiInvocationInput);
            log.info("Completed returned Bedrock API actionGroup={} method={} path={} status={}",
                    apiInvocationInput.actionGroup(), apiInvocationInput.httpMethod(),
                    apiInvocationInput.apiPath(), 200);
            return InvocationResultMember.fromApiResult(buildApiResult(apiInvocationInput, 200, response, null));
        }
        catch (IllegalArgumentException ex) {
            log.warn("Validation error for returned Bedrock API path={}: {}", apiInvocationInput.apiPath(),
                    ex.getMessage());
            return InvocationResultMember.fromApiResult(buildApiResult(apiInvocationInput, 400,
                    bedrockToolsService.validationError(ex), ResponseState.REPROMPT));
        }
        catch (Exception ex) {
            log.error("Failed to execute returned Bedrock API path={}", apiInvocationInput.apiPath(), ex);
            return InvocationResultMember.fromApiResult(buildApiResult(apiInvocationInput, 500,
                    bedrockToolsService.serverError(ex), ResponseState.FAILURE));
        }
    }

    private ApiResult buildApiResult(ApiInvocationInput apiInvocationInput, int httpStatusCode,
                                     BedrockToolResponse<?> responseBody, ResponseState responseState) {
        String serializedBody = serialize(responseBody);
        logToolResponseSize(apiInvocationInput, httpStatusCode, serializedBody);
        ApiResult.Builder resultBuilder = ApiResult.builder()
                .actionGroup(apiInvocationInput.actionGroup())
                .apiPath(apiInvocationInput.apiPath())
                .httpMethod(apiInvocationInput.httpMethod())
                .httpStatusCode(httpStatusCode)
                .responseBody(Map.of("application/json", ContentBody.builder().body(serializedBody).build()));

        if (responseState != null) {
            resultBuilder.responseState(responseState);
        }

        if (StringUtils.hasText(apiInvocationInput.agentId())) {
            resultBuilder.agentId(apiInvocationInput.agentId());
        }

        return resultBuilder.build();
    }

    private void logToolResponseSize(ApiInvocationInput apiInvocationInput, int httpStatusCode, String serializedBody) {
        int sizeBytes = serializedBody.getBytes(StandardCharsets.UTF_8).length;
        if (sizeBytes >= LARGE_TOOL_RESPONSE_BYTES) {
            log.warn("Large Bedrock tool response actionGroup={} method={} path={} status={} responseBytes={}",
                    apiInvocationInput.actionGroup(), apiInvocationInput.httpMethod(), apiInvocationInput.apiPath(),
                    httpStatusCode, sizeBytes);
            return;
        }

        log.info("Bedrock tool response actionGroup={} method={} path={} status={} responseBytes={}",
                apiInvocationInput.actionGroup(), apiInvocationInput.httpMethod(), apiInvocationInput.apiPath(),
                httpStatusCode, sizeBytes);
    }

    private String serialize(BedrockToolResponse<?> body) {
        try {
            return objectMapper.writeValueAsString(body);
        }
        catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize Bedrock tool response.", ex);
        }
    }

    private ApiInvocationInput withUserLanguage(ApiInvocationInput apiInvocationInput, String userLanguage) {
        if (apiInvocationInput == null || !StringUtils.hasText(userLanguage)) {
            return apiInvocationInput;
        }

        List<ApiParameter> parameters = new ArrayList<>(apiInvocationInput.parameters().stream()
                .filter(parameter -> !USER_LANGUAGE.equals(parameter.name()))
                .toList());
        parameters.add(ApiParameter.builder()
                .name(USER_LANGUAGE)
                .type("string")
                .value(userLanguage)
                .build());

        return apiInvocationInput.toBuilder()
                .parameters(parameters)
                .build();
    }
}
