package it.gdhi.internationalization.translations;

import lombok.Getter;

@Getter
public enum NOT_AVAILABLE {
    en("Not Available"),
    es("No disponible"),
    fr("Non disponible"),
    pt("Não disponível"),
    ar("غير متاح");

    private String translatedText;

    NOT_AVAILABLE(String translatedTextValue) {
        this.translatedText = translatedTextValue;
    }
}
