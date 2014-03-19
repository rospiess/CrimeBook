package ch.ethz.inf.dbproject.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import ch.ethz.inf.dbproject.database.MySQLConnection;

/**
 * This class should be the interface between the web application and the
 * database. Keeping all the data-access methods here will be very helpful for
 * part 2 of the project.
 */
public final class DatastoreInterface {

	private final static Case[] staticCases = new Case[] { 
//		new Case(0, "Noise pollution", "blablabla", new Date(1324512000000l), new Time(	12, 42, 35), new Address("Switzerland", "Zürich","Rämistrasse", 8000, 142), new Category("Murder", null),true)

	};
	private final static List<Case> staticCaseList = new ArrayList<Case>();
	static {
		for (int i = 0; i < staticCases.length; i++) {
			staticCaseList.add(staticCases[i]);
		}
	}
	
	private Connection sqlConnection;

	public DatastoreInterface() {
		 this.sqlConnection = MySQLConnection.getInstance().getConnection();
	}

	public final Case getCaseById(final int id) {

		try {
			  
			  final Statement stmt = this.sqlConnection.createStatement(); 
			  final ResultSet rs = stmt.executeQuery("Select * from Cases where idcase = " + id);
			  rs.next();
			  final Case caze = new Case(rs); 
			  
			  rs.close(); stmt.close();
			  
			  return caze;
			  
			  } catch (final SQLException ex) { ex.printStackTrace(); return null;
			  }


	}

	public final List<Case> getAllCases() {
		
		   try {
		  
		  final Statement stmt = this.sqlConnection.createStatement(); final
		  ResultSet rs = stmt.executeQuery("Select * from Cases");
		  
		  final List<Case> cases = new ArrayList<Case>(); while (rs.next()) {
		  cases.add(new Case(rs)); }
		  
		  rs.close(); stmt.close();
		  
		  return cases;
		  
		  } catch (final SQLException ex) { ex.printStackTrace(); return null;
		  }

	}


	public final List<Case> getOpenCases() {
		try {
			  
			  final Statement stmt = this.sqlConnection.createStatement(); final
			  ResultSet rs = stmt.executeQuery("Select * from Cases where open = 1");
			  
			  final List<Case> cases = new ArrayList<Case>(); while (rs.next()) {
			  cases.add(new Case(rs)); }
			  
			  rs.close(); stmt.close();
			  
			  return cases;
			  
			  } catch (final SQLException ex) { ex.printStackTrace(); return null;
			  }

	}

	public final List<Case> getClosedCases() {
		try {
			  
			  final Statement stmt = this.sqlConnection.createStatement(); final
			  ResultSet rs = stmt.executeQuery("Select * from Cases where open = 0");
			  
			  final List<Case> cases = new ArrayList<Case>(); while (rs.next()) {
			  cases.add(new Case(rs)); }
			  
			  rs.close(); stmt.close();
			  
			  return cases;
			  
			  } catch (final SQLException ex) { ex.printStackTrace(); return null;
			  }

	}

	public final List<Case> getMostRecentCases() {
		try {
			  
			  final Statement stmt = this.sqlConnection.createStatement(); final
			  ResultSet rs = stmt.executeQuery("Select * from Cases order by date desc");
			  
			  final List<Case> cases = new ArrayList<Case>(); while (rs.next()) {
			  cases.add(new Case(rs)); }
			  
			  rs.close(); stmt.close();
			  
			  return cases;
			  
			  } catch (final SQLException ex) { ex.printStackTrace(); return null;
			  }
	}
	public final List<Case> getOldestUnsolvedCases() {
		try {
			  
			  final Statement stmt = this.sqlConnection.createStatement(); final
			  ResultSet rs = stmt.executeQuery("Select * from Cases where open = 1 order by date asc");
			  
			  final List<Case> cases = new ArrayList<Case>(); while (rs.next()) {
			  cases.add(new Case(rs)); }
			  
			  rs.close(); stmt.close();
			  
			  return cases;
			  
			  } catch (final SQLException ex) { ex.printStackTrace(); return null;
			  }
	}
	
	
	public final List<Case> getProjectsByCategory(String category) {
		try {
			  
			  final Statement stmt = this.sqlConnection.createStatement();
			  final ResultSet rs;
			  if(category.equals("other"))
					 rs  = stmt.executeQuery("Select * from Cases where catname <> 'Assault' "
					 		+ "and catname <> 'Theft' and catname <> 'Murder'");
			  else
					  rs = stmt.executeQuery("Select * from Cases where catname = '" + category+"'");
			  
			  final List<Case> cases = new ArrayList<Case>(); while (rs.next()) {
			  cases.add(new Case(rs)); }
			  
			  rs.close(); stmt.close();
			  
			  return cases;
			  
			  } catch (final SQLException ex) { ex.printStackTrace(); return null;
			  }
	}
	
	public final List<Case> searchByCategory(String category) {
		try {
			  
			  final Statement stmt = this.sqlConnection.createStatement();
			  final ResultSet  rs = stmt.executeQuery("Select * from Cases where catname like '%" + category+"%'");
			  
			  final List<Case> cases = new ArrayList<Case>(); while (rs.next()) {
			  cases.add(new Case(rs)); }
			  
			  rs.close(); stmt.close();
			  
			  return cases;
			  
			  } catch (final SQLException ex) { ex.printStackTrace(); return null;
			  }
	}
	
	public final List<Case> searchByName(String name) {
		try {
			  
			  final Statement stmt = this.sqlConnection.createStatement();
			  final ResultSet   rs = stmt.executeQuery("Select * from Cases where title like '%" + name+"%'");
			  
			  final List<Case> cases = new ArrayList<Case>(); while (rs.next()) {
			  cases.add(new Case(rs)); }
			  
			  rs.close(); stmt.close();
			  
			  return cases;
			  
			  } catch (final SQLException ex) { ex.printStackTrace(); return null;
			  }
	}

}
