package de.jan.translator;

public enum Language {
    AUTO("auto"),
    GERMAN("de"),
    ENGLISH("en"),
    FRENCH("fr"),
    RUSSIAN("ru"),
    ITALIAN("it"),
    ARABIC("ar"),
    CZECH("cs"),
    DUTCH("nl"),
    HINDI("hi"),
    JAPANESE("ja"),
    KOREAN("ko"),
    PORTUGUESE("pt"),
    ROMANIAN("ro"),
    SWEDISH("sv"),
    CHINESE_SIMPLIFIED("zh-CN"),
    CHINESE_TRADITIONAL("zh-TW"),
    FINNISH("fi"),
    GREEK("el"),
    IRISH("ga"),
    ZULU("zu"),
    YORUBA("yo"),
    YIDDISH("yi"),
    XHOSA("xh"),
    WELSH("cy"),
    VIETNAMESE("vi"),
    UZBEK("uz"),
    URDU("ur"),
    UKRAINIAN("uk"),
    TURKISH("tr"),
    THAI("th"),
    TELUGU("te"),
    TAMIL("ta"),
    TAJIK("tg"),
    SWAHILI("sw"),
    SUNDANESE("su"),
    SOMALI("so"),
    SLOVENIAN("sl"),
    SLOVAK("sk"),
    SINHALA("si"),
    SINDHI("sd"),
    SHONA("sn"),
    SESOTHO("st"),
    SERBIAN("sr"),
    SCOTS_GAELIC("gd"),
    SAMOAN("sm"),
    PUNJABI("pa"),
    POLISH("pl"),
    PERSIAN("fa"),
    PASHTO("ps"),
    NORWEGIAN("no"),
    NEPALI("ne"),
    LATIN("la"),
    LATVIAN("lv"),
    LUXENBOURGISH("lb"),
    KANNADA("kn"),
    GALICIAN("gl"),
    FILIPINO("tl"),
    ESPERANTO("eo"),
    ESTONIAN("et"),
    GUJARATI("gu"),
    HMONG("hmn"),
    HEBREW("iw"),
    ICELANDIC("is"),
    KYRGYZ("ky");

    private String key;

    Language(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
