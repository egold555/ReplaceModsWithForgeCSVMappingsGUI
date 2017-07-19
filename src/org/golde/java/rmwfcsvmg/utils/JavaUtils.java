package org.golde.java.rmwfcsvmg.utils;

import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

public class JavaUtils {

	public enum MessageLogo{
		ERROR(0),
		INFORMATION(1),
		WARNING(2),
		QUESTION(3),
		NONE(-1);
		
		public final int INT_VALUE;
		MessageLogo(int value){
			INT_VALUE = value;
		}
	}
	
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
		dialog(message, parent, "Error", MessageLogo.ERROR);
	}
	
	public static void simpleError(String e, Component parent) {
		dialog(e, parent, "Error", MessageLogo.ERROR);
	}
	
	public static void successMessage(String message, Component parent) {
		dialog(message, parent, "Success", MessageLogo.INFORMATION);
	}

	public static void dialog(String message, Component parent, String title, MessageLogo logo)
	{
		JOptionPane.showMessageDialog(parent, message, title, logo.INT_VALUE);
		System.out.println("Dialog: " + message);
	}
}
