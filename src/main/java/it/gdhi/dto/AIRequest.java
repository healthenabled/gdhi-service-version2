package it.gdhi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class AIRequest {
    private String responseId;

    @NotBlank
    @Size(max = 2000)
    private String query;

    @JsonProperty("user_language")
    @Size(max = 20)
    private String userLanguage;
}
