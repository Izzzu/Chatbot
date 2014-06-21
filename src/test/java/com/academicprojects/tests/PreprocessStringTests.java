package com.academicprojects.tests;



import org.junit.Assert;
import org.junit.Test;

import com.academicprojects.util.PreprocessString;


public class PreprocessStringTests {
	
	PreprocessString pStr = new PreprocessString();
	@Test
	public void shouldRemoveAllWhitespaces()
	{
		String s = "hej spacja   duzo spacji";
		String exp = "hejspacjaduzospacji";
		System.out.println(pStr.removeWhite(s));
		Assert.assertTrue(exp.equals(pStr.removeWhite(s)));
		
	}
	
	@Test
	public void shouldReplacePolishCharsAndLowerCase()
	{
		String s = "¯ó³ta ¿aba Gryz³a";
		String exp = "zolta zaba gryzla";
		System.out.println(pStr.replacePolChars(s));
		Assert.assertTrue(exp.equals(pStr.replacePolChars(s)));
	}

}
