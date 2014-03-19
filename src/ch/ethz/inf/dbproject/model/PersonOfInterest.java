package ch.ethz.inf.dbproject.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonOfInterest {

	private final int idpoi;
	private final String firstname;
	private final String lastname;
	private final Date bdate;

	public PersonOfInterest( final int idpoi, final String firstname, final String lastname, final Date bdate
			) {

		this.idpoi = idpoi;
		this.firstname = firstname;
		this.lastname = lastname;
		this.bdate = bdate;

		
	}
	
	public PersonOfInterest(final ResultSet rs) throws SQLException {
		
		this.idpoi = rs.getInt("idpersonofinterest");
		this.firstname = rs.getString("firstname");
		this.lastname = rs.getString("lastname");
		this.bdate = rs.getDate("dateofbirth");
	}

	/**
	 * HINT: In eclipse, use Alt + Shirt + S menu and choose:
	 * "Generate Getters and Setters to auto-magically generate
	 * the getters. 
	 */	

	public int getIdpoi() {
		return idpoi;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public Date getBdate() {
		return bdate;
	}


}
