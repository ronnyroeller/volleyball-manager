/*
 * Created on 11.07.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client;

import javax.swing.JToolTip;

/**
 * @author ronny
 */
public class MultiLineToolTip extends JToolTip {

	private static final long serialVersionUID = -172136856517185652L;

	public MultiLineToolTip() {
		setUI(new MultiLineToolTipUI());
	}

}
