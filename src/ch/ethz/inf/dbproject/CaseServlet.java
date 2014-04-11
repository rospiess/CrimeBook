package ch.ethz.inf.dbproject;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.Address;
import ch.ethz.inf.dbproject.model.Comment;
import ch.ethz.inf.dbproject.model.Conviction;
import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.model.Person;
import ch.ethz.inf.dbproject.model.User;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;
import ch.ethz.inf.dbproject.util.html.SelectHelper;

/**
 * Servlet implementation class of Case Details Page
 */
@WebServlet(description = "Displays a specific case.", urlPatterns = { "/Case" })
public final class CaseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CaseServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected final void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		final HttpSession session = request.getSession(true);

		String idString = request.getParameter("id");
		if (idString == null) {
			idString = ""+(((Case)session.getAttribute("CurrentCase")).getIdcase());
		}

		try {

			final Integer id = Integer.parseInt(idString);
			final Case aCase = this.dbInterface.getCaseById(id);
			final List<Comment> clist = this.dbInterface.getCommentsById(id,
					"case");
			final List<Person> plist = this.dbInterface.getSuspectsById(id);
			final List<Person> wlist = this.dbInterface.getWitnessesById(id);
			final List<Person> vlist = this.dbInterface.getVictimsById(id);
			final List<Conviction> convlist = this.dbInterface.getConvictionsById(id,"case");
			final User loggedUser = UserManagement
					.getCurrentlyLoggedInUser(session);
			session.setAttribute("CurrentCase", aCase);
			session.setAttribute("CurrentCon",null);
			
			session.setAttribute("ConvDate",false);
			session.setAttribute("EditingCase",false);

			/*******************************************************
			 * Construct a table to present all properties of a case
			 *******************************************************/
			final BeanTableHelper<Case> table = new BeanTableHelper<Case>(
					"case" /* The table html id property */,
					"casesTable" /* The table html class property */, 
					Case.class /*The class of the objects (rows) that will be displayed*/

			);

			table.addBeanColumn("Title", "title");
			table.addBeanColumn("Case Description", "descr");
			table.addBeanColumn("Date", "dateString");
			table.addBeanColumn("Time", "timeString");
			table.addBeanColumn("Location", "loc");
			table.addBeanColumn("Category", "cat");
			table.addBeanColumn("Open", "open");

			table.addObject(aCase);
			table.setVertical(true);

			session.setAttribute("caseTable", table);

			//Comment Table
			final BeanTableHelper<Comment> ctable = new BeanTableHelper<Comment>(
					"comment" /* The table html id property */,
					"casesTable" /* The table html class property */,
					Comment.class /*
								 * The class of the objects (rows) that will be
								 * displayed
								 */
			);

			ctable.addBeanColumn("Note ID", "idnote");
			ctable.addBeanColumn("Text", "comment");
			ctable.addBeanColumn("Submitted by", "username");
			if (loggedUser != null){
				ctable.addLinkColumn("Delete", "<img src='./s_cancel.png'></img>", "Case?action=deleteNote&uname="+loggedUser.getUsername()+"&delete=", "idnote");
			}

			ctable.addObjects(clist);

			session.setAttribute("commentTable", ctable);

			
			//Suspect / Perpetrator table
			final BeanTableHelper<Person> ptable = new BeanTableHelper<Person>(
					"person", "personsTable", Person.class);

			ptable.addBeanColumn("First Name", "firstname");
			ptable.addBeanColumn("Last Name", "lastname");
			ptable.addBeanColumn("Date of Birth", "bdateString");
			ptable.addLinkColumn("", "View Person", "Person?id=", "idperson");
			if (aCase.getOpen() && loggedUser != null){
				ptable.addLinkColumn("Erase Suspicion","Reprieve", "Case?action=deleteSuspect&idperson=", "idperson");
			}

			ptable.addObjects(plist);

			session.setAttribute("suspectTable", ptable);
			
			
			//Witness table
			final BeanTableHelper<Person> wtable = new BeanTableHelper<Person>(
					"person", "personsTable", Person.class);

			wtable.addBeanColumn("First Name", "firstname");
			wtable.addBeanColumn("Last Name", "lastname");
			wtable.addBeanColumn("Date of Birth", "bdateString");
			wtable.addLinkColumn("", "View Person", "Person?id=", "idperson");
			if (aCase.getOpen() && loggedUser != null){
				wtable.addLinkColumn("Unlink from case","Unlink", "Case?action=deleteWitness&idperson=", "idperson");
			}

			wtable.addObjects(wlist);

			session.setAttribute("witnessTable", wtable);
			
			

			//Witness table
			final BeanTableHelper<Person> vtable = new BeanTableHelper<Person>(
					"person", "personsTable", Person.class);

			vtable.addBeanColumn("First Name", "firstname");
			vtable.addBeanColumn("Last Name", "lastname");
			vtable.addBeanColumn("Date of Birth", "bdateString");
			vtable.addLinkColumn("", "View Person", "Person?id=", "idperson");
			if (aCase.getOpen() && loggedUser != null){
				vtable.addLinkColumn("Unlink from case","Unlink", "Case?action=deleteWitness&idperson=", "idperson");
			}

			vtable.addObjects(vlist);

			session.setAttribute("victimTable", vtable);
			
			
			//
			
			
			//Conviction table
			final BeanTableHelper<Conviction> convtable = new BeanTableHelper<Conviction>(
					"conviction", "casesTable", Conviction.class);

			convtable.addBeanColumn("Type", "type");
			convtable.addBeanColumn("Begin Date", "dateString");
			convtable.addBeanColumn("End Date", "enddateString");
			convtable.addBeanColumn("Convicted First Name", "firstname");
			convtable.addBeanColumn("Convicted Last Name", "lastname");
			convtable.addLinkColumn("", "View Person", "Person?id=", "idperson");
			if (loggedUser != null){
				convtable.addLinkColumn("", "Edit dates", "Case?action=convDate&idcon=", "idcon");
			}

			convtable.addObjects(convlist);

			session.setAttribute("convictionTable", convtable);
			
			
			final SelectHelper<Person> personselect = new SelectHelper<Person>(
					"selectedPerson",
					"Available Persons",
					"fullname",
					"idperson",
					Person.class
					);
			personselect.addObjects(this.dbInterface.getUninvolvedInCase(aCase.getIdcase()));
			
			session.setAttribute("personSelect",personselect);
			

			final String action = request.getParameter("action");
			final String Nr = request.getParameter("delete");
			final String uname = request.getParameter("uname");
			final String idperson = request.getParameter("idperson");
			final String idcon = request.getParameter("idcon");
			
			if  (Nr != null && uname != null && action != null && action.trim().equals("deleteNote")){
				this.dbInterface.deleteNote(Integer.parseInt(Nr), uname, "case");
				// Refresh to show changes
				response.sendRedirect(request.getRequestURL().toString());
				return; //Return is needed to prevent the forwarding
			}	
			if  (action != null && action.trim().equals("deleteSuspect") && idperson != null){
				this.dbInterface.deleteInvolved(aCase.getIdcase(),Integer.parseInt(idperson), "suspect");
				// Refresh to show changes
				response.sendRedirect(request.getRequestURL().toString());
				return; //Return is needed to prevent the forwarding
			}
			if  (action != null && action.trim().equals("deleteWitness") && idperson != null){
				this.dbInterface.deleteInvolved(aCase.getIdcase(),Integer.parseInt(idperson), "witness");
				// Refresh to show changes
				response.sendRedirect(request.getRequestURL().toString());
				return; //Return is needed to prevent the forwarding
			}
			if (action != null && action.trim().equals("editDetails")){
				session.setAttribute("EditingCase",true);
				// By the way, the cancel button works by just calling this get method, which sets this attribute back to false
			}
			if (action != null && action.trim().equals("convDate") && idcon != null){
				session.setAttribute("ConvDate",true);
				Conviction aCon = dbInterface.getConvictionsById(Integer.parseInt(idcon), "conviction").get(0);
				session.setAttribute("CurrentCon", aCon);
			}

		} catch (final Exception ex) {
			ex.printStackTrace();
			this.getServletContext().getRequestDispatcher("/Cases.jsp")
					.forward(request, response);
		}

		this.getServletContext().getRequestDispatcher("/Case.jsp")
				.forward(request, response);
	}
	
	
	protected final void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		
		request.setCharacterEncoding("UTF-8"); // needed in order to allow for most of special printable UTF-8 char like Umlaut in casenote and personnote
		response.setContentType("text/plain; charset=utf-8"); // And just in case we do it as well for the response
		response.setCharacterEncoding("UTF-8");

		final HttpSession session = request.getSession(true);

		String idString = request.getParameter("id");
		if (idString == null) {
			idString = ""+(((Case)session.getAttribute("CurrentCase")).getIdcase());
		}

		try {

			final Integer id = Integer.parseInt(idString);
			final Case aCase = this.dbInterface.getCaseById(id);
			final User loggedUser = UserManagement
					.getCurrentlyLoggedInUser(session);
			session.setAttribute("CurrentCase", aCase);


			
			final String comment = request.getParameter("comment");
			final String action = request.getParameter("action");
			
			if (action != null && action.equals("add_comment")
					&& comment != null && !comment.isEmpty())
			{
				this.dbInterface.insertComment(id, comment,
						loggedUser.getUsername(), "case");
				response.setHeader("Refresh",  "0");
			}
			if (action != null && action.trim().equals("delete"))
			{
				dbInterface.deleteCase(id);
				response.sendRedirect(request.getRequestURL().toString() + "s");return;

			}
			if  (action != null && action.trim().equals("close")){
				this.dbInterface.setCaseOpen(id,false);
				response.setHeader("Refresh",  "0");
			}
			if  (action != null && action.trim().equals("open")){
				this.dbInterface.setCaseOpen(id,true);
				response.setHeader("Refresh",  "0");
			}
			if (action != null && action.trim().equals("submitdates")){
				session.setAttribute("ConvDate", false);
				String begindate = request.getParameter("begindate");
				try{
					java.sql.Date.valueOf(begindate);
				}catch(IllegalArgumentException e){
					begindate = null; // default = unknown value
				}
				String enddate = request.getParameter("enddate");
				try{
					java.sql.Date.valueOf(enddate);
				}catch(IllegalArgumentException e){
					enddate = null; // default = unknown value
				}
				
				dbInterface.updateConvictionDates(((Conviction)session.getAttribute("CurrentCon")).getIdcon(),
						begindate,
						enddate);
				response.setHeader("Refresh",  "0");
			}
			
			if (action != null && action.trim().equals("submitEdit")){
				// Edited case details
				session.setAttribute("EditingCase", false);
				
				String title = request.getParameter("title");
				if (title != null && title.isEmpty())
					title = aCase.getTitle(); // Reset to something valid
				final String descr = request.getParameter("descr");
				final String catname = request.getParameter("category");
				final String street = request.getParameter("street");
				final String city = request.getParameter("city");
				final String country = request.getParameter("country");
				
				// Handle invalid inputs or null values
				
				String date = request.getParameter("date");
				try{
					java.sql.Date.valueOf(date);
				}catch(IllegalArgumentException e){
					date = null; // default = unknown value
				}
				
				
				String time = request.getParameter("time");
				try{
					if (time != null && time.length() == 5){
						//For browser which only have hh:mm (e.g. Chrome)
						time = time.concat(":00");
					}
					java.sql.Time.valueOf(time);
				}catch(IllegalArgumentException e){
					time = null;
				}
				
				int streetno;
				String t_stno = request.getParameter("streetno");
				if (t_stno != null && t_stno != ""){
					try{
						streetno = Integer.parseInt(t_stno);
					}catch(NumberFormatException e){
						streetno = -1; // Unknown
					}
				}
				else{
					streetno = -1; // Unknown
				}
					
				
				int zipcode;
				String t_zip = request.getParameter("zipcode");
				if (t_zip != null && t_zip != ""){
					try{
						zipcode = Integer.parseInt(t_zip);
					}catch(NumberFormatException e){
						zipcode = -1; // Unknown
					}
				}
				else{
					zipcode = -1; // Unknown
				}
				
				final Address address = new Address(country, city, street, zipcode,
						streetno);
				
				dbInterface.updateCase(id,title,descr,date,time,address,catname);
				response.setHeader("Refresh", "0");
			}
			
			String selPerson = request.getParameter("selectedPerson");
			String role = request.getParameter("role");
			if(action != null && action.equals("link_person") && selPerson != null && role != null){
				int selidperson = Integer.parseInt(selPerson);
				
				//Check for duplicate
				
				if (!dbInterface.isInvolvedIn(selidperson, id)){
					dbInterface.addInvolvement(id, selidperson, role);
					response.setHeader("Refresh", "0");
				}
				//TODO print some error message "already connected to case"
			}
			

		} catch (final Exception ex) {
			ex.printStackTrace();
			this.getServletContext().getRequestDispatcher("/Cases.jsp")
					.forward(request, response);
		}

		this.getServletContext().getRequestDispatcher("/Case.jsp")
				.forward(request, response);
	}
}
