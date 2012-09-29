/*
 * Created on 01.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client;

import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;


import org.apache.log4j.Logger;

import com.sport.core.helper.Messages;

/**
 * @author ronny
 */
public class VolleyTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final Logger LOG = Logger.getLogger(VolleyTreeCellRenderer.class);

	private static final long serialVersionUID = 82971279247405399L;

	public VolleyTreeCellRenderer() {
		super();
		setFont(new Font("Helvetica", Font.PLAIN, 13)); //$NON-NLS-1$
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean aSelected, boolean expanded, boolean leaf, int row,
			boolean aHasFocus) {

		this.hasFocus = aHasFocus;
		this.selected = aSelected;
		setText(value.toString());
		ImageIcon icon = null;
		Font font = getFont();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		if (node.getParent() == null)
			icon = Icons.TURNIER;
		else {
			switch (((TreeNodeObject) node.getUserObject()).getType()) {
			case TreeNodeObject.TURNIER:
				icon = Icons.TURNIER;
				break;
			case TreeNodeObject.MANNSCHAFTEN:
				icon = Icons.MANNSCHAFTEN;
				break;
			case TreeNodeObject.GRUPPEN:
				icon = Icons.GRUPPEN;
				break;
			case TreeNodeObject.SPIELPLAN:
				icon = Icons.SPIELPLAN;
				break;
			case TreeNodeObject.ERGEBNISSE:
				icon = Icons.ERGEBNISSE;
				break;
			case TreeNodeObject.PLATZIERUNGEN:
				icon = Icons.PLATZIERUNGEN;
				break;
			default:
				LOG.error(Messages
						.getString("volleytreecellrenderer_unknow_node") //$NON-NLS-1$
						+ " "
						+ ((TreeNodeObject) node.getUserObject()).getType());
			}
		}
		if (aSelected)
			setForeground(getTextSelectionColor());
		else
			setForeground(getTextNonSelectionColor());
		setFont(font);
		// end hervorheben
		if (!tree.isEnabled()) {
			setEnabled(false);
			setDisabledIcon(icon);
			if (leaf) {
				// setDisabledIcon(getLeafIcon());
			} else if (expanded) {
				// setDisabledIcon(getOpenIcon());
			} else {
				// setDisabledIcon(getClosedIcon());
			}
		} else {
			setEnabled(true);
			setIcon(icon);
			if (leaf) {
				// setIcon(getLeafIcon());
			} else if (expanded) {
				// setIcon(getOpenIcon());
			} else {
				// setIcon(getClosedIcon());
			}
		}
		return this;
	}

}
