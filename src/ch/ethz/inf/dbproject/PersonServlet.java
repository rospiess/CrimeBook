package ch.ethz.inf.dbproject;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.Conviction;
import ch.ethz.inf.dbproject.model.Comment;
import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.Involved;
import ch.ethz.inf.dbproject.model.Person;
import ch.ethz.inf.dbproject.model.User;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class of Case Details Page
 */
@WebServlet(description = "Displays a specific Person.", urlPatterns = { "/Person" })
public final class PersonServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();

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
			final List<Conviction> conlist = this.dbInterface.getConvictionsById(id);
			final List<Involved> invlist = this.dbInterface.getInvolvedByPersonId(id);
			final User loggedUser = UserManagement
					.getCurrentlyLoggedInUser(session);

			
			/*******************************************************
			 * Construct a table to present all properties of a case
			 *******************************************************/
			final BeanTableHelper<Person> table = new BeanTableHelper<Person>(
					"person" 		/* The table html id property */,
					"casesTable" /* The table html class property */,
					Person.class 	/* The class of the objects (rows) that will be displayed */
			);

			// Add columns to the new table

			
			table.addBeanColumn("Person ID", "idperson");
			table.addBeanColumn("First Name", "firstname");
			table.addBeanColumn("Last Name", "lastname");
			table.addBeanColumn("Date of Birth", "bdate");			

			table.addObject(aPerson);
			table.setVertical(true);			

			session.setAttribute("caseTable", table);	
			
			final BeanTableHelper<Comment> ctable = new BeanTableHelper<Comment>(
					"comment" 		/* The table html id property */,
					"casesTable" /* The table html class property */,
					Comment.class 	/* The class of the objects (rows) that will be displayed */
			);
			
			ctable.addBeanColumn("Note ID", "idnote");
			ctable.addBeanColumn("Text", "comment");
			ctable.addBeanColumn("Submitted by", "username");
			
			ctable.addObjects(comlist);		

			session.setAttribute("commentTable", ctable);
			
			final BeanTableHelper<Conviction> contable = new BeanTableHelper<Conviction>(
					"conviction" 		/* The table html id property */,
					"casesTable" /* The table html class property */,
					Conviction.class 	/* The class of the objects (rows) that will be displayed */
			);
			contable.addBeanColumn("Conviction ID", "idcon");
			contable.addBeanColumn("Type", "type");
			contable.addBeanColumn("Start Date", "date");
			contable.addBeanColumn("End Date", "enddate");
			contable.addBeanColumn("Case ID", "idcase");
			
			contable.addObjects(conlist);	

			session.setAttribute("convictionTable", contable);
			
			
			//Create Involved Table
			final BeanTableHelper<Involved> invtable = new BeanTableHelper<Involved>(
					"involved" 		/* The table html id property */,
					"casesTable" /* The table html class property */,
					Involved.class 	/* The class of the objects (rows) that will be displayed */
			);
			invtable.addBeanColumn("Case", "caseTitle");
			invtable.addBeanColumn("Role", "role");
			
			invtable.addObjects(invlist);	

			session.setAttribute("involvedTable", invtable);
			
			
			
			final String comment = request.getParameter("comment");
			final String action = request.getParameter("action");
			if(action != null && action.equals("add_comment")&& comment  != null && !comment.isEmpty())
				this.dbInterface.insertComment(id, comment, loggedUser.getUsername(), "person");
			
		} catch (final Exception ex) {
			ex.printStackTrace();
			this.getServletContext().getRequestDispatcher("/Persons.jsp").forward(request, response);
		}

		this.getServletContext().getRequestDispatcher("/Person.jsp").forward(request, response);
	}
}
