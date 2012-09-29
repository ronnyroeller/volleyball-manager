/*
 * Created on 27.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client.panel.spielplan;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.sport.core.bo.SportMatch;


/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SpielTransferable implements Transferable {

	public static DataFlavor spielFlavor = null;
	private static DataFlavor locaSpielFlavor = null;

	static {
		spielFlavor = new DataFlavor(SportMatch.class, "Non local Spiel");

		locaSpielFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
				+ "; class=" + SportMatch.class.getName(), "Local Spiel");
	}
	private static final DataFlavor[] flavors = { spielFlavor, locaSpielFlavor };
	private static final List<DataFlavor> flavorList = Arrays.asList(flavors);

	private SportMatch spielBO;

	public SpielTransferable(SportMatch spielBO) {
		this.spielBO = spielBO;
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		return this.spielBO;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		// check to see if the requested flavor matches
		return flavors;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return (flavorList.contains(flavor));
	}

}
