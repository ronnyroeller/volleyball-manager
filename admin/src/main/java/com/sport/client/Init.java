package com.sport.client;

import com.sport.core.helper.Messages;
import com.sport.server.ejb.HomeGetter;
import com.sport.server.ejb.interfaces.UserSessionHome;

/**
 * Parameter: todo
 *  -> drop: l�scht alle Tabellen
 *  -> create: legt Standard-Daten an (Admin + Modi)
 * 
 * @author ronny
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Init {

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println(Messages.getString("init_wrong_parameter")); //$NON-NLS-1$
			System.exit(1);
		}

		try {
			UserSessionHome home = HomeGetter.getUserSessionHome();
			if ("drop".equals(args[0])) { //$NON-NLS-1$
				home.create().drop();
				System.out.println(Messages.getString("init_drop_db") + "\n"); //$NON-NLS-1$
			}
			if ("create".equals(args[0])) { //$NON-NLS-1$
				home.create().init();
				System.out.println(Messages.getString("init_db_is_inited") + "\n"); //$NON-NLS-1$
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
