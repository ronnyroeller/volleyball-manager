/***********************************************************************
 * Module:  KonstanteBean.java
 * Author:  ronny
 * Purpose: Defines the Class KonstanteBean
 ***********************************************************************/

package com.sport.server.ejb.entity;

import javax.ejb.CreateException;
import javax.ejb.EntityContext;

import com.sport.server.ejb.interfaces.KonstantePK;

/** @ejb:bean
  *   name="Konstante"
  *   type="CMP"
  *   local-jndi-name="KonstanteLocal"
  *   view-type="local"
  * 
  * @ejb:finder signature="KonstanteLocal findBySchluessel(java.lang.String name)" 
  *   unchecked="true"
  *   query="SELECT OBJECT(konstante) FROM Konstante AS konstante WHERE konstante.schluessel= ?1"
  * 
  * @ejb:transaction type="Required"
  * @jboss:table-name KONSTANTE */
public abstract class KonstanteBean implements javax.ejb.EntityBean
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
   * @jboss:column-name name="SCHLUESSEL"
   * @jboss:persistence dbindex="true"
   **/
   public abstract java.lang.String getSchluessel();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setSchluessel(java.lang.String schluessel);
   
   /**
   * @ejb:interface-method view-type="local"
   * @ejb:persistent-field
   * @jboss:column-name name="WERT"
   **/
   public abstract java.lang.String getWert();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setWert(java.lang.String wert);
   
   public KonstanteBean()
   {
      
   }
   
    /**
	* @ejb:create-method view-type="local"
    */
   public KonstantePK ejbCreate() throws CreateException{
	  this.setId(new Long (Math.round(Math.random()*2100000000)));
	  return new KonstantePK(getId());
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