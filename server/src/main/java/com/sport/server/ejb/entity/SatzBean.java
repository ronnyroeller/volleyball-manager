/***********************************************************************
 * Module:  SatzBean.java
 * Author:  ronny
 * Purpose: Defines the Class SatzBean
 ***********************************************************************/

package com.sport.server.ejb.entity;

import javax.ejb.CreateException;
import javax.ejb.EntityContext;

import com.sport.server.ejb.interfaces.SatzPK;

/** @ejb:bean
  *   name="Satz"
  *   type="CMP"
  *   local-jndi-name="SatzLocal"
  *   view-type="local"
  * 
  * @ejb:transaction type="Required"
  * @jboss:table-name SATZ */
public abstract class SatzBean implements javax.ejb.EntityBean
{
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
   * @jboss:column-name name="PUNKTE1"
   **/
   public abstract java.lang.Integer getPunkte1();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setPunkte1(java.lang.Integer punkte1);
   
   /**
   * @ejb:interface-method view-type="local"
   * @ejb:persistent-field
   * @jboss:column-name name="PUNKTE2"
   **/
   public abstract java.lang.Integer getPunkte2();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setPunkte2(java.lang.Integer punkte2);
   
   /**
   * @ejb:interface-method view-type="local"
   * @ejb:persistent-field
   * @jboss:column-name name="SATZNR"
	* @jboss:persistence dbindex="true"
   **/
   public abstract java.lang.Integer getSatznr();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setSatznr(java.lang.Integer satznr);
   
   /**
    * @ejb:interface-method view-type="local"
    * @ejb:relation
    *    name="spiel_satz"
    *    role-name="many-saetze-has-one-spiel"
    *    cascade-delete="yes"
    * @jboss:relation
    *    related-pk-field="id"
    *    fk-column="SPIEL_ID"
    */
   public abstract com.sport.server.ejb.interfaces.SpielLocal getSpiel();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setSpiel(com.sport.server.ejb.interfaces.SpielLocal spiel);

   public SatzBean()
   {
      
   }
   
    /**
	* @ejb:create-method view-type="local"
    */
   public SatzPK ejbCreate() throws CreateException{
	  this.setId(new Long (Math.round(Math.random()*2100000000)));
	  return new SatzPK(getId());
   }
   public void ejbPostCreate() throws CreateException{
   }

   /** A container invokes this method when the instance is taken out of the pool
     * of available instances to become associated with a specific EJB object.
     * 
     * @exception javax.ejb.EJBException */
   public void ejbActivate() throws javax.ejb.EJBException
   {
      
   }
   
   /** A container invokes this method to instruct the instance to synchronize its state
     * by loading it state from the underlying database.
     * 
     * @exception javax.ejb.EJBException */
   public void ejbLoad() throws javax.ejb.EJBException
   {
      
   }
   
   /** A container invokes this method on an instance before the instance becomes
     * disassociated with a specific EJB object.
     * 
     * @exception javax.ejb.EJBException */
   public void ejbPassivate() throws javax.ejb.EJBException
   {
      
   }
   
   /** A container invokes this method before it removes the EJB object that is currently
     * associated with the instance.
     * 
     * @exception javax.ejb.RemoveException
     * @exception javax.ejb.EJBException */
   public void ejbRemove() throws javax.ejb.RemoveException, javax.ejb.EJBException
   {
      
   }
   
   /** A container invokes this method to instruct the instance to synchronize its state
     * by storing it to the underlying database.
     * 
     * @exception javax.ejb.EJBException */
   public void ejbStore() throws javax.ejb.EJBException
   {
      
   }
   
   /** Set the associated entity context.
     * 
     * @param ctx
     * @exception javax.ejb.EJBException */
   public void setEntityContext(EntityContext ctx) throws javax.ejb.EJBException
   {
      this.ejbContext = ctx;
   }
   
   /** Unset the associated entity context.
     * 
     * @exception javax.ejb.EJBException */
   public void unsetEntityContext() throws javax.ejb.EJBException
   {
      this.ejbContext = null;
   }

}