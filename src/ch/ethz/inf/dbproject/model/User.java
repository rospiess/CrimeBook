package ch.ethz.inf.dbproject.model;

/**
 * Object that represents a registered in user.
 */
public final class User {

	private final String username;
	
	public User(final String username) {
		this.username = username;
	}

	
	public String getUsername() {
		return username;
	}

}
