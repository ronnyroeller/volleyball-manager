/***********************************************************************
 * Module:  UserBean.java
 * Author:  ronny
 * Purpose: Defines the Class UserBean
 ***********************************************************************/
package com.sport.server.ejb.entity;

import javax.ejb.CreateException;
import javax.ejb.EntityContext;

/** 
  * @ejb:bean
  *   name="User"
  *   type="CMP"
  *   local-jndi-name="UserLocal"
  *   view-type="local"
  * 
  * @ejb:finder signature="UserLocal findByUsername(java.lang.String username)" unchecked="true"
  *  query="SELECT OBJECT(n) FROM User AS n WHERE n.username = ?1"
  * 
  * @ejb:finder signature="UserLocal findById(java.lang.Long id)" unchecked="true"
  *  query="SELECT OBJECT(n) FROM User AS n WHERE n.id = ?1"
  * 
  * @ejb:transaction type="Required"
  * @jboss:table-name TUSER
  */
public abstract class UserBean implements javax.ejb.EntityBean {
	
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
	* @jboss:column-name name="USERNAME"
  	 * @jboss:persistence dbindex="true"
	**/
	public abstract java.lang.String getUsername();
	/**
	* @ejb:interface-method view-type="local"
	**/
	public abstract void setUsername(java.lang.String username);

	/**
	* @ejb:interface-method view-type="local"
	* @ejb:persistent-field
	* @jboss:column-name name="PASSWORT"
	**/
	public abstract java.lang.String getPasswort();
	/**
	* @ejb:interface-method view-type="local"
	**/
	public abstract void setPasswort(java.lang.String passwort);

	/**
	* @ejb:interface-method view-type="local"
	* @ejb:persistent-field
	* @jboss:column-name name="NAME"
	**/
	public abstract java.lang.String getName();
	/**
	* @ejb:interface-method view-type="local"
	**/
	public abstract void setName(java.lang.String name);

	/**
	* @ejb:interface-method view-type="local"
	* @ejb:persistent-field
	* @jboss:column-name name="VORNAME"
  	 * @jboss:persistence dbindex="true"
	**/
	public abstract java.lang.String getVorname();
	/**
	* @ejb:interface-method view-type="local"
	**/
	public abstract void setVorname(java.lang.String vorname);

	/**
	* @ejb:interface-method view-type="local"
	* @ejb:persistent-field
	* @jboss:column-name name="EMAIL"
  	 * @jboss:persistence dbindex="true"
	**/
	public abstract java.lang.String getEmail();
	/**
	* @ejb:interface-method view-type="local"
	**/
	public abstract void setEmail(java.lang.String email);

	/**
	* @ejb:interface-method view-type="local"
	* @ejb:persistent-field
	* @jboss:column-name name="LEVEL"
	**/
	public abstract int getLevel();
	/**
	* @ejb:interface-method view-type="local"
	**/
	public abstract void setLevel(int level);

	/**
	  * @ejb:interface-method view-type="local"
	  * @ejb:relation
	  *    name="turnier_mitbenutzeruser"
	  *    role-name="many-mitbenutzer-has-many-turniere"
	  * @jboss:relation
	  *    related-pk-field="id"
	  *    fk-column="TURNIER_ID"
	  * @jboss:relation-table table-name="TURNIER_MITBENUTZER"
	  */
	public abstract java.util.Set getTurnieremitnutzer();
	/**
	* @ejb:interface-method view-type="local"
	**/
	public abstract void setTurnieremitnutzer(
		java.util.Set turnieremitnutzer);

	/**
	  * @ejb:interface-method view-type="local"
	  * @ejb:relation
	  *    name="user_turnier"
	  *    role-name="one-user-has-many-turniere"
	  */
	public abstract java.util.Set getTurniere();
	/**
	* @ejb:interface-method view-type="local"
	**/
	public abstract void setTurniere(java.util.Set turniere);

	public UserBean() {
		
	}

	/**
	* @ejb:create-method view-type="local"
	*/
	public com.sport.server.ejb.interfaces.UserPK ejbCreate(String name) throws CreateException {
		this.setId(new Long(Math.round(Math.random() * 2100000000)));
		this.setName(name);
		return new com.sport.server.ejb.interfaces.UserPK(getId());
	}
	/*
	public void ejbPostCreate(String name) throws CreateException {
	}
	*/

	/**
	* @ejb:create-method view-type="local"
	*/
	public com.sport.server.ejb.interfaces.UserPK ejbCreate(long id, String name) throws CreateException {
		this.setId(new Long(id));
		this.setName(name);
		return new com.sport.server.ejb.interfaces.UserPK(getId());
	}
	/*
	public void ejbPostCreate(long id, String name) throws CreateException {
	}
	*/

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