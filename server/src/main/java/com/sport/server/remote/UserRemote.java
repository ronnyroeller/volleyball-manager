/*
 * Created on 14.07.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.server.remote;

import com.sport.core.bo.UserBO;
import com.sport.server.ejb.HomeGetter;


/**
 * @author ronny
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class UserRemote {

	public static UserBO getUserById(long userid)
	throws Exception {
		return HomeGetter.getUserSessionHome().create().getUserById(userid);
	}

}
