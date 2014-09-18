package com.academicprojects.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Cookiemonster on 2014-09-15.
 */
@Getter
public class PolishDictionary {
    List<Record> records = new LinkedList<Record>();

    public PolishDictionary() {
        File file = new File("C:\\Users\\Cookiemonster\\workspace\\Chatbot\\src\\main\\resources\\dictionary.txt");
        createDictionaryFromFile(file);
    }

    private void createDictionaryFromFile(File file) {
        try {
            String line = "";

            BufferedReader br = new BufferedReader(new FileReader(file));
            //FileInputStream fis = new FileInputStream(file);

            while((line = br.readLine())!=null) {
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
        if (cuttedLine.length==3) {
            records.add(new Record(cuttedLine[0], cuttedLine[1], createWordDetails(cuttedLine[2])));
        }
        else {
            throw TooManyFieldsInRecordException();
        }
    }

    private WordDetails createWordDetails(String s) throws Exception {
        String[] parts = s.split(":",2);
        if(parts.length>=2) {
            return new WordDetails(LanguagePart.matchLAnguagePart(parts[0]), parts[1]);
        }
        else if(parts.length==1) return new WordDetails(LanguagePart.matchLAnguagePart(parts[0]), "");
        else {
            throw TooFewFieldsInRecordException();
        }
    }

    private Exception TooManyFieldsInRecordException() {
        return new Exception("Too many fields in line detected during file parsing.");
    }

    private Exception TooFewFieldsInRecordException() {
        return new Exception("Too few fields in line detected during file parsing.");
    }

    @Getter
    @AllArgsConstructor
    protected class Record {
        String word;
        String mainWord;
        WordDetails form;
    }
}
