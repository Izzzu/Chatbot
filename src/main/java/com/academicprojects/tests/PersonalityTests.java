package com.academicprojects.tests;

import org.junit.Assert;
import org.junit.Test;

import com.academicprojects.model.Personality;

/**
 *  A testing class. 
 */
public class PersonalityTests {
	
	@Test
	public void shouldConstructCorrectString()
	{
		Personality p = new Personality();
		
		String expected = System.getProperty("line.separator")+"dom - 0"+System.getProperty("line.separator")+"dos - 0"+System.getProperty("line.separator")+"mak - 0"+
						System.getProperty("line.separator")+"min - 0"+System.getProperty("line.separator")+"ins - 0"+
						System.getProperty("line.separator")+"sys - 0"+System.getProperty("line.separator")+"odk - 0"+
						System.getProperty("line.separator")+"kon - 0"+System.getProperty("line.separator")+"wer - 0"+
						System.getProperty("line.separator")+"har - 0"+System.getProperty("line.separator")+"emp - 0"+
						System.getProperty("line.separator")+"rze - 0"+System.getProperty("line.separator")+"odw - 0"+
						System.getProperty("line.separator")+"ase - 0"+System.getProperty("line.separator")+"hoj - 0"+
						System.getProperty("line.separator")+"osz - 0"+System.getProperty("line.separator")+"faw - 0"+
						System.getProperty("line.separator")+"row - 0"+System.getProperty("line.separator");
		String actual = p.toString();
		Assert.assertTrue(actual.equals(expected));
	}

	@Test
	public void shouldReturnMainType()
	{
		Personality p = new Personality();
		p.setByKey("MAK", 20);
		Assert.assertTrue(p.getMainType().equals("MAK"));
		
	}
}

