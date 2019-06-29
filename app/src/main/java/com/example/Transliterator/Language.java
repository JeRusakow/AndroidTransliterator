package com.example.Transliterator;

import java.util.ArrayList;
import java.util.HashMap;

public class Language implements Comparable<Language> {

    private String name;

    private HashMap<String, WordPhonetic > lettersToSoundRules;
    private HashMap<Phoneme, String> soundsToLetters;

    private ArrayList<StringReplacementRule> simplificationRules;
    private ArrayList<StringReplacementRule> complicationRules;

    Language(String name, TransliterationRulesSet ruleSet){
        this.name = name;
        
        //constructing simplification rules list
        simplificationRules = new ArrayList<>();
        simplificationRules.addAll(ruleSet.getSimplificationRules());

        //constructing complication rules list
        complicationRules = new ArrayList<>();
        complicationRules.addAll(ruleSet.getComplicationRules());

        //creating HashMap for toSound convertion
        lettersToSoundRules = new HashMap<>();
        for (TransliterationRule rule : ruleSet.getTransliterationRules()) {
            if (!lettersToSoundRules.containsKey(rule.literal)) {
                lettersToSoundRules.put(rule.literal, new WordPhonetic(rule.sounds));
            }
        }

        //Creating HashMap for toLiteralConversion
        soundsToLetters = new HashMap<>();
        for (TransliterationRule rule : ruleSet.getTransliterationRules()) {
            if (rule.sounds.size() == 1){
                soundsToLetters.put(rule.sounds.get(0), rule.literal);
            }
        }

    }

    public String getName() {
        return name;
    }

    public String constructLiteralWord(WordPhonetic wordPhonetic){

        //Constructing raw word
        StringBuilder rawWord = new StringBuilder();

        for (Phoneme phoneme : wordPhonetic) {
            rawWord.append(soundsToLetters.get(phoneme));
        }

        //Debugging
    /*
        System.out.print("\nRawly constructed word:\n");
        System.out.print(rawWord.toString());
    */

        //Improving raw word
        //Marking the word edges
        rawWord.insert(0, '<');
        rawWord.insert(rawWord.length(), '>');

        for (StringReplacementRule rule : complicationRules) {
            int i = rawWord.indexOf(rule.from);

            while (rawWord.indexOf(rule.from) != -1) {
                rawWord.replace(rawWord.indexOf(rule.from), rawWord.indexOf(rule.from) + rule.from.length(), rule.to);
            }
        }

        //Unmarking the word edges
        rawWord.deleteCharAt(0);
        rawWord.deleteCharAt(rawWord.length() - 1);

        return rawWord.toString();
    }

    public WordPhonetic decomposeLiteralWord(String word){

        StringBuilder literalWord = new StringBuilder(word);

        //Simplification
        //Marking the word edges
        literalWord.insert(0, '<');
        literalWord.insert(literalWord.length(), '>');

        for (StringReplacementRule rule : simplificationRules) {
            while (literalWord.indexOf(rule.from) != -1){
                literalWord.replace(literalWord.indexOf(rule.from), literalWord.indexOf(rule.from) + rule.from.length(), rule.to);
            }
        }

        //Unmarking the word edges
        literalWord.deleteCharAt(0);
        literalWord.deleteCharAt(literalWord.length() - 1);

        //Debugging
    /*
        System.out.print("Symplified word:\n");
        System.out.print(literalWord);
    */

        //Decomposing
        WordPhonetic wordPhonetic = new WordPhonetic();

        int stringPointer = 0;
        StringBuilder buffer = new StringBuilder();
        while (literalWord.length() > 0 && stringPointer < literalWord.length()){
            buffer.append(literalWord.charAt(stringPointer));

            if(!lettersToSoundRules.containsKey(buffer.toString())){
                literalWord.replace(0, stringPointer, "");
                buffer.deleteCharAt(buffer.length() - 1);

                stringPointer = 0;

                if (buffer.length() == 0){
                    wordPhonetic.add(Phoneme.ErrorSign);
                    literalWord.deleteCharAt(0);
                }
                else {
                    wordPhonetic.append(lettersToSoundRules.get(buffer.toString()));
                    buffer.replace(0, buffer.length(), "");
                }
            }
            else
            {
                stringPointer++;
            }
        }
        if (buffer.length() > 0) {
            wordPhonetic.append(lettersToSoundRules.get(buffer.toString()));
        }

        return wordPhonetic;
    }

    @Override
    public int compareTo(Language o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return this.getName();
    }

}
