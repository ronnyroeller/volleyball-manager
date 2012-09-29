package com.sport.core.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ronny
 */
public class Tournament implements Serializable {

	private static final long serialVersionUID = 3378899498635165610L;

	private long id;
	private String linkid;
	private String name;
	private Date date;
	private long pointsPerSet = 2;
	private long pointsPerMatch = 2;
	private double pointsPerTie = 1;
	private long durationMatch = 1200; // in Sec
	private long durationBreak = 300; // in Sec
	private long durationProjectorSwitch = 30; // in Sec
	private String bannerLink = "<A HREF=\"http://www.volleyball-manager.com\" TARGET=\"_top\"><img src=\"/img/ad4.jpg\" border=\"1\" width=\"475\" height=\"60\" alt=\"Volleyball Manager\" /></a>";
	private String text;
	private boolean spielplangesperrt = false;

	public Tournament() {
		// Achtung: dieser Wert wird NICHT fuer die EJB-Ids verwendet!
		// eindeutige ID, damit Combo-Box funktioniert!
		setId(Math.round(Math.random() * 2100000000));
	}

	public Date getDate() {
		return date;
	}

	public long getId() {
		return id;
	}

	public String getLinkid() {
		return linkid;
	}

	public String getName() {
		return name;
	}

	public String getBannerLink() {
		return bannerLink;
	}

	public String getText() {
		return text;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setId(long l) {
		id = l;
	}

	public void setLinkid(String l) {
		linkid = l;
	}

	public void setName(String string) {
		name = string;
	}

	public void setBannerLink(String string) {
		bannerLink = string;
	}

	public void setText(String string) {
		text = string;
	}

	public String toString() {
		return getName();
	}

	public long getPointsPerSet() {
		return pointsPerSet;
	}

	public long getPointsPerMatch() {
		return pointsPerMatch;
	}

	public void setPointsPerSet(long l) {
		pointsPerSet = l;
	}

	public void setPointsPerMatch(long l) {
		pointsPerMatch = l;
	}

	public long getDurationProjectorSwitch() {
		return durationProjectorSwitch;
	}

	public void setDurationProjectorSwitch(long durationProjectorSwitch) {
		this.durationProjectorSwitch = durationProjectorSwitch;
	}

	public long getDurationBreak() {
		return durationBreak;
	}

	public void setDurationBreak(long durationBreak) {
		this.durationBreak = durationBreak;
	}

	public double getPointsPerTie() {
		return pointsPerTie;
	}

	public void setPointsPerTie(double pointsPerTie) {
		this.pointsPerTie = pointsPerTie;
	}

	public long getDurationMatch() {
		return durationMatch;
	}

	public void setDurationMatch(long durationMatch) {
		this.durationMatch = durationMatch;
	}

	public boolean isSpielplangesperrt() {
		return spielplangesperrt;
	}

	public boolean getSpielplangesperrt() {
		return isSpielplangesperrt();
	}

	public void setSpielplangesperrt(boolean spielplangesperrt) {
		this.spielplangesperrt = spielplangesperrt;
	}

}
