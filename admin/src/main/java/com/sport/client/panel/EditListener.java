/*
 * Created on 19.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client.panel;

import java.awt.event.*;

import javax.swing.event.*;

/**
 * @author ronny
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class EditListener implements KeyListener, MouseListener, ChangeListener {
		
	private VolleyPanel panel;
		
	public EditListener (VolleyPanel panel) {
		this.panel = panel;
	}
		
	public void keyTyped(KeyEvent e) {
		panel.dirty = true;
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		panel.dirty = true;
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void stateChanged(ChangeEvent e) {
		panel.dirty = true;
	}

}
