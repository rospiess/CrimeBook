package ch.ethz.inf.dbproject.model;

/**
 * Object that represents a registered in user.
 */
public final class User {

	private final int userid;
	private final String username;
	
	public User(final int userid, final String username) {
		this.userid = userid;
		this.username = username;
	}

	public int getUserid() {
		return userid;
	}
	
	public String getUsername() {
		return username;
	}

}
