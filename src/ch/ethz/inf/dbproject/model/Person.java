package ch.ethz.inf.dbproject.model;


import java.sql.ResultSet;
import java.sql.SQLException;

public class Person {

	private final int idperson;
	private final String firstname;
	private final String lastname;
	private final String bdate;

	public Person( final int idperson, final String firstname, final String lastname, final String bdate
			) {

		this.idperson = idperson;
		this.firstname = firstname;
		this.lastname = lastname;
		this.bdate = bdate;

		
	}
	
	public Person(final ResultSet rs) throws SQLException {
		
		this.idperson = rs.getInt("idpersonofinterest");
		this.firstname = rs.getString("firstname");
		this.lastname = rs.getString("lastname");
		this.bdate = rs.getString("dateofbirth");
	}

	/**
	 * HINT: In eclipse, use Alt + Shirt + S menu and choose:
	 * "Generate Getters and Setters to auto-magically generate
	 * the getters. 
	 */	


	public int getIdperson() {
		return idperson;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getBdate() {
		return bdate;
	}
	
	public String getBdateString() {
		if (bdate == null || bdate.toString().equals("0001-01-01"))
			return "???";
		else
			return bdate.toString();
	}
	
	public String getFullname()
	{
		return getLastname() + ", " + getFirstname();
	}

}
