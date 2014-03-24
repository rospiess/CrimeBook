package ch.ethz.inf.dbproject.model;

import java.sql.Connection;
import java.util.Date;
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

	private Connection sqlConnection;

	public DatastoreInterface() {
		this.sqlConnection = MySQLConnection.getInstance().getConnection();
	}

	public final Case getCaseById(final int id) {

		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select * from Cases c, Address a where "
							+ " c.idAddress = a.idAddress and idcase = " + id);
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
			if (type.equals("case"))
				rs = stmt.executeQuery("Select * from notecase where idcase = "
						+ id);
			else
				rs = stmt
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

	public void insertComment(final int id, final String text,
			final String username, String type) {

		try {

			final Statement stmt = this.sqlConnection.createStatement();

			if (type.equals("case"))
				stmt.execute("Insert into notecase(idCase, text, username) values ('"
						+ id + "', '" + text + "', '" + username + "')");

			else
				stmt.execute("Insert into noteperson(idpersonofinterest, text, username) values ('"
						+ id + "', '" + text + "', '" + username + "')");
			stmt.close();

		} catch (final SQLException ex) {
			ex.printStackTrace();
		}

	}

	public final List<Conviction> getConvictionsById(final int id, String type) {

		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs;
			if(type.equals("case"))
			rs = stmt
					.executeQuery("Select * from conviction co, personofinterest p, cases ca where co.idcase = "
							+ id
							+ " and p.idpersonofinterest = co.idpersonofinterest"
							+ " and ca.idcase = co.idcase");
			else
				rs = stmt
				.executeQuery("Select * from conviction co, personofinterest p, cases ca where co.idpersonofinterest = "
						+ id + " and co.idpersonofinterest = p.idpersonofinterest "
						+ " and co.idcase = ca.idcase");
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

	
	public final List<Involved> getInvolvedByPersonId(final int pid) {		
		// Returns list of involvement of a person by their id. 
		// The Involved class contains information about the case and the person.
		
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select * from involved, cases, personofinterest"
							+ " where involved.idperson = "
							+ pid
							+ " and personofinterest.idpersonofinterest = "
							+ pid
							+ " and cases.idCase = involved.idcase");
			final List<Involved> invlist = new ArrayList<Involved>();
			while (rs.next()) {
				invlist.add(new Involved(rs));
			}

			rs.close();
			stmt.close();

			return invlist;

		} catch (final SQLException ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public final List<Case> getAllCases() {

		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select * from Cases c, Address a where c.idAddress = a.idAddress");

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
						.executeQuery("Select * from Cases c, Category, Address a where "
								+ "c.idAddress = a.idAddress and c.catname"
								+ " = category.catname and supercat = 'personal crime'");
			else if (category.equals("property"))
				rs = stmt
						.executeQuery("Select * from Cases c, Category , Address a where "
								+ " c.idAddress = a.idAddress and c.catname"
								+ " = category.catname and  supercat = 'property crime'");
			else if (category.equals("otherper"))
				rs = stmt
						.executeQuery("Select * from Cases c, Address a, category ca where "
								+ "c.idAddress = a.idAddress and ca.supercat = 'personal crime' "
								+ "and c.catname <> 'Assault' and c.catname <> 'Murder' and c.catname = ca.catname");
			else if (category.equals("otherpro"))
				rs = stmt
						.executeQuery("Select * from Cases c, Address a, category ca where "
								+ "c.idAddress = a.idAddress and ca.supercat = 'property crime' "
								+ "and c.catname <> 'Theft' and c.catname <> 'Fraud' and c.catname = ca.catname");
			else
				rs = stmt
						.executeQuery("Select * from Cases c, Address a where "
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
					.executeQuery("Select * from Cases cas, Conviction c,personofinterest where cas.catname "
							+ "like '%"
							+ category
							+ "%' and personofinterest.idpersonofinterest = c.idpersonofinterest"
							+ " and cas.idcase = c.idcase");

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

	public final List<Conviction> searchByDate(String date) {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select * from Cases cas, Conviction c,personofinterest p where (beginDate = '"
							+ date
							+ "' or endDate = '"
							+ date
							+ "') and p.idpersonofinterest = c.idpersonofinterest"
							+ " and cas.idcase = c.idcase");

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

	public final List<Person> getSuspectsById(int id) {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select idpersonofinterest,firstname,lastname, dateofbirth from personofinterest p, involved i "
							+ "where i.role = 'Suspect' and i.idCase = '"
							+ id
							+ "' and i.idperson = p.idpersonofinterest");

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
	
	public final List<Person> getWitnessesById(int id) {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select idpersonofinterest,firstname,lastname, dateofbirth from personofinterest p, involved i "
							+ "where i.role = 'Witness' and i.idCase = '"
							+ id
							+ "' and i.idperson = p.idpersonofinterest");

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

	public final boolean identify(String username, String password) {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select password from user u where u.username = '"
							+ username + "'");
			final String correctpassword;

			if (!rs.next()) { // user not found
				rs.close();
				stmt.close();
				return false;
			} else
				correctpassword = rs.getString("password");

			rs.close();
			stmt.close();
			return password.equals(correctpassword);
		} catch (final SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	
}
