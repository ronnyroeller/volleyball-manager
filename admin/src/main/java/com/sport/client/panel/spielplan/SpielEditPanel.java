/*
 * Created on 16.06.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client.panel.spielplan;

import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sport.client.panel.EditListener;
import com.sport.core.bo.SpielplanTableEntryBO;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;
import com.sport.core.bo.comparators.GruppenComparator;
import com.sport.core.bo.comparators.SpielComparator;
import com.sport.core.bo.modus.ModusMgr;
import com.sport.core.helper.Messages;


/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SpielEditPanel extends JPanel {

	private static final long serialVersionUID = 6425504761006985570L;

	protected SportMatch spielBO = null; // aktuell angezeigtes Spiel

	private SpielplanPanel spielpanel = null;

	private int row;
	private int col;

	protected JComboBox gruppe = new JComboBox();
	private JComboBox paarung = new JComboBox();
	private JComboBox mannschaft1 = new JComboBox();
	private JComboBox mannschaft2 = new JComboBox();
	private JComboBox schiedsrichtergruppe = new JComboBox();
	private JComboBox schiedsrichter = new JComboBox();

	public SpielEditPanel(SpielplanPanel spielplanel) {
		this.spielpanel = spielplanel;

		row = spielpanel.spielplanTable.getSelectedRow();
		col = spielpanel.spielplanTable.getSelectedColumn();

		JLabel jLabel1 = new JLabel(Messages.getString("spieleditpanel_group")); //$NON-NLS-1$
		jLabel1.setBounds(new Rectangle(13, 5, 76, 15));
		JLabel jLabel2 = new JLabel(Messages
				.getString("spieleditpanel_setting")); //$NON-NLS-1$
		jLabel2.setBounds(new Rectangle(13, 31, 76, 15));
		JLabel jLabel3 = new JLabel(Messages
				.getString("spieleditpanel_referee")); //$NON-NLS-1$
		jLabel3.setBounds(new Rectangle(13, 56, 76, 15));

		gruppe.setBounds(new Rectangle(88, 2, 140, 21));
		paarung.setBounds(new Rectangle(88, 28, 307, 21));
		mannschaft1.setBounds(new Rectangle(88, 28, 140, 21));
		mannschaft2.setBounds(new Rectangle(255, 28, 140, 21));
		schiedsrichtergruppe.setBounds(new Rectangle(88, 53, 140, 21));
		schiedsrichter.setBounds(new Rectangle(255, 53, 140, 21));

		this.setLayout(null);
		this.add(jLabel1, null);
		this.add(jLabel2, null);
		this.add(jLabel3, null);

		this.add(gruppe, null);
		this.add(paarung, null);
		this.add(mannschaft1, null);
		this.add(mannschaft2, null);
		this.add(schiedsrichtergruppe, null);
		this.add(schiedsrichter, null);

		schiedsrichtergruppe
				.addItemListener(new SchiedsrichterGruppeItemListener());

		gruppe.addItemListener(new GruppePaarungItemListener());

		gruppe.addKeyListener(new EditListener(this.spielpanel));
		gruppe.addMouseListener(new EditListener(this.spielpanel));
		paarung.addKeyListener(new EditListener(this.spielpanel));
		paarung.addMouseListener(new EditListener(this.spielpanel));
		mannschaft1.addKeyListener(new EditListener(this.spielpanel));
		mannschaft1.addMouseListener(new EditListener(this.spielpanel));
		mannschaft2.addKeyListener(new EditListener(this.spielpanel));
		mannschaft2.addMouseListener(new EditListener(this.spielpanel));
		schiedsrichter.addKeyListener(new EditListener(this.spielpanel));
		schiedsrichter.addMouseListener(new EditListener(this.spielpanel));

	}

	/**
	 * F�llt Gruppen-Combobox mit allen m�glichen Gruppen
	 * 
	 * @param gruppen
	 */
	public void setGruppen(Vector<SportGroup> gruppen) {
		gruppe.removeAllItems();
		schiedsrichtergruppe.removeAllItems();
		// keine Gruppe als Auswahlmoeglichkeit ergaenzen
		gruppe.addItem(null);
		schiedsrichtergruppe.addItem(null);

		Collections.sort(gruppen, new GruppenComparator());
		for (SportGroup gruppeBO : gruppen) {
			// nur Gruppen mit Mannschaften anzeigen
			if (gruppeBO.getMannschaften().size() > 0) {
				gruppe.addItem(gruppeBO);
				schiedsrichtergruppe.addItem(gruppeBO);
			}
		}
	}

	/**
	 * alte Daten zur�ckspeichern
	 * 
	 */
	public void save() {
		requestFocus();

		SportMatch oldSpielBO = this.spielBO;

		// Zeile noch existent?
		if (spielpanel.spielplanTable.dataModel.getData().size() - 1 >= row) {
			SpielplanTableEntryBO tableEntry = (SpielplanTableEntryBO) spielpanel.spielplanTable.dataModel
					.getData().get(row);

			// insert new game
			if (oldSpielBO == null && gruppe.getSelectedItem() != null) {
				spielpanel.dirty = true;

				oldSpielBO = new SportMatch();
				oldSpielBO.setStartDate(tableEntry.getVondatum());
				oldSpielBO.setEndDate(tableEntry.getBisdatum());
				oldSpielBO.setField(spielpanel.spielplanTable.dataModel
						.getSpielplatz((col - 1) / 2));

				tableEntry.setSpiel((col - 1) / 2, oldSpielBO);
			}

			// new game or update
			Object selectedItem = paarung.getSelectedItem();
			if (gruppe.getSelectedItem() != null // group is selected?
					&& (selectedItem != null // and paarung is
					// preselected?
					|| (mannschaft1.getSelectedItem() != null // or two teams?
					&& mannschaft1.getSelectedItem() != null))) {
				SportGroup gruppeBO = (SportGroup) gruppe.getSelectedItem();

				if (oldSpielBO.getGroup() != null) {
					oldSpielBO.getGroup().removeMatch(oldSpielBO);
				}
				gruppeBO.addMatch(oldSpielBO);

				// Unterscheiden, ob Paarung- oder Mannschaftenmodus
				if (ModusMgr.isShowPaarung(gruppeBO)) {
					SportMatch thisSpielBO = ((MatchComboEntry) selectedItem)
							.getMatch();
					oldSpielBO.setTeam1(thisSpielBO.getTeam1());
					oldSpielBO.setTeam2(thisSpielBO.getTeam2());
				} else {
					TeamComboEntry teamComboEntry1 = (TeamComboEntry) mannschaft1
							.getSelectedItem();
					TeamComboEntry teamComboEntry2 = (TeamComboEntry) mannschaft2
							.getSelectedItem();

					oldSpielBO.setTeam1(teamComboEntry1.getTeam());
					oldSpielBO.setTeam2(teamComboEntry2.getTeam());
				}

				TeamComboEntry teamComboEntry = (TeamComboEntry) schiedsrichter
						.getSelectedItem();
				if (teamComboEntry != null)
					oldSpielBO.setReferee(teamComboEntry.getTeam());
				else
					oldSpielBO.setReferee(null);
			} else { // delete game
				oldSpielBO = null;

				SportMatch thisSpielBO = tableEntry.getSpiel((col - 1) / 2);
				if (thisSpielBO != null && thisSpielBO.getGroup() != null) {
					thisSpielBO.getGroup().getMatches().remove(thisSpielBO);
				}
				tableEntry.setSpiel((col - 1) / 2, null);
			}

			spielpanel.spielplanTable.updateUI();
		}
	}

	/**
	 * Setzt Daten
	 * 
	 * @param spielBO
	 */
	public void setSpielBO(SportMatch spielBO) {
		this.spielBO = spielBO;

		// kein Spiel ausgewaehlt
		if (spielBO != null) {
			gruppe.setSelectedItem(spielBO.getGroup());
			Team referee = spielBO.getReferee();
			if (referee != null) {
				SportGroup schiedsrichterGruppeBO = referee.getGruppeBO();
				SportMatch logspielBO = referee.getLogspielBO();
				schiedsrichterGruppeBO = (schiedsrichterGruppeBO != null) ? schiedsrichterGruppeBO
						: referee.getLoggruppeBO();
				schiedsrichterGruppeBO = (schiedsrichterGruppeBO != null) ? schiedsrichterGruppeBO
						: logspielBO.getGroup();
				schiedsrichtergruppe.setSelectedItem(schiedsrichterGruppeBO);

				// find referee - not equal on id-level -> logical level
				for (int i = 0; i < schiedsrichter.getItemCount(); i++) {
					TeamComboEntry teamComboEntry = (TeamComboEntry) schiedsrichter
							.getItemAt(i);
					if (teamComboEntry != null) {
						Team team = teamComboEntry.getTeam();
						if (team.logequals(referee) || team.equals(referee)) {
							schiedsrichter.setSelectedIndex(i);
						}
					}
				}
			} else {
				schiedsrichtergruppe.setSelectedItem(null);
				schiedsrichter.setSelectedItem(null);
			}
		} else {
			gruppe.setSelectedItem(null);
			schiedsrichter.setSelectedItem(null);
		}
	}

	/**
	 * Wechselt zwischen Paarungsansicht und Mannschaftsauswahl
	 * 
	 * @param visible
	 */
	private void setPaarungVisible(boolean visible) {
		paarung.setVisible(visible);
		mannschaft1.setVisible(!visible);
		mannschaft2.setVisible(!visible);
	}

	/**
	 * Initiallisiert Paarungs-Box
	 * 
	 * @param gruppeBO
	 */
	private void initPaarungen(SportGroup gruppeBO) {
		Vector<SportMatch> spiele = ModusMgr.getUnusedSpiele(gruppeBO);

		SportMatch aktSpielBO = null;

		// ggf. aktuelles Spiel einfuegen
		if (spielBO != null && gruppeBO == spielBO.getGroup()) {
			// Hinspiel
			aktSpielBO = new SportMatch();
			aktSpielBO.setTeam1(spielBO.getTeam1());
			aktSpielBO.setTeam2(spielBO.getTeam2());
			aktSpielBO.setGroup(spielBO.getGroup());
			spiele.add(aktSpielBO);

			// R�ckspiel
			SportMatch rueckspielBO = new SportMatch();
			rueckspielBO.setTeam1(spielBO.getTeam2());
			rueckspielBO.setTeam2(spielBO.getTeam1());
			rueckspielBO.setGroup(spielBO.getGroup());
			spiele.add(rueckspielBO);
		}

		Collections.sort(spiele, new SpielComparator());

		for (SportMatch spielBO2 : spiele) {
			MatchComboEntry entry = new MatchComboEntry(spielBO2);
			paarung.addItem(entry);

			// ggf. aktuelles Spiel selektieren
			if ((aktSpielBO != null) && (spielBO2 == aktSpielBO))
				paarung.setSelectedItem(entry);
		}
	}

	/**
	 * Initiallisiert beide Mannschaften-Boxen
	 * 
	 * @param gruppeBO
	 */
	private void initMannschaften(SportGroup gruppeBO, JComboBox comboBox) {
		SpielplanTableEntryBO tableEntry = (SpielplanTableEntryBO) spielpanel.spielplanTable.dataModel
				.getData().get(row);

		Vector<Team> mannschaften = ModusMgr.getMannschaften(gruppeBO,
				tableEntry.getVondatum());
		for (Team mannschaftBO : mannschaften) {
			TeamComboEntry teamComboEntry = new TeamComboEntry(mannschaftBO);
			comboBox.addItem(teamComboEntry);

			if (comboBox == mannschaft1) {
				// ggf. selectieren
				if (spielBO != null
						&& spielBO.getTeam1() != null
						&& (spielBO.getTeam1().equals(mannschaftBO) || spielBO
								.getTeam1().logequals(mannschaftBO))) {
					comboBox.setSelectedItem(teamComboEntry);
				}
			}
			if (comboBox == mannschaft2) {
				if (spielBO != null
						&& spielBO.getTeam2() != null
						&& (spielBO.getTeam2().equals(mannschaftBO) || spielBO
								.getTeam2().logequals(mannschaftBO))) {
					comboBox.setSelectedItem(teamComboEntry);
				}
			}
		}
	}

	/**
	 * Updated Paarungs/Mannschaftbox, wenn sich die Gruppe �ndertz
	 * 
	 * @param usedSpiele
	 */
	public void updatePaarung(SportGroup gruppeBO) {
		// default ist Paarung-Ansicht
		setPaarungVisible(true);

		paarung.removeAllItems();
		mannschaft1.removeAllItems();
		mannschaft2.removeAllItems();

		// auf keine Gruppe setzen?
		if (gruppeBO == null) {
			return;
		}

		// Paarungen fuer gruppe laden
		if (ModusMgr.isShowPaarung(gruppeBO)) {
			initPaarungen(gruppeBO);
		} else {
			initMannschaften(gruppeBO, mannschaft1);
			initMannschaften(gruppeBO, mannschaft2);
			setPaarungVisible(false);
		}

	}

	/**
	 * L�dt Mannschaften in mannschaftCombo, wenn Gruppenauswahl in gruppeCombo
	 * ge�ndert wurde
	 * 
	 * @param gruppeCombo
	 * @param mannschaftCombo
	 */
	public void updateSchiedsrichterCombo(JComboBox gruppeCombo,
			SportGroup gruppeBO) {
		schiedsrichter.removeAllItems();
		schiedsrichter.addItem(null);

		if (gruppeBO != null) {
			initMannschaften(gruppeBO, schiedsrichter);
		} else {
			schiedsrichter.setSelectedItem(null);
		}
	}

	/**
	 * Bei Gruppenauswahl -> M�gliche Mannschaften nachladen
	 * 
	 * @author ronny
	 * 
	 *         To change the template for this generated type comment go to
	 *         Window>Preferences>Java>Code Generation>Code and Comments
	 */
	class SchiedsrichterGruppeItemListener implements ItemListener {

		public void itemStateChanged(ItemEvent e) {
			JComboBox gruppeCombo = (JComboBox) e.getSource();
			SportGroup gruppeBO = (SportGroup) gruppeCombo.getSelectedItem();
			if (e.getStateChange() == ItemEvent.SELECTED || gruppeBO == null) {
				updateSchiedsrichterCombo(gruppeCombo, gruppeBO);
			}
		}

	}

	/**
	 * Bei Gruppenauswahl -> M�gliche Paarungen nachladen
	 * 
	 * @author ronny
	 * 
	 *         To change the template for this generated type comment go to
	 *         Window>Preferences>Java>Code Generation>Code and Comments
	 */
	class GruppePaarungItemListener implements ItemListener {

		public void itemStateChanged(ItemEvent e) {
			JComboBox gruppeCombo = (JComboBox) e.getSource();
			SportGroup gruppeBO = (SportGroup) gruppeCombo.getSelectedItem();
			if (e.getStateChange() == ItemEvent.SELECTED || gruppeBO == null) {
				updatePaarung(gruppeBO);
			}
		}

	}

}