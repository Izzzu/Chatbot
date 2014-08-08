package com.academicprojects.model;

import java.util.*;
import java.util.Map.Entry;

public class Personality {
	private Map<String, Integer> types = new LinkedHashMap<String,Integer>();
	
	public Map<String, Integer> getTypes() {
		return types;
	}

	public void setTypes(Map<String, Integer> types) {
		this.types = types;
	}

	public Personality()
	{
		types.put("dom", 0);
		types.put("dos", 0);
		types.put("mak", 0);
		types.put("min", 0);
		types.put("ins", 0);
		types.put("sys", 0);
		types.put("odk", 0);
		types.put("kon", 0);
		types.put("wer", 0);
		types.put("har", 0);
		types.put("emp", 0);
		types.put("rze", 0);
		types.put("odw", 0);
		types.put("ase", 0);
		types.put("hoj", 0);
		types.put("osz", 0);
		types.put("faw", 0);
		types.put("row", 0);
	}
	
	/**
	 * 
	 * @param key
	 * @param p
	 */
	public void setNewPersonalityType(String key, int p)
	{
		if(types.containsKey(key)) {
			 types.put(key,p);
			}
			else {
				throw new UnsupportedOperationException("This personality type doesn't exist");
			}
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public int getByPersonalityType(String key)
	{
		return types.get(key).intValue();
	}

	/**
	 * 
	 * @return
	 */
	public String getMainType()
	{
		Map<String, Integer> sortedMap = sortByComparator(types);
		return sortedMap.keySet().toArray()[0].toString();
	}
	
	/**
	 * 
	 * @param unsortMap
	 * @return
	 */
	private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap)
    {

        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>(){
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2)
            {
               
                    return o2.getValue().compareTo(o1.getValue());
                
            }
        });
		// Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
	
	/**
	 * 
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(System.getProperty("line.separator"));
		for (int i = 0; i<types.keySet().size(); i++) {
			sb.append(types.keySet().toArray()[i].toString().toLowerCase());
			sb.append(" - ");
			sb.append(types.get(types.keySet().toArray()[i].toString()));
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
		
	}
	

}
