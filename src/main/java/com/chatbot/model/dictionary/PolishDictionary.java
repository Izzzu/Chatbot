package com.chatbot.model.dictionary;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.*;

import static com.chatbot.model.dictionary.Genre.*;
import static com.chatbot.model.dictionary.GrammaCase.*;
import static com.chatbot.model.dictionary.LanguagePart.*;
import static com.chatbot.model.util.PreprocessString.replacePolishCharsAndLowerCase;
import static java.lang.Character.isUpperCase;

@Getter
public class PolishDictionary {
    List<Record> recordsWithoutVerbs = new ArrayList<Record>();
    List<Record> verbs = new ArrayList<Record>();
    List<Record> pronouns = new ArrayList<>();
    Set<String> verbsToPharaprase = new HashSet<String>();
    private List<String> names = new ArrayList<String>();
    private Map<String, List<Record>> mainWordToOtherWords = new HashMap<>();
    List<Record> allRecords = new ArrayList<>();

    public PolishDictionary() {
        File file = new File("src/main/resources/dictionary.txt");
        createDictionaryFromFile(file);
        fillVerbsToPharaprase();
        fillPronouns();
        concatRecords();
        matchMainWordToOtherWords();
    }

    private void concatRecords() {
        allRecords.addAll(recordsWithoutVerbs);
        allRecords.addAll(verbs);
    }

    public List<Record> getAllRecords() {
        return ImmutableList.copyOf(allRecords);
    }

    private void matchMainWordToOtherWords() {

        for (Record record : allRecords) {
            List<Record> words = mainWordToOtherWords.get(record.getMainWord());
            if (words == null) {
                words = new ArrayList<>();
            }
            words.add(record);
            mainWordToOtherWords.put(record.getMainWord(), words);
        }
    }

    private void fillPronouns() {
        for (Record recordWithoutVerbs : recordsWithoutVerbs) {
            if (recordWithoutVerbs.getForm().getLanguagePart().equals(PRONOUN))
                pronouns.add(recordWithoutVerbs);
        }
    }

    private void fillVerbsToPharaprase() {
        for (Record verb : verbs) {
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
            String mainWord = replacePolishCharsAndLowerCase(cuttedLine[1]);
            if (isUpperCase(mainWord.substring(0, 1).charAt(0))) names.add(mainWord);
            Record record = new Record(cuttedLine[0], cuttedLine[1], createWordDetails(cuttedLine[2]));
            if (record.getForm().getLanguagePart().equals(VERB)) {
                verbs.add(record);
            } else {
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
        if (languagePart.equals(ADVERB)) {
            Grade grade = parts.length > 1 ? matchGrade(parts[1]) : Grade.DEFAULT;
            return new WordDetails(languagePart, grade, GrammaCase.DEFAULT, SingularOrPlural.N_A, NEUTER, VerbForm.DEFAULT, GrammaPerson.DEFAULT);
        } else if (languagePart.equals(ADJECTIV)) {
            if (parts.length >= 5) {
                return new WordDetails(languagePart, matchGrade(parts[4]), GrammaCase.DEFAULT, matchSingularOrPlural(parts[1]),
                        NEUTER, VerbForm.DEFAULT, GrammaPerson.DEFAULT);
            } else return new WordDetails(languagePart);
        } else if (languagePart.equals(SUBSTANTIV)) {
            if (parts.length >= 4)
                return new WordDetails(languagePart, Grade.DEFAULT, matchGrammaCase(parts[2]), matchSingularOrPlural(parts[1]),
                        matchGenre(parts[3]), VerbForm.DEFAULT, GrammaPerson.DEFAULT);
        } else if (languagePart.equals(VERB)) {
            Genre genre = matchGenre(parts[3]);
            GrammaPerson grammaPerson = parts.length >= 5 ? matchGrammaPerson(parts[4]) : GrammaPerson.DEFAULT;
            if (genre.equals(NEUTER) && grammaPerson.equals(GrammaPerson.DEFAULT))
                grammaPerson = matchGrammaPerson(parts[3]);
            return new WordDetails(languagePart, Grade.DEFAULT, GrammaCase.DEFAULT, matchSingularOrPlural(parts[2]), genre
                    , matchVerbForm(parts[1]), grammaPerson);
        } else if (languagePart.equals(PRONOUN)) {
            SingularOrPlural singularOrPlural = matchSingularOrPlural(parts[1]);
            GrammaCase grammaCase = matchGrammaCase(parts[2]);
            return new WordDetails(languagePart, Grade.DEFAULT, grammaCase, singularOrPlural, NEUTER, VerbForm.DEFAULT, GrammaPerson.DEFAULT);
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
        if (part.contains("m")) return MALE;
        else if (part.contains("f")) return FEMALE;
        else return NEUTER;
    }

    private GrammaCase matchGrammaCase(String part) {

        if (part.equals("nom")) return NOMINATIV;
        else if (part.equals("dat")) return DATIVE;
        else if (part.equals("gen")) return GENITIVE;
        else if (part.equals("acc")) return ACCUSATIVE;
        else if (part.equals("inst")) return INSTRUMENTAL;
        else if (part.equals("loc")) return LOCATIVE;
        else if (part.equals("voc")) return VOCATIVE;
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
        if (shortcut.equals("conj")) return CONJUCTION;
        else if (shortcut.equals("ppas") || shortcut.equals("pact") || shortcut.equals("pcon") || shortcut.equals("pant"))
            return PARTICIPLE;
        else if (shortcut.equals("adj") || shortcut.equals("adjc") || shortcut.equals("adjp"))
            return ADJECTIV;
        else if (shortcut.equals("adv")) return ADVERB;
        else if (shortcut.equals("subst") || shortcut.equals("ger")) return SUBSTANTIV;
        else if (shortcut.equals("prep")) return PREPOSITION;
        else if (shortcut.equals("pred")) return PREDICATIV;
        else if (shortcut.equals("ppron12") || shortcut.equals("ppron3")) return PRONOUN;
        else if (shortcut.equals("num")) return NUMERAL;
        else if (shortcut.equals("verb")) return VERB;
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

        if (!foundVerbs.isEmpty()) return foundVerbs.get(0);
        else return new Record();
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
        List<Record> recordWithMatchingMainWord = findVerbRecordsByMainWord(mainWord);
        GrammaPerson oppositeGrammaPerson;
        if (currentGrammaPerson.equals(GrammaPerson.PRIMARY)) oppositeGrammaPerson = GrammaPerson.SECOND;
        else {
            if (currentGrammaPerson.equals(GrammaPerson.SECOND)) oppositeGrammaPerson = GrammaPerson.PRIMARY;
            else return new Record();
        }
        for (int i = 0; i < recordWithMatchingMainWord.size(); i++) {
            if (recordWithMatchingMainWord.get(i).getForm().getGrammaPerson().equals(oppositeGrammaPerson)
                    && recordWithMatchingMainWord.get(i).getForm().getSingularOrPlural().equals(singularOrPlural)
                    && recordWithMatchingMainWord.get(i).getForm().getVerbForm().equals(verbForm)
                    && recordWithMatchingMainWord.get(i).getForm().getGenre().equals(genre))
                return recordWithMatchingMainWord.get(i);
        }
        return new Record();
    }

    public List<Record> findVerbRecordsByMainWord(String mainWord) {
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

    public ImmutableSet<String> convertToMainWords(final String userWord) {
        List<Record> records = this.allRecords;


        Predicate<Record> byUserWord = new Predicate<Record>() {
            @Override
            public boolean apply(Record record) {
                return record.getWord().equals(userWord);
            }
        };
        return FluentIterable.from(records).filter(byUserWord).transform(toMainWord).toSet();
    }

    public ImmutableSet<String> convertToMainWordsWithoutPolishChars(final String userWord) {
        List<Record> records = this.allRecords;

        Predicate<Record> byUserWordWithoutPolishChars = new Predicate<Record>() {
            @Override
            public boolean apply(Record record) {
                return replacePolishCharsAndLowerCase(record.getWord()).equals(userWord);
            }
        };
        return FluentIterable.from(records).filter(byUserWordWithoutPolishChars).transform(toMainWord).transform(withoutPolishChars).toSet();
    }

    Function<String, String> withoutPolishChars = new Function<String, String>() {
        @Override
        public String apply(String s) {
            return replacePolishCharsAndLowerCase(s);
        }
    };

    Function<Record, String> toMainWord = new Function<Record, String>() {
        @Override
        public String apply(Record record) {
            return record.getMainWord();
        }
    };


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public class Record {
        String word = "";
        String mainWord = "";
        WordDetails form = new WordDetails(LanguagePart.DEFAULT);

        public boolean isRightToPharaprasize() {
            if (this.getForm().getLanguagePart().equals(VERB) && this.getForm().getGrammaPerson().equals(GrammaPerson.PRIMARY))
                return true;
            else return false;
        }

        public boolean isEmpty() {
            if (this.getWord().isEmpty()) return true;
            return false;
        }

        public Genre getGenre() {
            return form.getGenre();
        }
    }
}
