package ch.ethz.inf.dbproject.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ch.ethz.inf.dbproject.model.simpleDatabase.operators.*;
import ch.ethz.inf.dbproject.model.simpleDatabase.*;

/**
 * This class should be the interface between the web application and the
 * database. Keeping all the data-access methods here will be very helpful for
 * part 2 of the project.
 */
public final class DatastoreInterfaceSimpleDatabase {
		
	final String[] schemaAddress = new String[] { "idaddress", "country", "zip", "city", "street", "streetno"};
	final String[] schemaCase = new String[] { "idcase", "title", "descr", "date", "time", "idaddress", "cat", "open", "username" };
	final String[] schemaCaseAddress = new String[] { "idcase", "title", "descr", "date", "time", "cat", "open", "country", "zip", "city", "street", "streetno" };
	final String[] schemaCategory = new String[] { "cat", "supercat"};
	final String[] schemaConviction = new String[] {"idcon", "startdate", "enddate", "idcase", "idperson" };
	final String[] schemaInvolved = new String[] { "idperson", "idcase", "role"};
	final String[] schemaPerson = new String[] { "idperson", "firstname", "lastname", "birth" };
	
	public DatastoreInterfaceSimpleDatabase() {
	}

	public final Case getCaseById(final int id) {
		final Scan scan = new Scan("Cases.txt", schemaCase);
		final Select<Integer> select = new Select<Integer>(scan, "idcase", id);

		return joinAddressToCase(select).get(0);

	}

	public final Person getPersonById(final int id) {

		final Scan scan = new Scan("Persons.txt", schemaPerson);
		final Select<Integer> select = new Select<Integer>(scan, "idperson", id);

		if (select.moveNext()) {

			final Tuple tuple = select.current();
			final Person p = new Person(tuple.getInt(0), tuple.getString(1), tuple.getString(2), tuple.getDate(3));
			return p;

		}
		return null;

	}

	public final List<Comment> getCommentsById(final int id, String type) {
		String s;
		if (type.equals("case"))
			s = "NoteCases";
		else
			s = "NotePersons";

		final Scan scan = new Scan(s + ".txt", new String[] { "noteid", "id", "comment", "username" });

		final Select<Integer> select = new Select<Integer>(scan, "id", id);
		final List<Comment> comments = new ArrayList<Comment>();
		while (select.moveNext()) {

			final Tuple tuple = select.current();
			final Comment c = new Comment(tuple.getString(2), tuple.getString(3), tuple.getInt(1));
			comments.add(c);
		}
		return comments;
	}

	public void insertComment(final int id, final String text, final String username, String type) {
			Insert.insertIntoGenerateKey("Note"+type+".txt", new String[]{"idnote",id+"",text,username});

	}

	public final List<Conviction> getConvictionsById(final int id, String type) {
		// Get Conviction by ID
		// type determines whether the ID belongs to the case, the person or the conviction itself
		
		final Scan scan = new Scan("Convictions.txt", schemaConviction);
		final Scan scan2 = new Scan("Persons.txt", schemaPerson);
		final Scan scan3 = new Scan("Cases.txt", schemaCase);
		
		final Select<Integer> select = new Select<Integer>(scan, type, id);
		
		final Join join = new Join(scan2, select, "idperson", new String[]{"idcon","startdate", "enddate","idcase","idperson","firstname", "lastname"});
		final Join join2 = new Join(scan3, join,"idcase",new String[]{"idcon","startdate", "enddate","idperson","firstname", "lastname","idcase","title","cat"});
		
		final List<Conviction> convictions = new ArrayList<Conviction>();
		
		while (join2.moveNext()) {
			
			final Tuple tuple = join2.current();
			final Conviction c = new Conviction(
				tuple.getInt(0),
				tuple.getDate(1),
				tuple.getDate(2),
				new Case(tuple.getInt(6),tuple.getString(7),null,null,null,null,new Category(tuple.getString(8), null), false), 
				new Person(tuple.getInt(3),tuple.getString(4),tuple.getString(5),null)
			);
			convictions.add(c);
		}
		return convictions;
	}

	public final void updateConvictionDates(int idcon, String begindate, String enddate) {
		/*
		 * try{ final PreparedStatement stmt = ps_updateConvictionDates;
		 * stmt.setString(1, begindate); stmt.setString(2, enddate);
		 * stmt.setInt(3,idcon); stmt.execute();
		 * 
		 * } catch (final SQLException ex){ ex.printStackTrace(); return; }
		 */
	}

	public final List<Involved> getInvolvedByPersonId(final int pid) {
		// Returns list of involvement of a person by their id.
		// The Involved class contains information about the case and the
		// person.
		
		final Scan scan = new Scan("Involved.txt", schemaInvolved);
		final Scan scan2 = new Scan("Cases.txt", schemaCase);
		
	
		final Select<Integer> select = new Select<Integer>(scan,"idperson",pid);
		
		final Join join = new Join(scan2, select, "idcase", new String[]{"role","idcase","title"});
		
		List<Involved> invlist = new ArrayList<Involved>();
		
		while(join.moveNext())
		{
			Tuple tuple = join.current();
			Involved i = new Involved(tuple.getString(0),null, 
					new Case(tuple.getInt(1),tuple.getString(2),null,null,null,null, null, false));
			invlist.add(i);
		}
		
		return invlist;

	}

	public final boolean isInvolvedIn(final int pid, final int cid) {

		final Scan scan = new Scan("Involved.txt", schemaInvolved);
		
		final Select<Integer> select = new Select<Integer>(scan,"idcase",cid);
		
		final Select<Integer> select2 = new Select<Integer>(select,"idperson",pid);
		
		return select2.moveNext();
	}


	public final void setCaseOpen(int id, boolean open) {
		/*
		 * try { PreparedStatement s = ps_setCaseOpen; s.setString(1, open ? "1"
		 * : "0"); s.setInt(2, id); s.execute();
		 * 
		 * if (open){ // delete convictions associated with this case s =
		 * ps_deleteConvictions; s.setInt(1, id); s.execute(); } else{ // Create
		 * convictions based on suspects
		 * 
		 * // Get suspects with query final Statement stmt =
		 * this.sqlConnection.createStatement(); final ResultSet rs = stmt
		 * .executeQuery(
		 * "Select idpersonofinterest, catname from personofinterest p, involved i, cases c "
		 * + "where i.role = 'Suspect' and i.idCase = " + id +
		 * " and i.idperson = p.idpersonofinterest and c.idCase = " + id);
		 * 
		 * while (rs.next()) { // Create conviction with the suspects in the
		 * resultset ps_createConviction.setInt(1, id);
		 * ps_createConviction.setInt(2, rs.getInt("idpersonofinterest"));
		 * 
		 * Date d =getCurrentDate(); ps_createConviction.setDate(3, d);
		 * 
		 * Category c = new Category(rs.getString("catname"),null);
		 * switch(c.getName()){ case "Assault": d.setYear(d.getYear()+5); break;
		 * case "Murder": d.setYear(d.getYear()+20); break; case "Kidnapping":
		 * d.setYear(d.getYear()+8); break; case "OtherPers":
		 * d.setYear(d.getYear()+4); break; case "Theft":
		 * d.setYear(d.getYear()+1); break; case "Fraud":
		 * d.setYear(d.getYear()+2); break; case "Burglary":
		 * d.setYear(d.getYear()+2); break; case "OtherProp":
		 * d.setYear(d.getYear()+3); break; default: d.setYear(d.getYear()+1);
		 * break; }
		 * 
		 * ps_createConviction.setDate(4, d);
		 * 
		 * ps_createConviction.execute(); }
		 * 
		 * rs.close();
		 * 
		 * }
		 * 
		 * 
		 * 
		 * } catch (final SQLException ex) { ex.printStackTrace(); }
		 */
	}

	private final String getCurrentDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
	}

	public void deleteInvolved(int idcase, int idperson, String type) {
		// Deletes an involvement of case + person
		// Type distinguishes between suspect and witness
		/*
		 * try { PreparedStatement s = ps_deleteInvolved; s.setInt(1, idcase);
		 * s.setInt(2, idperson); if (type == "suspect"){ s.setString(3,
		 * "Suspect"); } else { s.setString(3, "Witness"); } s.execute();
		 * 
		 * } catch (final SQLException ex) { ex.printStackTrace(); }
		 */
	}

	public final void deleteCase(int id) {
		/*
		 * try { PreparedStatement s = ps_deleteCases; s.setInt(1, id);
		 * s.execute();
		 * 
		 * } catch (final SQLException ex) { ex.printStackTrace(); }
		 */

	}

	public final void deletePerson(int id) {
		/*
		 * try {
		 * 
		 * PreparedStatement s = ps_deletePersons; s.setInt(1, id); s.execute();
		 * 
		 * } catch (final SQLException ex) { ex.printStackTrace(); }
		 */

	}

	public final void deleteNote(int Nr, String uname, final String type) {
		// Deletes a caseNote with a certain Nr as key
		// Type distinguishes between casenote and personnote
		/*
		 * try { PreparedStatement s; if (type == "case"){ s =
		 * ps_deleteCaseNote; } else { s = ps_deletePersonNote; } s.setInt(1,
		 * Nr); s.setString(2, uname); s.execute();
		 * 
		 * } catch (final SQLException ex) { ex.printStackTrace(); }
		 */

	}

	public final void openNewCase(String title, String descr, String date, String time, Address address, String catname, String username) {
//		TODO: check if Address already exists
		int idAddress = Insert.insertIntoGenerateKey("Addresses.txt", new String[]{address.getCountry(), Integer.toString(address.getZipCode()), address.getCity(), address.getStreet(),Integer.toString(address.getStreetNo())});
		Insert.insertIntoGenerateKey("Cases.txt", new String[]{title,descr,date,time,""+idAddress,catname,"true",username});
	}

	public final void updateAddress(int id, Address address) {

		/*
		 * try { final Statement stmt = sqlConnection.createStatement();
		 * ResultSet rs = stmt.executeQuery("Select * FROM Address a");
		 * while(rs.next()){ Address a = new Address(rs); if(a.equals(address))
		 * { String s ="Update Cases set idAddress = " + rs.getInt("idAddress")
		 * + " where idCase = " + id; stmt.execute(s); return; } }
		 * 
		 * rs = stmt.executeQuery("Select  idCase, idAddress FROM Cases"); int
		 * idAddress = getIdAddressByCase(id); while(rs.next()){
		 * 
		 * if(rs.getInt("idAddress") == idAddress && rs.getInt("idCase") != id)
		 * { PreparedStatement s = ps_insertAddress; s.setString(1,
		 * address.getCountry()); s.setString(2, address.getCity());
		 * s.setString(3, address.getStreet()); s.setInt(4,
		 * address.getZipCode()); s.setInt(5, address.getStreetNo());
		 * s.execute(); ResultSet rs2 = s.getGeneratedKeys(); rs2.next();
		 * stmt.execute("Update Cases set idAddress = " + rs2.getInt(1) +
		 * " where idCase = " + id); return; } }
		 * 
		 * PreparedStatement s = ps_updateAddress; s.setString(1,
		 * address.getCountry()); s.setString(2, address.getCity());
		 * s.setString(3, address.getStreet()); s.setInt(4,
		 * address.getZipCode()); s.setInt(5, address.getStreetNo());
		 * s.setInt(6, getIdAddressByCase(id)); s.execute();
		 * 
		 * 
		 * } catch (final SQLException ex) { ex.printStackTrace(); }
		 */

	}

	public final void updateCase(int id, String title, String descr, String date, String time, Address address, String catname) {

		/*
		 * try { updateAddress(id,address);
		 * 
		 * PreparedStatement s = ps_updateCase; s.setString(1, title);
		 * s.setString(2, descr); s.setString(3, date); s.setString(4, time);
		 * s.setString(5, catname); s.setInt(6,id); s.execute();
		 * 
		 * } catch (final SQLException ex) { ex.printStackTrace(); }
		 */

	}

	private final int getIdAddressByCase(int idcase) {
		final Scan scan = new Scan("Cases.txt", schemaCase);
		final Select<Integer> select = new Select<Integer>(scan, "idcase", idcase);
		if(select.moveNext())
			return select.current().getInt(5);
		return -1;
	}

	public final List<Case> getCasesByUser(String username) {

		final Scan scan = new Scan("Cases.txt", schemaCase);
		final Select<String> select = new Select<String>(scan,"username", username);
		
		return joinAddressToCase(select);


	}

	public final List<Case> getAllCases() {
		final Scan scan = new Scan("Cases.txt", schemaCase);
		final Sort sort = new Sort(scan, "title", true);
		return joinAddressToCase(sort);

	}

	public final List<Case> getOpenCases() {
		final Scan scan = new Scan("Cases.txt", schemaCase);
		final Select<Boolean> select = new Select<Boolean>(scan, "open", true);

		return joinAddressToCase(select);

	}

	public final List<Case> getClosedCases() {

		final Scan scan = new Scan("Cases.txt", schemaCase);
		final Select<Boolean> select = new Select<Boolean>(scan, "open", false);

		return joinAddressToCase(select);

	}

	public final List<Case> getMostRecentCases() {
		final Scan scan = new Scan("Cases.txt", schemaCase);
		final Sort sort = new Sort(scan, "date", false);
		
		return joinAddressToCase(sort);
	}

	public final List<Case> getOldestUnsolvedCases() {
		final Scan scan = new Scan("Cases.txt", schemaCase);
		final Select<Boolean> select = new Select<Boolean>(scan, "open", false);
		final Sort sort = new Sort(select, "date", true);
		
		return joinAddressToCase(sort);
	}

	public final List<Case> getCasesByCategory(String category) {
		final Scan scan = new Scan("Cases.txt", schemaCase);
		Select<String> select;
		
		if(category.equals("Property Crime")||category.equals("Personal Crime"))
		{
			final Scan scan2 = new Scan("Category.txt", schemaCategory);
			final Join join = new Join(scan2, scan, "cat",
					new String[] { "idcase", "title", "descr", "date", "time", "idaddress", "cat", "open", "supercat" });
			  select = new Select<String> (join, "supercat", category);
//			  Project project = new Project(select, schemaCaseAddress);
			  return joinAddressToCase(select);
		}
		else
			select = new Select<String> (scan, "cat", category);	
		
		return joinAddressToCase(select);
	}

	public final List<Conviction> searchByCategory(String category) {
		
		final Scan scan = new Scan("Convictions.txt", schemaConviction);
		final Scan scan2 = new Scan("Cases.txt", schemaCase);
		final Scan scan3 = new Scan("Persons.txt", schemaPerson);


		final Join join = new Join(scan2, scan,"idcase",new String[]{"idcon","startdate", "enddate","idcase","idperson","title", "cat"});
		
		final Select<String> select = new Select<String>(join, "cat", category);
		
		final Join join2 = new Join(scan3, select, "idperson", new String[]{"idcon","startdate", "enddate","idperson","firstname", "lastname","idcase","title","cat"});
		final List<Conviction> convictions = new ArrayList<Conviction>();
		
		while (join2.moveNext()) {
			
			final Tuple tuple = join2.current();
			final Conviction c = new Conviction(
				tuple.getInt(0),
				tuple.getString(1),
				tuple.getString(2),
				new Case(tuple.getInt(6),tuple.getString(7),null,null,null,null,new Category(tuple.getString(8), null), false), 
				new Person(tuple.getInt(3),tuple.getString(4),tuple.getString(5),null)
			);
			convictions.add(c);
		}
		return convictions;
		
	}

	public final List<Conviction> searchByDate(String date) {
		final Scan scan = new Scan("Convictions.txt", schemaConviction);
		final Scan scan1 = new Scan("Convictions.txt", schemaConviction);//Stupid, but necessary
		final Scan scan2 = new Scan("Persons.txt", schemaPerson);
		final Scan scan3 = new Scan("Cases.txt", schemaCase);
		
		final Select<String> select = new Select<String>(scan, "startdate", date);
		final Select<String> select2 = new Select<String>(scan1, "enddate", date);

		final Union union = new Union(select,select2);
		
		final Join join = new Join(scan2, union, "idperson", new String[]{"idcon","startdate", "enddate","idcase","idperson","firstname", "lastname"});
		final Join join2 = new Join(join, scan3,"idcase",new String[]{"idcon","startdate", "enddate","idperson","firstname", "lastname","idcase","title","cat"});
		
		
		final List<Conviction> convictions = new ArrayList<Conviction>();
		
		while (join2.moveNext()) {
			
			final Tuple tuple = join2.current();
			final Conviction c = new Conviction(
				tuple.getInt(0),
				tuple.getString(1),
				tuple.getString(2),
				new Case(tuple.getInt(6),tuple.getString(7),null,null,null,null,new Category(tuple.getString(8), null), false), 
				new Person(tuple.getInt(3),tuple.getString(4),tuple.getString(5),null)
			);
			convictions.add(c);
		}
		return convictions;
	}

	public final List<Case> searchByTitle(String title) {
		return null;
		/*
		 * try {
		 * 
		 * final Statement stmt = this.sqlConnection.createStatement(); final
		 * ResultSet rs = stmt .executeQuery(
		 * "Select * from Cases c, Address a where c.idAddress = a.idAddress and c.title like '%"
		 * + title + "%'" + " ORDER BY Title ASC");
		 * 
		 * final List<Case> persons = new ArrayList<Case>(); while (rs.next()) {
		 * persons.add(new Case(rs)); }
		 * 
		 * rs.close(); stmt.close();
		 * 
		 * return persons;
		 * 
		 * } catch (final SQLException ex) { ex.printStackTrace(); return null;
		 * }
		 */
	}

	public final List<Person> searchByName(String name) {
		return null;
		/*
		 * try {
		 * 
		 * final Statement stmt = this.sqlConnection.createStatement(); final
		 * ResultSet rs = stmt
		 * .executeQuery("Select * from personofinterest where firstname like '%"
		 * + name + "%'" + "or lastname like '%" + name + "%'" +
		 * " ORDER BY LastName ASC");
		 * 
		 * final List<Person> persons = new ArrayList<Person>(); while
		 * (rs.next()) { persons.add(new Person(rs)); }
		 * 
		 * rs.close(); stmt.close();
		 * 
		 * return persons;
		 * 
		 * } catch (final SQLException ex) { ex.printStackTrace(); return null;
		 * }
		 */
	}

	public final List<Person> getAllPersons() {
		final Scan scan = new Scan("Persons.txt", schemaPerson);
		final Sort sort = new Sort(scan, "lastname", true);
		final List<Person> persons = new ArrayList<Person>();
		while (sort.moveNext()) {

			final Tuple tuple = sort.current();
			final Person p = new Person(tuple.getInt(0), tuple.getString(1), tuple.getString(2), tuple.getDate(3));
			persons.add(p);

		}
		return persons;
	}

	public final List<Person> getUninvolvedInCase(int idcase) {
		final Scan scan = new Scan("Involved.txt", schemaInvolved);
		final Scan scan1 = new Scan("Involved.txt", schemaInvolved);
		final Scan scan2 = new Scan("Persons.txt", schemaPerson);
		
	
		final Select<Integer> select = new Select<Integer>(scan,"idcase",idcase);
		
		final Minus minus = new Minus(scan1,select,"idperson"); 
		
		final Join join = new Join(scan2, minus, "idperson", new String[]{"idperson","firstname","lastname"});
		
		final List<Person> persons = new ArrayList<Person>();
		final HashSet<Integer> h = new HashSet<Integer>();
		
		while(join.moveNext())
		{
			Tuple tuple = join.current();
			int id = tuple.getInt(0);
			Person p = new Person(id,tuple.getString(1),tuple.getString(2), null);
			if(!h.contains(id))
			{
				persons.add(p);
				h.add(id);
			}
			
		}
		
		return persons;
	}

	public final List<Case> getCasesUninvolvedIn(int pid) {
		final Scan scan = new Scan("Involved.txt", schemaInvolved);
		final Scan scan1 = new Scan("Involved.txt", schemaInvolved);
		final Scan scan2 = new Scan("Cases.txt", schemaCase);
		
	
		final Select<Integer> select = new Select<Integer>(scan,"idperson", pid);
		
		final Minus minus = new Minus(scan1,select,"idcase");
		
		final Join join = new Join(scan2, minus, "idcase", new String[]{"idcase","title"});
		
		final List<Case> cases = new ArrayList<Case>();
		final HashSet<Integer> h = new HashSet<Integer>();
		
		while(join.moveNext())
		{
			Tuple tuple = join.current();
			int id = tuple.getInt(0);
			Case c = new Case(tuple.getInt(0),tuple.getString(1),null, null,null,null,null,false);
			if(!h.contains(id))
			{
				cases.add(c);
				h.add(id);
			}
			
		}
		
		return cases;
	}

	public final List<Person> getSuspectsById(int id) {
		final Scan scan = new Scan("Persons.txt", schemaPerson);
		final Scan scan2 = new Scan("Involved.txt", schemaInvolved);
		final Select<Integer> select = new Select<Integer>(scan2, "idcase",id);
		final Select<String> select2 = new Select<String>(select, "role","Suspect");
		final Join join = new Join(scan, select2, "idperson", new String[]{"idperson", "firstname", "lastname", "birth","role"});
		
		final List<Person> persons = new ArrayList<Person>();
		while (join.moveNext()) {

			final Tuple tuple = join.current();
			final Person p = new Person(tuple.getInt(0), tuple.getString(1), tuple.getString(2), tuple.getDate(3));
			persons.add(p);

		}
		return persons;
	}

	public final List<Person> getWitnessesById(int id) {
		final Scan scan = new Scan("Persons.txt", schemaPerson);
		final Scan scan2 = new Scan("Involved.txt", schemaInvolved);
		final Select<Integer> select = new Select<Integer>(scan2, "idcase",id);
		final Select<String> select2 = new Select<String>(select, "role","Witness");
		final Join join = new Join(scan, select2, "idperson", new String[]{"idperson", "firstname", "lastname", "birth","role"});
		
		final List<Person> persons = new ArrayList<Person>();
		while (join.moveNext()) {

			final Tuple tuple = join.current();
			final Person p = new Person(tuple.getInt(0), tuple.getString(1), tuple.getString(2), tuple.getDate(3));
			persons.add(p);

		}
		return persons;
	}

	public final List<Person> getVictimsById(int id) {
		final Scan scan = new Scan("Persons.txt", schemaPerson);
		final Scan scan2 = new Scan("Involved.txt", schemaInvolved);
		final Select<Integer> select = new Select<Integer>(scan2, "idcase",id);
		final Select<String> select2 = new Select<String>(select, "role","Victim");
		final Join join = new Join(scan, select2, "idperson", new String[]{"idperson", "firstname", "lastname", "birth","role"});
		
		final List<Person> persons = new ArrayList<Person>();
		while (join.moveNext()) {

			final Tuple tuple = join.current();
			final Person p = new Person(tuple.getInt(0), tuple.getString(1), tuple.getString(2), tuple.getDate(3));
			persons.add(p);

		}
		return persons;
	}

	public final void addInvolvement(int idcase, int idperson, String role) {
		Insert.insertInto("Involved.txt", new String[]{idperson+"",idcase+"",role});
	}

	public final void addNewPerson(String firstname, String lastname, String date) {
		Insert.insertIntoGenerateKey("Persons.txt", new String[]{firstname,lastname,date});
	}

	public final void updatePerson(int idperson, String firstname, String lastname, String date) {

		/*
		 * try { PreparedStatement stmt = ps_updatePerson; stmt.setString(1,
		 * firstname); stmt.setString(2, lastname); stmt.setString(3, date);
		 * stmt.setInt(4, idperson);
		 * 
		 * stmt.execute();
		 * 
		 * } catch (final SQLException ex) { ex.printStackTrace(); }
		 */
	}

	public final String getPassword(String username) {
		final Scan scan = new Scan("Users.txt", new String[] { "username", "password" });

		final Select<String> select = new Select<String>(scan, "username", username);

		if (select.moveNext()) {

			final Tuple tuple = select.current();
			return tuple.getString(1);

		}
		return null;

	}

	public void insertUser(String username, String password) {
		Insert.insertInto("Users.txt", new String[]{username,password});
	}

	public final boolean getNameIsTaken(String username) {

		final Scan scan = new Scan("Users.txt", new String[] { "username", "password" });

		final Select<String> select = new Select<String>(scan, "username", username);
		return select.moveNext();
	}

	public void changePassword(String username, String newpassword) {
		/*
		 * try {
		 * 
		 * final PreparedStatement stmt = ps_changePassword; stmt.setString(1,
		 * newpassword); stmt.setString(2, username); stmt.execute(); } catch
		 * (final SQLException ex) { ex.printStackTrace();
		 * 
		 * }
		 */
	}

	public final List<Pair<String, Integer>> getStatCategories() {
		return null;
		/*
		 * try {
		 * 
		 * final Statement stmt = this.sqlConnection.createStatement(); final
		 * ResultSet rs = stmt .executeQuery(
		 * "SELECT Title, CatName, COUNT(*) as amount FROM cases GROUP BY CatName"
		 * );
		 * 
		 * final List<Pair<String,Integer>> stats = new
		 * ArrayList<Pair<String,Integer>>(); while (rs.next()) {
		 * Pair<String,Integer> a_cat_val = new Pair<String,
		 * Integer>(rs.getString("CatName"), rs.getInt("amount"));
		 * stats.add(a_cat_val); }
		 * 
		 * rs.close(); stmt.close();
		 * 
		 * return stats;
		 * 
		 * } catch (final SQLException ex) { ex.printStackTrace(); return null;
		 * }
		 */

	}

	public final List<Pair<String, Integer>> getStatInvolvements() {
		return null;
		/*
		 * try {
		 * 
		 * final Statement stmt = this.sqlConnection.createStatement(); final
		 * ResultSet rs = stmt .executeQuery(
		 * "(SELECT FirstName, LastName, COUNT(*) AS amount FROM PersonOfInterest AS POI, involved AS inv WHERE POI.idPersonOfInterest = inv.idPerson GROUP BY POI.idPersonOfInterest LIMIT 5) UNION (SELECT 'Other', '', sum(t2.amount2) from (SELECT COUNT(*) AS amount2 FROM PersonOfInterest as POI2, involved AS inv2 WHERE POI2.idPersonOfInterest = inv2.idPerson GROUP BY POI2.idPersonOfInterest LIMIT 5,1000) AS t2);"
		 * );
		 * 
		 * final List<Pair<String,Integer>> stats = new
		 * ArrayList<Pair<String,Integer>>(); while (rs.next()) {
		 * Pair<String,Integer> a_cat_val = new Pair<String,
		 * Integer>((rs.getString("FirstName") + " " +
		 * rs.getString("LastName")), rs.getInt("amount"));
		 * stats.add(a_cat_val); }
		 * 
		 * rs.close(); stmt.close();
		 * 
		 * return stats;
		 * 
		 * } catch (final SQLException ex) { ex.printStackTrace(); return null;
		 * }
		 */
	}

	public final List<Pair<Integer, Integer>> getStatCrimesPerYear() {
		return null;
		/*
		 * try {
		 * 
		 * final Statement stmt = this.sqlConnection.createStatement(); final
		 * ResultSet rs = stmt .executeQuery(
		 * "SELECT YEAR(date) AS year, COUNT(*) AS numberOfCrimes FROM cases GROUP BY year ORDER BY year DESC LIMIT 5;"
		 * );
		 * 
		 * final List<Pair<Integer,Integer>> stats = new
		 * ArrayList<Pair<Integer,Integer>>(); while (rs.next()) {
		 * Pair<Integer,Integer> a_year_val = new Pair<Integer,
		 * Integer>(rs.getInt("year"), rs.getInt("numberOfCrimes"));
		 * stats.add(a_year_val); }
		 * 
		 * rs.close(); stmt.close();
		 * 
		 * return stats;
		 * 
		 * } catch (final SQLException ex) { ex.printStackTrace(); return null;
		 * }
		 */
	}

	public final List<Pair<Integer, Integer>> getStatConvictionsPerYear() {
		return null;
		/*
		 * try {
		 * 
		 * final Statement stmt = this.sqlConnection.createStatement(); final
		 * ResultSet rs = stmt .executeQuery(
		 * "SELECT YEAR(beginDate) AS year, COUNT(*) AS numberOfConvictions FROM conviction GROUP BY year ORDER BY year DESC LIMIT 5;"
		 * );
		 * 
		 * final List<Pair<Integer,Integer>> stats = new
		 * ArrayList<Pair<Integer,Integer>>(); while (rs.next()) {
		 * Pair<Integer,Integer> a_year_val = new Pair<Integer,
		 * Integer>(rs.getInt("year"), rs.getInt("numberOfConvictions"));
		 * stats.add(a_year_val); }
		 * 
		 * rs.close(); stmt.close();
		 * 
		 * return stats;
		 * 
		 * } catch (final SQLException ex) { ex.printStackTrace(); return null;
		 * }
		 */
	}

	public final List<Pair<String, Integer>> getStatSuperCategories() {
		return null;
		/*
		 * try {
		 * 
		 * final Statement stmt = this.sqlConnection.createStatement(); final
		 * ResultSet rs = stmt .executeQuery(
		 * "SELECT category.SuperCat AS SuperCatName, COUNT(*) AS amount FROM cases, category WHERE cases.CatName = category.CatName GROUP BY category.SuperCat;"
		 * );
		 * 
		 * final List<Pair<String,Integer>> stats = new
		 * ArrayList<Pair<String,Integer>>(); while (rs.next()) {
		 * Pair<String,Integer> a_SuperCat_val = new Pair<String,
		 * Integer>(rs.getString("SuperCatName"), rs.getInt("amount"));
		 * stats.add(a_SuperCat_val); }
		 * 
		 * rs.close(); stmt.close();
		 * 
		 * return stats;
		 * 
		 * } catch (final SQLException ex) { ex.printStackTrace(); return null;
		 * }
		 */
	}

	public final List<Pair<String, Integer>> getAverageAges() {
		return null;
		/*
		 * try {
		 * 
		 * final Statement stmt = this.sqlConnection.createStatement(); final
		 * ResultSet rs = stmt .executeQuery(
		 * "SELECT * FROM ((SELECT 'Convicted'  as 'Registered as', AVG(TIMESTAMPDIFF(YEAR,DateOfBirth,CURDATE())) as 'age' FROM personOfInterest AS p, conviction AS c WHERE p.idPersonOfINterest = c.idpersonofinterest) UNION (SELECT CatName as 'Registered as', AVG(TIMESTAMPDIFF(YEAR,DateOfBirth,CURDATE())) as 'age' FROM personOfInterest AS p, conviction AS conv, cases WHERE p.idPersonOfINterest = conv.idpersonofinterest and cases.idCase = conv.idCase GROUP BY CatName) UNION (SELECT 'Person of interest' as 'Registered as', AVG(TIMESTAMPDIFF(YEAR,DateOfBirth,CURDATE())) as 'age' FROM personOfInterest)) AS allAges;"
		 * );
		 * 
		 * final List<Pair<String,Integer>> stats = new
		 * ArrayList<Pair<String,Integer>>(); while (rs.next()) {
		 * Pair<String,Integer> a_persons_ages = new Pair<String,
		 * Integer>(rs.getString("Registered as"), rs.getInt("age"));
		 * stats.add(a_persons_ages); }
		 * 
		 * rs.close(); stmt.close();
		 * 
		 * return stats;
		 * 
		 * } catch (final SQLException ex) { ex.printStackTrace(); return null;
		 * }
		 */
	}
	private List<Case> joinAddressToCase(Operator cases)//Own function since it's used so often
	{
		final Scan addresses = new Scan("Addresses.txt", schemaAddress);
		final Join join = new Join(addresses, cases, "idaddress", schemaCaseAddress);
		
		final List<Case> list = new ArrayList<Case>();
		while (join.moveNext()) {

			final Tuple tuple = join.current();
			final Case c = new Case(tuple.getInt(0), tuple.getString(1), tuple.getString(2), tuple.getDate(3), tuple.getTime(4),
					new Address(tuple.getString(7), tuple.getString(9), tuple.getString(10), tuple.getInt(8), tuple.getInt(11)), new Category(tuple.getString(5), null), tuple.getBoolean(6));
			list.add(c);

		}
		return list;
	}

}
