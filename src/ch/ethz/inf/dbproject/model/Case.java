package ch.ethz.inf.dbproject.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public final class Case {
	
	/**
	 * The properties of the case are added here
	 */
	private final int idcase;
	private final String title;
	private final String descr;
	private final Date date;
	private final Time time;
	private final Address loc;
	private final Category cat;
	private final boolean open;
	
	/**
	 * Construct a new case.
	 * 
	 * @param descr		The name of the case
	 */
	public Case( final int idcase, final String title, final String descr, final Date date,
			final Time time, final Address loc, final Category cat, final boolean open) {

		this.idcase = idcase;
		this.title = title;
		this.descr = descr;
		this.date = date;
		this.time = time;
		this.loc = loc;
		this.cat = cat;
		this.open = open;
		
	}
	
	public Case(final ResultSet rs) throws SQLException {
		
		this.idcase = rs.getInt("idcase");
		this.descr = rs.getString("description");
		this.title = rs.getString("title");
		this.date = rs.getDate("date");
		this.time = rs.getTime("time");
		
		//Address information might not always be provided
		Address addr;
		try{
			addr = new Address(rs);
		}
		catch(SQLException e){
			addr = new Address("Unbekannt","???","???",0,0);
		}
		this.loc = addr;
		
		this.cat = new Category(rs.getString("catname"),null);
		this.open = rs.getBoolean("open");
	}

	/**
	 * HINT: In eclipse, use Alt + Shift + S menu and choose:
	 * "Generate Getters and Setters to auto-magically generate
	 * the getters. 
	 */	

	public int getIdcase() {
		return idcase;
	}

	public String getTitle() {
		return title;
	}

	public String getDescr() {
		return descr;
	}

	public Date getDate() {
		return date;
	}

	public Time getTime() {
		return time;
	}

	public String getLoc() {
		return loc.getStreet() +
				" " + loc.getStreetNoString()+
				", " +	loc.getZipCodeString() + " " + loc.getCity()+ ", " + loc.getCountry();
	}

	public String getCat() {
		return cat.getName();
	}

	public boolean getOpen() {
		return open;
	}

}