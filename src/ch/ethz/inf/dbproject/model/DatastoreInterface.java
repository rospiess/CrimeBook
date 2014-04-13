package ch.ethz.inf.dbproject.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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
	
	// PreparedStatements declaration
	private PreparedStatement ps_createConviction, ps_insertCaseComment, ps_insertPersonComment,
				ps_updateConvictionDates, ps_setCaseOpen, ps_deleteConvictions, ps_deleteInvolved,
				ps_deleteCaseNote, ps_deletePersonNote, ps_insertAddress, ps_insertCase, ps_updateAddress, ps_updateCase,
				ps_addInvolvement, ps_addPerson, ps_updatePerson, ps_changePassword, ps_deletePersons, ps_deleteCases;
					

	public DatastoreInterface() {
		this.sqlConnection = MySQLConnection.getInstance().getConnection();
		
		try {
			// Create prepared Statements
			
			ps_createConviction = sqlConnection.prepareStatement("INSERT INTO conviction(idcase, idpersonofinterest, beginDate, endDate) VALUES (?,?,?,?)");
			ps_insertCaseComment = sqlConnection.prepareStatement("INSERT INTO notecase (idCase, text, username) VALUES (?, ?, ?)");
			ps_insertPersonComment = sqlConnection.prepareStatement("INSERT INTO noteperson (idPersonofinterest, text, username) VALUES (?, ?, ?)");
			ps_updateConvictionDates = sqlConnection.prepareStatement("UPDATE conviction SET begindate = ?, enddate = ?  WHERE idconviction = ?");
			ps_setCaseOpen = sqlConnection.prepareStatement("UPDATE Cases SET open = ? WHERE idcase = ?");
			ps_deleteConvictions = sqlConnection.prepareStatement("DELETE FROM conviction WHERE conviction.idcase = ?");
			ps_deleteInvolved = sqlConnection.prepareStatement("DELETE FROM involved WHERE idCase = ? AND idperson = ?  AND role = ?");
			ps_deleteCaseNote = sqlConnection.prepareStatement("DELETE FROM notecase WHERE Nr = ? AND username = ?");
			ps_deletePersonNote = sqlConnection.prepareStatement("DELETE FROM noteperson WHERE Nr = ? AND username = ?");
			ps_deletePersons = sqlConnection.prepareStatement("DELETE FROM personofinterest WHERE idpersonofinterest = ?");
			ps_deleteCases = sqlConnection.prepareStatement("DELETE FROM cases WHERE idcase = ?");
			ps_insertAddress = sqlConnection.prepareStatement("INSERT INTO Address(country,city,street, zipcode,streetno) VALUES ( ?, ?, ?, ?, ?)",PreparedStatement.RETURN_GENERATED_KEYS);
			ps_insertCase = sqlConnection.prepareStatement("INSERT INTO Cases(title,description,open,date,time,idAddress,catname,username) VALUES (?, ?, 1, ?, ?, ?, ?,?)");
			ps_updateAddress = sqlConnection.prepareStatement("UPDATE Address SET country=?,city=?,street=?,zipcode=?,streetno=? WHERE idAddress = ?");
			ps_updateCase = sqlConnection.prepareStatement("UPDATE Cases SET title=?,description=?,date=?,time=?,catname=? WHERE idcase = ?");
			ps_addInvolvement = sqlConnection.prepareStatement("INSERT INTO involved (idCase, idPerson, role) VALUES (?,?,?)");
			ps_addPerson = sqlConnection.prepareStatement("INSERT INTO personofinterest(firstname,lastname,dateofbirth) VALUES ( ?, ?, ?)");
			ps_updatePerson = sqlConnection.prepareStatement("UPDATE personofinterest SET firstname = ?, lastname = ?, dateofbirth = ? WHERE idpersonofinterest = ?");
			ps_changePassword = sqlConnection.prepareStatement("UPDATE user SET password = ? WHERE username = ?");
			
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
			
			final PreparedStatement stmt;
			if (type.equals("case")){
				stmt = ps_insertCaseComment;
			}

			else{
				stmt = ps_insertPersonComment;
			}
			
			stmt.setInt(1, id);
			stmt.setString(2, text);
			stmt.setString(3, username);
			stmt.execute();

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
								+ " and ca.idcase = co.idcase ORDER BY lastname ASC");
			else if (type.equals("person")){
				rs = stmt
						.executeQuery("Select * from conviction co, personofinterest p, cases ca where co.idpersonofinterest = "
								+ id
								+ " and co.idpersonofinterest = p.idpersonofinterest "
								+ " and co.idcase = ca.idcase ORDER BY title ASC");
			}
			else{
				// Search by conviction id
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
			final PreparedStatement stmt = ps_updateConvictionDates;
			stmt.setString(1, begindate);
			stmt.setString(2, enddate);
			stmt.setInt(3,idcon);
			stmt.execute();
			
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
							+ pid + " and cases.idCase = involved.idcase ORDER BY title ASC");
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
	
	public final boolean isInvolvedIn(final int pid, final int cid){
		try{
			final Statement stmt = sqlConnection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT count(*)>0 FROM involved WHERE idperson ="+pid+" AND idcase="+cid);
			if (rs.next())
				return rs.getBoolean(1);
			return false;
		}catch(final SQLException ex){
			ex.printStackTrace();
			return true;
		}
	}

	@SuppressWarnings( "deprecation" )
	public final void setCaseOpen(int id, boolean open) {

		try {
			PreparedStatement s = ps_setCaseOpen;
			s.setString(1, open ? "1" : "0");
			s.setInt(2, id);
			s.execute();
			
			if (open){
				// delete convictions associated with this case
				s = ps_deleteConvictions;
				s.setInt(1, id);
				s.execute();
			}
			else{
				// Create convictions based on suspects
				
				// Get suspects with query
				final Statement stmt = this.sqlConnection.createStatement();
				final ResultSet rs = stmt
						.executeQuery("Select idpersonofinterest, catname from personofinterest p, involved i, cases c "
								+ "where i.role = 'Suspect' and i.idCase = "
								+ id
								+ " and i.idperson = p.idpersonofinterest and c.idCase = " + id);

				while (rs.next()) {
					// Create conviction with the suspects in the resultset
					ps_createConviction.setInt(1, id);
					ps_createConviction.setInt(2, rs.getInt("idpersonofinterest"));
					
					Date d =getCurrentDate();
					ps_createConviction.setDate(3, d);
					
					Category c = new Category(rs.getString("catname"),null);
					switch(c.getName()){
					case "Assault":
						d.setYear(d.getYear()+5); break;
					case "Murder":
						d.setYear(d.getYear()+20); break;
					case "Kidnapping":
						d.setYear(d.getYear()+8); break;
					case "OtherPers":
						d.setYear(d.getYear()+4); break;
					case "Theft":
						d.setYear(d.getYear()+1); break;
					case "Fraud":
						d.setYear(d.getYear()+2); break;
					case "Burglary":
						d.setYear(d.getYear()+2); break;
					case "OtherProp":
						d.setYear(d.getYear()+3); break;
					default:
						d.setYear(d.getYear()+1); break;
					}
					
					ps_createConviction.setDate(4, d);
					
					ps_createConviction.execute();
				}

				rs.close();

			}
			


		} catch (final SQLException ex) {
			ex.printStackTrace();
		}

	}
	
	private final Date getCurrentDate(){
		try{
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT CURDATE()");
			if(rs.next())
				return rs.getDate(1);
		}catch(final SQLException ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public void deleteInvolved(int idcase, int idperson, String type) {
		// Deletes an involvement of case + person
		// Type distinguishes between suspect and witness
		try {
			PreparedStatement s = ps_deleteInvolved;
			s.setInt(1, idcase);
			s.setInt(2, idperson);
			if (type == "suspect"){
				s.setString(3, "Suspect");
			}
			else
			{
				s.setString(3, "Witness");
			}
			s.execute();
			
		} catch (final SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public final void deleteCase(int id) {
		try {
			PreparedStatement s = ps_deleteCases;
			s.setInt(1, id);
			s.execute();
			
		} catch (final SQLException ex) {
			ex.printStackTrace();
		}

	}
	public final void deletePerson(int id) {
		try {

			PreparedStatement s = ps_deletePersons;
			s.setInt(1, id);
			s.execute();
			
		} catch (final SQLException ex) {
			ex.printStackTrace();
		}

	}
	
	public final void deleteNote(int Nr, String uname, final String type) {
		// Deletes a caseNote with a certain Nr as key
		// Type distinguishes between casenote and personnote
		try {
			PreparedStatement s;
			if (type == "case"){
				s = ps_deleteCaseNote;
			}
			else
			{
				s = ps_deletePersonNote;
			}
			s.setInt(1, Nr);
			s.setString(2, uname);
			s.execute();
			
		} catch (final SQLException ex) {
			ex.printStackTrace();
		}

	}

	public final void openNewCase(String title, String descr, String date,
			String time, Address address, String catname, String username) {

		try {
			PreparedStatement s = ps_insertAddress;
			s.setString(1, address.getCountry());
			s.setString(2, address.getCity());
			s.setString(3, address.getStreet());
			s.setInt(4, address.getZipCode());
			s.setInt(5, address.getStreetNo());
			s.execute();
			ResultSet rs = s.getGeneratedKeys();
			rs.next();
			s = ps_insertCase;
			s.setString(1, title);
			s.setString(2, descr);
			s.setString(3, date);
			s.setString(4, time);
			s.setInt(5, rs.getInt(1));
			s.setString(6, catname);
			s.setString(7, username);
			s.execute();


		} catch (final SQLException ex) {
			ex.printStackTrace();
		}

	}
	
	public final void updateAddress(int id, Address address) {

		try {
			final Statement stmt = sqlConnection.createStatement();
			ResultSet rs = stmt.executeQuery("Select * FROM Address a");
			while(rs.next()){
			Address a = new Address(rs);
			if(a.equals(address))
				{
				String s ="Update Cases set idAddress = " + rs.getInt("idAddress")
						+ " where idCase = " + id;
				stmt.execute(s);				
				return;
				}
			}
			
			rs = stmt.executeQuery("Select  idCase, idAddress FROM Cases");
			int idAddress = getIdAddressByCase(id);
			while(rs.next()){
			
				if(rs.getInt("idAddress") == idAddress && rs.getInt("idCase") != id)
				{
					PreparedStatement s = ps_insertAddress;
					s.setString(1, address.getCountry());
					s.setString(2, address.getCity());
					s.setString(3, address.getStreet());
					s.setInt(4, address.getZipCode());
					s.setInt(5, address.getStreetNo());
					s.execute();
					ResultSet rs2 = s.getGeneratedKeys();
					rs2.next();
					stmt.execute("Update Cases set idAddress = " + rs2.getInt(1) + " where idCase = " + id);
					return;
				}
			}
			
			PreparedStatement s = ps_updateAddress;
			s.setString(1, address.getCountry());
			s.setString(2, address.getCity());
			s.setString(3, address.getStreet());
			s.setInt(4, address.getZipCode());
			s.setInt(5, address.getStreetNo());
			s.setInt(6, getIdAddressByCase(id));
			s.execute();
			
			
		} catch (final SQLException ex) {
			ex.printStackTrace();
		}

	}
	
	public final void updateCase(int id, String title, String descr, String date,
			String time, Address address, String catname) {

		try {
			updateAddress(id,address);
			
			PreparedStatement s = ps_updateCase;
			s.setString(1, title);
			s.setString(2, descr);
			s.setString(3, date);
			s.setString(4, time);
			s.setString(5, catname);
			s.setInt(6,id);
			s.execute();
			
		} catch (final SQLException ex) {
			ex.printStackTrace();
		}

	}
	
	private final int getIdAddressByCase(int idcase){
		try{
			final Statement s = sqlConnection.createStatement();
			final ResultSet rs = s.executeQuery("Select idAddress FROM Cases c" +
					" WHERE c.idcase = "+idcase);
			if(rs.next()){
				return rs.getInt("idAddress");
			}
		}
		catch(final SQLException ex){
			ex.printStackTrace();
		}
		return -1;
	}

	public final List<Case> getCasesByUser(String username) {

		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select * from Cases c, Address a where c.idAddress = a.idAddress and c.username = '" + username +"' ORDER BY title ASC");

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
	
	public final List<Case> getAllCases() {

		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select * from Cases c, Address a where c.idAddress = a.idAddress ORDER BY title ASC");

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
					.executeQuery("Select * from Cases c, Address a where c.idAddress = a.idAddress and open = 1" +
							" order by title asc");

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
					.executeQuery("Select * from Cases c, Address a where c.idAddress = a.idAddress and open = 0 ORDER BY title ASC");

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

	public final List<Case> getCasesByCategory(String category) {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs;
			if (category.equals("personal"))
				rs = stmt
						.executeQuery("Select * from Cases c, Category, Address a where "
								+ "c.idAddress = a.idAddress and c.catname"
								+ " = category.catname and supercat = 'personal crime' ORDER BY title ASC");
			else if (category.equals("property"))
				rs = stmt
						.executeQuery("Select * from Cases c, Category , Address a where "
								+ " c.idAddress = a.idAddress and c.catname"
								+ " = category.catname and  supercat = 'property crime' ORDER BY title ASC");
			else
				rs = stmt
						.executeQuery("Select * from Cases c, Address a where "
								+ "c.idAddress = a.idAddress and catname = '"
								+ category + "' ORDER BY title ASC");

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
							+ " and cas.idcase = c.idcase" +
							" ORDER BY LastName ASC");

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
							+ " and cas.idcase = c.idcase" +
							" ORDER BY LastName ASC");

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

	public final List<Case> searchByTitle(String title) {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select * from Cases c, Address a where c.idAddress = a.idAddress and c.title like '%"
							+ title + "%'" +
									" ORDER BY Title ASC");

			final List<Case> persons = new ArrayList<Case>();
			while (rs.next()) {
				persons.add(new Case(rs));
			}

			rs.close();
			stmt.close();

			return persons;

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
							+ name + "%'" + "or lastname like '%" + name + "%'" +
									" ORDER BY LastName ASC");

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
					.executeQuery("Select * from personofinterest ORDER BY LastName ASC");

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
	
	public final List<Person> getUninvolvedInCase(int idcase) {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("SELECT * from personofinterest p " +
							"WHERE p.idpersonofinterest NOT IN (SELECT p2.idpersonofinterest " +
								"FROM personofinterest p2, involved i2 " +
								"WHERE p2.idpersonofinterest = i2.idperson AND i2.idcase = " + idcase +")"+
							" ORDER BY p.LastName ASC");

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
	
	public final List<Case> getCasesUninvolvedIn(int pid) {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("SELECT * FROM Cases c, Address a where c.idAddress = a.idAddress and open = 1" +
							" AND c.idCase NOT IN ( " +
								" SELECT i2.idCase FROM Involved i2 " +
								" WHERE i2.idperson = "+pid+")" +
							" ORDER BY title asc");

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

	public final List<Person> getSuspectsById(int id) {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select idpersonofinterest,firstname,lastname, dateofbirth from personofinterest p, involved i "
							+ "where i.role = 'Suspect' and i.idCase = '"
							+ id
							+ "' and i.idperson = p.idpersonofinterest ORDER BY lastname ASC");

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
							+ "' and i.idperson = p.idpersonofinterest ORDER BY lastname ASC");

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
	
	public final List<Person> getVictimsById(int id) {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("Select idpersonofinterest,firstname,lastname, dateofbirth from personofinterest p, involved i "
							+ "where i.role = 'Victim' and i.idCase = '"
							+ id
							+ "' and i.idperson = p.idpersonofinterest ORDER BY lastname ASC");

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
	
	
	public final void addInvolvement(int idcase, int idperson, String role){
		try{
			final PreparedStatement stmt = ps_addInvolvement;
			stmt.setInt(1, idcase);
			stmt.setInt(2, idperson);
			stmt.setString(3, role);
			stmt.execute();
		}catch(final SQLException ex){
			ex.printStackTrace();
		}
	}
	

	public final void addNewPerson(String firstname, String lastname, String date) {

		try {
			PreparedStatement stmt = ps_addPerson;
			stmt.setString(1, firstname);
			stmt.setString(2, lastname);
			stmt.setString(3, date);
			
			stmt.execute();

		} catch (final SQLException ex) {
			ex.printStackTrace();
		}

	}
	
	public final void updatePerson(int idperson, String firstname, String lastname, String date) {

		try {
			PreparedStatement stmt = ps_updatePerson;
			stmt.setString(1, firstname);
			stmt.setString(2, lastname);
			stmt.setString(3, date);
			stmt.setInt(4, idperson);
			
			stmt.execute();

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

			final PreparedStatement stmt = ps_changePassword;
			stmt.setString(1, newpassword);
			stmt.setString(2, username);
			stmt.execute();
		} catch (final SQLException ex) {
			ex.printStackTrace();

		}
	}
	
	
	public final List<Pair<String,Integer>> getStatCategories() {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("SELECT Title, CatName, COUNT(*) as amount FROM cases GROUP BY CatName");

			final List<Pair<String,Integer>> stats = new ArrayList<Pair<String,Integer>>();
			while (rs.next()) {
				Pair<String,Integer> a_cat_val = new Pair<String, Integer>(rs.getString("CatName"), rs.getInt("amount"));
				stats.add(a_cat_val);
			}

			rs.close();
			stmt.close();

			return stats;

		} catch (final SQLException ex) {
			ex.printStackTrace();
			return null;
		}

	}
	
	public final List<Pair<String,Integer>> getStatInvolvements() {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("(SELECT FirstName, LastName, COUNT(*) AS amount FROM PersonOfInterest AS POI, involved AS inv WHERE POI.idPersonOfInterest = inv.idPerson GROUP BY POI.idPersonOfInterest LIMIT 5) UNION (SELECT 'Other', '', sum(t2.amount2) from (SELECT COUNT(*) AS amount2 FROM PersonOfInterest as POI2, involved AS inv2 WHERE POI2.idPersonOfInterest = inv2.idPerson GROUP BY POI2.idPersonOfInterest LIMIT 5,1000) AS t2);");

			final List<Pair<String,Integer>> stats = new ArrayList<Pair<String,Integer>>();
			while (rs.next()) {
				Pair<String,Integer> a_cat_val = new Pair<String, Integer>((rs.getString("FirstName") + " " + rs.getString("LastName")), rs.getInt("amount"));
				stats.add(a_cat_val);
			}

			rs.close();
			stmt.close();

			return stats;

		} catch (final SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public final List<Pair<Integer,Integer>> getStatCrimesPerYear() {
		try {

			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt
					.executeQuery("SELECT YEAR(date) AS year, COUNT(*) AS numberOfCrimes FROM cases GROUP BY year ORDER BY year DESC LIMIT 5;");

			final List<Pair<Integer,Integer>> stats = new ArrayList<Pair<Integer,Integer>>();
			while (rs.next()) {
				Pair<Integer,Integer> a_year_val = new Pair<Integer, Integer>(rs.getInt("year"), rs.getInt("numberOfCrimes"));
				stats.add(a_year_val);
			}

			rs.close();
			stmt.close();

			return stats;

		} catch (final SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	

}
