/*
 * Created on 01.12.2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package com.sport.client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import com.sport.core.bo.LicenceBO;
import com.sport.core.helper.Messages;


/**
 * @author ronny
 * 
 *         Includes the Menubar for the Volley Mainframe.
 */
public class VolleyMenuBar extends JMenuBar {

	private static final long serialVersionUID = 6826846347520446520L;

	public static final String WEBSITE_GENERIC = "http://www.volleyball-manager.com";
	private static final String WEBSITE_GERMAN = WEBSITE_GENERIC+"/de/";
	private static final String WEBSITE_ENGLISH = WEBSITE_GENERIC+"/en/";
	private static final String WEBSITE_ORDER_GERMAN = WEBSITE_GERMAN + "bestellen/";
	private static final String WEBSITE_ORDER_ENGLISH = WEBSITE_ENGLISH + "order/";
	public static final String WEBSITE_BUG_GERMAN = WEBSITE_GERMAN + "kontakt/fehler-melden/";
	public static final String WEBSITE_BUG_ENGLISH = WEBSITE_ENGLISH + "contact/";
	private static final String WEBSITE_DOCUMENTATION = WEBSITE_GERMAN + "dokumentation/";
	
	private VolleyFrame parentFrame;

	private JMenuItem jMenuFileNew;
	private JMenuItem jMenuFileImport;
	private JMenuItem jMenuFileExport;
	private JMenu jMenuFilePdf;
	private JMenuItem jMenuFilePdfSpielplan;
	private JMenuItem jMenuFilePdfSchiedsrichter;
	private JMenuItem jMenuFilePdfSpielbericht;
	private JMenuItem jMenuFilePdfGruppen;
	private JMenuItem jMenuFileHtml;
	private JMenuItem jMenuFileExit;
	private JMenuItem jMenuFileOnline;
	private JMenuItem jMenuFileBeamer;

	private JMenu jMenuView;
	private JRadioButtonMenuItem jMenuViewTurnier;
	private JRadioButtonMenuItem jMenuViewGruppen;
	private JRadioButtonMenuItem jMenuViewMannschaften;
	private JRadioButtonMenuItem jMenuViewSpielplan;
	private JRadioButtonMenuItem jMenuViewErgebnisse;
	private JRadioButtonMenuItem jMenuViewPlatzierungen;

	private JRadioButtonMenuItem jMenuHelpLanguageEnglish;
	private JRadioButtonMenuItem jMenuHelpLanguageGerman;
	private JRadioButtonMenuItem jMenuHelpLanguageSpanish;
	private JRadioButtonMenuItem jMenuHelpLanguageDutch;

	private JMenuItem jMenuHelpHelp;
	private JMenuItem jMenuHelpWebsite;
	private JMenuItem jMenuHelpOrder;
	private JMenuItem jMenuHelpAbout;

	public VolleyMenuBar(VolleyFrame parentFrame) {
		this.parentFrame = parentFrame;

		// create Menuitems
		JMenu jMenuFile = new JMenu(Messages.getString("volleyframe_file")); //$NON-NLS-1$
		jMenuFileNew = new JMenuItem(Messages
				.getString("volleyframe_new_challenge")); //$NON-NLS-1$
		jMenuFileImport = new JMenuItem(Messages
				.getString("volleyframe_import_challenge")); //$NON-NLS-1$
		jMenuFileExport = new JMenuItem(Messages
				.getString("volleyframe_export_challenge")); //$NON-NLS-1$
		jMenuFilePdf = new JMenu(Messages.getString("volleyframe_file_pdf")); //$NON-NLS-1$
		jMenuFilePdfSpielplan = new JMenuItem(Messages
				.getString("volleyframe_pdf_schedule")); //$NON-NLS-1$
		jMenuFilePdfSchiedsrichter = new JMenuItem(Messages
				.getString("volleyframe_pdf_referee")); //$NON-NLS-1$
		jMenuFilePdfSpielbericht = new JMenuItem(Messages
				.getString("volleyframe_pdf_spielbericht")); //$NON-NLS-1$
		jMenuFilePdfGruppen = new JMenuItem(Messages
				.getString("volleyframe_pdf_gruppen")); //$NON-NLS-1$
		jMenuFileHtml = new JMenuItem(Messages
				.getString("volleyframe_html_challenge")); //$NON-NLS-1$
		jMenuFileExit = new JMenuItem(Messages.getString("volleyframe_exit")); //$NON-NLS-1$
		jMenuFileOnline = new JMenuItem(Messages
				.getString("volleyframe_online")); //$NON-NLS-1$
		jMenuFileBeamer = new JMenuItem(Messages
				.getString("volleyframe_beamer")); //$NON-NLS-1$

		jMenuView = new JMenu(Messages.getString("volleyframe_view")); //$NON-NLS-1$
		jMenuViewTurnier = new JRadioButtonMenuItem(Messages
				.getString("volleyframe_view_challenge")); //$NON-NLS-1$
		jMenuViewGruppen = new JRadioButtonMenuItem(Messages
				.getString("volleyframe_view_groups")); //$NON-NLS-1$
		jMenuViewMannschaften = new JRadioButtonMenuItem(Messages
				.getString("volleyframe_view_teams")); //$NON-NLS-1$
		jMenuViewSpielplan = new JRadioButtonMenuItem(Messages
				.getString("volleyframe_view_schedule")); //$NON-NLS-1$
		jMenuViewErgebnisse = new JRadioButtonMenuItem(Messages
				.getString("volleyframe_view_results")); //$NON-NLS-1$
		jMenuViewPlatzierungen = new JRadioButtonMenuItem(Messages
				.getString("volleyframe_view_placings")); //$NON-NLS-1$

		JMenu jMenuHelp = new JMenu(Messages.getString("volleyframe_help")); //$NON-NLS-1$
		JMenu jMenuHelpLanguage = new JMenu(Messages
				.getString("volleyframe_language")); //$NON-NLS-1$
		jMenuHelpLanguageEnglish = new JRadioButtonMenuItem(Messages
				.getString("volleyframe_language_english")); //$NON-NLS-1$
		jMenuHelpLanguageGerman = new JRadioButtonMenuItem(Messages
				.getString("volleyframe_language_german")); //$NON-NLS-1$
		jMenuHelpLanguageSpanish = new JRadioButtonMenuItem(Messages
				.getString("volleyframe_language_spanish")); //$NON-NLS-1$
		jMenuHelpLanguageDutch = new JRadioButtonMenuItem(Messages
				.getString("volleyframe_language_dutch")); //$NON-NLS-1$
		jMenuHelpHelp = new JMenuItem(Messages.getString("volleyframe_help")); //$NON-NLS-1$
		jMenuHelpWebsite = new JMenuItem(Messages
				.getString("volleyframe_website")); //$NON-NLS-1$
		jMenuHelpOrder = new JMenuItem(Messages.getString("volleyframe_order")); //$NON-NLS-1$
		jMenuHelpOrder.setFont(jMenuHelpOrder.getFont().deriveFont(Font.BOLD)); // bold
		jMenuHelpAbout = new JMenuItem(Messages.getString("volleyframe_info")); //$NON-NLS-1$

		// Add shortcuts
		jMenuFileNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				KeyEvent.CTRL_DOWN_MASK));
		jMenuFileImport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				KeyEvent.CTRL_DOWN_MASK));
		jMenuFileExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				KeyEvent.CTRL_DOWN_MASK));

		jMenuViewTurnier.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				KeyEvent.CTRL_DOWN_MASK));
		jMenuViewGruppen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
				KeyEvent.CTRL_DOWN_MASK));
		jMenuViewMannschaften.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_3, KeyEvent.CTRL_DOWN_MASK));
		jMenuViewSpielplan.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4,
				KeyEvent.CTRL_DOWN_MASK));
		jMenuViewErgebnisse.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_5, KeyEvent.CTRL_DOWN_MASK));
		jMenuViewPlatzierungen.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_6, KeyEvent.CTRL_DOWN_MASK));

		jMenuHelpHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));

		// Add ActionListener
		jMenuFileNew.addActionListener(new MenuFileActionAdapter());
		jMenuFileImport.addActionListener(new MenuFileActionAdapter());
		jMenuFileExport.addActionListener(new MenuFileActionAdapter());
		jMenuFilePdfSpielplan.addActionListener(new MenuFilePdfActionAdapter());
		jMenuFilePdfSchiedsrichter
				.addActionListener(new MenuFilePdfActionAdapter());
		jMenuFilePdfSpielbericht
				.addActionListener(new MenuFilePdfActionAdapter());
		jMenuFilePdfGruppen.addActionListener(new MenuFilePdfActionAdapter());
		jMenuFileHtml.addActionListener(new MenuFileHtmlActionAdapter());
		jMenuFileOnline.addActionListener(new MenuFileActionAdapter());
		jMenuFileBeamer.addActionListener(new MenuFileActionAdapter());
		jMenuFileExit.addActionListener(new MenuFileActionAdapter());

		jMenuViewTurnier.addActionListener(new MenuViewActionAdapter());
		jMenuViewGruppen.addActionListener(new MenuViewActionAdapter());
		jMenuViewMannschaften.addActionListener(new MenuViewActionAdapter());
		jMenuViewSpielplan.addActionListener(new MenuViewActionAdapter());
		jMenuViewErgebnisse.addActionListener(new MenuViewActionAdapter());
		jMenuViewPlatzierungen.addActionListener(new MenuViewActionAdapter());

		jMenuHelpHelp.addActionListener(new MenuHelpActionAdapter());
		jMenuHelpWebsite.addActionListener(new MenuHelpActionAdapter());
		jMenuHelpOrder.addActionListener(new MenuHelpActionAdapter());
		jMenuHelpAbout.addActionListener(new MenuHelpActionAdapter());
		jMenuHelpLanguageEnglish
				.addActionListener(new MenuHelpLanguageActionAdapter());
		jMenuHelpLanguageGerman
				.addActionListener(new MenuHelpLanguageActionAdapter());
		jMenuHelpLanguageSpanish
				.addActionListener(new MenuHelpLanguageActionAdapter());
		jMenuHelpLanguageDutch
				.addActionListener(new MenuHelpLanguageActionAdapter());

		// Ausgew�hlte Sprache selektieren
		if (Locale.getDefault().getLanguage().equals(
				Messages.GERMAN.getLanguage())) {
			jMenuHelpLanguageGerman.setSelected(true);
		} else if (Locale.getDefault().getLanguage().equals(
				Messages.ENGLISH.getLanguage())) {
			jMenuHelpLanguageEnglish.setSelected(true);
		} else if (Locale.getDefault().getLanguage().equals(
				Messages.SPANISH.getLanguage())) {
			jMenuHelpLanguageSpanish.setSelected(true);
		} else if (Locale.getDefault().getLanguage().equals(
				Messages.DUTCH.getLanguage())) {
			jMenuHelpLanguageDutch.setSelected(true);
		}

		// Ansichten gruppieren
		ButtonGroup group = new ButtonGroup();
		group.add(jMenuViewTurnier);
		group.add(jMenuViewGruppen);
		group.add(jMenuViewMannschaften);
		group.add(jMenuViewSpielplan);
		group.add(jMenuViewErgebnisse);
		group.add(jMenuViewPlatzierungen);

		// Mainmenu structure
		jMenuFile.add(jMenuFileNew);
		jMenuFile.add(jMenuFileImport);
		jMenuFile.add(jMenuFileExport);
		jMenuFile.add(new JSeparator());
		jMenuFile.add(jMenuFilePdf);
		jMenuFile.add(jMenuFileHtml);
		jMenuFile.add(new JSeparator());
		jMenuFile.add(jMenuFileOnline);
		jMenuFile.add(jMenuFileBeamer);
		jMenuFile.add(new JSeparator());
		jMenuFile.add(jMenuFileExit);

		jMenuView.add(jMenuViewTurnier);
		jMenuView.add(jMenuViewGruppen);
		jMenuView.add(jMenuViewMannschaften);
		jMenuView.add(jMenuViewSpielplan);
		jMenuView.add(jMenuViewErgebnisse);
		jMenuView.add(jMenuViewPlatzierungen);

		jMenuHelp.add(jMenuHelpHelp);
		jMenuHelp.add(jMenuHelpWebsite);

		// only show order if a demo version
		if (parentFrame.licenceBO.getLicencetype().equals(LicenceBO.LICENCE_DEMO)) {
			jMenuHelp.add(jMenuHelpOrder);
		}
		jMenuHelp.add(jMenuHelpLanguage);
		jMenuHelp.add(new JSeparator());
		jMenuHelp.add(jMenuHelpAbout);

		jMenuFileNew.setIcon(Icons.NEW_SMALL);
		jMenuFileImport.setIcon(Icons.OPEN_SMALL);
		jMenuFileExport.setIcon(Icons.SAVE_SMALL);
		jMenuFileOnline.setIcon(Icons.ONLINE_SMALL);
		jMenuFileBeamer.setIcon(Icons.BEAMER_SMALL);
		jMenuViewTurnier.setIcon(Icons.TURNIER_SMALL);
		jMenuViewGruppen.setIcon(Icons.GRUPPEN_SMALL);
		jMenuViewMannschaften.setIcon(Icons.MANNSCHAFTEN_SMALL);
		jMenuViewSpielplan.setIcon(Icons.SPIELPLAN_SMALL);
		jMenuViewErgebnisse.setIcon(Icons.ERGEBNISSE_SMALL);
		jMenuViewPlatzierungen.setIcon(Icons.PLATZIERUNGEN_SMALL);

		// Submenu PDF
		jMenuFilePdf.add(jMenuFilePdfSpielplan);
		jMenuFilePdf.add(jMenuFilePdfSchiedsrichter);
		jMenuFilePdf.add(jMenuFilePdfSpielbericht);
		jMenuFilePdf.add(jMenuFilePdfGruppen);
		jMenuFilePdf.setIcon(Icons.PDF_SMALL);

		// Submenu Language
		jMenuHelpLanguage.add(jMenuHelpLanguageEnglish);
		jMenuHelpLanguage.add(jMenuHelpLanguageGerman);
		jMenuHelpLanguage.add(jMenuHelpLanguageSpanish);
		jMenuHelpLanguage.add(jMenuHelpLanguageDutch);

		add(jMenuFile);
		add(jMenuView);
		add(jMenuHelp);
	}

	/**
	 * Activates/Deactivates all menuelements depending if a tournament is
	 * selected.
	 * 
	 * @param enable
	 */
	public void setTurnierEnable(boolean enable) {
		jMenuFileExport.setEnabled(enable);
		jMenuFileHtml.setEnabled(enable);
		jMenuFilePdf.setEnabled(enable);
		jMenuFilePdfSpielplan.setEnabled(enable);
		jMenuFilePdfSchiedsrichter.setEnabled(enable);
		jMenuFilePdfSpielbericht.setEnabled(enable);
		jMenuFilePdfGruppen.setEnabled(enable);
		jMenuFileOnline.setEnabled(enable);
		jMenuFileBeamer.setEnabled(enable);

		jMenuView.setEnabled(enable);
	}

	/**
	 * Selects the right view in the menu
	 * 
	 */
	public void selectView(int treeNodeType) {
		switch (treeNodeType) {
		case TreeNodeObject.TURNIER:
			jMenuViewTurnier.setSelected(true);
			break;
		case TreeNodeObject.GRUPPEN:
			jMenuView.setSelected(true);
			break;
		case TreeNodeObject.MANNSCHAFTEN:
			jMenuViewMannschaften.setSelected(true);
			break;
		case TreeNodeObject.SPIELPLAN:
			jMenuViewSpielplan.setSelected(true);
			break;
		case TreeNodeObject.ERGEBNISSE:
			jMenuViewErgebnisse.setSelected(true);
			break;
		case TreeNodeObject.PLATZIERUNGEN:
			jMenuViewPlatzierungen.setSelected(true);
			break;
		default:
			break;
		}
		jMenuView.setSelected(false);
	}

	/**
	 * Checks if the user speaks German
	 * @return
	 */
	public static boolean isGerman() {
		return Locale.getDefault().getLanguage() == Locale.GERMAN.getLanguage();
	}

	class MenuHelpLanguageActionAdapter implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == jMenuHelpLanguageEnglish) {
				VolleyApp.setLocale(Messages.ENGLISH);
			} else if (e.getSource() == jMenuHelpLanguageGerman) {
				VolleyApp.setLocale(Messages.GERMAN);
			} else if (e.getSource() == jMenuHelpLanguageSpanish) {
				VolleyApp.setLocale(Messages.SPANISH);
			} else {
				VolleyApp.setLocale(Messages.DUTCH);
			}
		}
	}

	class MenuHelpActionAdapter implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == jMenuHelpHelp) {
					VolleyFrame.showExternalExplorer(WEBSITE_DOCUMENTATION);
			} else if (e.getSource() == jMenuHelpWebsite) {
				String url = (isGerman()) ? WEBSITE_GERMAN : WEBSITE_ENGLISH;

				VolleyFrame.showExternalExplorer(url);
			} else if (e.getSource() == jMenuHelpOrder) {
				String url = (isGerman()) ? WEBSITE_ORDER_GERMAN : WEBSITE_ORDER_ENGLISH;

				VolleyFrame.showExternalExplorer(url);
			} else {
				AboutDialog dlg = new AboutDialog(parentFrame);
				Dimension dlgSize = dlg.getPreferredSize();
				Dimension frmSize = parentFrame.getSize();
				Point loc = parentFrame.getLocation();
				dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
						(frmSize.height - dlgSize.height) / 2 + loc.y);
				dlg.setModal(true);
				dlg.pack();
				dlg.show();
			}
		}
	}

	class MenuFileActionAdapter implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == jMenuFileNew) {
				parentFrame.newTurnier();
			} else if (e.getSource() == jMenuFileImport) {
				parentFrame.openTurnier();
			} else if (e.getSource() == jMenuFileExport) {
				parentFrame.saveTurnier();
			} else if (e.getSource() == jMenuFileOnline) {
				parentFrame.showOnline();
			} else if (e.getSource() == jMenuFileBeamer) {
				parentFrame.showBeamer();
			} else if (e.getSource() == jMenuFileExit) {
				System.exit(0);
			}
		}
	}

	class MenuViewActionAdapter implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == jMenuViewTurnier) {
				parentFrame.setAktPanelByTreeNodeType(TreeNodeObject.TURNIER);
			} else if (e.getSource() == jMenuViewGruppen) {
				parentFrame.setAktPanelByTreeNodeType(TreeNodeObject.GRUPPEN);
			} else if (e.getSource() == jMenuViewMannschaften) {
				parentFrame
						.setAktPanelByTreeNodeType(TreeNodeObject.MANNSCHAFTEN);
			} else if (e.getSource() == jMenuViewSpielplan) {
				parentFrame.setAktPanelByTreeNodeType(TreeNodeObject.SPIELPLAN);
			} else if (e.getSource() == jMenuViewErgebnisse) {
				parentFrame
						.setAktPanelByTreeNodeType(TreeNodeObject.ERGEBNISSE);
			} else if (e.getSource() == jMenuViewPlatzierungen) {
				parentFrame
						.setAktPanelByTreeNodeType(TreeNodeObject.PLATZIERUNGEN);
			}
			;
		}
	}

	class MenuFilePdfActionAdapter implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == jMenuFilePdfSpielplan) {
				parentFrame.exportPdfSpielplan();
			} else if (e.getSource() == jMenuFilePdfSchiedsrichter) {
				parentFrame.exportPdfSchiedsrichter();
			} else if (e.getSource() == jMenuFilePdfSpielbericht) {
				parentFrame.exportPdfSpielbericht();
			} else if (e.getSource() == jMenuFilePdfGruppen) {
				parentFrame.exportPdfGruppen();
			}
		}
	}

	class MenuFileHtmlActionAdapter implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			parentFrame.exportHtml();
		}
	}
}
