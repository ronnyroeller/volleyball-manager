/***********************************************************************
 * Module:  SpielBean.java
 * Author:  ronny
 * Purpose: Defines the Class SpielBean
 ***********************************************************************/

package com.sport.server.ejb.entity;

import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EntityContext;

/** @ejb:bean
  *   name="Spiel"
  *   type="CMP"
  *   local-jndi-name="SpielLocal"
  *   view-type="local"
  * 
  * @ejb:finder signature="SpielLocal findById(java.lang.Long id)" unchecked="true"
  *  query="SELECT OBJECT(n) FROM Spiel AS n WHERE n.id = ?1"
  * 
  * @ejb:transaction type="Required"
  * @jboss:table-name SPIEL */
public abstract class SpielBean implements javax.ejb.EntityBean {
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
	* @jboss:column-name name="VONDATUM"
	* @jboss:persistence dbindex="true"
	**/
	public abstract java.util.Date getVondatum();
	/**
	* @ejb:interface-method view-type="local"
	**/
	public abstract void setVondatum(java.util.Date vondatum);

	/**
	* @ejb:interface-method view-type="local"
	* @ejb:persistent-field
	* @jboss:column-name name="BISDATUM"
	**/
	public abstract java.util.Date getBisdatum();
	/**
	* @ejb:interface-method view-type="local"
	**/
	public abstract void setBisdatum(java.util.Date bisdatum);

	/**
		* @ejb:interface-method view-type="local"
		* @ejb:relation
		*    name="mannschaft1_spiel"
		*    role-name="many-spiel-has-one-mannschaft1"
	    *    cascade-delete="yes"
		* @jboss:relation
		*    related-pk-field="id"
		*    fk-column="MANNSCHAFT1_ID"
		*/
	public abstract com.sport.server.ejb.interfaces.MannschaftLocal getMannschaft1();
	/**
	* @ejb:interface-method view-type="local"
	**/
	public abstract void setMannschaft1(com.sport.server.ejb.interfaces.MannschaftLocal mannschaft1);

	/**
		* @ejb:interface-method view-type="local"
		* @ejb:relation
		*    name="mannschaft2_spiel"
		*    role-name="many-spiel-has-one-mannschaft2"
        *    cascade-delete="yes"
		* @jboss:relation
		*    related-pk-field="id"
		*    fk-column="MANNSCHAFT2_ID"
		*/
	public abstract com.sport.server.ejb.interfaces.MannschaftLocal getMannschaft2();
	/**
	* @ejb:interface-method view-type="local"
	**/
	public abstract void setMannschaft2(com.sport.server.ejb.interfaces.MannschaftLocal mannschaft2);

	/**
		* @ejb:interface-method view-type="local"
		* @ejb:relation
		*    name="schiedsrichter_spiel"
		*    role-name="many-spiel-has-one-schiedrichter"
		* @jboss:relation
		*    related-pk-field="id"
		*    fk-column="SCHIEDSRICHTER_ID"
		*/
	public abstract com.sport.server.ejb.interfaces.MannschaftLocal getSchiedsrichter();
	/**
	* @ejb:interface-method view-type="local"
	**/
	public abstract void setSchiedsrichter(com.sport.server.ejb.interfaces.MannschaftLocal schiedsrichter);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:relation
	 *    name="spielplatz_spiel"
	 *    role-name="many-spiele-has-one-spielplatz"
     *    cascade-delete="yes"
	 * @jboss:relation
	 *    related-pk-field="id"
	 *    fk-column="SPIELPLATZ_ID"
	 */
	public abstract com.sport.server.ejb.interfaces.SpielplatzLocal getSpielplatz();
	/**
	* @ejb:interface-method view-type="local"
	**/
	public abstract void setSpielplatz(com.sport.server.ejb.interfaces.SpielplatzLocal spielplatz);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:relation
	 *    name="spiel_satz"
	 *    role-name="one-spiel-has-many-saetze"
	 */
	public abstract java.util.Set getSaetze();
	/**
	* @ejb:interface-method view-type="local"
	**/
	public abstract void setSaetze(java.util.Set saetze);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:relation
	 *    name="gruppe_spiel"
	 *    role-name="many-spiele-has-one-gruppe"
	 * @jboss:relation
	 *    related-pk-field="id"
	 *    fk-column="GRUPPE_ID"
	 * 
	 *kein cascadierendes l�schen n�tig, da �ber mannschaften abgedeckt!
	 *
	 *	 */
	public abstract com.sport.server.ejb.interfaces.GruppeLocal getGruppe();
	/**
	* @ejb:interface-method view-type="local"
	**/
	public abstract void setGruppe(com.sport.server.ejb.interfaces.GruppeLocal gruppe);

	/**
	 * @ejb:interface-method view-type="local"
	 * @ejb:relation
	 *    name="spiel_logmannschaft"
	 *    role-name="one-spiel-has-many-logmannschaften"
	 */
	public abstract Set getLogmannschaften();
	/**
	* @ejb:interface-method view-type="local"
	**/
	public abstract void setLogmannschaften(Set logmannschaften);   

	public SpielBean() {
		
	}

	/**
	* @ejb:create-method view-type="local"
	*/
	public com.sport.server.ejb.interfaces.SpielPK ejbCreate() throws CreateException {
		setId(new Long(Math.round(Math.random() * 2100000000)));
		return new com.sport.server.ejb.interfaces.SpielPK(getId());
	}

	public void ejbPostCreate() throws CreateException {
	}

	/** A container invokes this method when the instance is taken out of the pool
	  * of available instances to become associated with a specific EJB object.
	  * 
	  * @exception javax.ejb.EJBException */
	public void ejbActivate() throws javax.ejb.EJBException {
		
	}

	/** A container invokes this method to instruct the instance to synchronize its state
	  * by loading it state from the underlying database.
	  * 
	  * @exception javax.ejb.EJBException */
	public void ejbLoad() throws javax.ejb.EJBException {
		
	}

	/** A container invokes this method on an instance before the instance becomes
	  * disassociated with a specific EJB object.
	  * 
	  * @exception javax.ejb.EJBException */
	public void ejbPassivate() throws javax.ejb.EJBException {
		
	}

	/** A container invokes this method before it removes the EJB object that is currently
	  * associated with the instance.
	  * 
	  * @exception javax.ejb.RemoveException
	  * @exception javax.ejb.EJBException */
	public void ejbRemove()
		throws javax.ejb.RemoveException, javax.ejb.EJBException {
		
	}

	/** A container invokes this method to instruct the instance to synchronize its state
	  * by storing it to the underlying database.
	  * 
	  * @exception javax.ejb.EJBException */
	public void ejbStore() throws javax.ejb.EJBException {
		
	}

	/** Set the associated entity context.
	  * 
	  * @param ctx
	  * @exception javax.ejb.EJBException */
	public void setEntityContext(EntityContext ctx)
		throws javax.ejb.EJBException {
		this.ejbContext = ctx;
	}

	/** Unset the associated entity context.
	  * 
	  * @exception javax.ejb.EJBException */
	public void unsetEntityContext() throws javax.ejb.EJBException {
		this.ejbContext = null;
	}

}