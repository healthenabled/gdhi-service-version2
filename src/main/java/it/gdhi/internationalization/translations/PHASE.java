package it.gdhi.internationalization.translations;

import lombok.Getter;

@Getter
public enum PHASE {
    en("Phase "),
    es("Fase "),
    fr("Phase "),
    pt("Fase "),
    ar("");

    private String translatedText;

    PHASE(String translatedTextValue) {
        this.translatedText = translatedTextValue;
    }
}
