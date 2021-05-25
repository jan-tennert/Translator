package de.jan.translator.translators;

public class Translated {

    private final String originalText;
    private final String translatedText;
    private final String originalLanguage;
    private final String translatedLanguage;

    public Translated(String originalText, String translatedText, String originalLanguage, String translatedLanguage) {
        this.originalText = originalText;
        this.translatedText = translatedText;
        this.originalLanguage = originalLanguage;
        this.translatedLanguage = translatedLanguage;
    }

    public String getOriginalText() {
        return originalText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getTranslatedLanguage() {
        return translatedLanguage;
    }

    @Override
    public String toString() {
        return "Translated{" +
                "originalText='" + originalText + '\'' +
                ", translatedText='" + translatedText + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", translatedLanguage='" + translatedLanguage + '\'' +
                '}';
    }
}
