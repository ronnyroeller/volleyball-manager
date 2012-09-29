/*
* JBoss, the OpenSource J2EE webOS
*
* Distributable under LGPL license.
* See terms of license at gnu.org.
*/
package com.sport.server.ejb.session;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sport.core.bo.TournamentSystem;
import com.sport.core.bo.UserBO;
import com.sport.server.ejb.HomeGetter;
import com.sport.server.ejb.interfaces.ModusLocalHome;
import com.sport.server.ejb.interfaces.UserLocal;

/**
 * @ejb:bean name="UserSession"
 *           type="Stateless"
 *           jndi-name="UserSessionHome"
 *           local-jndi-name="UserSessionLocal"
 *           view-type="both"
 * 
 **/
public class UserSessionBean implements SessionBean {

	private static final Logger LOG = Logger.getLogger(UserSessionBean.class);

	private SessionContext mContext;

	/**
	 * gibt einen bestimmten Nutzer zur�ck
	 * @ejb:interface-method view-type="both"
	 **/
	public UserBO getUserById(long userid) {
		UserLocal userLocal = null;
		try {
			userLocal = HomeGetter.getUserHome().findById(new Long(userid));
		}
		catch (Exception e) {
			LOG.error("User-ID is invalid.", e);
		}

		return BOCreator.createUserBO(userLocal);
	}

	/**
	 * Initialisiert DB. Schreibt ben�tigte Daten und legt Standarduser an.
 	 * @ejb:interface-method view-type="remote"
	 */
	public void init()
		throws RemoteException, Exception {
		try {
			// Standarduser
			UserLocal userLocal = HomeGetter.getUserHome().create(1, "Admin");
			userLocal.setUsername("admin");
			userLocal.setPasswort("admin");

			// Modi
			ModusLocalHome modusHome = HomeGetter.getModusHome();
			modusHome.create(TournamentSystem.GRUPPENMODUS,"modusbo_groupmode"); //$NON-NLS-1$
			modusHome.create(TournamentSystem.GRUPPE_RUECKSPIELMODUS,"modusbo_groupmodeback"); //$NON-NLS-1$
			modusHome.create(TournamentSystem.KOMODUS,"modusbo_komode"); //$NON-NLS-1$
			modusHome.create(TournamentSystem.DOPPELKOMODUS,"modusbo_doppelkomode"); //$NON-NLS-1$
		}
		catch (Exception e) {
			LOG.error("Couldn't initialize the database.", e);
		}
	}

	/**
	 * L�scht alle Tabellen
	 * @ejb:interface-method view-type="remote"
	 */
	public void drop()
		throws RemoteException {
		Connection conn = null;
		try {
			Context ctx = HomeGetter.getMyInitialContext();
			javax.sql.DataSource ds =
				(javax.sql.DataSource) ctx.lookup("java:/jvolleyDS");
			conn = ds.getConnection();

			Statement st = conn.createStatement();
			st.execute("DROP TABLE gruppe");
			st.execute("DROP TABLE konstante");
			st.execute("DROP TABLE mannschaft");
			st.execute("DROP TABLE modus");
			st.execute("DROP TABLE platzierung");
			st.execute("DROP TABLE regel");
			st.execute("DROP TABLE satz");
			st.execute("DROP TABLE spiel");
			st.execute("DROP TABLE spielplatz");
			st.execute("DROP TABLE turnier");
			st.execute("DROP TABLE turnier_mitbenutzer");
			st.execute("DROP TABLE tuser");
			st.close();
			conn.close();
		}
		catch (NamingException e) {
			LOG.error("Couldn't find datenbase java:/jvolleyDS.", e);
		}
		catch (SQLException e) {
			LOG.error("Encountered SQL error.", e);
		}
	}

	/**
	* Create the Session Bean
	*
	* @throws CreateException 
	*
	* @ejb:create-method view-type="remote"
	**/
	public void ejbCreate() throws CreateException {
	}

	/**
	* Describes the instance and its content for debugging purpose
	*
	* @return Debugging information about the instance and its content
	**/
	public String toString() {
		return "UserSessionBean [ " + " ]";
	}

	public void setSessionContext(SessionContext aContext)
		throws EJBException {
		mContext = aContext;
	}

	public void ejbActivate() throws EJBException {
	}

	public void ejbPassivate() throws EJBException {
	}

	public void ejbRemove() throws EJBException {
	}
}
