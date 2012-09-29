/*
 * Created on 27.01.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.sport.key_gen.helper_notexport;

import javax.swing.UIManager;

/**
 * @author ronny
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class GenerateLicence {
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
			"com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
		
		GenerateLicenseFrame frame = new GenerateLicenseFrame();
		frame.setVisible(true);
	}

}
