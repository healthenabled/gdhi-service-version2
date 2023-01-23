package it.gdhi.internationalization.translations;

import lombok.Getter;

@Getter
public enum INDICATOR {
    en("Indicator "),
    es("Indicador "),
    fr("Indicateur "),
    pt("Indicador "),
    ar("مؤشر ");

    private String translatedText;

    INDICATOR(String translatedTextValue) {
        this.translatedText = translatedTextValue;
    }
}
