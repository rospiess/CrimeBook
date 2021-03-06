package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Object that represents a conviction.
 */
public class Involved {
	private final String role;
	private final Person person;
	private final Case caze;

	
	public int getIdcase() {
		return caze.getIdcase();
	}

	public String getCasetitle(){
		return caze.getTitle();
	}

	public String getRole() {
		return role;
	}


	public String getFirstname() {
		return person.getFirstname();
	}
	
	public String getLastname() {
		return person.getLastname();
	}

	public Involved(final String role, final Person person, final Case caze) {
		this.role = role;
		this.person = person;
		this.caze = caze;
	}
	
	public Involved(final ResultSet rs) throws SQLException {
		this.role = rs.getString("role");
		this.person = new Person(0,rs.getString("firstname"),rs.getString("lastname"),null);
		this.caze = new Case(rs);
	}



		
}
