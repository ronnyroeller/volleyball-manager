/***********************************************************************
 * Module:  GruppeBean.java
 * Author:  ronny
 * Purpose: Defines the Class GruppeBean
 ***********************************************************************/
package com.sport.server.ejb.entity;

import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EntityContext;

/** @ejb:bean
  *   name="Gruppe"
  *   type="CMP"
  *   local-jndi-name="GruppeLocal"
  *   view-type="local"
  * 
  * @ejb:finder signature="GruppeLocal findById(java.lang.Long id)" unchecked="true"
  *  query="SELECT OBJECT(n) FROM Gruppe AS n WHERE n.id = ?1"
  * 
  * @ejb:finder signature="Collection findByTurnierid(java.lang.Long turnierid)" unchecked="true"
  *  query="SELECT OBJECT(n) FROM Gruppe AS n WHERE n.turnier.id = ?1"
  * 
  * @ejb:transaction type="Required"
  * @jboss:table-name GRUPPE */
public abstract class GruppeBean implements javax.ejb.EntityBean
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
   * @jboss:column-name name="SORT"
   * @jboss:persistence dbindex="true"
   **/
   public abstract java.lang.Integer getSort();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setSort(java.lang.Integer sort);

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
   * @jboss:column-name name="FARBE"
   **/
   public abstract java.lang.String getFarbe();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setFarbe(java.lang.String farbe);
   
   /**
	* @ejb:interface-method view-type="local"
	* @ejb:relation
	*    name="gruppe_logmannschaft"
	*    role-name="one-gruppe-has-many-logmannschaften"
	*/
   public abstract Set getLogmannschaften();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setLogmannschaften(Set logmannschaften);
   
   /**
	* @ejb:interface-method view-type="local"
	* @ejb:relation
	*    name="gruppe_mannschaft"
	*    role-name="one-gruppe-has-many-mannschaften"
	*/
   public abstract Set getMannschaften();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setMannschaften(Set mannschaften);
   
   /**
    * @ejb:interface-method view-type="local"
    * @ejb:relation
    *    name="gruppe_spiel"
    *    role-name="one-gruppe-has-many-spiele"
    */
   public abstract Set getSpiele();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setSpiele(Set spiele);
   
   /**
    * @ejb:interface-method view-type="local"
    * @ejb:relation
    *    name="modus_gruppe"
    *    role-name="many-gruppe-has-one-modus"
    * @jboss:relation
    *    related-pk-field="id"
    *    fk-column="MODUS_ID"
    */
   public abstract com.sport.server.ejb.interfaces.ModusLocal getModus();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setModus(com.sport.server.ejb.interfaces.ModusLocal modus);
   
   /**
    * @ejb:interface-method view-type="local"
    * @ejb:relation
    *    name="turnier_gruppe"
    *    role-name="many-gruppen-has-one-turnier"
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

   public GruppeBean()
   {
      
   }
   
    /**
	* @ejb:create-method view-type="local"
    */
   public com.sport.server.ejb.interfaces.GruppePK ejbCreate() throws CreateException{
	  this.setId(new Long (Math.round(Math.random()*2100000000)));
	  return new com.sport.server.ejb.interfaces.GruppePK(getId());
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