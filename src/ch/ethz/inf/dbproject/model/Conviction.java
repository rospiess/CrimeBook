package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Object that represents a conviction.
 */
public class Conviction {

	private final int idcon;
	private final String date;
	private final String enddate;
	private final Case caze;
	private final Person person;

	public String getDate() {
		return date;
	}

	public String getEnddate() {
		return enddate;
	}
	
	public String getDateString() {
		if (date == null)
			return "???";
		return date.toString();
	}

	public String getEnddateString() {
		if (enddate == null)
			return "???";
		return enddate.toString();
	}

	public String getType() {
		return caze.getCat();
	}
	
	public String getCasetitle() {
		return caze.getTitle();
	}
	
	public int getIdcase(){
		return caze.getIdcase();
	}

	
	public int getIdcon() {
		return idcon;
	}
	
	public int getIdperson() {
		return person.getIdperson();
	}


	public String getFirstname() {
		return person.getFirstname();
	}
	
	public String getLastname() {
		return person.getLastname();
	}

	public Conviction(final int idcon, final String date, final String enddate, final Case caze, final Person person) {
		this.idcon = idcon;
		this.date = date;
		this.enddate = enddate;
		this.caze = caze;
		this.person = person;
	}
	
	public Conviction(final ResultSet rs) throws SQLException {
		this.idcon = rs.getInt("idconviction");
		this.date = rs.getString("begindate");
		this.enddate = rs.getString("enddate");
		this.caze = new Case(rs);
		this.person = new Person(rs);
	}



		
}
