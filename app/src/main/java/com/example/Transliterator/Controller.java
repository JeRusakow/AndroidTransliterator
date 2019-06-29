package com.example.Transliterator;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;

public class Controller {

    private HashMap<String, Language> languageMap;
    private LanguageConstructor languageConstructor;

    Controller(){
        languageConstructor = new LanguageConstructor();

        languageMap = new HashMap<>();
    }

    public void constructLanguagesFromFile(BufferedReader reader){
        languageConstructor.constructLanguagesFromFile(reader);

        for (Language lang : languageConstructor.getLanguageList()){
            if (!languageMap.containsKey(lang.getName())){
                languageMap.put(lang.getName(), lang);
            }
        }
    }

    public String[] getAvailableLanguages(){
        String[] lang = languageMap.keySet().toArray(new String[0]);
        Arrays.sort(lang);
        return lang;
    }

    public String transliterate(String phraseToTransliterate, String languageFrom, String languageTo) {

        StringBuilder result = new StringBuilder();
        StringBuilder phrase = new StringBuilder(phraseToTransliterate);

        StringBuilder buff = new StringBuilder();

        if (phrase.length() == 0){
            return ("");
        }

        boolean isAlphabetic = (Character.isAlphabetic(phrase.charAt(0)));

        for (int i = 0; i < phrase.length(); i++){

            if (Character.isAlphabetic(phrase.charAt(i)) != isAlphabetic){

                if (isAlphabetic){
                    result.append(languageMap.get(languageTo).constructLiteralWord(languageMap.get(languageFrom).decomposeLiteralWord(buff.toString().toLowerCase())));
                    buff.setLength(0);
                    buff.append(phrase.charAt(i));
                    isAlphabetic = false;
                }
                else
                {
                    result.append(buff.toString());
                    buff.setLength(0);
                    buff.append(phrase.charAt(i));
                    isAlphabetic = true;
                }
            }
            else
            {
                buff.append(phrase.charAt(i));
            }

        }

        if (isAlphabetic){
            result.append(languageMap.get(languageTo).constructLiteralWord(languageMap.get(languageFrom).decomposeLiteralWord(buff.toString().toLowerCase())));
        }
        else
        {
            result.append(buff.toString());
        }

        return result.toString();

    }

}
