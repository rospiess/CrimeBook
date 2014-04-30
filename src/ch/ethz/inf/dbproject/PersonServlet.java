package ch.ethz.inf.dbproject;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.model.Conviction;
import ch.ethz.inf.dbproject.model.Comment;
import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.DatastoreInterfaceSimpleDatabase;
import ch.ethz.inf.dbproject.model.Involved;
import ch.ethz.inf.dbproject.model.Person;
import ch.ethz.inf.dbproject.model.User;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;
import ch.ethz.inf.dbproject.util.html.SelectHelper;

/**
 * Servlet implementation class of Case Details Page
 */
@WebServlet(description = "Displays a specific Person.", urlPatterns = { "/Person" })
public final class PersonServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterfaceSimpleDatabase dbInterface = new DatastoreInterfaceSimpleDatabase();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PersonServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		final HttpSession session = request.getSession(true);

		 String idString = request.getParameter("id");
		if (idString == null) {
			idString = (String)session.getAttribute("Last Case");
			}

		try {

			session.setAttribute("Last Case", idString);
			final Integer id = Integer.parseInt(idString);
			final Person aPerson = this.dbInterface.getPersonById(id);
			final List<Comment> comlist = this.dbInterface.getCommentsById(id,"person");
			final List<Conviction> conlist = this.dbInterface.getConvictionsById(id,"idperson");
			final List<Involved> invlist = this.dbInterface.getInvolvedByPersonId(id);
			final User loggedUser = UserManagement
					.getCurrentlyLoggedInUser(session);

			
			session.setAttribute("editingPerson",false);
			session.setAttribute("currentPerson",aPerson);
			
			/*******************************************************
			 * Construct a table to present all properties of a case
			 *******************************************************/
			final BeanTableHelper<Person> table = new BeanTableHelper<Person>(
					"person" 		/* The table html id property */,
					"personsTable" /* The table html class property */,
					Person.class 	/* The class of the objects (rows) that will be displayed */
			);

			// Add columns to the new table

			
			//table.addBeanColumn("Person ID", "idperson");
			table.addBeanColumn("First Name", "firstname");
			table.addBeanColumn("Last Name", "lastname");
			table.addBeanColumn("Date of Birth", "bdateString");			

			table.addObject(aPerson);
			table.setVertical(true);			

			session.setAttribute("personTable", table);	
			
			final BeanTableHelper<Comment> ctable = new BeanTableHelper<Comment>(
					"comment" 		/* The table html id property */,
					"casesTable" /* The table html class property */,
					Comment.class 	/* The class of the objects (rows) that will be displayed */
			);
			
			ctable.addBeanColumn("Note ID", "idnote");
			ctable.addBeanColumn("Text", "comment");
			ctable.addBeanColumn("Submitted by", "username");
			if (loggedUser != null){
				ctable.addLinkColumn("Delete", "<img src='./s_cancel.png'></img>", "Person?action=deleteNote&uname="+loggedUser.getUsername()+"&delete=", "idnote");
			}
				
			ctable.addObjects(comlist);		

			session.setAttribute("commentTable", ctable);
			
			//Conviction table
			final BeanTableHelper<Conviction> contable = new BeanTableHelper<Conviction>(
					"conviction" 		/* The table html id property */,
					"casesTable" /* The table html class property */,
					Conviction.class 	/* The class of the objects (rows) that will be displayed */
			);
			contable.addBeanColumn("Case", "casetitle");
			contable.addBeanColumn("Type", "type");
			contable.addBeanColumn("Start Date", "dateString");
			contable.addBeanColumn("End Date", "enddateString");
			contable.addLinkColumn("", "View Case", "Case?id=", "idcase");
			
			contable.addObjects(conlist);	

			session.setAttribute("convictionTable", contable);
			
			
			//Create Involved Table
			final BeanTableHelper<Involved> invtable = new BeanTableHelper<Involved>(
					"involved" 		/* The table html id property */,
					"personsTable" /* The table html class property */,
					Involved.class 	/* The class of the objects (rows) that will be displayed */
			);
			invtable.addBeanColumn("Case", "casetitle");
			invtable.addBeanColumn("Role", "role");
			invtable.addLinkColumn("", "View Case", "Case?id=", "idcase");

//			invtable.addObjects(invlist);	

			session.setAttribute("involvedTable", invtable);
			
			// Add selection to add person to a case
			final SelectHelper<Case> caseselect = new SelectHelper<Case>(
					"selectedCase",
					"Available Cases",
					"title",
					"idcase",
					Case.class
					);
//			caseselect.addObjects(this.dbInterface.getCasesUninvolvedIn(aPerson.getIdperson()));
			
			session.setAttribute("caseSelect",caseselect);
			
			
			
			
			final String action = request.getParameter("action");
			final String Nr = request.getParameter("delete");
			final String uname = request.getParameter("uname");
			
			if  (Nr != null && uname != null && action != null && action.trim().equals("deleteNote")){
				this.dbInterface.deleteNote(Integer.parseInt(Nr), uname, "person");
				// Refresh
				response.sendRedirect(request.getRequestURL().toString());
				return;
			}
			if (action != null && action.trim().equals("editDetails")){
				session.setAttribute("editingPerson",true);
			}
			if (action != null && action.trim().equals("cancelEditing")){
				session.setAttribute("editingPerson",false);
			}
			
		} catch (final Exception ex) {
			ex.printStackTrace();
			this.getServletContext().getRequestDispatcher("/Persons.jsp").forward(request, response);
		}

		this.getServletContext().getRequestDispatcher("/Person.jsp").forward(request, response);
	}
	
	protected final void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8"); // needed in order to allow for most of special printable UTF-8 char like Umlaut in casenote and personnote
		response.setContentType("text/plain; charset=utf-8"); // And just in case we do it as well for the response
		response.setCharacterEncoding("UTF-8");
		
		final HttpSession session = request.getSession(true);

		 String idString = request.getParameter("id");
		if (idString == null) {
			idString = (String)session.getAttribute("Last Case");
			}

		try {

			session.setAttribute("Last Case", idString);
			final Integer id = Integer.parseInt(idString);
			final User loggedUser = UserManagement
					.getCurrentlyLoggedInUser(session);

			
			
			final String comment = request.getParameter("comment");
			final String action = request.getParameter("action");
			
			

			if(action != null && action.equals("add_comment")&& comment  != null && !comment.isEmpty()){
				this.dbInterface.insertComment(id, comment, loggedUser.getUsername(), "person");
				response.setHeader("Refresh", "0");
			}
			
			String selCase = request.getParameter("selectedCase");
			String role = request.getParameter("role");
			if(action != null && action.equals("link_case") && selCase != null && role != null){
				int selidcase = Integer.parseInt(selCase);

				if (!dbInterface.isInvolvedIn(id, selidcase)){
					dbInterface.addInvolvement(selidcase, id, role);
					response.setHeader("Refresh", "0");
				}
				
			}
			
			else if (action != null && action.trim().equals("delete"))
			{
				dbInterface.deletePerson(id);
				response.sendRedirect(request.getRequestURL().toString() + "sOfInterest");return;
			}
			else if (action != null && action.trim().equals("submitEdit"))
			{
				// Edited Person
				session.setAttribute("editingPerson", false);
				
				
				String firstname = request.getParameter("firstname");
				String lastname = request.getParameter("lastname");
				String date = request.getParameter("birthdate");
				
				if (firstname.isEmpty()){
					firstname = "???"; // Unknown value
					
				}
				if (lastname.isEmpty()){
					lastname = "???"; 
					
				}
				
				try{
					java.sql.Date.valueOf(date);
				}catch(IllegalArgumentException e){
					date = "0001-01-01"; // default = unknown value
					
				}
				
				this.dbInterface.updatePerson(id,firstname, lastname, date);
				response.setHeader("Refresh", "0");
			}
			
		} catch (final Exception ex) {
			ex.printStackTrace();
			this.getServletContext().getRequestDispatcher("/Persons.jsp").forward(request, response);
		}

		this.getServletContext().getRequestDispatcher("/Person.jsp").forward(request, response);
	}
	
}
