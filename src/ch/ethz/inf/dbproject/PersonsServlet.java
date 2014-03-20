package ch.ethz.inf.dbproject;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.Person;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;



@WebServlet(description = "The home page of the project", urlPatterns = { "/PersonsOfInterest" })
public final class PersonsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PersonsServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) 
			throws ServletException, IOException {

		final HttpSession session = request.getSession(true);

		/*******************************************************
		 * Construct a table to present all our results
		 *******************************************************/
		final BeanTableHelper<Person> table = new BeanTableHelper<Person>(
				"persons" 		/* The table html id property */,
				"casesTable" /* The table html class property */,
				Person.class 	/* The class of the objects (rows) that will bedisplayed */
		);


		table.addBeanColumn("Person ID", "idperson");
		table.addBeanColumn("First Name", "firstname");
		table.addBeanColumn("Last Name", "lastname");
		table.addBeanColumn("Date of Birth", "bdate");
		table.addLinkColumn(""	/* The header. We will leave it empty */,
				"View Person" 	/* What should be displayed in every row */,
				"Person?id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
				"idperson"); 
		

		// Pass the table to the session. This will allow the respective jsp page to display the table.
		session.setAttribute("persons", table);

		table.addObjects(this.dbInterface.getAllPersons());


		// Finally, proceed to the Projects.jsp page which will render the Projects
		this.getServletContext().getRequestDispatcher("/Persons.jsp").forward(request, response);
	}
}
