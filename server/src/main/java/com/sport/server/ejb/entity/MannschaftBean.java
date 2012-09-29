/***********************************************************************
 * Module:  MannschaftBean.java
 * Author:  ronny
 * Purpose: Defines the Class MannschaftBean
 ***********************************************************************/

package com.sport.server.ejb.entity;

import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EntityContext;

/** @ejb:bean
  *   name="Mannschaft"
  *   type="CMP"
  *   local-jndi-name="MannschaftLocal"
  *   view-type="local"
  * 
  * @ejb:finder signature="MannschaftLocal findById(java.lang.Long id)" unchecked="true"
  *  query="SELECT OBJECT(n) FROM Mannschaft AS n WHERE n.id = ?1"
  * 
  * @ejb:transaction type="Required"
  * @jboss:table-name MANNSCHAFT */
public abstract class MannschaftBean implements javax.ejb.EntityBean
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
   * @jboss:column-name name="LOGSORT"
   **/
   public abstract java.lang.Integer getLogsort();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setLogsort(java.lang.Integer logsort);
   
   /**
	* @ejb:interface-method view-type="local"
	* @ejb:relation
	*    name="gruppe_logmannschaft"
	*    role-name="many-logmannschaften-has-one-gruppe"
    *    cascade-delete="yes"
	* @jboss:relation
	*    related-pk-field="id"
	*    fk-column="LOGGRUPPE_ID"
	*/
   public abstract com.sport.server.ejb.interfaces.GruppeLocal getLoggruppe();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setLoggruppe(com.sport.server.ejb.interfaces.GruppeLocal loggruppe);
   
   /**
	* @ejb:interface-method view-type="local"
	* @ejb:relation
	*    name="spiel_logmannschaft"
	*    role-name="many-logmannschaften-has-one-spiel"
    *    cascade-delete="yes"
	* @jboss:relation
	*    related-pk-field="id"
	*    fk-column="LOGSPIEL_ID"
	*/
   public abstract com.sport.server.ejb.interfaces.SpielLocal getLogspiel();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setLogspiel(com.sport.server.ejb.interfaces.SpielLocal logspiel);
   
   /**
    * @ejb:interface-method view-type="local"
    * @ejb:relation
    *    name="gruppe_mannschaft"
    *    role-name="many-mannschaften-has-one-gruppe"
    *    cascade-delete="yes"
    * @jboss:relation
    *    related-pk-field="id"
    *    fk-column="GRUPPE_ID"
    */
   public abstract com.sport.server.ejb.interfaces.GruppeLocal getGruppe();
   /**
    * @ejb:interface-method view-type="local"
    **/
   public abstract void setGruppe(com.sport.server.ejb.interfaces.GruppeLocal gruppe);
   
   /**
    * @ejb:interface-method view-type="local"
    * @ejb:relation
    *    name="turnier_mannschaft"
    *    role-name="many-mannschaften-has-one-turnier"
    *    cascade-delete="yes"
    * @jboss:relation
    *    related-pk-field="id"
    *    fk-column="TURNIER_ID"
    */
   public abstract com.sport.server.ejb.interfaces.GruppeLocal getTurnier();
   /**
    * @ejb:interface-method view-type="local"
    **/
   public abstract void setTurnier(com.sport.server.ejb.interfaces.TurnierLocal turnier);
   
   /**
	* @ejb:interface-method view-type="local"
	* @ejb:relation
	*    name="platzierung_mannschaft"
	*    role-name="one-mannschaft-has-one-platzierungen"
    *    cascade-delete="yes"
	*/
   public abstract com.sport.server.ejb.interfaces.PlatzierungLocal getPlatzierung();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setPlatzierung(com.sport.server.ejb.interfaces.PlatzierungLocal platzierungen);

   /**
	* @ejb:interface-method view-type="local"
	* @ejb:relation
	*    name="mannschaft1_spiel"
	*    role-name="one-mannschaft1-has-many-spiele"
	*/
   public abstract Set getSpiele1();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setSpiele1(Set spiele1);
   
   /**
	* @ejb:interface-method view-type="local"
	* @ejb:relation
	*    name="mannschaft2_spiel"
	*    role-name="one-mannschaft2-has-many-spiele"
	*/
   public abstract Set getSpiele2();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setSpiele2(Set spiele2);
   
   /**
	* @ejb:interface-method view-type="local"
	* @ejb:relation
	*    name="schiedsrichter_spiel"
	*    role-name="one-schiedsrichter-has-many-spiele"
	*/
   public abstract Set getSpieleSchiedsrichter();
   /**
   * @ejb:interface-method view-type="local"
   **/
   public abstract void setSpieleSchiedsrichter(Set spieleSchiedsrichter);
   
   public MannschaftBean getPhysicalmannschaft()
   {
      
      return null;
   }
   
   public MannschaftBean()
   {
      
   }
   
    /**
	* @ejb:create-method view-type="local"
    */
   public com.sport.server.ejb.interfaces.MannschaftPK ejbCreate() throws CreateException{
	  this.setId(new Long (Math.round(Math.random()*2100000000)));
	  return new com.sport.server.ejb.interfaces.MannschaftPK(getId());
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