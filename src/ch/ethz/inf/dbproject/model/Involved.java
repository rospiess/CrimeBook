package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Object that represents a conviction.
 */
public class Involved {
	private final int idcase;
	private final String role;
	private final Person person;
	private final Case caze;

	
	public int getIdcase() {
		return idcase;
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

	public Involved(final String role, final int idcase, final Person person, final Case caze) {
		this.role = role;
		this.idcase = idcase;
		this.person = person;
		this.caze = caze;
	}
	
	public Involved(final ResultSet rs) throws SQLException {
		this.role = rs.getString("role");
		this.idcase = rs.getInt("idcase");
		this.person = new Person(0,rs.getString("firstname"),rs.getString("lastname"),null);
		this.caze = new Case(rs);
	}



		
}
