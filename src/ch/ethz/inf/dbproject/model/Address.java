package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class represents an Address 
 */
public final class Address {

	private final String city;
	private final String country;
	private final String street;
	private final int streetNo;
	private final int zipCode;


	public Address(final String country, final String city, final String street, final int zipCode, final int streetNo) {
		super();
		this.city = city;
		this.country = country;
		this.street = street;
		this.streetNo = streetNo;
		this.zipCode = zipCode;
	}
	
	public Address(final ResultSet rs) throws SQLException {
		this.city = rs.getString("city");
		this.country = rs.getString("country");
		this.street = rs.getString("street");
		this.streetNo= rs.getInt("streetNo");
		this.zipCode = rs.getInt("zipCode");
	}

	public final String getName() {
		return city;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getStreet() {
		return street;
	}

	public int getStreetNo() {
		return streetNo;
	}

	public int getZipCode() {
		return zipCode;
	}
}