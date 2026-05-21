package it.gdhi.config;

import it.gdhi.utils.LanguageCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "aws.bedrock")
@Getter
@Setter
public class BedrockAgentProperties {

    private String agentId;
    private String agentAliasId;
    private int maxModelRetryAttempts = 2;
    private boolean enableTrace = true;
    private int applyGuardrailInterval = 20;
    private boolean logFullTrace;
    private Map<String, Agent> agents = new LinkedHashMap<>();

    public Agent resolve(LanguageCode languageCode) {
        LanguageCode effectiveLanguageCode = languageCode == null ? LanguageCode.en : languageCode;
        Agent agent = findConfiguredAgent(effectiveLanguageCode);
        if (agent != null) {
            return agent;
        }

        Agent englishAgent = findConfiguredAgent(LanguageCode.en);
        if (englishAgent != null) {
            return englishAgent;
        }

        throw new IllegalStateException("No English Bedrock agent is configured.");
    }

    private Agent findConfiguredAgent(LanguageCode languageCode) {
        if (languageCode == null) {
            return null;
        }

        Agent languageAgent = agents.get(languageCode.name());
        if (isConfigured(languageAgent)) {
            return languageAgent;
        }
        if (languageCode == LanguageCode.en && StringUtils.hasText(agentId)
                && StringUtils.hasText(agentAliasId)) {
            return new Agent(agentId, agentAliasId);
        }
        return null;
    }

    private boolean isConfigured(Agent agent) {
        return agent != null && StringUtils.hasText(agent.getAgentId())
                && StringUtils.hasText(agent.getAgentAliasId());
    }

    public void setAgents(Map<String, Agent> agents) {
        this.agents = agents == null ? new LinkedHashMap<>() : agents;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Agent {
        private String agentId;
        private String agentAliasId;
    }
}
