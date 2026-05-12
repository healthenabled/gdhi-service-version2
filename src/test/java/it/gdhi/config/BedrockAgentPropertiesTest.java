package it.gdhi.config;

import it.gdhi.utils.LanguageCode;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BedrockAgentPropertiesTest {

    @Test
    public void shouldResolveAgentForLanguage() {
        BedrockAgentProperties properties = new BedrockAgentProperties();
        properties.setAgents(Map.of("es", new BedrockAgentProperties.Agent("spanish-agent", "spanish-alias")));

        BedrockAgentProperties.Agent agent = properties.resolve(LanguageCode.es);

        assertEquals("spanish-agent", agent.getAgentId());
        assertEquals("spanish-alias", agent.getAgentAliasId());
    }

    @Test
    public void shouldResolveLegacyEnglishAgentConfig() {
        BedrockAgentProperties properties = new BedrockAgentProperties();
        properties.setAgentId("english-agent");
        properties.setAgentAliasId("english-alias");

        BedrockAgentProperties.Agent agent = properties.resolve(LanguageCode.en);

        assertEquals("english-agent", agent.getAgentId());
        assertEquals("english-alias", agent.getAgentAliasId());
    }

    @Test
    public void shouldFallBackToEnglishWhenRequestedLanguageAgentIsNotConfigured() {
        BedrockAgentProperties properties = new BedrockAgentProperties();
        properties.setAgents(Map.of("en", new BedrockAgentProperties.Agent("english-agent", "english-alias")));

        BedrockAgentProperties.Agent agent = properties.resolve(LanguageCode.fr);

        assertEquals("english-agent", agent.getAgentId());
        assertEquals("english-alias", agent.getAgentAliasId());
    }
}
