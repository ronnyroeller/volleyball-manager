/*
 * Created on 31.05.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import com.sport.client.export.ExportDialog;
import com.sport.client.export.ProgressDialog;
import com.sport.client.export.ProgressRunnables;
import com.sport.client.panel.NullPanel;
import com.sport.client.panel.VolleyPanel;
import com.sport.client.panel.gruppen.GruppenPanel;
import com.sport.client.panel.mannschaften.MannschaftenPanel;
import com.sport.client.panel.platzierungen.PlatzierungenPanel;
import com.sport.client.panel.spielplan.ErgebnissePanel;
import com.sport.client.panel.spielplan.SpielplanPanel;
import com.sport.client.panel.turnier.TurnierPanel;
import com.sport.core.bo.LicenceBO;
import com.sport.core.bo.Tournament;
import com.sport.core.bo.UserBO;
import com.sport.core.helper.Messages;
import com.sport.server.ejb.HomeGetter;
import com.sport.server.remote.TurnierRemote;
import com.sport.server.remote.UserRemote;

public class VolleyFrame extends JFrame {

	private static final Logger LOG = Logger.getLogger(VolleyFrame.class
			.getName());

	private static final long serialVersionUID = -7340451118308069158L;

	private JPanel contentPane;
	private JLabel statusBar = new JLabel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JSplitPane splitPane = null;
	private NavigationTree navTree = null;
	private VolleyMenuBar menuBar;
	public VolleyToolBar toolBar;
	public LicenceBO licenceBO;

	private TurnierPanel turnierPanel = new TurnierPanel(this);
	private MannschaftenPanel mannschaftenPanel = new MannschaftenPanel(this);
	private GruppenPanel gruppenPanel = new GruppenPanel(this);
	private SpielplanPanel spielplanPanel = new SpielplanPanel(this);
	private ErgebnissePanel ergebnissePanel = new ErgebnissePanel(this);
	private PlatzierungenPanel platzierungenPanel = new PlatzierungenPanel(this);
	private NullPanel nullPanel = new NullPanel(this);
	protected VolleyPanel aktPanel = nullPanel;

	private UserBO userBO = null; // aktuell angemeldeter User
	public File lastChoosenDir = null;
	// directory that was last choosen with JFileChooser
	private static VolleyFrame instance = null;

	// Singleton pattern
	static public VolleyFrame getInstance() {
		if (instance == null) {
			instance = new VolleyFrame();
		}
		return instance;
	}

	static public VolleyFrame getNewInstance() {
		instance = new VolleyFrame();
		return instance;
	}

	// Den Frame konstruieren
	private VolleyFrame() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setIconImage(Icons.APPLICATION.getImage());

		// User laden
		try {
			userBO = UserRemote.getUserById(1);
			licenceBO = TurnierRemote.getLicenceBO();
		} catch (Exception e) {
			LOG
					.error(
							"Can't fetch user data from server "
									+ HomeGetter.getHost()
									+ ":"
									+ HomeGetter.getPort()
									+ ". You can modify the host name in the file 'VolleyballManager/admin/jvolley.properties'.",
							e);

			// Kann Nutzer nicht anmelden
			JOptionPane.showMessageDialog(this, Messages
					.getString("volleyframe_test_server1") //$NON-NLS-1$
					+ "\n" + Messages.getString("volleyframe_test_server2"), //$NON-NLS-1$
					Messages.getString("volleyframe_test_server_titel"), //$NON-NLS-1$
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		try {
			init();

			// deact. menus in the case that there is no tournament
			toolBar.setTurnierEnable(false, TreeNodeObject.NOPANEL, null);
			menuBar.setTurnierEnable(false);

			// Default: select first tournament in Navigationtree
			navTree.selectFirstTurnier();
		} catch (Exception e) {
			LOG.error("Can't initial main window", e);
		}
	}

	// Initialisierung der Komponenten
	private void init() throws Exception {
		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(borderLayout1);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (screenSize.width >= 800) ? 800 : screenSize.width;
		int height = (screenSize.height >= 600) ? 600 : screenSize.height;
		int x = (screenSize.width - width) / 2;
		int y = (screenSize.height - height) / 2;
		setBounds(x, y, width, height);
		setSize(width, height);

		this.setTitle("Volleyball Manager"); //$NON-NLS-1$
		statusBar.setText(" "); //$NON-NLS-1$

		menuBar = new VolleyMenuBar(this);
		this.setJMenuBar(menuBar);

		toolBar = new VolleyToolBar(this);

		navTree = new NavigationTree(this);
		navTree.setCellRenderer(new VolleyTreeCellRenderer());
		new TreeController(navTree, this);

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(250);
		splitPane.setDividerSize(1);

		JScrollPane jscroll = new JScrollPane(navTree);
		jscroll.setMinimumSize(new Dimension(250, 0));
		splitPane.add(jscroll);
		splitPane.add(aktPanel);

		contentPane.add(toolBar, BorderLayout.NORTH);
		contentPane.add(splitPane, BorderLayout.CENTER);
		contentPane.add(statusBar, BorderLayout.SOUTH);

	}

	// �berschrieben, so dass eine Beendigung beim Schlie�en des Fensters
	// m�glich ist.
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			setNullPanel();
			// if not [chanel]
			if (aktPanel == nullPanel) {
				System.exit(0);
			}
		} else {
			super.processWindowEvent(e);
		}
	}

	public VolleyPanel getAktPanel() {
		return aktPanel;
	}

	public void setAktPanelByTreeNodeType(int treeNodeType, Tournament turnierBO) {
		VolleyPanel newPanel = null;

		switch (treeNodeType) {
		case TreeNodeObject.NOPANEL:
			newPanel = nullPanel;
			break;
		case TreeNodeObject.TURNIER:
			newPanel = turnierPanel;
			break;
		case TreeNodeObject.MANNSCHAFTEN:
			newPanel = mannschaftenPanel;
			break;
		case TreeNodeObject.GRUPPEN:
			newPanel = gruppenPanel;
			break;
		case TreeNodeObject.SPIELPLAN:
			newPanel = spielplanPanel;
			break;
		case TreeNodeObject.ERGEBNISSE:
			newPanel = ergebnissePanel;
			break;
		case TreeNodeObject.PLATZIERUNGEN:
			newPanel = platzierungenPanel;
			break;
		default:
			break;
		}

		// unbedingt neu laden, wenn andere Ansicht oder verschiedenes
		// Turniere!
		if ((aktPanel != newPanel)
				|| ((aktPanel.turnierBO != null) && turnierBO.getId() != aktPanel.turnierBO
						.getId())) {

			boolean keinwechsel = false; // nicht Seite wechseln

			// �nderungen im alten Panel? -> fragen, ob speichern
			if (aktPanel.dirty == true) {
				JOptionPane.setDefaultLocale(Locale.getDefault());
				int result = JOptionPane
						.showConfirmDialog(
								aktPanel,
								Messages.getString("volleyframe_save_changes"), Messages.getString("volleyframe_save_changes_title"), JOptionPane.YES_NO_CANCEL_OPTION); //$NON-NLS-1$

				// bei [Abbruch] auf alter Seite bleiben
				if (result == JOptionPane.CANCEL_OPTION) {
					keinwechsel = true;
				}

				if (result == JOptionPane.YES_OPTION) {
					aktPanel.save();
				}
			}

			if (!keinwechsel) {
				// Menu anpassen
				menuBar.selectView(treeNodeType);
				navTree.selectTurnierView(turnierBO, treeNodeType);

				splitPane.remove(aktPanel);

				aktPanel = newPanel;
				splitPane.add(aktPanel);
				aktPanel.updateUI();

				getAktPanel().loadData(turnierBO);
			}
		}

		if (aktPanel.turnierBO != null) {
			String title = "Volleyball Manager - " //$NON-NLS-1$
					+ aktPanel.turnierBO.getName() + " - " //$NON-NLS-1$
					+ getStringByAktPanel();

			if (licenceBO.licencetype.equals(LicenceBO.LICENCE_DEMO)) {
				title += " - " + Messages.getString("licence_notregistered");
			}

			this.setTitle(title);
			String datum = DateFormat.getDateInstance(DateFormat.LONG).format(
					aktPanel.turnierBO.getDate());
			this.statusBar.setText(Messages.getString(licenceBO.licencetype)
					+ " - " + getUserBO().getName() + " - " //$NON-NLS-1$
					+ aktPanel.turnierBO.getName() + " - " //$NON-NLS-1$
					+ datum);

			menuBar.setTurnierEnable(true);
			toolBar.setTurnierEnable(true, treeNodeType, aktPanel.turnierBO);
		} else {
			this.setTitle("Volleyball Manager"); //$NON-NLS-1$
			this.statusBar.setText(getUserBO().getName());
			menuBar.setTurnierEnable(false);
			toolBar.setTurnierEnable(false, treeNodeType, null);
		}
	}

	public void setAktPanelByTreeNodeType(int treeNodeType) {
		setAktPanelByTreeNodeType(treeNodeType, aktPanel.turnierBO);
	}

	public String getStringByAktPanel() {

		switch (getTypeByAktPanel()) {
		case TreeNodeObject.TURNIER:
			return Messages.getString("volleyframe_view_challenge"); //$NON-NLS-1$
		case TreeNodeObject.MANNSCHAFTEN:
			return Messages.getString("volleyframe_view_teams"); //$NON-NLS-1$
		case TreeNodeObject.GRUPPEN:
			return Messages.getString("volleyframe_view_groups"); //$NON-NLS-1$
		case TreeNodeObject.SPIELPLAN:
			return Messages.getString("volleyframe_view_schedule"); //$NON-NLS-1$
		case TreeNodeObject.ERGEBNISSE:
			return Messages.getString("volleyframe_view_results"); //$NON-NLS-1$
		case TreeNodeObject.PLATZIERUNGEN:
			return Messages.getString("volleyframe_view_placings"); //$NON-NLS-1$
		case TreeNodeObject.NOPANEL:
			return Messages.getString("volleyframe_view_start"); //$NON-NLS-1$
		}

		return Messages.getString("volleyframe_view_unknown"); //$NON-NLS-1$
	}

	public int getTypeByAktPanel() {
		if (aktPanel == turnierPanel) {
			return TreeNodeObject.TURNIER;
		}
		if (aktPanel == mannschaftenPanel) {
			return TreeNodeObject.MANNSCHAFTEN;
		}
		if (aktPanel == gruppenPanel) {
			return TreeNodeObject.GRUPPEN;
		}
		if (aktPanel == spielplanPanel) {
			return TreeNodeObject.SPIELPLAN;
		}
		if (aktPanel == ergebnissePanel) {
			return TreeNodeObject.ERGEBNISSE;
		}
		if (aktPanel == platzierungenPanel) {
			return TreeNodeObject.PLATZIERUNGEN;
		}
		if (aktPanel == nullPanel) {
			return TreeNodeObject.NOPANEL;
		}

		return -1;
	}

	/**
	 * Setzt auf Anfangsansicht zur�ck
	 * 
	 */
	public void setNullPanel() {
		setAktPanelByTreeNodeType(TreeNodeObject.NOPANEL, null);
	}

	public void newTurnier() {
		// Turnier einf�gen
		Tournament turnierBO = new Tournament();
		turnierBO.setDate(new Date());
		turnierBO.setName(Messages.getString("title_new_challenge")); //$NON-NLS-1$

		// generate some random linkid as default
		String linkid = "";
		Random r = new Random();
		String chars = "abcdefghijklmonpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		for (int i = 0; i < 20; i++) {
			linkid += chars.charAt(r.nextInt(chars.length()));
		}
		turnierBO.setLinkid(linkid);

		try {
			turnierBO = TurnierRemote.saveByTurnierBO(turnierBO, getUserBO());
		} catch (Exception e) {
			LOG.error("Can't save tournament", e);
		}

		navTree.addNewTurnier(turnierBO);
		navTree.selectTurnier(turnierBO);
	}

	public void saveTurnier() {
		TreePath path = navTree.getSelectionPath();
		if (path != null) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();

			TreeNodeObject treeNodeObject = (TreeNodeObject) node
					.getUserObject();

			Tournament turnierBO = treeNodeObject.getTurnier();

			JFileChooser fc = new JFileChooser();
			ExampleFileFilter filter = new ExampleFileFilter();
			filter.addExtension("tur"); //$NON-NLS-1$
			filter.setDescription(Messages
					.getString("volleyframe_jvolley_challenge_file")); //$NON-NLS-1$
			fc.setFileFilter(filter);
			fc.setApproveButtonText(Messages.getString("volleyframe_save")); //$NON-NLS-1$
			fc.setDialogTitle(Messages
					.getString("volleyframe_export_challenge")); //$NON-NLS-1$
			fc.setCurrentDirectory(getLastChoosenDir());
			// set default save name to the name of the turnament + date
			String filename = turnierBO.getName() + " ";
			DateFormat formater = SimpleDateFormat.getDateTimeInstance(
					SimpleDateFormat.SHORT, SimpleDateFormat.SHORT);
			filename += formater.format(new Date()).replaceAll(":", "-")
					.replaceAll("\\.", "-");
			filename += ".tur";
			filename = filename.replaceAll(" ", "_");
			fc.setSelectedFile(new File(filename));
			int result = fc.showSaveDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				setLastChoosenDir(fc.getCurrentDirectory());

				// if the file exist -> ask it should be overwritten
				boolean write = false; // really save?
				if (!fc.getSelectedFile().exists()) {
					write = true;
				} else {
					JOptionPane.setDefaultLocale(Locale.getDefault());
					int resultDlg = JOptionPane
							.showConfirmDialog(
									this,
									Messages.getString("overwrite_file"), Messages.getString("volleyframe_export_challenge"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$

					write = (resultDlg == JOptionPane.YES_OPTION);
				}

				if (write) {
					ProgressDialog progDlg = new ProgressDialog(this);

					Runnable runnable = new ProgressRunnables.SaveTournamentRunnable(
							fc.getSelectedFile(), turnierBO, progDlg);
					Thread thread = new Thread(runnable);
					thread.start();

					progDlg.show();
				}
			}
		}
	}

	public void openTurnier() {
		JFileChooser fc = new JFileChooser();
		ExampleFileFilter filter = new ExampleFileFilter();
		filter.addExtension("tur"); //$NON-NLS-1$
		filter.setDescription(Messages
				.getString("volleyframe_jvolley_challenge_file")); //$NON-NLS-1$
		fc.setFileFilter(filter);
		fc.setApproveButtonText(Messages.getString("volleyframe_open")); //$NON-NLS-1$
		fc.setDialogTitle(Messages.getString("volleyframe_import_challenge")); //$NON-NLS-1$
		fc.setCurrentDirectory(getLastChoosenDir());
		int result = fc.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			setLastChoosenDir(fc.getCurrentDirectory());

			ProgressDialog progDlg = new ProgressDialog(this);

			Runnable runnable = new ProgressRunnables.OpenTournamentRunnable(fc
					.getSelectedFile().getPath(), navTree, getUserBO().getId(),
					progDlg);
			Thread thread = new Thread(runnable);
			thread.start();

			progDlg.show();
		}
	}

	/**
	 * @return
	 */
	public UserBO getUserBO() {
		return userBO;
	}

	/**
	 * @param userBO
	 */
	public void setUserBO(UserBO userBO) {
		this.userBO = userBO;
	}

	/**
	 * Startet Appliaction, um eine PDF/Html-Datei anzuzeigen
	 * 
	 * @param pfad
	 */
	public static void showExternal(String pfad) {
		// falls vorhanden -> Acrobat Reader starten
		try {
			Runtime.getRuntime().exec(
					"cmd /q /c start " + pfad.replaceAll(" ", "\" \"")); //$NON-NLS-1$
		} catch (Exception ex) {
			// ok now change show a window
			JOptionPane
					.showMessageDialog(
							getInstance(),
							Messages.getString("volleyframe_noexternal") + " " + pfad, Messages.getString("volleyframe_noexternal_title"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
		}
	}

	public static void showExternalExplorer(String pfad) {
		// falls vorhanden -> Internet Explorer starten
		try {
			Runtime.getRuntime().exec("explorer \"" + pfad + "\""); //$NON-NLS-1$
		} catch (Exception ex) {
			// let's try some other common browsers
			try {
				String[] cmd = { "mozilla", pfad };
				Runtime.getRuntime().exec(cmd); //$NON-NLS-1$
			} catch (Exception ex2) {
				try {
					String[] cmd = { "netscape", pfad };
					Runtime.getRuntime().exec(cmd); //$NON-NLS-1$
				} catch (Exception ex3) {
					// no standard browser available
					showExternal(pfad);
				}
			}
		}
	}

	/**
	 * Shows Terminal view
	 * 
	 */
	public void showOnline() {
		try {
			String param = "linkid="
					+ URLEncoder
							.encode(aktPanel.turnierBO.getLinkid(), "utf-8")
					+ "&locale=" + Locale.getDefault().getLanguage();
			VolleyFrame.showExternalExplorer(HomeGetter.getWebUrl()
					+ "/turnier.do?" + param);
		} catch (UnsupportedEncodingException e) {
			LOG.error("Can't UTF-8 encode", e);
		}
	}

	/**
	 * Shows Beamer view
	 * 
	 */
	public void showBeamer() {
		try {
			String param = "linkid="
					+ URLEncoder
							.encode(aktPanel.turnierBO.getLinkid(), "utf-8")
					+ "&locale=" + Locale.getDefault().getLanguage();
			VolleyFrame.showExternalExplorer(HomeGetter.getWebUrl()
					+ "/beamerspielplan.do?" + param);
		} catch (UnsupportedEncodingException e) {
			LOG.error("Can't UTF-8 encode", e);
		}
	}

	// Spielplan nach Pdf exportieren
	public void exportPdfSpielplan() {
		exportPdf(Messages.getString("volleyframe_schedule"),
				new ProgressRunnables.PdfSpielplanRunnable());
	}

	// Schiedsrichter nach Pdf exportieren
	public void exportPdfSchiedsrichter() {
		exportPdf(Messages.getString("volleyframe_referee"),
				new ProgressRunnables.PdfSchiedsrichterRunnable());
	}

	// Schiedsrichter nach Pdf exportieren
	public void exportPdfSpielbericht() {
		exportPdf(Messages.getString("volleyframe_spielbericht"),
				new ProgressRunnables.PdfSpielberichtRunnable());
	}

	// Gruppenansicht nach Pdf exportieren
	public void exportPdfGruppen() {
		exportPdf(Messages.getString("volleyframe_groups"),
				new ProgressRunnables.PdfGruppenRunnable());
	}

	/**
	 * Generic function to export PDF
	 * 
	 */
	private void exportPdf(String exportDialogTitle,
			ProgressRunnables.PdfRunnable runnable) {
		Tournament turnierBO = navTree.getSelectedTurnierBO();
		if (turnierBO != null) {

			ExportDialog dlg = new ExportDialog(this, turnierBO,
					exportDialogTitle); //$NON-NLS-1$
			dlg.setModal(true);
			dlg.show();
			if (dlg.getResult() == true) {
				// if the file exist -> ask it should be overwritten
				File f = new File(dlg.getPfad());
				boolean write = false; // really save?
				if (!f.exists()) {
					write = true;
				} else {
					JOptionPane.setDefaultLocale(Locale.getDefault());
					int result = JOptionPane
							.showConfirmDialog(
									this,
									Messages.getString("overwrite_file"), exportDialogTitle, JOptionPane.YES_NO_OPTION); //$NON-NLS-1$

					write = (result == JOptionPane.YES_OPTION);
				}

				if (write) {
					ProgressDialog progDlg = new ProgressDialog(this);

					runnable.setValues(dlg.getPfad(), dlg
							.getSelectedGruppeIds(), turnierBO, progDlg);
					Thread thread = new Thread(runnable);
					thread.start();

					progDlg.show();
				}
			}
		}
	}

	public void exportHtml() {
		Tournament turnierBO = navTree.getSelectedTurnierBO();
		if (turnierBO != null) {
			JFileChooser fc = new JFileChooser();
			JFileChooser.setDefaultLocale(Locale.getDefault());
			fc.setFileFilter(null);
			fc.setDialogTitle(Messages.getString("volleyframe_export_to_html")); //$NON-NLS-1$
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setCurrentDirectory(getLastChoosenDir());
			int result = fc.showDialog(null, Messages
					.getString("volleyframe_save")); //$NON-NLS-1$
			if (result == JFileChooser.APPROVE_OPTION) {
				setLastChoosenDir(fc.getCurrentDirectory());

				// if the file exist -> ask it should be overwritten
				boolean write = false; // really save?
				if (!fc.getSelectedFile().exists()) {
					write = true;
				} else {
					JOptionPane.setDefaultLocale(Locale.getDefault());
					int resultDlg = JOptionPane
							.showConfirmDialog(
									this,
									Messages.getString("overwrite_dir"), Messages.getString("volleyframe_export_challenge"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$

					write = (resultDlg == JOptionPane.YES_OPTION);
				}

				if (write) {
					ProgressDialog dlg = new ProgressDialog(this);

					Runnable exportRunnable = new ProgressRunnables.ExportHtmlRunnable(
							fc.getSelectedFile(), turnierBO, dlg);
					Thread thread = new Thread(exportRunnable);
					thread.start();

					dlg.show();
				}
			}
		}
	}

	/**
	 * @return Returns the lastChoosenDir.
	 */
	public File getLastChoosenDir() {
		return lastChoosenDir;
	}

	/**
	 * @param lastChoosenDir
	 *            The lastChoosenDir to set.
	 */
	public void setLastChoosenDir(File lastChoosenDir) {
		if (this.lastChoosenDir != lastChoosenDir) {
			this.lastChoosenDir = lastChoosenDir;
			VolleyApp.writeLastChoosenDir(lastChoosenDir.getPath());
		}
	}
}

class TreeController implements TreeSelectionListener {

	private JTree tree;
	private VolleyFrame jvolleyFrame;

	public TreeController(JTree tree, VolleyFrame jvolleyFrame) {
		super();
		this.tree = tree;
		this.jvolleyFrame = jvolleyFrame;
		tree.addTreeSelectionListener(this);
	}

	public void valueChanged(TreeSelectionEvent e) {
		if (e.getSource() == tree) {
			TreePath path = e.getPath();
			DefaultMutableTreeNode node = null;
			if (path != null)
				node = (DefaultMutableTreeNode) path.getLastPathComponent();
			if (node == null) {
				return;
			}

			tree.expandPath(path);
			TreeNodeObject treeNodeObject = (TreeNodeObject) node
					.getUserObject();
			jvolleyFrame.setAktPanelByTreeNodeType(treeNodeObject.getType(),
					treeNodeObject.getTurnier());
		}
	}
}
