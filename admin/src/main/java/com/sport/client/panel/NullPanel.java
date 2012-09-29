/*
 * Created on 04.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client.panel;

import com.sport.client.VolleyFrame;
import com.sport.core.bo.Tournament;


/**
 * Empty panel
 * 
 * @author ronny
 */
public class NullPanel extends VolleyPanel {

	private static final long serialVersionUID = 7687992944253279951L;

	/**
	 * @param parentFrame
	 */
	public NullPanel(VolleyFrame parentFrame) {
		super(parentFrame);
	}
	
	public void loadData(Tournament aTurnierBO) {
	}

	public void save() {
	}

}
