package com.example.Transliterator;

import android.os.health.PackageHealthStats;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LanguageConstructor {

    private ArrayList<Language> languages;
    private HashMap<String, Phoneme> phonemeHashMap;

    //transliterator tags
    private String nameTag = "<name>";
    private String nameTagCl = "</name>";
    private String langTag = "<language>";
    private String langTagCl = "</language>";
    private String transRulesTag = "<transliterationRules>";
    private String transRulesTagCl = "</transliterationRules>";
    private String simpRulesTag = "<simplificationRules>";
    private String simpRulesTagCl = "</simplificationRules>";
    private String complRulesTag = "<complificationRules>";
    private String complRulesTagCl = "</complificationRules>";
    private String arrow = "@";
    private String ruleTag = "<rule>";
    private String ruleTagCl = "</rule>";

    LanguageConstructor(){

        languages = new ArrayList<>();

        phonemeHashMap = new HashMap<>();

        // Hash map initialization
        {
            phonemeHashMap.put("A", Phoneme.VovelA);
            phonemeHashMap.put("O", Phoneme.VovelO);
            phonemeHashMap.put("E", Phoneme.VovelE);
            phonemeHashMap.put("U", Phoneme.VovelU);
            phonemeHashMap.put("Y", Phoneme.VovelY);
            phonemeHashMap.put("I", Phoneme.VovelI);
            phonemeHashMap.put("E_nasal", Phoneme.VovelE_Nasal);
            phonemeHashMap.put("O_nasal", Phoneme.VovelO_Nasal);
            phonemeHashMap.put("A_rounded", Phoneme.VovelA_Rounded);
            phonemeHashMap.put("O_rounded", Phoneme.VovelO_Rounded);
            phonemeHashMap.put("U_rounded", Phoneme.VovelU_Rounded);

            phonemeHashMap.put("B", Phoneme.ConsonantB);
            phonemeHashMap.put("C", Phoneme.ConsonantC);
            phonemeHashMap.put("D", Phoneme.ConsonantD);
            phonemeHashMap.put("DZ", Phoneme.ConsonantDZ);
            phonemeHashMap.put("DZh", Phoneme.ConsonantDZh);
            phonemeHashMap.put("F", Phoneme.ConsonantF);
            phonemeHashMap.put("G", Phoneme.ConsonantG);
            phonemeHashMap.put("H", Phoneme.ConsonantH);
            phonemeHashMap.put("J", Phoneme.ConsonantJ);
            phonemeHashMap.put("K", Phoneme.ConsonantK);
            phonemeHashMap.put("L", Phoneme.ConsonantL);
            phonemeHashMap.put("W", Phoneme.ConsonantW);
            phonemeHashMap.put("M", Phoneme.ConsonantM);
            phonemeHashMap.put("N", Phoneme.ConsonantN);
            phonemeHashMap.put("P", Phoneme.ConsonantP);
            phonemeHashMap.put("R", Phoneme.ConsonantR);
            phonemeHashMap.put("S", Phoneme.ConsonantS);
            phonemeHashMap.put("T", Phoneme.ConsonantT);
            phonemeHashMap.put("V", Phoneme.ConsonantV);
            phonemeHashMap.put("Z", Phoneme.ConsonantZ);
            phonemeHashMap.put("Zh", Phoneme.ConsonantZh);
            phonemeHashMap.put("Sh", Phoneme.ConsonantSh);
            phonemeHashMap.put("Ch", Phoneme.ConsonantCh);
            phonemeHashMap.put("Sch", Phoneme.ConsonantSch);

            phonemeHashMap.put("Softened", Phoneme.ModSoftened);
            phonemeHashMap.put("Hardened", Phoneme.ModHardened);
            phonemeHashMap.put("Ioted", Phoneme.ModIoted);

            phonemeHashMap.put("Error", Phoneme.ErrorSign);
        }

    }

    public void constructLanguagesFromFile(BufferedReader reader){

        String name = "";
        ArrayList<TransliterationRule> transliterationRules = new ArrayList<>();
        ArrayList<StringReplacementRule> simplificationRules = new ArrayList<>();
        ArrayList<StringReplacementRule> complicationRules = new ArrayList<>();

        String buff, smallBuff;
        try {
            while ((buff = reader.readLine()) != null){

                if (buff.contains(langTag)) {

                    //read language name
                    if ((buff = reader.readLine()) != null){
                        if (buff.contains(nameTag) && buff.contains(nameTagCl)){
                            name = buff.substring(buff.indexOf(nameTag) + nameTag.length(), buff.indexOf(nameTagCl));
                        }
                    }

                    //skipping to transliteration rules
                    while ((buff = reader.readLine()) != null){
                        if (buff.contains(transRulesTag)){
                            break;
                        }
                    }

                    //read transliteration rules
                    while ((buff = reader.readLine()) != null){
                        if (buff.contains(transRulesTagCl)){
                            break;
                        }
                        if (buff.contains(ruleTag) && buff.contains(ruleTagCl)) {
                            smallBuff = buff.substring(buff.indexOf(ruleTag) + ruleTag.length(), buff.indexOf(ruleTagCl));

                            if (smallBuff.contains(arrow)) {
                                String[] arr = smallBuff.substring(smallBuff.indexOf(arrow) + arrow.length()).split(" ");
                                WordPhonetic sounds = new WordPhonetic();
                                for (String str : arr) {
                                    sounds.add(phonemeHashMap.get(str));
                                }
                                transliterationRules.add(new TransliterationRule(smallBuff.substring(0, smallBuff.indexOf(arrow)), sounds));
                            }
                        }
                    }

                    simplificationRules = readRules(reader, simpRulesTag, simpRulesTagCl);

                    complicationRules = readRules(reader, complRulesTag, complRulesTagCl);
                }

                if (buff.contains(langTagCl)){
                    languages.add(new Language(name, new TransliterationRulesSet(transliterationRules, simplificationRules, complicationRules)));
//System.out.println("Created a lang " + name);
                    name = "";
                    transliterationRules.clear();
                    simplificationRules.clear();
                    complicationRules.clear();
                }

            }
        }
        catch (IOException err){
            System.out.print(err.getLocalizedMessage());
        }

    }

    private ArrayList<StringReplacementRule> readRules(BufferedReader reader, String tagStart, String tagEnd) throws IOException{

        String buff, smallBuff;
        ArrayList<StringReplacementRule> list = new ArrayList<>();

        //skipping to rules
        while ((buff = reader.readLine()) != null){
            if (buff.contains(tagStart)){
                break;
            }
        }

        while ((buff = reader.readLine()) != null){
            if (buff.contains(tagEnd)){
                break;
            }
            if (buff.contains(ruleTag) && buff.contains(ruleTagCl)){
                smallBuff = buff.substring(buff.indexOf(ruleTag) + ruleTag.length(), buff.indexOf(ruleTagCl));
                list.add(new StringReplacementRule(smallBuff.substring(0, smallBuff.indexOf(arrow)), smallBuff.substring(smallBuff.indexOf(arrow) + arrow.length())));
            }
        }

        return list;
    }


    //Polish
    @Deprecated
    public Language constructPolishLanguage(){

        //Polish Language Rules
        TransliterationRule[] transliterationRulesArray = new TransliterationRule[]
                {
                        new TransliterationRule("a", new Phoneme[]{Phoneme.VovelA}),
                        new TransliterationRule("ą", new Phoneme[]{Phoneme.VovelO_Nasal}),
                        new TransliterationRule("b", new Phoneme[]{Phoneme.ConsonantB}),
                        new TransliterationRule("c", new Phoneme[]{Phoneme.ConsonantC}),
                        new TransliterationRule("d", new Phoneme[]{Phoneme.ConsonantD}),
                        new TransliterationRule("e", new Phoneme[]{Phoneme.VovelE}),
                        new TransliterationRule("ę", new Phoneme[]{Phoneme.VovelE_Nasal}),
                        new TransliterationRule("f", new Phoneme[]{Phoneme.ConsonantF}),
                        new TransliterationRule("g", new Phoneme[]{Phoneme.ConsonantG}),
                        new TransliterationRule("h", new Phoneme[]{Phoneme.ConsonantH}),
                        new TransliterationRule("i", new Phoneme[]{Phoneme.VovelI}),
                        new TransliterationRule("i", new Phoneme[]{Phoneme.ModSoftened}),
                        new TransliterationRule("j", new Phoneme[]{Phoneme.ConsonantJ}),
                        new TransliterationRule("k", new Phoneme[]{Phoneme.ConsonantK}),
                        new TransliterationRule("l", new Phoneme[]{Phoneme.ConsonantL, Phoneme.ModSoftened}),
                        new TransliterationRule("l", new Phoneme[]{Phoneme.ConsonantL}),
                        new TransliterationRule("ł", new Phoneme[]{Phoneme.ConsonantW}),
                        new TransliterationRule("m", new Phoneme[]{Phoneme.ConsonantM}),
                        new TransliterationRule("n", new Phoneme[]{Phoneme.ConsonantN}),
                        new TransliterationRule("ń", new Phoneme[]{Phoneme.ConsonantN, Phoneme.ModSoftened}),
                        new TransliterationRule("o", new Phoneme[]{Phoneme.VovelO}),
                        new TransliterationRule("p", new Phoneme[]{Phoneme.ConsonantP}),
                        new TransliterationRule("r", new Phoneme[]{Phoneme.ConsonantR}),
                        new TransliterationRule("s", new Phoneme[]{Phoneme.ConsonantS}),
                        new TransliterationRule("t", new Phoneme[]{Phoneme.ConsonantT}),
                        new TransliterationRule("u", new Phoneme[]{Phoneme.VovelU}),
                        new TransliterationRule("w", new Phoneme[]{Phoneme.ConsonantV}),
                        new TransliterationRule("x", new Phoneme[]{Phoneme.ConsonantK, Phoneme.ConsonantS}),
                        new TransliterationRule("y", new Phoneme[]{Phoneme.VovelY}),
                        new TransliterationRule("z", new Phoneme[]{Phoneme.ConsonantZ}),
                        new TransliterationRule("ż", new Phoneme[]{Phoneme.ConsonantZh}),
                        new TransliterationRule("ź", new Phoneme[]{Phoneme.ConsonantZ, Phoneme.ModSoftened}),
                        new TransliterationRule("cz", new Phoneme[]{Phoneme.ConsonantCh}),
                        new TransliterationRule("ć", new Phoneme[]{Phoneme.ConsonantCh, Phoneme.ModSoftened}),
                        new TransliterationRule("sz", new Phoneme[]{Phoneme.ConsonantSh}),
                        new TransliterationRule("ś", new Phoneme[]{Phoneme.ConsonantSch}),

                        new TransliterationRule("#", new Phoneme[]{Phoneme.ErrorSign}),
                };

        StringReplacementRule[] simplificationRulesArray = new StringReplacementRule[]
                {
                        new StringReplacementRule("ch", "h"),
                        new StringReplacementRule("krz","ksz"),
                        new StringReplacementRule("prz","psz"),
                        new StringReplacementRule("trz","tsz"),
                        new StringReplacementRule("rz","ż"),
                        new StringReplacementRule("ó", "u"),
                        new StringReplacementRule("si", "ś"),
                        new StringReplacementRule("ci", "czi"),
                        //new StringReplacementRule("zi", "żi"),
                };

        StringReplacementRule[] complicationRulesArray = new StringReplacementRule[]
                {
                        new StringReplacementRule("czi", "ci"),
                        new StringReplacementRule("ci>", "ć>"),
                        new StringReplacementRule("si>", "ś>"),
                        new StringReplacementRule("zi", "ź"),
                        new StringReplacementRule("ni>", "ń>"),
                        new StringReplacementRule("ii","i"),
                        //new StringReplacementRule("li", "l"),
                };

        TransliterationRulesSet polishRuleSet = new TransliterationRulesSet(transliterationRulesArray, simplificationRulesArray, complicationRulesArray);

        Language polish = new Language("Polish", polishRuleSet);

        return polish;

    }

    //Russian
    @Deprecated
    public Language constructRussianLanguage(){

        //Russian Language Rules
        TransliterationRule[] transliterationArray = new TransliterationRule[]
                {
                        new TransliterationRule("а", new Phoneme[]{Phoneme.VovelA}),
                        new TransliterationRule("б", new Phoneme[]{Phoneme.ConsonantB}),
                        new TransliterationRule("в", new Phoneme[]{Phoneme.ConsonantV}),
                        new TransliterationRule("г", new Phoneme[]{Phoneme.ConsonantG}),
                        new TransliterationRule("д", new Phoneme[]{Phoneme.ConsonantD}),
                        new TransliterationRule("е", new Phoneme[]{Phoneme.ModSoftened, Phoneme.VovelE}),
                        new TransliterationRule("ё", new Phoneme[]{Phoneme.ModSoftened, Phoneme.VovelO}),
                        new TransliterationRule("ж", new Phoneme[]{Phoneme.ConsonantZh}),
                        new TransliterationRule("з", new Phoneme[]{Phoneme.ConsonantZ}),
                        new TransliterationRule("и", new Phoneme[]{Phoneme.VovelI}),
                        new TransliterationRule("й", new Phoneme[]{Phoneme.ConsonantJ}),
                        new TransliterationRule("к", new Phoneme[]{Phoneme.ConsonantK}),
                        new TransliterationRule("л", new Phoneme[]{Phoneme.ConsonantL}),
                        new TransliterationRule("м", new Phoneme[]{Phoneme.ConsonantM}),
                        new TransliterationRule("н", new Phoneme[]{Phoneme.ConsonantN}),
                        new TransliterationRule("о", new Phoneme[]{Phoneme.VovelO}),
                        new TransliterationRule("п", new Phoneme[]{Phoneme.ConsonantP}),
                        new TransliterationRule("р", new Phoneme[]{Phoneme.ConsonantR}),
                        new TransliterationRule("с", new Phoneme[]{Phoneme.ConsonantS}),
                        new TransliterationRule("т", new Phoneme[]{Phoneme.ConsonantT}),
                        new TransliterationRule("у", new Phoneme[]{Phoneme.VovelU}),
                        new TransliterationRule("ф", new Phoneme[]{Phoneme.ConsonantF}),
                        new TransliterationRule("х", new Phoneme[]{Phoneme.ConsonantH}),
                        new TransliterationRule("ц", new Phoneme[]{Phoneme.ConsonantC}),
                        new TransliterationRule("ч", new Phoneme[]{Phoneme.ConsonantCh}),
                        new TransliterationRule("ш", new Phoneme[]{Phoneme.ConsonantSh}),
                        new TransliterationRule("щ", new Phoneme[]{Phoneme.ConsonantSch}),
                        new TransliterationRule("ъ", new Phoneme[]{Phoneme.ModHardened}),
                        new TransliterationRule("ы", new Phoneme[]{Phoneme.VovelY}),
                        new TransliterationRule("ь", new Phoneme[]{Phoneme.ModSoftened}),
                        new TransliterationRule("э", new Phoneme[]{Phoneme.VovelE}),
                        new TransliterationRule("ю", new Phoneme[]{Phoneme.ModSoftened, Phoneme.VovelU}),
                        new TransliterationRule("я", new Phoneme[]{Phoneme.ModSoftened, Phoneme.VovelA}),
                    //Rules to cover phonemes for reverse transliteration to Russian
                        new TransliterationRule("л", new Phoneme[]{Phoneme.ConsonantW}),
                        new TransliterationRule("о", new Phoneme[]{Phoneme.VovelO_Nasal}),
                        new TransliterationRule("э", new Phoneme[]{Phoneme.VovelE_Nasal}),
                        new TransliterationRule("#", new Phoneme[]{Phoneme.ErrorSign}),
                };

        StringReplacementRule[] simplificationRulesArray = new StringReplacementRule[]
                {
                        new StringReplacementRule("<я", "<йа"),
                        new StringReplacementRule("<е", "<йэ"),
                        new StringReplacementRule("<ё", "<йо"),
                        new StringReplacementRule("<ю", "<йу"),
                        new StringReplacementRule("ия", "ийа"),
                        new StringReplacementRule("оя", "ойа"),
                        new StringReplacementRule("уя", "уйа"),
                        new StringReplacementRule("ию", "ийу"),
                        new StringReplacementRule("уе", "уйэ"),
                        new StringReplacementRule("ие", "ийэ"),
                        new StringReplacementRule("эе", "эйэ"),
                        new StringReplacementRule("ия", "ийа"),
                        new StringReplacementRule("оё", "ойо"),
                        new StringReplacementRule("уё", "уйо"),
                        new StringReplacementRule("иё", "ийо"),
                        new StringReplacementRule("эё", "эйо"),
                        new StringReplacementRule("<ю", "<йу"),
                        new StringReplacementRule("ъя", "йа"),
                        new StringReplacementRule("ъю", "йу"),
                        new StringReplacementRule("ъе", "йэ"),
                        new StringReplacementRule("ъё", "йо"),
                        new StringReplacementRule("ьо", "ьйо"),
                };

        StringReplacementRule[] complicationRulesArray = new StringReplacementRule[]
                {
                        new StringReplacementRule("ьэ", "е"),
                        new StringReplacementRule("ьу", "ю"),
                        new StringReplacementRule("ьа", "я"),
                        new StringReplacementRule("ьо", "ё"),
                        new StringReplacementRule("иэ", "е"),
                };

        TransliterationRulesSet RussianRulesSet = new TransliterationRulesSet(transliterationArray, simplificationRulesArray, complicationRulesArray);

        return new Language("Russian", RussianRulesSet);
    }

    ArrayList<Language> getLanguageList(){
        return languages;
    }

}
