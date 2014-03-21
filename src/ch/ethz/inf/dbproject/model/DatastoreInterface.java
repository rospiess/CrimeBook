package ch.ethz.inf.dbproject.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	// new Case(0, "Noise pollution", "blablabla", new Date(1324512000000l), new
	// Time( 12, 42, 35), new Address("Switzerland", "ZÃ¼rich","RÃ¤mistrasse",
	// 8000, 142), new Category("Murder", null),true)

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
			final ResultSet rs = stmt
					.executeQuery("Select * from Cases c, Address a where "
							+" c.idAddress = a.idAddress and idcase = " + id);
			rs.next();
			final Case caze = new Case(rs);

			rs.close();
			stmt.close();

			return caze;

		} catch (final SQLException ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public final Person getPersonById(final int id) {

		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select * from Personofinterest where idpersonofinterest = "
							+ id);
			rs.next();
			final Person person = new Person(rs);

			rs.close();
			stmt.close();

			return person;

		} catch (final SQLException ex) {
			ex.printStackTrace();
			return null;
		}

	}

	
	public final List<Comment> getCommentsById(final int id, String type) {

		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs;
			if(type.equals("case"))
				rs = stmt
					.executeQuery("Select * from notecase where idcase = "
							+ id);
			else rs = stmt
					.executeQuery("Select * from noteperson where idpersonofinterest = "
							+ id);
			final List<Comment> clist = new ArrayList<Comment>();
			while (rs.next()) {
				clist.add(new Comment(rs));
			}

			rs.close();
			stmt.close();

			return clist;

		} catch (final SQLException ex) {
			ex.printStackTrace();
			return null;
		}

	}
	
	public final List<Conviction> getConvictionsById(final int id) {

		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs= stmt
					.executeQuery("Select * from conviction, personofinterest where conviction.idpersonofinterest = "
							+ id + " and personofinterest.idpersonofinterest = " + id);
			final List<Conviction> clist = new ArrayList<Conviction>();
			while (rs.next()) {
				clist.add(new Conviction(rs));
			}

			rs.close();
			stmt.close();

			return clist;

		} catch (final SQLException ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public final List<Case> getAllCases() {

		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("Select * from Cases c, Address a where c.idAddress = a.idAddress");

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

	}

	public final List<Case> getOpenCases() {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select * from Cases c, Address a where c.idAddress = a.idAddress and open = 1");

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

	}

	public final List<Case> getClosedCases() {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select * from Cases c, Address a where c.idAddress = a.idAddress and open = 0");

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

	}

	public final List<Case> getMostRecentCases() {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select * from Cases c, Address a where c.idAddress = a.idAddress order by date desc");

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
	}

	public final List<Case> getOldestUnsolvedCases() {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select * from Cases c, Address a where c.idAddress = a.idAddress and "
							+ "open = 1 order by date asc");

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
	}

	public final List<Case> getProjectsByCategory(String category) {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs;
			if (category.equals("personal"))
				rs = stmt
						.executeQuery("Select * from Cases c,Category, Address a where "
								+ "c.idAddress = a.idAddress and c.catname"
								+ " = category.catname and supercat = 'personal crime'");
			else if (category.equals("property"))
				rs = stmt
						.executeQuery("Select * from Cases c,Category , Address a where "
								+ " c.idAddress = a.idAddress and c.catname"
								+ " = category.catname and  supercat = 'property crime'");
			else if (category.equals("other"))
				rs = stmt
						.executeQuery("Select * from Cases c, Address a where "
								+ "c.idAddress = a.idAddress and catname <> 'Assault' "
								+ "and catname <> 'Theft' and catname <> 'Murder'");
			else
				rs = stmt.executeQuery("Select * from Cases c, Address a where "
						+ "c.idAddress = a.idAddress and catname = '"
						+ category + "'");

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
	}

	public final List<Conviction> searchByCategory(String category) {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select * from Conviction,personofinterest where type "
							+ "like '%"
							+ category
							+ "%' and personofinterest.idpersonofinterest = conviction.idpersonofinterest");

			final List<Conviction> conv = new ArrayList<Conviction>();
			while (rs.next()) {
				conv.add(new Conviction(rs));
			}

			rs.close();
			stmt.close();

			return conv;

		} catch (final SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public final List<Person> searchByName(String name) {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select * from personofinterest where firstname like '%"
							+ name + "%'" + "or lastname like '%" + name + "%'");

			final List<Person> persons = new ArrayList<Person>();
			while (rs.next()) {
				persons.add(new Person(rs));
			}

			rs.close();
			stmt.close();

			return persons;

		} catch (final SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public final List<Person> getAllPersons() {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select * from personofinterest");

			final List<Person> persons = new ArrayList<Person>();
			while (rs.next()) {
				persons.add(new Person(rs));
			}

			rs.close();
			stmt.close();

			return persons;

		} catch (final SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
