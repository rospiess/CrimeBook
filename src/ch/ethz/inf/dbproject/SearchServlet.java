package ch.ethz.inf.dbproject;

import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.Conviction;
import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.Person;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class Search
 */
@WebServlet(description = "The search page for cases", urlPatterns = { "/Search" })
public final class SearchServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();
		
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		final HttpSession session = request.getSession(true);
		
		/*******************************************************
		 * Construct a table to present all our search results
		 *******************************************************/
		final BeanTableHelper<Person> ptable = new BeanTableHelper<Person>(
				"persons" 		/* The table html id property */,
				"casesTable" /* The table html class property */,
				Person.class 	/* The class of the objects (rows) that will bedisplayed */
		);


		
		final BeanTableHelper<Conviction> ctable = new BeanTableHelper<Conviction>(
				"conviction" 		/* The table html id property */,
				"casesTable" /* The table html class property */,
				Conviction.class 	/* The class of the objects (rows) that will bedisplayed */
		);

		
		
		final String filter = request.getParameter("filter");

		if (filter != null) {
		
			if(filter.equals("description")) {

				session.setAttribute("results", ptable);
				final String name = request.getParameter("description");				
				ptable.addBeanColumn("Person ID", "idperson");
				ptable.addBeanColumn("First Name", "firstname");
				ptable.addBeanColumn("Last Name", "lastname");
				ptable.addBeanColumn("Date of Birth", "bdate");
				ptable.addLinkColumn(""	/* The header. We will leave it empty */,
						"View Person" 	/* What should be displayed in every row */,
						"Person?id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
						"idperson" 			/* For every case displayed, the ID will be retrieved and will be attached to the url base above */);
				ptable.addObjects(this.dbInterface.searchByName(name));
				
			} else if (filter.equals("category")) {

				session.setAttribute("results", ctable);
				final String name = request.getParameter("category");
				ctable.addBeanColumn("Conviction ID", "idcon");
				ctable.addBeanColumn("Type", "type");
				ctable.addBeanColumn("Start Date", "date");
				ctable.addBeanColumn("End Date", "enddate");
				ctable.addBeanColumn("Case ID", "idcase");
				ctable.addBeanColumn("Convict First Name", "firstname");
				ctable.addBeanColumn("Convict Last Name", "lastname");
				ctable.addObjects(this.dbInterface.searchByCategory(name));
				

			} else if (filter.equals("date")) {

				session.setAttribute("results", ctable);
				ctable.addBeanColumn("Conviction ID", "idcon");
				ctable.addBeanColumn("Type", "type");
				ctable.addBeanColumn("Start Date", "date");
				ctable.addBeanColumn("End Date", "enddate");
				ctable.addBeanColumn("Case ID", "idcase");
				ctable.addBeanColumn("Convict First Name", "firstname");
				ctable.addBeanColumn("Convict Last Name", "lastname");
				ctable.addObjects(this.dbInterface.searchByDate(request.getParameter("date")));
			}			
		}

		// Finally, proceed to the Seaech.jsp page which will render the search results
        this.getServletContext().getRequestDispatcher("/Search.jsp").forward(request, response);	        
	}
}
