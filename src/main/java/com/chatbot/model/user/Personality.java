package com.chatbot.model.user;

import com.chatbot.model.capabilities.PersonalityId;
import com.google.common.collect.ImmutableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import static com.chatbot.model.capabilities.PersonalityId.*;

public class Personality {

    private Map<PersonalityId, PersonalityType> personalityTypes = new HashMap<>();

    public double getWholePoints() {

        double wholePoints = 0.0;
        for (PersonalityType personalityType : personalityTypes.values()) {
            wholePoints += personalityType.getLevel();

        }
        return wholePoints;
    }


    public Personality() {
        try {
            getPersonalitiesFromFile(new File("src/main/resources/personalityTypes.csv"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getPersonalitiesFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String s = null;
        while ((s=br.readLine()) != null)
        {
            String [] tab = s.split(" ");
            personalityTypes.put(Enum.valueOf(PersonalityId.class, tab[2].toUpperCase()), new PersonalityType(Integer.valueOf(tab[0]), tab[1], Enum.valueOf(PersonalityId.class, tab[2].toUpperCase()), 0));
        }
        br.close();
    }

    public List<PersonalityType> getTypes() {
        List<PersonalityType> types = new ArrayList<>();
        types.addAll(personalityTypes.values());
        Collections.sort(types, new Personality.SortByPercentageLevel());
        return ImmutableList.copyOf(types);
    }

    public static class SortByPercentageLevel implements Comparator<PersonalityType> {

        @Override
        public int compare(PersonalityType o, PersonalityType t1) {
            return o.compareTo(t1);
        }
    }

    public void setTypes(List<PersonalityType> types) {
    }

    public void setNewPersonalityType(PersonalityId id, double level) {
        PersonalityType.wholePoints = getWholePoints();
        PersonalityType personalityType = personalityTypes.get(id);
        if(personalityType == null) throw new UnsupportedOperationException("This personality type doesn't exist");
        PersonalityType updatedType = personalityType.copyWithUpdatedType(level);
        personalityTypes.put(id, updatedType);
    }

    public PersonalityType getById(PersonalityId id) {
        return personalityTypes.get(id);
    }

    /**
     * @return
     */
    public PersonalityType getMainType() {
        PersonalityType mainType = personalityTypes.get(DOMINUJACY);
        for (PersonalityId id : personalityTypes.keySet()) {
            PersonalityType currentType = personalityTypes.get(id);
            if(currentType.getLevel() > mainType.getLevel()) mainType = currentType;
        }
        return mainType;
    }
    /**
     * @param unsortMap
     * @return
     */
    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {

        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>() {
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    /**
     *
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty("line.separator"));
        for (PersonalityId personalityId : personalityTypes.keySet()) {


            sb.append(personalityId);
            sb.append(" - ");
            sb.append(getPercentageAmount(personalityId));
            sb.append(System.getProperty("line.separator"));
        }
        return sb.toString();

    }

    private int getPercentageAmount(PersonalityId id) {
        PersonalityType personalityType = personalityTypes.get(id);
        double wholePoints = getWholePoints();
        return (int) (personalityType.getLevel()*100/ wholePoints);
    }

}
