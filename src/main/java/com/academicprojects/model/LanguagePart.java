package com.academicprojects.model;
/*
* adj - przymiotnik (np. „niemiecki”)
        * adjc - przymiotnik przedykatywny (np. „ciekaw”, „dłużen”)
        * adjp - przymiotnik poprzyimkowy (np. „niemiecku”)
        * adv - przysłówek (np. „głupio”)
        * burk - burkinostka (np. „Burkina Faso”)
        * depr - forma deprecjatywna
        * ger - rzeczownik odsłowny
        * conj - spójnik łączący zdania współrzędne
        * comp - spójnik wprowadzający zdanie podrzędne
        * num - liczebnik
        * pact - imiesłów przymiotnikowy czynny
        * pant - imiesłów przysłówkowy uprzedni
        * pcon - imiesłów przysłówkowy współczesny
        * ppas - imiesłów przymiotnikowy bierny
        * ppron12 - zaimek nietrzecioosobowy
        * ppron3 - zaimek trzecioosobowy
        * pred - predykatyw (np. „trzeba”)
        * prep - przyimek
        * siebie - zaimek "siebie"
        * subst - rzeczownik
        * verb - czasownik
        * brev - skrót
        * interj - wykrzyknienie
        * qub - kublik (np. „nie” lub „tak”)
*/


public enum LanguagePart {

    VERB,
    SUBSTANTIV,
    ADJECTIV,
    ADVERB,
    CONJUCTION,
    PARTICIPLE,
    PREPOSITION,
    PREDICATIV,
    PRONOUN,
    NUMERAL,
    DEFAULT,;

    public static LanguagePart matchLAnguagePart(String shortcut) {
        if (shortcut.equals("conj")) return CONJUCTION;
        else if (shortcut.equals("ppas") || shortcut.equals("pact") || shortcut.equals("pcon") || shortcut.equals("pant"))
            return PARTICIPLE;
        else if (shortcut.equals("adj") || shortcut.equals("adjc") || shortcut.equals("adjp")) return ADJECTIV;
        else if (shortcut.equals("adv")) return ADVERB;
        else if (shortcut.equals("subst")) return SUBSTANTIV;
        else if (shortcut.equals("prep")) return PREPOSITION;
        else if (shortcut.equals("pred")) return PREDICATIV;
        else if (shortcut.equals("ppron12") || shortcut.equals("ppron3")) return PRONOUN;
        else if (shortcut.equals("num")) return PRONOUN;
        else if (shortcut.equals("verb")) return VERB;
        else return DEFAULT;
    }



}
