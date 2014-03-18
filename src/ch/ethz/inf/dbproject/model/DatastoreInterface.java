package ch.ethz.inf.dbproject.model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import ch.ethz.inf.dbproject.database.MySQLConnection;

/**
 * This class should be the interface between the web application
 * and the database. Keeping all the data-access methods here
 * will be very helpful for part 2 of the project.
 */
public final class DatastoreInterface {

	//FIXME This is a temporary list of cases that will be displayed until all the methods have been properly implemented
	private final static Case[] staticCases = new Case[] { 
			new Case(0, "Noise pollution..", "1287.9%", 10000), 
			new Case(1, "Highway overspeed...", "54.7%", 250000),
			new Case(2, "Money Laundring...", "1.2%", 1000000),
			new Case(3, "Corruption...", "0.0%", 1000000000),
		};
	private final static List<Case> staticCaseList = new ArrayList<Case>();
	static {
		for (int i = 0; i < staticCases.length; i++) {
			staticCaseList.add(staticCases[i]);
		}
	}
	
	@SuppressWarnings("unused")
	private Connection sqlConnection;

	public DatastoreInterface() {
		//TODO Uncomment the following line once MySQL is running
		//this.sqlConnection = MySQLConnection.getInstance().getConnection();
	}
	
	public final Case getCaseById(final int id) {
	
		/**
		 * TODO this method should return the case with the given id
		 */
		
		if (id < staticCases.length) {
			return staticCases[id];
		} else {
			return null;
		}
		
	}
	
	public final List<Case> getAllCases() {

		/**
		 * TODO this method should return all the cases in the database
		 */
			
		/*
		//Code example for DB access
		try {
			
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("Select ...");
		
			final List<Case> cases = new ArrayList<Case>(); 
			while (rs.next()) {
				cases.add(new Case(rs));
			}
			
			rs.close();
			stmt.close();

			return cases;
			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		
		*/
		
		// If you chose to use PreparedStatements instead of statements, you should prepare them in the constructor of DatastoreInterface.
		
		// For the time being, we return some bogus projects
		return staticCaseList;
	}
	
	//TODO Implement all missing data access methods


}
