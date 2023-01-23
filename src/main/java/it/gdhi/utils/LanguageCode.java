package it.gdhi.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/* Do not change, dependency on front-end */
@Slf4j
public enum LanguageCode {
    en("english"), es("spanish"), fr("french"), pt("portuguese"), ar("arabic");

    private String name;
    public static final String USER_LANGUAGE = "user_language";

    LanguageCode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static LanguageCode getValueFor(String string) {
        List<LanguageCode> languageCodes = Arrays.asList(LanguageCode.values());
        for (LanguageCode code: languageCodes) {
            if(code.toString().equals(string)) return code;
        }
        log.error("Couldn't find languageCode = {}, using en instead.", string);
        return en;
    }
}
