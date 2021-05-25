package de.jan.translator.translators;

public interface ITranslator {

    Translated translate(String text, String translateTo, String translateFrom);

}
