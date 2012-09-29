package com.sport.client.export;

import javax.swing.JCheckBox;

import com.sport.core.bo.SportGroup;


/**
 * Stores instances of checkbox and groups
 * @author Ronny
 *
 */
public class GruppeCheck {

	private JCheckBox checkBox;
	private SportGroup gruppeBO;
	
	public GruppeCheck(JCheckBox checkBox, SportGroup gruppeBO) {
		this.checkBox = checkBox;
		this.gruppeBO = gruppeBO;
	}

	public JCheckBox getCheckBox() {
		return checkBox;
	}

	public SportGroup getGruppeBO() {
		return gruppeBO;
	}

}
