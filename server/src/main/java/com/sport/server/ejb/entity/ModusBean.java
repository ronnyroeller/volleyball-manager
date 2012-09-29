/***********************************************************************
 * Module:  ModusBean.java
 * Author:  ronny
 * Purpose: Defines the Class ModusBean
 ***********************************************************************/

package com.sport.server.ejb.entity;

import javax.ejb.CreateException;
import javax.ejb.EntityContext;

/**
 * @ejb:bean name="Modus" type="CMP" local-jndi-name="ModusLocal"
 *           view-type="local"
 * 
 * @ejb.finder signature="java.util.Collection findAll()" unchecked="true"
 *             query="SELECT OBJECT(n) FROM Modus AS n"
 * 
 * @ejb:finder signature="ModusLocal findById(java.lang.Long id)"
 *             unchecked="true"
 *             query="SELECT OBJECT(n) FROM Modus AS n WHERE n.id = ?1"
 * 
 * @ejb:transaction type="Required"
 * @jboss:table-name MODUS
 */
public abstract class ModusBean implements javax.ejb.EntityBean {
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
	 * @ejb:relation name="modus_gruppe" role-name="one-modus-has-many-gruppen"
	 */
	public abstract java.util.Collection getGruppen();

	/**
	 * @ejb:interface-method view-type="local"
	 **/
	public abstract void setGruppen(java.util.Collection gruppen);

	public ModusBean() {

	}

	/**
	 * @ejb:create-method view-type="local"
	 */
	public com.sport.server.ejb.interfaces.ModusPK ejbCreate(long id, String name) throws CreateException {
		this.setId(new Long(id));
		this.setName(name);
		return new com.sport.server.ejb.interfaces.ModusPK(getId());
	}

	/*
	 * public void ejbPostCreate(long id, String name) throws CreateException {
	 * }
	 */

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