package it.gdhi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gdhi.ai.dto.BedrockToolResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.bedrockagentruntime.model.ApiInvocationInput;
import software.amazon.awssdk.services.bedrockagentruntime.model.ApiParameter;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvocationInputMember;
import software.amazon.awssdk.services.bedrockagentruntime.model.ReturnControlPayload;

import java.util.List;
import java.util.Map;

import static it.gdhi.utils.LanguageCode.USER_LANGUAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BedrockReturnControlServiceTest {

    @Mock
    private BedrockToolsService bedrockToolsService;

    private BedrockReturnControlService service;

    @BeforeEach
    public void setUp() {
        service = new BedrockReturnControlService(bedrockToolsService, new ObjectMapper());
    }

    @Test
    public void shouldOverrideModelProvidedLanguageWithRequestLanguage() {
        doReturn(BedrockToolResponse.ok("listCountries", "Fetched countries", Map.of(), List.of()))
                .when(bedrockToolsService).executeApiInvocation(any());
        ReturnControlPayload payload = ReturnControlPayload.builder()
                .invocationId("invocation-1")
                .invocationInputs(InvocationInputMember.fromApiInvocationInput(ApiInvocationInput.builder()
                        .actionGroup("GDHI")
                        .apiPath("/countries")
                        .httpMethod("GET")
                        .parameters(List.of(
                                ApiParameter.builder().name(USER_LANGUAGE).type("string").value("es").build(),
                                ApiParameter.builder().name("limit").type("integer").value("5").build()
                        ))
                        .build()))
                .build();

        service.buildSessionState(payload, "fr");

        ArgumentCaptor<ApiInvocationInput> captor = ArgumentCaptor.forClass(ApiInvocationInput.class);
        verify(bedrockToolsService).executeApiInvocation(captor.capture());
        List<ApiParameter> parameters = captor.getValue().parameters();
        List<String> languageValues = parameters.stream()
                .filter(parameter -> USER_LANGUAGE.equals(parameter.name()))
                .map(ApiParameter::value)
                .toList();

        assertEquals(List.of("fr"), languageValues);
        assertEquals("5", parameters.stream()
                .filter(parameter -> "limit".equals(parameter.name()))
                .findFirst()
                .orElseThrow()
                .value());
    }
}
