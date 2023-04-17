package it.gdhi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class GdhiQuestionnaires {
    private List<GdhiQuestionnaire> gdhiQuestionnaires;

    public GdhiQuestionnaires(List<GdhiQuestionnaire> questionnaires) {
        this.gdhiQuestionnaires = questionnaires;
    }
}
