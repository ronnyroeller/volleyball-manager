/*
 * Created on 15.01.2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package com.sport.client.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.sport.client.NavigationTree;
import com.sport.client.VolleyFrame;
import com.sport.core.bo.Tournament;
import com.sport.core.helper.Messages;
import com.sport.server.remote.XmlRemote;

/**
 * @author ronny
 */
public class ProgressRunnables {

	private static final Logger LOG = Logger.getLogger(ProgressRunnables.class);

	public static abstract class PdfRunnable implements Runnable {
		String pfad;
		Vector<Long> selectedGruppeIds;
		Tournament turnierBO;
		ProgressDialog dlg;

		public PdfRunnable() {
		}

		public PdfRunnable(
			String pfad,
			Vector<Long> selectedGruppeIds,
			Tournament turnierBO,
			ProgressDialog dlg) {
			setValues(pfad, selectedGruppeIds, turnierBO, dlg);
		}

		public void setValues(
			String pfad,
			Vector<Long> selectedGruppeIds,
			Tournament turnierBO,
			ProgressDialog dlg) {
			this.pfad = pfad;
			this.selectedGruppeIds = selectedGruppeIds;
			this.turnierBO = turnierBO;
			this.dlg = dlg;
		}
	}

	public static class PdfSpielplanRunnable extends PdfRunnable {

		public PdfSpielplanRunnable() {
		}

		public PdfSpielplanRunnable(
			String pfad,
			Vector<Long> selectedGruppeIds,
			Tournament turnierBO,
			ProgressDialog dlg) {
			super(pfad, selectedGruppeIds, turnierBO, dlg);
		}

		public void run() {
			try {
				Exporter.exportPdfSpielplan(pfad, turnierBO, selectedGruppeIds);
			} catch (FileNotFoundException ex) {
				JOptionPane.showMessageDialog(null, Messages.getString("volleyframe_path_not_found"), //$NON-NLS-1$
				Messages.getString("volleyframe_path_not_found_titel"), //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				LOG.error("Can't export schedule to PDF.", e);
			}
			VolleyFrame.showExternal(pfad);
			dlg.hide();
		}

	};

	public static class PdfSchiedsrichterRunnable extends PdfRunnable {

		public PdfSchiedsrichterRunnable() {
		}

		public PdfSchiedsrichterRunnable(
			String pfad,
			Vector<Long> selectedGruppeIds,
			Tournament turnierBO,
			ProgressDialog dlg) {
			super(pfad, selectedGruppeIds, turnierBO, dlg);
		}

		public void run() {
			try {
				Exporter.exportPdfSchiedsrichter(
					pfad,
					turnierBO,
					selectedGruppeIds);
			} catch (FileNotFoundException ex) {
				// Kann Nutzer nicht anmelden
				JOptionPane.showMessageDialog(null, Messages.getString("volleyframe_path_not_found"), //$NON-NLS-1$
				Messages.getString("volleyframe_path_not_found_titel"), //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				LOG.error("Can't referrers to PDF.", e);
			}
			VolleyFrame.showExternal(pfad);
			dlg.hide();
		}
	};

	public static class PdfSpielberichtRunnable extends PdfRunnable {

		public PdfSpielberichtRunnable() {
		}

		public PdfSpielberichtRunnable(
			String pfad,
			Vector<Long> selectedGruppeIds,
			Tournament turnierBO,
			ProgressDialog dlg) {
			super(pfad, selectedGruppeIds, turnierBO, dlg);
		}

		public void run() {
			try {
				Exporter.exportPdfSpielbericht(
					pfad,
					turnierBO,
					selectedGruppeIds);
			} catch (FileNotFoundException ex) {
				// Kann Nutzer nicht anmelden
				JOptionPane.showMessageDialog(null, Messages.getString("volleyframe_path_not_found"), //$NON-NLS-1$
				Messages.getString("volleyframe_path_not_found_titel"), //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				LOG.error("Can't export tournament report to PDF.", e);
			}
			VolleyFrame.showExternal(pfad);
			dlg.hide();
		}
	};

	public static class PdfGruppenRunnable extends PdfRunnable {

		public PdfGruppenRunnable() {
		}

		public PdfGruppenRunnable(
			String pfad,
			Vector<Long> selectedGruppeIds,
			Tournament turnierBO,
			ProgressDialog dlg) {
			super(pfad, selectedGruppeIds, turnierBO, dlg);
		}

		public void run() {
			try {
				Exporter.exportPdfGruppen(pfad, turnierBO, selectedGruppeIds);
			} catch (FileNotFoundException ex) {
				// Kann Nutzer nicht anmelden
				JOptionPane.showMessageDialog(null, Messages.getString("volleyframe_path_not_found"), //$NON-NLS-1$
				Messages.getString("volleyframe_path_not_found_titel"), //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				LOG.error("Can't export groups to PDF.", e);
			}
			VolleyFrame.showExternal(pfad);
			dlg.hide();
		}
	};

	public static class ExportHtmlRunnable implements Runnable {
		File file;
		Tournament turnierBO;
		ProgressDialog dlg;

		public ExportHtmlRunnable(
			File file,
			Tournament turnierBO,
			ProgressDialog dlg) {
			this.file = file;
			this.turnierBO = turnierBO;
			this.dlg = dlg;
		}

		public void run() {
			// only save to directories
			if (file.isFile()) {
				file = file.getParentFile();
			}
			Exporter.exportHtml(file, turnierBO);
			VolleyFrame.showExternal(file + "/turnier.html"); //$NON-NLS-1$
			dlg.hide();
		}
	};

	public static class OpenTournamentRunnable implements Runnable {
		String pfad;
		NavigationTree navTree;
		long userId;
		ProgressDialog dlg;

		public OpenTournamentRunnable(
			String pfad,
			NavigationTree navTree,
			long userId,
			ProgressDialog dlg) {
			this.pfad = pfad;
			this.navTree = navTree;
			this.userId = userId;
			this.dlg = dlg;
		}

		public void run() {
			// XML lesen
			File f = new File(pfad);
			byte buffer[] = new byte[(int) f.length()];
			try {
				FileInputStream in = new FileInputStream(f);
				int len = in.read(buffer, 0, (int) f.length());
				String xml = new String(buffer, 0, len);

				Tournament turnierBO =
					XmlRemote.loadTurnierByXMLUserid(xml, userId);
				// Knoten in Tree einf�gen
				DefaultMutableTreeNode rootNode =
					((DefaultMutableTreeNode) navTree.getModel().getRoot());
				rootNode.add(navTree.getTurnierNode(turnierBO));
				((DefaultTreeModel) navTree.getModel()).nodeStructureChanged(
					rootNode);
			} catch (Exception e) {
				LOG.error("Can't open tournament from file '"+pfad+"'.", e);
			}
			dlg.hide();
		}
	};

	public static class SaveTournamentRunnable implements Runnable {
		File file;
		Tournament turnierBO;
		ProgressDialog dlg;

		public SaveTournamentRunnable(
			File file,
			Tournament turnierBO,
			ProgressDialog dlg) {
			this.file = file;
			this.turnierBO = turnierBO;
			this.dlg = dlg;
		}

		public void run() {
			String path = file.getPath();

			// falls keine erweiterung -> auto. ergaenzen
			if (!file.getName().matches(".*\\..*")) { //$NON-NLS-1$
				path += ".tur"; //$NON-NLS-1$
			}

			try {
				// XML schreiben
				String xml = XmlRemote.saveXMLByTurnierid(turnierBO.getId());

				FileUtils.writeStringToFile(new File(path), xml, "UTF-8");
			} catch (Exception e) {
				LOG.error("Can't save tournament to file '"+file.getPath()+"'.", e);
			}

			dlg.hide();
		}
	};

}
