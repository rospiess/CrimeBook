package ch.ethz.inf.dbproject.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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

	private Connection sqlConnection;
	
	// PreparedStatements declaration
	PreparedStatement createConviction;

	public DatastoreInterface() {
		this.sqlConnection = MySQLConnection.getInstance().getConnection();
		
		try {
			// Create prepared Statements
			
			createConviction = sqlConnection.prepareStatement("insert into conviction(idcase, idpersonofinterest, beginDate, endDate) " 
					+ " values(?,?,?,?)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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
			
			String query = "Insert into $tableName (idCase, text, username) values ('"+ id + "', ?, ?)";

			if (type.equals("case")){
				query = query.replace("$tableName", "notecase");
			}

			else{
				query = query.replace("$tableName","noteperson");
			}
			
			final PreparedStatement stmt = sqlConnection.prepareStatement(query);
			stmt.setString(1, text);
			stmt.setString(2, username);
			stmt.execute();
			stmt.close();

		} catch (final SQLException ex) {
			ex.printStackTrace();
		}

	}

	public final List<Conviction> getConvictionsById(final int id, String type) {
		// Get Conviction by ID
		// type determines whether the ID belongs to the case, the person or the conviction itself
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs;
			if (type.equals("case"))
				rs = stmt
						.executeQuery("Select * from conviction co, personofinterest p, cases ca where co.idcase = "
								+ id
								+ " and p.idpersonofinterest = co.idpersonofinterest"
								+ " and ca.idcase = co.idcase");
			else if (type.equals("person")){
				rs = stmt
						.executeQuery("Select * from conviction co, personofinterest p, cases ca where co.idpersonofinterest = "
								+ id
								+ " and co.idpersonofinterest = p.idpersonofinterest "
								+ " and co.idcase = ca.idcase");
			}
			else{
				rs = stmt.executeQuery("Select * from conviction co, cases ca, personofinterest p where "
						+ " co.idconviction = " + id
						+ " and co.idpersonofinterest = p.idpersonofinterest "
						+ " and co.idcase = ca.idcase");
			}
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
	
	public final void updateConvictionDates(int idcon, String begindate, String enddate){
		try{
			final PreparedStatement stmt = sqlConnection.prepareStatement("UPDATE conviction" +
					" SET begindate = ?, enddate = ? " +
					" WHERE idconviction = "+idcon);
			stmt.setString(1, begindate);
			stmt.setString(2, enddate);
			stmt.execute();
			stmt.close();
		} catch (final SQLException ex){
			ex.printStackTrace();
			return;
		}
	}

	public final List<Involved> getInvolvedByPersonId(final int pid) {
		// Returns list of involvement of a person by their id.
		// The Involved class contains information about the case and the
		// person.

		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select * from involved, cases, personofinterest"
							+ " where involved.idperson = "
							+ pid
							+ " and personofinterest.idpersonofinterest = "
							+ pid + " and cases.idCase = involved.idcase");
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

	public final void setCaseOpen(int id, boolean open) {

		try {
			PreparedStatement s = sqlConnection
					.prepareStatement("Update Cases set open = ? where idcase = "
							+ id);
			s.setString(1, open ? "1" : "0");
			s.execute();
			
			if (open){
				// delete convictions associated with this case
				s = sqlConnection.prepareStatement("delete from conviction where "
						+ " conviction.idcase = " + id);
				s.execute();
			}
			else{
				// Create convictions based on suspects
				
				// Get suspects with query
				final Statement stmt = this.sqlConnection.createStatement();
				final ResultSet rs = stmt
						.executeQuery("Select idpersonofinterest from personofinterest p, involved i "
								+ "where i.role = 'Suspect' and i.idCase = "
								+ id
								+ " and i.idperson = p.idpersonofinterest");

				while (rs.next()) {
					// Create conviction with the suspects in the resultset
					createConviction.setInt(1, id);
					createConviction.setInt(2, rs.getInt("idpersonofinterest"));
					createConviction.setDate(3, Date.valueOf("2000-01-01"));
					createConviction.setDate(4, Date.valueOf("2000-01-01"));
					
					createConviction.execute();
				}

				rs.close();

			}
			
			s.close();

		} catch (final SQLException ex) {
			ex.printStackTrace();
		}

	}
	
	public void deleteInvolved(int idcase, int idperson, String type) {
		// Deletes an involvement of case + person
		// Type distinguishes between suspect and witness
		try {
			if (type == "suspect"){
				PreparedStatement s = sqlConnection
						.prepareStatement("delete from involved where idCase = ?"
								+ " and idperson = ? "
								+ " and role = 'Suspect'");
				s.setInt(1, idcase);
				s.setInt(2, idperson);
				s.execute();
				s.close();
			}
			else
			{
				PreparedStatement s = sqlConnection
						.prepareStatement("delete from involved where idCase = ?"
								+ " and idperson = ? "
								+ " and role = 'Witness'");
				s.setInt(1, idcase);
				s.setInt(2, idperson);
				s.execute();
				s.close();
			}

		} catch (final SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public final void deleteNote(int Nr, String uname, final String type) {
		// Deletes a caseNote with a certain Nr as key
		// Type distinguishes between casenote and personnote
		try {
			if (type == "case"){
				PreparedStatement s = sqlConnection
						.prepareStatement("delete from notecase where Nr = ? and username = ?");
				s.setString(1, ""+Nr+"");
				s.setString(2, uname);
				s.execute();
				s.close();
			}
			else
			{
				PreparedStatement s = sqlConnection
						.prepareStatement("delete from noteperson where Nr = ? and username = ?");
				s.setString(1, ""+Nr+"");
				s.setString(2, uname);
				s.execute();
				s.close();
			}

		} catch (final SQLException ex) {
			ex.printStackTrace();
		}

	}

	public final void openNewCase(String title, String descr, String date,
			String time, Address address, String catname) {

		try {
			PreparedStatement s = sqlConnection
					.prepareStatement("Insert into Address(country,city,street, zipcode,streetno)"+
			" values ( ?, ?, ?, ?, ?)",PreparedStatement.RETURN_GENERATED_KEYS);
			s.setString(1, address.getCountry());
			s.setString(2, address.getCity());
			s.setString(3, address.getStreet());
			s.setInt(4, address.getZipCode());
			s.setInt(5, address.getStreetNo());
			s.execute();
			ResultSet rs = s.getGeneratedKeys();
			rs.next();
			s = sqlConnection
					.prepareStatement("Insert into Cases(title,description,open,date,time,idAddress,catname) values (?, ?, 1, ?, ?, ?, ?)");
			s.setString(1, title);
			s.setString(2, descr);
			s.setString(3, date);
			s.setString(4, time);
			s.setInt(5, rs.getInt(1));
			s.setString(6, catname);
			s.execute();
			s.close();

		} catch (final SQLException ex) {
			ex.printStackTrace();
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

	public final void addNewPerson(String firstname, String lastname, String date) {

		try {
			PreparedStatement stmt = sqlConnection
					.prepareStatement("Insert into personofinterest(firstname,lastname,dateofbirth)"+
			" values ( ?, ?, ?)");
			stmt.setString(1, firstname);
			stmt.setString(2, lastname);
			stmt.setString(3, date);
			
			stmt.execute();
			stmt.close();

		} catch (final SQLException ex) {
			ex.printStackTrace();
		}

	}
	
	public final String getPassword(String username) {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select password from user u where u.username = '"
							+ username + "'");
			final String correctpassword;

			if (!rs.next())
				correctpassword = null;
			else
				correctpassword = rs.getString("password");
			rs.close();
			stmt.close();
			return correctpassword;
		} catch (final SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void insertUser(String username, String password) {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			stmt.execute("Insert into user (username, password) values ('"
					+ username + "', '" + password + "')");
			stmt.close();
		} catch (final SQLException ex) {
			ex.printStackTrace();

		}
	}

	public final boolean getNameIsTaken(String username) {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select * from user u where u.username = '"
							+ username + "'");
			boolean result = rs.next();
			stmt.close();
			rs.close();
			return result;
		} catch (final SQLException ex) {
			ex.printStackTrace();
			return true;

		}
	}

	
	public void changePassword(String username, String newpassword) {
		try {

			final PreparedStatement stmt = sqlConnection
					.prepareStatement("Update user set password = ? where username = ?");
			stmt.setString(1, newpassword);
			stmt.setString(2, username);
			stmt.execute();
			stmt.close();
		} catch (final SQLException ex) {
			ex.printStackTrace();

		}
	}
	

}
