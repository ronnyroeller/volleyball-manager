/*
 * Created on 28.01.2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package com.sport.web.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;

import com.sport.core.bo.LicenceBO;
import com.sport.core.bo.Tournament;
import com.sport.server.ejb.HomeGetter;
import com.sport.server.ejb.interfaces.TurnierSession;

/**
 * Class to give an abstract basis level of functionality to all Action-Classes
 * 
 * @author ronny
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class VolleyAction extends Action {

	/**
	 * Checks for a turnierBO. If not loaded until now or another linkid ->
	 * load turnierBO
	 * 
	 * @param request
	 * @param turnierSession
	 * @throws Exception
	 */
	protected Tournament getTurnierBO(
		HttpServletRequest request,
		TurnierSession turnierSession)
		throws Exception {
		HttpSession session = request.getSession();
		String linkid = request.getParameter("linkid");
		Tournament turnierBO = (Tournament) session.getAttribute("tournament");
		if (turnierBO == null
			|| (linkid != null && !turnierBO.getLinkid().equals(linkid))) {
			turnierBO = turnierSession.getTurnierByLinkid(linkid);
			session.setAttribute("tournament", turnierBO);

			LicenceBO licenceBO =
				HomeGetter.getTurnierSessionHome().create().getLicenceBO();
			session.setAttribute("licenceBO", licenceBO);
		}

		return turnierBO;
	}

	/**
	 * Loads the Licence-Information if not there
	 *  
	 */
	protected void checkLicenceBO(HttpServletRequest request)
		throws Exception {
		HttpSession session = request.getSession();
		LicenceBO licenceBO = (LicenceBO) session.getAttribute("licenceBO");
		if (licenceBO == null) {
			licenceBO =
				HomeGetter.getTurnierSessionHome().create().getLicenceBO();
			session.setAttribute("licenceBO", licenceBO);
		}
	}
}
