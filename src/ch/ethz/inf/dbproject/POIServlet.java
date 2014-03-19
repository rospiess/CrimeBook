package ch.ethz.inf.dbproject;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.PersonOfInterest;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;



@WebServlet(description = "The home page of the project", urlPatterns = { "/PersonsOfInterest" })
public final class POIServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public POIServlet() {
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
		final BeanTableHelper<PersonOfInterest> table = new BeanTableHelper<PersonOfInterest>(
				"persons" 		/* The table html id property */,
				"personsTable" /* The table html class property */,
				PersonOfInterest.class 	/* The class of the objects (rows) that will bedisplayed */
		);


		table.addBeanColumn("Person ID", "idpoi");
		table.addBeanColumn("First Name", "firstname");
		table.addBeanColumn("Last Name", "lastname");
		table.addBeanColumn("Date of Birth", "bdate");
		

		// Pass the table to the session. This will allow the respective jsp page to display the table.
		session.setAttribute("persons", table);

		table.addObjects(this.dbInterface.getAllPersons());


		// Finally, proceed to the Projects.jsp page which will render the Projects
		this.getServletContext().getRequestDispatcher("/Persons.jsp").forward(request, response);
	}
}
