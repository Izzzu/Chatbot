package com.academicprojects.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class Personality {

    private List<PersonalityType> types = new ArrayList<PersonalityType>();
    private int wholePoints;

    public int getWholePoints() {

        for (int i = 0; i < types.size(); i++) {
            wholePoints += types.get(i).getLevel();
        }
        return wholePoints;
    }

    private int getPercentageType(int id) {
        int sum = getWholePoints();
        int typePoints = getById(id).getLevel();
        return typePoints / sum * 100;
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
            types.add(new PersonalityType(Integer.valueOf(tab[0]), tab[1], tab[2], 0));
        }
        br.close();
    }

    public List<PersonalityType> getTypes() {
        return types;
    }

    public void setTypes(List<PersonalityType> types) {
        this.types = types;
    }


    public void setNewPersonalityType(int id, int p) {
        for (int i = 0; i < types.size(); i++) {
            if (i == id) {
                types.get(i).updateType(p);
                return;
            }
        }
        throw new UnsupportedOperationException("This personality type doesn't exist");
    }

    public PersonalityType getById(int id) {
        return types.get(id);
    }

    /**
     * @return
     */
    public PersonalityType getMainType() {
        PersonalityType mainType = types.get(0);
        int max = 0;
        for(int i = 0; i<types.size(); i++) {
            if(types.get(i).getLevel() > mainType.getLevel()) mainType = types.get(i);
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
        for (int i = 0; i < types.size(); i++) {
            System.out.println(types.size());
            sb.append(types.get(i).getShortDescription().toLowerCase());
            sb.append(" - ");
            sb.append(types.get(i).getLevel());
            sb.append(System.getProperty("line.separator"));
        }
        return sb.toString();

    }


}
