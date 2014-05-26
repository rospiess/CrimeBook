package ch.ethz.inf.dbproject.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
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
		
		final Scan scan = new Scan("Note"+type + ".txt", new String[] { "noteid", "id", "comment", "username" });

		final Select<Integer> select = new Select<Integer>(scan, "id", id);
		final List<Comment> comments = new ArrayList<Comment>();
		while (select.moveNext()) {

			final Tuple tuple = select.current();
			final Comment c = new Comment(tuple.getString(3), tuple.getString(2), tuple.getInt(0));
			comments.add(c);
		}
		return comments;
	}

	public void insertComment(final int id, final String text, final String username, String type) {
			Insert.insertIntoGenerateKey("Note"+type+".txt", new String[]{id+"",text,username});

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
		Update.update("Convictions.txt", idcon, new String[]{null,begindate,enddate,null,null});
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


	@SuppressWarnings( "deprecation" )
	public final void setCaseOpen(int id, boolean open) {
		
		Update.update("Cases.txt", id, new String[]{null,null,null,null,null,null,null,open+"",null});
		
		if(open)// delete convictions associated with this case
		{
			for(Conviction c:  getConvictionsById(id, "idcase"))
				Delete.deleteFrom("Convictions.txt", c.getIdcon());
		}
		else// Create convictions based on suspects
		{
			Case c = getCaseById(id);
			Date d = getCurrentDate();
			switch(c.getCat()){
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
			
			SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
			
			for(Person p: getSuspectsById(id))
				Insert.insertIntoGenerateKey("Convictions.txt", new String[]{s.format(getCurrentDate()),s.format(d),""+id,""+p.getIdperson()});
		}
	}

	private final Date getCurrentDate() {
		return Calendar.getInstance().getTime();
	}

	public void deleteInvolved(int idcase, int idperson) {
		Delete.deleteFrom("Involved.txt", new int[]{idperson,idcase});
	}

	public final void deleteCase(int id) {		
		for(Comment c:  getCommentsById(id, "Cases"))//Drop all Notes
			Delete.deleteFrom("NotePersons.txt", c.getIdnote());
		
		for(Person p:  getSuspectsById(id))//Drop all Suspects
			Delete.deleteFrom("Involved.txt", new int[]{p.getIdperson(),id});
		
		for(Person p:  getWitnessesById(id))//Drop all Witnesses
			Delete.deleteFrom("Involved.txt", new int[]{p.getIdperson(),id});
		
		for(Person p:  getVictimsById(id))//Drop all Victims
			Delete.deleteFrom("Involved.txt", new int[]{p.getIdperson(),id});
		
		//If Address is no longer used, drop it
		int idAddress = getIdAddressByCase(id);
		final Scan scan = new Scan("Cases.txt", schemaCase);
		final Select<Integer> select = new Select<Integer>(scan, "idaddress", idAddress);
		if(!(select.moveNext() && select.moveNext()))
			Delete.deleteFrom("Addresses.txt", idAddress);
		
		
		Delete.deleteFrom("Cases.txt", id);		
	}

	public final void deletePerson(int id) {		
		for(Conviction c: getConvictionsById(id, "idperson"))//Drop all Convictions
			Delete.deleteFrom("Convictions.txt", c.getIdcon());
		
		for(Comment c:  getCommentsById(id, "Persons"))//Drop all Notes
			Delete.deleteFrom("NotePersons.txt", c.getIdnote());
		
		for(Involved i:  getInvolvedByPersonId(id))//Drop all Involvements
			Delete.deleteFrom("Involved.txt", new int[]{id,i.getIdcase()});		

		
		Delete.deleteFrom("Persons.txt", id);
	}

	public final void deleteNote(int Nr, final String type) {
		Delete.deleteFrom("Note"+type+".txt", Nr);
	}

	public final void openNewCase(String title, String descr, String date, String time, Address address, String catname, String username) {

		String country = address.getCountry(), city = address.getCity(), street = address.getStreet(),
				zip = Integer.toString(address.getZipCode()), streetno = Integer.toString(address.getStreetNo());
		
		final Scan scan = new Scan("Addresses.txt",schemaAddress);
		
		final Select<String> select1 = new Select<String>(scan,"country",country);
		final Select<String> select2 = new Select<String>(select1,"city",city);
		final Select<String> select3 = new Select<String>(select2,"street",street);
		final Select<String> select4 = new Select<String>(select3,"zip",zip);
		final Select<String> select5 = new Select<String>(select4,"streetno",streetno);
		
		int idAddress;
		if(select5.moveNext())//Address already exists
			idAddress = select5.current().getInt(0);
		
		else//Insert new Address
			idAddress = Insert.insertIntoGenerateKey("Addresses.txt", new String[]{country, zip, city, street, streetno});
		
		Insert.insertIntoGenerateKey("Cases.txt", new String[]{title,descr,date,time,""+idAddress,catname,"true",username});
	}

	public final int updateAddress(int id, Address address) {
		//returns the id of the address, used if old address was shared
		int idAddress = getIdAddressByCase(id);
		boolean shared = false;
		
		//check if Address was shared
		final Scan scan = new Scan("Cases.txt", schemaCase);
		final Select<Integer> select = new Select<Integer>(scan, "idaddress", idAddress);
		if(select.moveNext() && select.moveNext()){
			// There is more than one case using the old address
			shared = true;
		}
		
		// Check if new address already exists
		final Scan scan2 = new Scan("Addresses.txt",schemaAddress);
		
		//Get data from address
		String country = address.getCountry(), city = address.getCity(), street = address.getStreet(),
				zip = Integer.toString(address.getZipCode()), streetno = Integer.toString(address.getStreetNo());

		final Select<String> select1 = new Select<String>(scan2,"country",country);
		final Select<String> select2 = new Select<String>(select1,"city",city);
		final Select<String> select3 = new Select<String>(select2,"street",street);
		final Select<String> select4 = new Select<String>(select3,"zip",zip);
		final Select<String> select5 = new Select<String>(select4,"streetno",streetno);
		
		if(select5.moveNext()){
			// If not shared delete Address with old ID
			if (!shared && idAddress != select5.current().getInt(0)){
				Delete.deleteFrom("Addresses.txt", idAddress);
			}
			
			// Since the address already exists, save the new id
			idAddress = select5.current().getInt(0);
		}
		else{
			// New Address doesn't exist, insert or update
			if (shared){
				// Insert new Address
				idAddress = Insert.insertIntoGenerateKey("Addresses.txt", new String[]{country, zip, city, street, streetno});
			}
			else
			{
				// Address is not shared, just update the old one
				Update.update("Addresses.txt", idAddress, new String[]{null,address.getCountry(), Integer.toString(address.getZipCode()), address.getCity(), address.getStreet(),Integer.toString(address.getStreetNo())});
			}
		}
		
		// return the ID of the new Address
		return idAddress;

	}

	public final void updateCase(int id, String title, String descr, String date, String time, Address address, String catname) {
		
		int idAddress = updateAddress(id,address);
		Update.update("Cases.txt", id, new String[]{null,title,descr,date,time,Integer.toString(idAddress),catname,null,null});
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
		final Sort sort = new Sort(select, "title", true);
		
		return joinAddressToCase(sort);

	}

	public final List<Case> getClosedCases() {

		final Scan scan = new Scan("Cases.txt", schemaCase);
		final Select<Boolean> select = new Select<Boolean>(scan, "open", false);
		final Sort sort = new Sort(select, "title", true);
		
		return joinAddressToCase(sort);

	}

	public final List<Case> getMostRecentCases() {
		final Scan scan = new Scan("Cases.txt", schemaCase);
		final Sort sort = new Sort(scan, "date", false);
		
		return joinAddressToCase(sort);
	}

	public final List<Case> getOldestUnsolvedCases() {
		final Scan scan = new Scan("Cases.txt", schemaCase);
		final Select<Boolean> select = new Select<Boolean>(scan, "open", true);
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
		final Scan scan1 = new Scan("Convictions.txt", schemaConviction);
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
		final Scan scan = new Scan("Cases.txt", schemaCase);
		final Like like = new Like(scan, "title", title);
		final Sort sort = new Sort(like, "title", true);

		return joinAddressToCase(sort);
	}
	
	public final List<Case> searchByDescription(String description) {
		final Scan scan = new Scan("Cases.txt", schemaCase);
		final Like like = new Like(scan, "descr", description);
		final Sort sort = new Sort(like, "descr", true);

		return joinAddressToCase(sort);
	}

	public final List<Person> searchByName(String name) {
		final Scan scan = new Scan("Persons.txt",schemaPerson);
		final Like like = new Like(scan, new String[]{"firstname","lastname"}, name);
		final Sort sort = new Sort(like, "lastname", true);
		
		final List<Person> persons = new ArrayList<Person>();
		
		while (sort.moveNext()) {
			final Tuple tuple = sort.current();
			final Person p = new Person(tuple.getInt(0), tuple.getString(1), tuple.getString(2), tuple.getDate(3));
			persons.add(p);
		}
		
		return persons;
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
		final Scan scan2 = new Scan("Persons.txt", schemaPerson);
		final Scan scan3 = new Scan("Persons.txt", schemaPerson);
		
	
		final Select<Integer> select = new Select<Integer>(scan,"idcase",idcase, false);
				
		final Join join = new Join(scan2, select, "idperson", new String[]{"idperson","firstname","lastname"});
		
		final Minus minus = new Minus(scan3, join, "idperson");
		
		final Sort sort = new Sort(minus,"lastname",true);
		
		final List<Person> persons = new ArrayList<Person>();
		
		while(sort.moveNext())
		{
			Tuple tuple = sort.current();

			Person p = new Person(tuple.getInt(0),tuple.getString(1),tuple.getString(2), null);
			persons.add(p);
			
		}
		
		return persons;
	}
	

	public final List<Case> getCasesUninvolvedIn(int pid) {
		final Scan scan = new Scan("Involved.txt", schemaInvolved);
		final Scan scan2 = new Scan("Cases.txt", schemaCase);
		final Scan scan3 = new Scan("Cases.txt", schemaCase);
		
		//Get cases in which the person is involved
		final Select<Integer> select = new Select<Integer>(scan,"idperson", pid, false);
		final Join join = new Join(scan2, select, "idcase", new String[]{"idcase","title"});
		
		//subtract them from open cases
		final Select<Boolean> select2 =  new Select<Boolean>(scan3, "open", true);
		final Minus minus = new Minus(select2, join, "idcase");
		final Sort sort = new Sort(minus,"title",true);
		
		final List<Case> cases = new ArrayList<Case>();
		
		
		while(sort.moveNext())
		{
			Tuple tuple = sort.current();
			
			Case c = new Case(tuple.getInt(0),tuple.getString(1),null, null,null,null,null,false);
			cases.add(c);
			
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
		Update.update("Persons.txt", idperson, new String[]{null,firstname,lastname,date});
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

	public final boolean isNameTaken(String username) {

		final Scan scan = new Scan("Users.txt", new String[] { "username", "password" });

		final Select<String> select = new Select<String>(scan, "username", username);
		return select.moveNext();
	}

	public void changePassword(String username, String newpassword) {
		Update.update("Users.txt", username, new String[]{null,newpassword});
	}

	public final List<Pair<String, Integer>> getStatCategories() {

		final Scan scan = new Scan("Cases.txt", schemaCase);
		final GroupBy groupby = new GroupBy(scan,"cat",Function.Type.COUNT);

		List<Pair<String, Integer>> pairs = new ArrayList<Pair<String, Integer>>(); 
		
		while (groupby.moveNext()){
			Pair<String, Integer> p = new Pair<String, Integer>(groupby.current().getString(0),groupby.current().getInt(1));
			pairs.add(p);
		}
		return pairs;
		
	}

	public final List<Pair<String, Integer>> getStatInvolvements() {
		//(SELECT FirstName, LastName, COUNT(*) AS amount FROM PersonOfInterest AS POI, involved AS inv WHERE POI.idPersonOfInterest = inv.idPerson GROUP BY POI.idPersonOfInterest LIMIT 5) " +
		//"UNION (SELECT 'Other', '', sum(t2.amount2) from (SELECT COUNT(*) AS amount2 FROM PersonOfInterest as POI2, involved AS inv2 WHERE POI2.idPersonOfInterest = inv2.idPerson GROUP BY POI2.idPersonOfInterest LIMIT 5,1000) AS t2);");
		
		final Scan scan = new Scan("Involved.txt", schemaInvolved); // FROM involved AS inv,
		final Scan scan1 = new Scan("Persons.txt", schemaPerson); // PersonOfInterest AS POI
		final GroupBy groupby = new GroupBy(scan,"idperson",Function.Type.COUNT); // GROUP BY POI.idPersonOfInterest
		final Join join = new Join(groupby,scan1,"idperson", new String[]{
				"firstname","lastname","count"}); // SELECT FirstName, LastName, COUNT(*) WHERE POI.idPersonOfInterest = inv.idPerson
		final Limit limit = new Limit(join,5); // LIMIT 5
		
		final Scan scan2 = new Scan("Involved.txt", schemaInvolved); // FROM involved AS inv2,
		final Scan scan3 = new Scan("Persons.txt", schemaPerson); // PersonOfInterest as POI2
		final GroupBy groupby2 = new GroupBy(scan2,"idperson",Function.Type.COUNT); // GROUP BY POI2.idPersonOfInterest and COUNT(*)
		final Join join2 = new Join(groupby2,scan3,"idperson", new String[]{
				"count"}); // SELECT COUNT(*) AS amount2 WHERE POI2.idPersonOfInterest = inv2.idPerson as t2
		final Limit limit2 = new Limit(join2,5,1000); // LIMIT 5,1000
		
		List<Pair<String, Integer>> pairs = new ArrayList<Pair<String, Integer>>(); 
		while (limit.moveNext()){
			Pair<String, Integer> p = new Pair<String, Integer>
			(limit.current().getString(0) + " "+ limit.current().getString(1),
					limit.current().getInt(2));
			pairs.add(p);
		}
		int otherInvCounter = 0;
		while (limit2.moveNext()){
			otherInvCounter = otherInvCounter + limit2.current().getInt(0); // sum(t2.amount2) from t2
		}
		Pair<String, Integer> p2 = new Pair<String, Integer>
			("Other", otherInvCounter); // SELECT 'Other',
		pairs.add(p2); // t1 UNION t2
		return pairs;
		
	}

	public final List<Pair<Integer, Integer>> getStatCrimesPerYear() {
		final Scan scan = new Scan("Cases.txt", schemaCase);
		final GroupBy groupby = new GroupBy(scan,"date",Function.Type.COUNT,4);
		final Sort sort = new Sort(groupby,"date",false);

		List<Pair<Integer, Integer>> pairs = new ArrayList<Pair<Integer, Integer>>(); 
		
		while (sort.moveNext()){
			Pair<Integer, Integer> p = new Pair<Integer, Integer>
			(sort.current().getInt(0), sort.current().getInt(1));
			pairs.add(p);
		}
		return pairs;
		
	}

	public final List<Pair<Integer, Integer>> getStatConvictionsPerYear() {
		final Scan scan = new Scan("Convictions.txt", schemaConviction);
		final GroupBy groupby = new GroupBy(scan,"startdate",Function.Type.COUNT,4);
		final Sort sort = new Sort(groupby, "startdate", false);

		List<Pair<Integer, Integer>> pairs = new ArrayList<Pair<Integer, Integer>>(); 
		
		while (sort.moveNext()){
			Pair<Integer, Integer> p = new Pair<Integer, Integer>
			(sort.current().getInt(0), sort.current().getInt(1));
			pairs.add(p);
		}
		return pairs;
		
	}

	public final List<Pair<String, Integer>> getStatSuperCategories() {
		
		final Scan scan1 = new Scan("Category.txt",schemaCategory);
		final Scan scan2 = new Scan("Cases.txt", schemaCase);
		final Join join1 = new Join(scan1,scan2, "cat", new String[] {"cat","supercat"});
		final GroupBy groupby = new GroupBy(join1,"supercat",Function.Type.COUNT);

		List<Pair<String, Integer>> pairs = new ArrayList<Pair<String, Integer>>(); 
		
		while (groupby.moveNext()){
			Pair<String, Integer> p = new Pair<String, Integer>(groupby.current().getString(0),groupby.current().getInt(1));
			pairs.add(p);
		}
		return pairs;
		
	}

	public final List<Pair<String, Integer>> getAverageAges() {
		@SuppressWarnings("deprecation")
		final int year = getCurrentDate().getYear() + 1900; //Year 0 is 1900 for some reason..
		
		//Categories
		final Scan scan = new Scan("Persons.txt", schemaPerson);
		final Scan scan1 = new Scan("Convictions.txt", schemaConviction);
		final Scan scan2 = new Scan("Cases.txt", schemaCase);
		final Join join = new Join(scan,scan1,"idperson",new String[]{"idcase","idperson","birth"});

		final Join join2 = new Join(scan2,join,"idcase",new String[]{"cat","birth"});
		final GroupBy groupby = new GroupBy(join2,"cat",Function.Type.AVERAGEYEAR,"birth");

		List<Pair<String, Integer>> pairs = new ArrayList<Pair<String, Integer>>(); 
		
		while (groupby.moveNext()){
			Pair<String, Integer> p = new Pair<String, Integer>
			(groupby.current().getString(0), year - groupby.current().getInt(1));
			pairs.add(p);
		}
		
		//Persons
		final Scan scan3 = new Scan("Persons.txt", schemaPerson);
		final GroupBy groupby2 = new GroupBy(scan3,"lastname",Function.Type.AVERAGEYEAR,"birth",0);

		if(groupby2.moveNext()){
			Pair<String, Integer> p = new Pair<String, Integer>
			("All Persons", year - groupby2.current().getInt(1));
			pairs.add(p);
		}		
		
		//Convictions
		final Scan scan4 = new Scan("Persons.txt", schemaPerson);
		final Scan scan5 = new Scan("Convictions.txt", schemaConviction);
		final Join join3 = new Join(scan4,scan5,"idperson",new String[]{"lastname","birth"});
		
		final GroupBy groupby3 = new GroupBy(join3,"lastname",Function.Type.AVERAGEYEAR,"birth",0);

		if(groupby3.moveNext()){
			Pair<String, Integer> p = new Pair<String, Integer>
			("All Convicts", year - groupby3.current().getInt(1));
			pairs.add(p);
		}
		
		return pairs;
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
