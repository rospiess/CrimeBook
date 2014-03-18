package ch.ethz.inf.dbproject.model;

/**
 * This class represents an Address 
 */
public final class Address {

	private final String city;
	private final String country;
	private final String street;
	private final int streetNo;
	private final int zipCode;


	public Address(final String city, final String country, final String street, final int streetNo, final int zipCode) {
		super();
		this.city = city;
		this.country = country;
		this.street = street;
		this.streetNo = streetNo;
		this.zipCode = zipCode;
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