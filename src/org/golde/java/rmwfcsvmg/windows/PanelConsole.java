package org.golde.java.rmwfcsvmg.windows;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTextField;

/*
 * TODO: Actuall console window
 */
@SuppressWarnings({"serial", "rawtypes", "unchecked"}) //Ugg java D:
public class PanelConsole extends JFrame {
	private static JTextField textField;
	public PanelConsole() {
		
		textField = new JTextField();
		getContentPane().add(textField, BorderLayout.CENTER);
		textField.setColumns(10);
		textField.setEditable(false);
		add(textField);
	}
	
	public static void write(String s) {
		//textField.
	}

}
