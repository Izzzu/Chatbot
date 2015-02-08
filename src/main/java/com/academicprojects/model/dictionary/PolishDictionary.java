package com.academicprojects.model.dictionary;

import com.academicprojects.util.PreprocessString;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.*;

import static java.lang.Character.isUpperCase;



@Getter
public class PolishDictionary {
    List<Record> recordsWithoutVerbs = new ArrayList<Record>();
    List<Record> verbs = new ArrayList<Record>();
    Set<String> verbsToPharaprase = new HashSet<String>();
    private List<String> names = new ArrayList<String>();

    public PolishDictionary() {
        File file = new File("src/main/resources/dictionary.txt");
        createDictionaryFromFile(file);
        fillVerbsToPharaprase();
    }

    private void fillVerbsToPharaprase() {
        for(Record verb: verbs) {
            if (verb.isRightToPharaprasize()) {
                verbsToPharaprase.add(verb.getWord());
            }
        }
    }

    private void createDictionaryFromFile(File file) {
        try {
            String line = "";

            BufferedReader br = new BufferedReader(new FileReader(file));
            //FileInputStream fis = new FileInputStream(file);

            while ((line = br.readLine()) != null) {
                addRecordToDictionary(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRecordToDictionary(String line) throws Exception {
        String[] cuttedLine = line.split("\t");
        if (cuttedLine.length == 3) {
            String mainWord = PreprocessString.replacePolishCharsAndLowerCase(cuttedLine[1]);
            if(isUpperCase(mainWord.substring(0, 1).charAt(0))) names.add(mainWord);
            Record record = new Record(cuttedLine[0], cuttedLine[1], createWordDetails(cuttedLine[2]));
            if (record.getForm().getLanguagePart().equals(LanguagePart.VERB)) {
                verbs.add(record);
            }
            else {
                recordsWithoutVerbs.add(record);
            }
        } else {
            throw TooManyFieldsInRecordException();
        }
    }

    private WordDetails createWordDetails(String s) throws Exception {
        String[] cutts = s.split("\\+");
        String[] parts = cutts[0].split(":");
        LanguagePart languagePart = matchLanguagePart(parts[0]);
        return createDetails(languagePart, parts);
    }

    private WordDetails createDetails(LanguagePart languagePart, String[] parts) throws Exception {
        if (languagePart.equals(LanguagePart.ADVERB)) {
            Grade grade = parts.length > 1 ? matchGrade(parts[1]) : Grade.DEFAULT;
            return new WordDetails(languagePart, grade, GrammaCase.DEFAULT, SingularOrPlural.N_A, Genre.NEUTER, VerbForm.DEFAULT, GrammaPerson.DEFAULT);
        } else if (languagePart.equals(LanguagePart.ADJECTIV)) {
            if (parts.length >= 5) {
                return new WordDetails(languagePart, matchGrade(parts[4]), GrammaCase.DEFAULT, matchSingularOrPlural(parts[1]),
                        Genre.NEUTER, VerbForm.DEFAULT, GrammaPerson.DEFAULT);
            } else return new WordDetails(languagePart);
        } else if (languagePart.equals(LanguagePart.SUBSTANTIV)) {
            if (parts.length >= 4)
                return new WordDetails(languagePart, Grade.DEFAULT, matchGrammaCase(parts[2]), matchSingularOrPlural(parts[1]),
                        matchGenre(parts[3]), VerbForm.DEFAULT, GrammaPerson.DEFAULT);
        } else if (languagePart.equals(LanguagePart.VERB)) {
            Genre genre = matchGenre(parts[3]);
            GrammaPerson grammaPerson = parts.length>=5 ? matchGrammaPerson(parts[4]) : GrammaPerson.DEFAULT;
            if(genre.equals(Genre.NEUTER) && grammaPerson.equals(GrammaPerson.DEFAULT)) grammaPerson = matchGrammaPerson(parts[3]);
            return new WordDetails(languagePart, Grade.DEFAULT, GrammaCase.DEFAULT, matchSingularOrPlural(parts[2]), genre
                    , matchVerbForm(parts[1]), grammaPerson);


        } else return new WordDetails(languagePart);

        throw TooFewFieldsInRecordException();

    }

    private GrammaPerson matchGrammaPerson(String part) {
        if (part.equals("pri")) return GrammaPerson.PRIMARY;
        else if (part.equals("sec")) return GrammaPerson.SECOND;
        else if (part.equals("ter")) return GrammaPerson.THIRD;
        else return GrammaPerson.DEFAULT;

    }

    private VerbForm matchVerbForm(String part) {
        if (part.equals("inf")) return VerbForm.INFINITIVE;
        else if (part.equals("imps")) return VerbForm.IMPERSONAL;
        else if (part.equals("impt")) return VerbForm.IMPERATIVE;
        else if (part.equals("praet")) return VerbForm.PAST;
        else if (part.equals("fin")) return VerbForm.PRESENT;
        else return VerbForm.DEFAULT;
    }

    private Genre matchGenre(String part) {
        String[] cases = part.split(".");
        if (part.contains("m")) return Genre.MALE;
        else if (part.contains("f")) return Genre.FEMALE;
        else return Genre.NEUTER;
    }

    private GrammaCase matchGrammaCase(String part) {

        if (part.equals("nom")) return GrammaCase.NOMINATIV;
        else if (part.equals("dat")) return GrammaCase.DATIVE;
        else if (part.equals("gen")) return GrammaCase.GENITIVE;
        else if (part.equals("acc")) return GrammaCase.ACCUSATIVE;
        else if (part.equals("inst")) return GrammaCase.INSTRUMENTAL;
        else if (part.equals("loc")) return GrammaCase.LOCATIVE;
        else if (part.equals("voc")) return GrammaCase.VOCATIVE;
        else return GrammaCase.DEFAULT;
    }

    private Grade matchGrade(String part) {
        if (part.equals("pos")) return Grade.POSITIVE;
        else if (part.equals("com")) return Grade.COMPAR;
        else if (part.equals("sup")) return Grade.SUPREME;
        else return Grade.DEFAULT;
    }

    private Exception TooManyFieldsInRecordException() {
        return new Exception("Too many fields in line detected during file parsing.");
    }

    private Exception TooFewFieldsInRecordException() {
        return new Exception("Too few fields in line detected during file parsing.");
    }

    private LanguagePart matchLanguagePart(String shortcut) {
        if (shortcut.equals("conj")) return LanguagePart.CONJUCTION;
        else if (shortcut.equals("ppas") || shortcut.equals("pact") || shortcut.equals("pcon") || shortcut.equals("pant"))
            return LanguagePart.PARTICIPLE;
        else if (shortcut.equals("adj") || shortcut.equals("adjc") || shortcut.equals("adjp"))
            return LanguagePart.ADJECTIV;
        else if (shortcut.equals("adv")) return LanguagePart.ADVERB;
        else if (shortcut.equals("subst") || shortcut.equals("ger")) return LanguagePart.SUBSTANTIV;
        else if (shortcut.equals("prep")) return LanguagePart.PREPOSITION;
        else if (shortcut.equals("pred")) return LanguagePart.PREDICATIV;
        else if (shortcut.equals("ppron12") || shortcut.equals("ppron3")) return LanguagePart.PRONOUN;
        else if (shortcut.equals("num")) return LanguagePart.NUMERAL;
        else if (shortcut.equals("verb")) return LanguagePart.VERB;
        else return LanguagePart.DEFAULT;
    }

    private SingularOrPlural matchSingularOrPlural(String shortcut) {
        if (shortcut.equals("pl")) return SingularOrPlural.PLURAL;
        else if (shortcut.equals("sg")) return SingularOrPlural.SINGULAR;
        else return SingularOrPlural.N_A;
    }


    Function<String, Integer> lengthFunction = new Function<String, Integer>() {
        public Integer apply(String string) {
            return string.length();
        }
    };

    public Record findVerb(final String userAnswer) {

        ImmutableList<Record> foundVerbs = FluentIterable.from(verbs).filter(filterVerbs(userAnswer)).toList();

        if(!foundVerbs.isEmpty()) return foundVerbs.get(0);
        else  return new Record();
       /* for(int i =0; i< verbs.size(); i++) {

            if (verbs.get(i).getWord().equals(userAnswer)) return verbs.get(i);
        }*/


    }

    private com.google.common.base.Predicate<Record> filterVerbs(final String userAnswer) {
        return new com.google.common.base.Predicate<Record>() {
            public boolean apply(Record record) {
                return record.getWord().equals(userAnswer);
            }
        };
    }

    public Record findVerbFromMainWordAndMatchOppositePerson(GrammaPerson currentGrammaPerson, String mainWord, SingularOrPlural singularOrPlural, VerbForm verbForm, Genre genre) {
        List<Record> recordWithMatchingMainWord = findRecordsByMainWord(mainWord);
        GrammaPerson oppositeGrammaPerson;
        if (currentGrammaPerson.equals(GrammaPerson.PRIMARY)) oppositeGrammaPerson = GrammaPerson.SECOND;
        else {
            if(currentGrammaPerson.equals(GrammaPerson.SECOND)) oppositeGrammaPerson = GrammaPerson.PRIMARY;
            else return new Record();
        }
        for (int i = 0; i<recordWithMatchingMainWord.size(); i++) {
            if(recordWithMatchingMainWord.get(i).getForm().getGrammaPerson().equals(oppositeGrammaPerson)
                    && recordWithMatchingMainWord.get(i).getForm().getSingularOrPlural().equals(singularOrPlural)
                    && recordWithMatchingMainWord.get(i).getForm().getVerbForm().equals(verbForm)
                    && recordWithMatchingMainWord.get(i).getForm().getGenre().equals(genre))
                return recordWithMatchingMainWord.get(i);
        }
        return new Record();
    }

    public List<Record> findRecordsByMainWord(String mainWord) {
        List<Record> foundRecords = new LinkedList<Record>();
        for (int i = 0; i < this.getVerbs().size(); i++) {
            if (verbs.get(i).getMainWord().equals(mainWord)) foundRecords.add(verbs.get(i));
        }
        return foundRecords;
    }

    public String findMainVerb(String word) {

        for (int i = 0; i < this.getVerbs().size(); i++) {
            if (verbs.get(i).getWord().equals(word)) return verbs.get(i).getMainWord();
        }
        return "";
    }

    public Record getEmptyRecord() {
        return new Record();
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public class Record {
        String word = "";
        String mainWord = "";
        WordDetails form = new WordDetails(LanguagePart.DEFAULT);

        public boolean isRightToPharaprasize() {
            if(this.getForm().getLanguagePart().equals(LanguagePart.VERB) && this.getForm().getGrammaPerson().equals(GrammaPerson.PRIMARY))
                return true;
            else return false;
        }

        public boolean isEmpty()
        {
            if(this.getWord().isEmpty()) return true;
            return false;
        }
    }
}
