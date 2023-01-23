package it.gdhi.internationalization.translations;

import lombok.Getter;

@Getter
public enum CATEGORY {
    en("Category "),
    es("Categoría "),
    fr("Catégorie "),
    pt("Categoria "),
    ar(""); //TODO: add Arabic translation

    private String translatedText;

    CATEGORY(String translatedText) {
        this.translatedText = translatedText;
    }
}
