package com.example.Transliterator;

import java.util.ArrayList;

public class WordPhonetic extends ArrayList<Phoneme> {

    WordPhonetic() {
        super();
    }

    WordPhonetic(ArrayList<Phoneme> sounds){
        super(sounds);
    }

    void append(ArrayList<Phoneme> sounds) {

        if (sounds != null) {
            for (Phoneme phoneme : sounds) {
                super.add(phoneme);
            }
        } else {
            super.add(Phoneme.ErrorSign);
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Phoneme phoneme : this) {
            string.append(phoneme.toString());
        }
        return string.toString();
    }

}
