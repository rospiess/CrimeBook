package ch.ethz.inf.dbproject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.model.Conviction;
import ch.ethz.inf.dbproject.model.DatastoreInterfaceSimpleDatabase;
import ch.ethz.inf.dbproject.model.Person;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class Search
 */
@WebServlet(description = "The search page for cases", urlPatterns = { "/Search" })
public final class SearchServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private final DatastoreInterfaceSimpleDatabase dbInterface = new DatastoreInterfaceSimpleDatabase();
		
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
		session.setAttribute("results", null);
		
		/*******************************************************
		 * Construct a table to present all our search results
		 *******************************************************/
		final BeanTableHelper<Person> ptable = new BeanTableHelper<Person>(
				"persons" 		/* The table html id property */,
				"casesTable" /* The table html class property */,
				Person.class 	/* The class of the objects (rows) that will bedisplayed */
		);

		final BeanTableHelper<Case> table = new BeanTableHelper<Case>(
				"cases" 		/* The table html id property */,
				"casesTable" /* The table html class property */,
				Case.class 	/* The class of the objects (rows) that will bedisplayed */
		);
		
		final BeanTableHelper<Conviction> ctable = new BeanTableHelper<Conviction>(
				"conviction" 		/* The table html id property */,
				"casesTable" /* The table html class property */,
				Conviction.class 	/* The class of the objects (rows) that will bedisplayed */
		);

		
		
		final String filter = request.getParameter("filter");

		if (filter != null) {
		
			
			if(filter.equals("title")) {

				session.setAttribute("results", table);
				final String title = request.getParameter("title");	

				table.addBeanColumn("Title", "title");
				table.addBeanColumn("Case Description", "descr");
				table.addBeanColumn("Date", "dateString");
				table.addBeanColumn("Time", "timeString");
				table.addBeanColumn("Location", "loc");
				table.addBeanColumn("Category", "cat");
				table.addBeanColumn("Open", "open");
				table.addLinkColumn(""	/* The header. We will leave it empty */,
						"View Case" 	/* What should be displayed in every row */,
						"Case?id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
						"idcase" 			/* For every case displayed, the ID will be retrieved and will be attached to the url base above */);

				table.addObjects(this.dbInterface.searchByTitle(title));
				
			}
			
			if(filter.equals("description")) {

				session.setAttribute("results", ptable);
				final String name = request.getParameter("description");				
				ptable.addBeanColumn("First Name", "firstname");
				ptable.addBeanColumn("Last Name", "lastname");
				ptable.addBeanColumn("Date of Birth", "bdateString");
				ptable.addLinkColumn(""	/* The header. We will leave it empty */,
						"View Person" 	/* What should be displayed in every row */,
						"Person?id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
						"idperson" 			/* For every case displayed, the ID will be retrieved and will be attached to the url base above */);
				ptable.addObjects(this.dbInterface.searchByName(name));
				
			} else if (filter.equals("category")) {

				session.setAttribute("results", ctable);
				final String name = request.getParameter("category");
				ctable.addBeanColumn("Type", "type");
				ctable.addBeanColumn("Start Date", "dateString");
				ctable.addBeanColumn("End Date", "enddateString");
				ctable.addBeanColumn("Case Title", "casetitle");
				ctable.addLinkColumn(""	/* The header. We will leave it empty */,
						"View Case" 	/* What should be displayed in every row */,
						"Case?id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
						"idcase");
				//ctable.addBeanColumn("Person ID", "idperson");
				ctable.addBeanColumn("Convict First Name", "firstname");
				ctable.addBeanColumn("Convict Last Name", "lastname");				 
				ctable.addLinkColumn(""	/* The header. We will leave it empty */,
						"View Person" 	/* What should be displayed in every row */,
						"Person?id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
						"idperson");
				ctable.addObjects(this.dbInterface.searchByCategory(name));
				

			} else if (filter.equals("date")) {

				session.setAttribute("results", ctable);
				ctable.addBeanColumn("Type", "type");
				ctable.addBeanColumn("Start Date", "dateString");
				ctable.addBeanColumn("End Date", "enddateString");
				ctable.addBeanColumn("Case Title", "casetitle");
				ctable.addLinkColumn(""	/* The header. We will leave it empty */,
						"View Case" 	/* What should be displayed in every row */,
						"Case?id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
						"idcase");
				ctable.addBeanColumn("Convict First Name", "firstname");
				ctable.addBeanColumn("Convict Last Name", "lastname");
				ctable.addLinkColumn(""	/* The header. We will leave it empty */,
						"View Person" 	/* What should be displayed in every row */,
						"Person?id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
						"idperson");
				ctable.addObjects(this.dbInterface.searchByDate(request.getParameter("date")));
			}			
		}

		// Finally, proceed to the Seaech.jsp page which will render the search results
        this.getServletContext().getRequestDispatcher("/Search.jsp").forward(request, response);	        
	}
}
