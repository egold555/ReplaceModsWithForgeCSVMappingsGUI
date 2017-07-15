package org.golde.java.rmwfcsvmg.utils;

import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

public class JavaUtils {

	public static boolean isStringEmpty(String s) {
		
		if(s == null) {return true;}
		if(s.equalsIgnoreCase("")) {return true;}
		if(s.equalsIgnoreCase(" ")) {return true;}
		
		return false;
	}
	
	public static void simpleError(Exception e, Component parent) {
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		String message = writer.toString();
		simpleError(message, parent);
	}

	public static void simpleError(String message, Component parent)
	{
		JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
