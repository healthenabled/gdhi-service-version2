package it.gdhi.internationalization.translations;

import lombok.Getter;

@Getter
public enum OVERALL_PHASE {
    en("Overall Phase"),
    es("En general Fase"),
    fr("Dans l'ensemble Phase"),
    pt("Total Fase"), ar("المؤشر العام المرحلة");

    private String translatedText;

    OVERALL_PHASE(String translatedTextValue) {
        this.translatedText = translatedTextValue;
    }
}