/*
 * Created on 05.06.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.server.ejb;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.sport.server.ejb.interfaces.GruppeLocalHome;
import com.sport.server.ejb.interfaces.MannschaftLocalHome;
import com.sport.server.ejb.interfaces.ModusLocalHome;
import com.sport.server.ejb.interfaces.PlatzierungLocalHome;
import com.sport.server.ejb.interfaces.SatzLocalHome;
import com.sport.server.ejb.interfaces.SpielLocalHome;
import com.sport.server.ejb.interfaces.SpielplatzLocalHome;
import com.sport.server.ejb.interfaces.TurnierLocalHome;
import com.sport.server.ejb.interfaces.TurnierSessionHome;
import com.sport.server.ejb.interfaces.UserLocalHome;
import com.sport.server.ejb.interfaces.UserSessionHome;
import com.sport.server.ejb.interfaces.XmlSessionHome;

/**
 * @author ronny
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class HomeGetter {
	static private String host = "localhost";
	static private String port = "8080";
	static private InitialContext initalContext = null;

	public static Context getMyInitialContext() throws NamingException {
		if (initalContext != null) {
			return initalContext;
		}
		Hashtable env = new Hashtable();

		env.put(
			"java.naming.factory.initial",
			"org.jnp.interfaces.NamingContextFactory");
		env.put("java.naming.provider.url", "jnp://" + host);
		initalContext = new InitialContext(env);
		return initalContext;
	}

	/**
	 * Gibt den UserLocalHome-Schnittstelle zur�ck
	 */
	public static UserLocalHome getUserHome()
		throws RemoteException, NamingException {
		Context lContext = getMyInitialContext();
		UserLocalHome lhome =
			(UserLocalHome) PortableRemoteObject.narrow(
				lContext.lookup("UserLocal"),
				UserLocalHome.class);
		return lhome;
	}

	/**
	 * Gibt den TurnierLocalHome-Schnittstelle zur�ck
	 */
	public static TurnierLocalHome getTurnierHome()
		throws RemoteException, NamingException {
		Context lContext = getMyInitialContext();
		TurnierLocalHome lhome =
			(TurnierLocalHome) PortableRemoteObject.narrow(
				lContext.lookup("TurnierLocal"),
				TurnierLocalHome.class);
		return lhome;
	}

	/**
	 * Gibt den MannschaftLocalHome-Schnittstelle zur�ck
	 */
	public static MannschaftLocalHome getMannschaftHome()
		throws RemoteException, NamingException {
		Context lContext = getMyInitialContext();
		MannschaftLocalHome lhome =
			(MannschaftLocalHome) PortableRemoteObject.narrow(
				lContext.lookup("MannschaftLocal"),
				MannschaftLocalHome.class);
		return lhome;
	}

	/**
	 * Gibt den GruppeLocalHome-Schnittstelle zur�ck
	 */
	public static GruppeLocalHome getGruppeHome()
		throws RemoteException, NamingException {
		Context lContext = getMyInitialContext();
		GruppeLocalHome lhome =
			(GruppeLocalHome) PortableRemoteObject.narrow(
				lContext.lookup("GruppeLocal"),
				GruppeLocalHome.class);
		return lhome;
	}

	/**
	 * Gibt den ModusLocalHome-Schnittstelle zur�ck
	 */
	public static ModusLocalHome getModusHome()
		throws RemoteException, NamingException {
		Context lContext = getMyInitialContext();
		ModusLocalHome lhome =
			(ModusLocalHome) PortableRemoteObject.narrow(
				lContext.lookup("ModusLocal"),
				ModusLocalHome.class);
		return lhome;
	}

	/**
	 * Gibt den SpielLocalHome-Schnittstelle zur�ck
	 */
	public static SpielLocalHome getSpielHome()
		throws RemoteException, NamingException {
		Context lContext = getMyInitialContext();
		SpielLocalHome lhome =
			(SpielLocalHome) PortableRemoteObject.narrow(
				lContext.lookup("SpielLocal"),
				SpielLocalHome.class);
		return lhome;
	}

	/**
	 * Gibt den SpielplatzLocalHome-Schnittstelle zur�ck
	 */
	public static SpielplatzLocalHome getSpielplatzHome()
		throws RemoteException, NamingException {
		Context lContext = getMyInitialContext();
		SpielplatzLocalHome lhome =
			(SpielplatzLocalHome) PortableRemoteObject.narrow(
				lContext.lookup("SpielplatzLocal"),
				SpielplatzLocalHome.class);
		return lhome;
	}

	/**
	 * Gibt den SatzLocalHome-Schnittstelle zur�ck
	 */
	public static SatzLocalHome getSatzHome()
		throws RemoteException, NamingException {
		Context lContext = getMyInitialContext();
		SatzLocalHome lhome =
			(SatzLocalHome) PortableRemoteObject.narrow(
				lContext.lookup("SatzLocal"),
				SatzLocalHome.class);
		return lhome;
	}

	/**
	 * Gibt den PlatzierungLocalHome-Schnittstelle zur�ck
	 */
	public static PlatzierungLocalHome getPlatzierungHome()
		throws RemoteException, NamingException {
		Context lContext = getMyInitialContext();
		PlatzierungLocalHome lhome =
			(PlatzierungLocalHome) PortableRemoteObject.narrow(
				lContext.lookup("PlatzierungLocal"),
				PlatzierungLocalHome.class);
		return lhome;
	}

	/**
	 * Gibt den TurnierSessionHome-Schnittstelle zur�ck
	 */
	public static TurnierSessionHome getTurnierSessionHome()
		throws RemoteException, NamingException {
		Context lContext = getMyInitialContext();
		Object obj = lContext.lookup("TurnierSessionHome");
		TurnierSessionHome home =
			(TurnierSessionHome) javax.rmi.PortableRemoteObject.narrow(
				obj,
				TurnierSessionHome.class);
		return home;
	}

	/**
	 * Gibt den XmlSessionHome-Schnittstelle zur�ck
	 */
	public static XmlSessionHome getXmlSessionHome()
		throws RemoteException, NamingException {
		Context lContext = getMyInitialContext();
		Object obj = lContext.lookup("XmlSessionHome");
		XmlSessionHome home =
			(XmlSessionHome) javax.rmi.PortableRemoteObject.narrow(
				obj,
				XmlSessionHome.class);
		return home;
	}

	/**
	 * Gibt den UserSessionHome-Schnittstelle zur�ck
	 */
	public static UserSessionHome getUserSessionHome()
		throws RemoteException, NamingException {
		Context lContext = getMyInitialContext();
		Object obj = lContext.lookup("UserSessionHome");
		UserSessionHome home =
			(UserSessionHome) javax.rmi.PortableRemoteObject.narrow(
				obj,
				UserSessionHome.class);
		return home;
	}

	/**
	 * @param host
	 *            The host to set.
	 */
	public static void setHost(String aHost) {
		if (aHost != null && aHost != "") {
			HomeGetter.host = aHost;
		}
	}

	/**
	 * @return Returns the host.
	 */
	public static String getHost() {
		return host;
	}

	/**
	 * @return Returns the port.
	 */
	public static String getPort() {
		return port;
	}

	/**
	 * @param port
	 *            The port to set.
	 */
	public static void setPort(String port) {
		HomeGetter.port = port;
	}

	/**
	 * Give back the Address of the Online-Module
	 * @return
	 */
	public static String getWebUrl() {
		return "http://"
			+ HomeGetter.getHost()
			+ ":"
			+ HomeGetter.getPort();
	}
}
