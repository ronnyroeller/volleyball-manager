/***********************************************************************
 * Module:  TurnierBean.java
 * Author:  ronny
 * Purpose: Defines the Class TurnierBean
 ***********************************************************************/

package com.sport.server.ejb.entity;

import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EntityContext;

/**
 * @ejb:bean name="Turnier" type="CMP" local-jndi-name="TurnierLocal"
 *           view-type="local"
 * 
 * @ejb:finder signature="TurnierLocal findById(java.lang.Long id)"
 *             unchecked="true"
 *             query="SELECT OBJECT(n) FROM Turnier AS n WHERE n.id = ?1"
 * 
 * @ejb:finder signature="TurnierLocal findByLinkid(java.lang.String linkid)"
 *             unchecked="true"
 *             query="SELECT OBJECT(n) FROM Turnier AS n WHERE n.linkid = ?1"
 * 
 * @ejb.finder signature="java.util.Collection findAll()" unchecked="true"
 *             query="SELECT OBJECT(n) FROM Turnier AS n"
 * 
 * @ejb:transaction type="Required"
 * @jboss:table-name TURNIER
 */
public abstract class TurnierBean implements javax.ejb.EntityBean {
	private EntityContext ejbContext;

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @jboss:column-name name="ID"
	 **/
	public abstract java.lang.Long getId();

	public abstract void setId(java.lang.Long id);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:persistent-field
	 * @jboss:column-name name="LINKID"
	 * @jboss:persistence dbindex="true"
	 * @jboss:persistence dbindex="true"
	 **/
	public abstract java.lang.String getLinkid();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setLinkid(java.lang.String linkid);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:persistent-field
	 * @jboss:column-name name="NAME"
	 * @jboss:persistence dbindex="true"
	 **/
	public abstract java.lang.String getName();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setName(java.lang.String name);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:persistent-field
	 * @jboss:column-name name="DATUM"
	 * @jboss:persistence dbindex="true"
	 **/
	public abstract java.util.Date getDatum();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setDatum(java.util.Date datum);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:persistent-field
	 * @jboss:column-name name="TEXT"
	 **/
	public abstract java.lang.String getText();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setText(java.lang.String text);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:persistent-field
	 * @jboss:column-name name="PUNKTPROSATZ"
	 **/
	public abstract java.lang.Long getPunkteprosatz();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setPunkteprosatz(java.lang.Long punkteprosatz);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:persistent-field
	 * @jboss:column-name name="PUNKTPROSPIEL"
	 **/
	public abstract java.lang.Long getPunkteprospiel();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setPunkteprospiel(java.lang.Long punkteprospiel);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:persistent-field
	 * @jboss:column-name name="PUNKTPROUNENTSCHIEDENSPIEL"
	 **/
	public abstract java.lang.Double getPunkteprounentschiedenspiel();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setPunkteprounentschiedenspiel(
			java.lang.Double punkteprounentschiedenspiel);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:persistent-field
	 * @jboss:column-name name="SPIELDAUER"
	 **/
	public abstract java.lang.Long getSpieldauer();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setSpieldauer(java.lang.Long spieldauer);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:persistent-field
	 * @jboss:column-name name="PAUSEDAUER"
	 **/
	public abstract java.lang.Long getPausedauer();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setPausedauer(java.lang.Long pausedauer);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:persistent-field
	 * @jboss:column-name name="BEAMERUMSCHALTZEIT"
	 **/
	public abstract java.lang.Long getBeamerumschaltzeit();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setBeamerumschaltzeit(java.lang.Long beamerumschaltzeit);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:persistent-field
	 * @jboss:column-name name="BANNERLINK"
	 **/
	public abstract java.lang.String getBannerLink();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setBannerLink(java.lang.String bannerlink);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:persistent-field
	 * @jboss:column-name name="SPIELPLANGESPERRT"
	 **/
	public abstract java.lang.Boolean getSpielplangesperrt();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setSpielplangesperrt(
			java.lang.Boolean spielplangesperrt);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:relation name="regel_turnier" role-name="many-turnier-has-one-regel"
	 * @jboss:relation related-pk-field="id" fk-column="REGEL_ID"
	 */
	public abstract com.sport.server.ejb.interfaces.RegelLocal getRegel();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setRegel(com.sport.server.ejb.interfaces.RegelLocal regeln);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:relation name="turnier_platzierung"
	 *               role-name="one-turnier-has-many-platzierungen"
	 */
	public abstract Set getPlatzierungen();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setPlatzierungen(Set platzierungen);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:relation name="turnier_gruppe"
	 *               role-name="one-turnier-has-many-gruppen"
	 */
	public abstract Set getGruppen();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setGruppen(Set gruppen);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:relation name="turnier_mannschaft"
	 *               role-name="one-turnier-has-many-mannschaften"
	 */
	public abstract Set getMannschaften();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setMannschaften(Set mannschaften);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:relation name="turnier_spielplatz"
	 *               role-name="one-turnier-has-many-spielplaetze"
	 */
	public abstract Set getSpielplaetze();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setSpielplaetze(Set spielplaetze);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:relation name="user_turnier" role-name="many-turnier-has-one-user"
	 *               cascade-delete="yes"
	 * @jboss:relation related-pk-field="id" fk-column="USER_ID"
	 */
	public abstract com.sport.server.ejb.interfaces.UserLocal getUser();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setUser(com.sport.server.ejb.interfaces.UserLocal user);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:relation name="turnier_mitbenutzeruser"
	 *               role-name="many-turnier-has-many-mitbenutzeruser"
	 * @jboss:relation related-pk-field="id" fk-column="USER_ID"
	 * @jboss:relation-table table-name="TURNIER_MITBENUTZER"
	 */
	public abstract Set getTurnieremitnutzer();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setTurnieremitnutzer(Set turnieremitnutzer);

	public TurnierBean() {

	}

	/**
	 * @ejb:create-method view-type="local"
	 */
	public com.sport.server.ejb.interfaces.TurnierPK ejbCreate() throws CreateException {
		this.setId(new Long(Math.round(Math.random() * 2100000000)));
		return new com.sport.server.ejb.interfaces.TurnierPK(getId());
	}

	public void ejbPostCreate() throws CreateException {
	}

	/**
	 * A container invokes this method when the instance is taken out of the
	 * pool of available instances to become associated with a specific EJB
	 * object.
	 * 
	 * @exception javax.ejb.EJBException
	 */
	public void ejbActivate() throws javax.ejb.EJBException {

	}

	/**
	 * A container invokes this method to instruct the instance to synchronize
	 * its state by loading it state from the underlying database.
	 * 
	 * @exception javax.ejb.EJBException
	 */
	public void ejbLoad() throws javax.ejb.EJBException {

	}

	/**
	 * A container invokes this method on an instance before the instance
	 * becomes disassociated with a specific EJB object.
	 * 
	 * @exception javax.ejb.EJBException
	 */
	public void ejbPassivate() throws javax.ejb.EJBException {

	}

	/**
	 * A container invokes this method before it removes the EJB object that is
	 * currently associated with the instance.
	 * 
	 * @exception javax.ejb.RemoveException
	 * @exception javax.ejb.EJBException
	 */
	public void ejbRemove() throws javax.ejb.RemoveException,
			javax.ejb.EJBException {

		// rekursiv Gruppen mitl�schen
		Object[] gruppen = getGruppen().toArray();
		for (int i = 0; i < gruppen.length; i++) {
			com.sport.server.ejb.interfaces.GruppeLocal gruppeLocal = (com.sport.server.ejb.interfaces.GruppeLocal) gruppen[i];
			gruppeLocal.remove();
		}
	}

	/**
	 * A container invokes this method to instruct the instance to synchronize
	 * its state by storing it to the underlying database.
	 * 
	 * @exception javax.ejb.EJBException
	 */
	public void ejbStore() throws javax.ejb.EJBException {

	}

	/**
	 * Set the associated entity context.
	 * 
	 * @param ctx
	 * @exception javax.ejb.EJBException
	 */
	public void setEntityContext(EntityContext ctx)
			throws javax.ejb.EJBException {
		this.ejbContext = ctx;
	}

	/**
	 * Unset the associated entity context.
	 * 
	 * @exception javax.ejb.EJBException
	 */
	public void unsetEntityContext() throws javax.ejb.EJBException {
		this.ejbContext = null;
	}

}