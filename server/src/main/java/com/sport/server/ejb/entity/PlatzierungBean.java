/***********************************************************************
 * Module:  PlatzierungBean.java
 * Author:  ronny
 * Purpose: Defines the Class PlatzierungBean
 ***********************************************************************/

package com.sport.server.ejb.entity;

import javax.ejb.CreateException;
import javax.ejb.EntityContext;

/** @ejb:bean
  *   name="Platzierung"
  *   type="CMP"
  *   local-jndi-name="PlatzierungLocal"
  *   view-type="local"
  * 
  * @ejb:finder signature="PlatzierungLocal findById(java.lang.Long id)" unchecked="true"
  *  query="SELECT OBJECT(n) FROM Platzierung AS n WHERE n.id = ?1"
  * 
  * @ejb:transaction type="Required"
  * @jboss:table-name PLATZIERUNG */
public abstract class PlatzierungBean implements javax.ejb.EntityBean
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
   * @jboss:column-name name="PLATZNR"
   * @jboss:persistence dbindex="true"
   **/
   public abstract java.lang.Long getPlatznr();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setPlatznr(java.lang.Long platznr);
      
   /**
	* @ejb:interface-method view-type="local"
	* @ejb:relation
	*    name="platzierung_mannschaft"
	*    role-name="one-platzierungen-has-one-mannschaft"
	* @jboss:relation
	*    related-pk-field="id"
	*    fk-column="MANNSCHAFT_ID"
	*/
   public abstract com.sport.server.ejb.interfaces.MannschaftLocal getMannschaft();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setMannschaft(com.sport.server.ejb.interfaces.MannschaftLocal mannschaft);

   /**
    * @ejb:interface-method view-type="local"
    * @ejb:relation
    *    name="turnier_platzierung"
    *    role-name="many-platzierungen-has-one-turnier"
    *    cascade-delete="yes"
    * @jboss:relation
    *    related-pk-field="id"
    *    fk-column="TURNIER_ID"
    */
   public abstract com.sport.server.ejb.interfaces.TurnierLocal getTurnier();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setTurnier(com.sport.server.ejb.interfaces.TurnierLocal turnier);

   public PlatzierungBean()
   {
      
   }
   
    /**
	* @ejb:create-method view-type="local"
    */
   public com.sport.server.ejb.interfaces.PlatzierungPK ejbCreate() throws CreateException{
	  this.setId(new Long (Math.round(Math.random()*2100000000)));
	  return new com.sport.server.ejb.interfaces.PlatzierungPK(getId());
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