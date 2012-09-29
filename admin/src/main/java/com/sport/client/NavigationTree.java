/*
 * Created on 15.06.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


import org.apache.log4j.Logger;

import com.sport.core.bo.Tournament;
import com.sport.core.bo.comparators.TurnierComparator;
import com.sport.core.helper.Messages;
import com.sport.server.remote.TurnierRemote;

/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NavigationTree extends JTree {

	private static final Logger LOG = Logger.getLogger(NavigationTree.class
			.getName());

	private static final long serialVersionUID = 1931504536006530678L;

	private JPopupMenu popupMenu = new JPopupMenu();
	private final String POPUP_DELTURNIER = Messages
			.getString("navigationtree_del_challenge"); //$NON-NLS-1$
	private final String POPUP_EXPORTTURNIER = Messages
			.getString("navigationtree_export_challenge"); //$NON-NLS-1$

	private VolleyFrame parentFrame = null;

	public NavigationTree(VolleyFrame parentFrame) {
		super();

		this.parentFrame = parentFrame;

		setModel(new DefaultTreeModel(loadData(), false));
		setShowsRootHandles(true);
		setRootVisible(false);
		setRowHeight(30);

		// add Popup menu
		JMenuItem menuItem = new JMenuItem(POPUP_EXPORTTURNIER,
				Icons.SAVE_SMALL);
		menuItem.addActionListener(new Listener(this));
		popupMenu.add(menuItem);
		menuItem = new JMenuItem(POPUP_DELTURNIER, Icons.DEL_SMALL);
		menuItem.addActionListener(new Listener(this));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		popupMenu.add(menuItem);
		addMouseListener(new MouseListener());
		addKeyListener(new Listener(this));
	}

	public Tournament getSelectedTurnierBO() {
		Tournament turnierBO = null;
		TreePath path = getSelectionPath();
		if (path != null) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();

			TreeNodeObject treeNodeObject = (TreeNodeObject) node
					.getUserObject();

			turnierBO = treeNodeObject.getTurnier();
		}
		return turnierBO;
	}

	/**
	 * Selects specific Turnier and expands this path
	 * 
	 * @param turnierBO
	 */
	public void selectTurnier(Tournament turnierBO) {
		selectTurnierView(turnierBO, TreeNodeObject.TURNIER);
	}

	/**
	 * Selects specific Turnier and expands this path and select specific view
	 * 
	 * @param turnierBO
	 */
	public void selectTurnierView(Tournament turnierBO, int view) {

		TreeNode root = (TreeNode) getModel().getRoot();
		TreePath rootpath = new TreePath(root);

		// Are tournaments in the system?
		if (root.getChildCount() >= 0) {
			for (Enumeration<TreeNode> e = root.children(); e.hasMoreElements();) {
				TreeNode n = e.nextElement();
				TreePath path = rootpath.pathByAddingChild(n);

				// Receive Turnier from Childnode
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
						.getLastPathComponent();
				TreeNodeObject treeNodeObject = (TreeNodeObject) node
						.getUserObject();
				Tournament thisTurnierBO = treeNodeObject.getTurnier();

				// Found a match?
				if (thisTurnierBO.equals(turnierBO)) {

					// Search for correct view
					if (view == treeNodeObject.getType()) {
						setSelectionPath(path);
					} else {
						for (Enumeration<TreeNode> e2 = n.children(); e2
								.hasMoreElements();) {
							TreeNode n2 = e2.nextElement();
							TreePath path2 = path.pathByAddingChild(n2);
							DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) path2
									.getLastPathComponent();

							TreeNodeObject treeNodeObject2 = (TreeNodeObject) node2
									.getUserObject();
							if (view == treeNodeObject2.getType()) {
								setSelectionPath(path2);
							}
						}
					}
					break;
				}
			}
		}
	}

	/**
	 * Selects the first tournament in the Navigationtree
	 * 
	 */
	public void selectFirstTurnier() {
		TreeNode root = (TreeNode) getModel().getRoot();
		TreePath rootpath = new TreePath(root);
		// falls min. ein Turnier vorhanden
		if (root.getChildCount() > 0) {
			TreeNode n = root.getChildAt(0);
			TreePath path = rootpath.pathByAddingChild(n);
			setSelectionPath(path);
		}
	}

	public DefaultMutableTreeNode getTurnierNode(Tournament turnierBO) {
		DefaultMutableTreeNode turnierNode = new DefaultMutableTreeNode(
				new TreeNodeObject(turnierBO, TreeNodeObject.TURNIER));
		turnierNode.add(new DefaultMutableTreeNode(new TreeNodeObject(
				turnierBO, TreeNodeObject.GRUPPEN)));
		turnierNode.add(new DefaultMutableTreeNode(new TreeNodeObject(
				turnierBO, TreeNodeObject.MANNSCHAFTEN)));
		turnierNode.add(new DefaultMutableTreeNode(new TreeNodeObject(
				turnierBO, TreeNodeObject.SPIELPLAN)));
		turnierNode.add(new DefaultMutableTreeNode(new TreeNodeObject(
				turnierBO, TreeNodeObject.ERGEBNISSE)));
		turnierNode.add(new DefaultMutableTreeNode(new TreeNodeObject(
				turnierBO, TreeNodeObject.PLATZIERUNGEN)));
		return turnierNode;
	}

	private DefaultMutableTreeNode loadData() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Turniere"); //$NON-NLS-1$
		try {
			Set<Tournament> turniere = TurnierRemote
					.getTurniereByUserid(parentFrame.getUserBO().getId());

			Vector<Tournament> sortedVector = new Vector<Tournament>(turniere);
			Collections.sort(sortedVector, new TurnierComparator());
			for (Tournament turnierBO : sortedVector)
				top.add(getTurnierNode(turnierBO));
		} catch (Exception e) {
			LOG.error("Can't fetch tournaments from server", e);
		}
		return top;
	}

	public Insets getInsets() {
		return new Insets(5, 5, 5, 5);
	}

	/**
	 * Add a new Turnier in the tree
	 * 
	 * @param turnierBO
	 */
	public void addNewTurnier(Tournament turnierBO) {
		// Knoten in Tree einf�gen
		DefaultMutableTreeNode rootNode = ((DefaultMutableTreeNode) getModel()
				.getRoot());
		rootNode.add(getTurnierNode(turnierBO));
		((DefaultTreeModel) getModel()).nodeStructureChanged(rootNode);
	}

	class MouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			showPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			showPopup(e);
		}

		private void showPopup(MouseEvent e) {
			// nur wenn Turnier markiert ist, darf gel�scht/gespeichert
			// werden
			if (e.isPopupTrigger() && (getSelectionRows() != null)) {
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	class Listener implements ActionListener, KeyListener {

		private NavigationTree tree = null;

		public Listener(NavigationTree tree) {
			this.tree = tree;
		}

		public void actionPerformed(ActionEvent e) {
			JMenuItem source = (JMenuItem) (e.getSource());

			// L�schen
			if (source.getText() == POPUP_DELTURNIER) {
				delTournament();
			}
			// Exportieren
			else if (source.getText() == POPUP_EXPORTTURNIER) {
				parentFrame.saveTurnier();
			}
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_INSERT) {
				parentFrame.newTurnier();
			} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				delTournament();
			}
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}

		private void delTournament() {
			TreePath path = tree.getSelectionPath();
			if (path != null) {
				// ask if the tournament should be really deleted?
				JOptionPane.setDefaultLocale(Locale.getDefault());
				int result = JOptionPane
						.showConfirmDialog(
								parentFrame,
								Messages
										.getString("navigationtree_del_tournament"), Messages.getString("navigationtree_del_challenge"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$

				// bei [Abbruch] auf alter Seite bleiben
				if (result == JOptionPane.YES_OPTION) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
							.getLastPathComponent();

					TreeNodeObject treeNodeObject = (TreeNodeObject) node
							.getUserObject();

					Tournament turnierBO = treeNodeObject.getTurnier();

					try {
						TurnierRemote.removeByTurnierBO(turnierBO);
					} catch (Exception e) {
						LOG.error("Can't remove tournament from server", e);
					}

					// Knoten aus Tree l�schen
					DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node
							.getParent();

					// Turnier-Knoten suchen
					while (((TreeNodeObject) node.getUserObject()).getType() != TreeNodeObject.TURNIER) {
						node = parentNode;
						parentNode = (DefaultMutableTreeNode) node.getParent();
					}
					parentNode.remove(node);
					((DefaultTreeModel) tree.getModel())
							.nodeStructureChanged(parentNode);

					tree.parentFrame.setNullPanel();
				}
			}
		}

	}
}