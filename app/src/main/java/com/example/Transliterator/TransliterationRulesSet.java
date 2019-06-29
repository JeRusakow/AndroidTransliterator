package com.example.Transliterator;

import java.util.ArrayList;
import java.util.Arrays;

public class TransliterationRulesSet {

    private ArrayList<StringReplacementRule> simplificationRules;
    private ArrayList<StringReplacementRule> complicationRules;

    private ArrayList<TransliterationRule> transliterationRules;

    TransliterationRulesSet(TransliterationRule[] transliterationRulesArray,
                            StringReplacementRule[] simplificationRulesArray,
                            StringReplacementRule[] complicationRulesArray){

        this.transliterationRules =  new ArrayList<>();
        this.transliterationRules.addAll(Arrays.asList(transliterationRulesArray));

        this.complicationRules = new ArrayList<>();
        this.complicationRules.addAll(Arrays.asList(complicationRulesArray));

        this.simplificationRules = new ArrayList<>();
        this.simplificationRules.addAll(Arrays.asList(simplificationRulesArray));
    }

    TransliterationRulesSet(ArrayList<TransliterationRule> transliterationRuleList,
                            ArrayList<StringReplacementRule> simplificationRuleList,
                            ArrayList<StringReplacementRule> complicationRuleList){

        this.transliterationRules = transliterationRuleList;
        this.simplificationRules = simplificationRuleList;
        this.complicationRules = complicationRuleList;

    }

    public ArrayList<StringReplacementRule> getSimplificationRules(){
        return this.simplificationRules;
    }

    public ArrayList<StringReplacementRule> getComplicationRules(){
        return this.complicationRules;
    }

    public ArrayList<TransliterationRule> getTransliterationRules(){
        return this.transliterationRules;
    }
}
