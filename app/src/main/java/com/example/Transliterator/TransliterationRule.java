package com.example.Transliterator;

public class TransliterationRule {

    public String literal;
    public WordPhonetic sounds;

    TransliterationRule(String literal, WordPhonetic sounds){
        this.literal = literal;
        this.sounds = sounds;
    }

    @Deprecated
    TransliterationRule(String literal, Phoneme[] sounds){
        this.literal = literal;
        for (Phoneme sound : sounds){
            this.sounds.add(sound);
        }
    }


}
