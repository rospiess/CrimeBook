package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class Case {
	
	/**
	 * TODO The properties of the case should be added here
	 */
	private final int id;
	private final String description;
	private final String field2;
	private final int field3;
	
	/**
	 * Construct a new case.
	 * 
	 * @param description		The name of the case
	 */
	public Case(	final int id, final String description, final String field2, final int field3) {
		this.id = id;
		this.description = description;
		this.field2 = field2;
		this.field3 = field3;
	}
	
	public Case(	final ResultSet rs) throws SQLException {
		// TODO These need to be adapted to your schema
		// TODO Extra properties need to be added
		this.id = rs.getInt("id");
		this.description = rs.getString("name");
		this.field2 = rs.getString("field2");
		this.field3  = rs.getInt("field3");
	}

	/**
	 * HINT: In eclipse, use Alt + Shirt + S menu and choose:
	 * "Generate Getters and Setters to auto-magically generate
	 * the getters. 
	 */
	public String getDescription() {
		return description;
	}

	public int getId() {
		return id;
	}

	public String getField2() {
		return field2;
	}

	public int getField3() {
		return field3;
	}	
}