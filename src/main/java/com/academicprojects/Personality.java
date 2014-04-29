package com.academicprojects;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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
		types.put("DOM", 0);
		types.put("MAK", 0);
		types.put("INS", 0);
		types.put("ODK", 0);
		types.put("WER", 0);
		types.put("EMP", 10);
		types.put("ODW", 0);
		types.put("HOJ", 0);
		types.put("FAW", 0);
		types.put("DOS", 0);
		types.put("MIN", 0);
		types.put("SYS", 0);
		types.put("KON", 0);
		types.put("HAR", 0);
		types.put("RZE", 0);
		types.put("ASE", 0);
		types.put("OSZ", 0);
		types.put("ROW", 0);
		
	}
	
	public void setByKey(String key, int p)
	{
		if(types.containsKey(key)) {
			 types.put(key,p);
			}
			else {
				throw new UnsupportedOperationException("This personality type doesn't exist");
			}
	}
	public int getByKey(String key)
	{
		return types.get(key).intValue();
	}

	public String getFirstType()
	{
		Map<String, Integer> sortedMap = sortByComparator(types);
		return sortedMap.keySet().toArray()[0].toString();
	}
	
	
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
	

}
