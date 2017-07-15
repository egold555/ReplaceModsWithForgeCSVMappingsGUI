package org.golde.java.rmwfcsvmg.utils;

public class StringUtils {

	public static boolean isStringEmpty(String s) {
		
		if(s == null) {return true;}
		if(s.equalsIgnoreCase("")) {return true;}
		if(s.equalsIgnoreCase(" ")) {return true;}
		
		return false;
	}
	
}
