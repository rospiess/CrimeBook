package ch.ethz.inf.dbproject.util;

import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.User;

public class UserManagement {

	public final static String SESSION_USER = "LOGGED_IN_USER";
	
	public static final User getCurrentlyLoggedInUser(final HttpSession session) {
		final Object obj = session.getAttribute(SESSION_USER);
		if (obj == null) {
			return null;
		} else {
			return (User) obj; 
		}
	}	
}