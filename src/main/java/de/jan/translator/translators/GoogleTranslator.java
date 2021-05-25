package de.jan.translator.translators;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GoogleTranslator implements ITranslator {

    private final String baseURL = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s";

    @Override
    public Translated translate(String text, String translateTo, String translateFrom) {
        try {
            URL url = new URL(String.format(baseURL, translateFrom, translateTo, URLEncoder.encode(text, "UTF-8")));
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();
            String s = response.toString();
            System.out.println(s);
            String translatedText = s.substring(s.indexOf("[[[\"") + 4, s.indexOf("\",\"")).replace("\\\"", "").replace("\\u003c", "<").replace("\\u003e", ">");
            return new Translated(text, translatedText, translateFrom, translateTo);
        } catch (IOException ignored) {
            return new Translated("", "", "", "");
        }
    }
}
