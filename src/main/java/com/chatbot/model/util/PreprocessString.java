package com.chatbot.model.util;

import java.util.Comparator;

public class PreprocessString {

    public static String replacePolishCharsAndLowerCase(String a) {

        String result = a;
        char[] pol = {'ą', 'ć', 'ę', 'ó', 'ł', 'ń', 'ż', 'ź', 'ś'};
        char[] res = {'a', 'c', 'e', 'o', 'l', 'n', 'z', 'z', 's'};
        for (int i = 0; i < pol.length; i++)
            result = result.replace(pol[i], res[i]);
        return result.toLowerCase();
    }

    public String removeWhite(String a) {
        return a.replaceAll("\\s+", "").trim();
    }


    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    public static int computeLevenshteinDistance(String str1, String str2) {
        int[][] distance = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++)
            distance[i][0] = i;
        for (int j = 1; j <= str2.length(); j++)
            distance[0][j] = j;

        for (int i = 1; i <= str1.length(); i++)
            for (int j = 1; j <= str2.length(); j++)
                distance[i][j] = minimum(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1] + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1));

        return distance[str1.length()][str2.length()];
    }

    public static Comparator<String> lengthComparator = new Comparator<String>() {
        @Override
        public int compare(String s, String t1) {
            int firstSentenceLength = s.length();
            int secondSentenceLength = t1.length();
            if (firstSentenceLength > secondSentenceLength) return -1;
            if (firstSentenceLength < secondSentenceLength) return 1;
            return 0;
        }
    };

    public static String removePunctuationMarks(String s) {
        return s.replace("?", "").replace(",", "").replace(".", "").replace(";", "").replace("!", "");

    }


}
