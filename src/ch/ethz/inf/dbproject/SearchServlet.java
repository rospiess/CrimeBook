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

	public SearchServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		final HttpSession session = request.getSession(true);
		session.setAttribute("results", null);
		this.getServletContext().getRequestDispatcher("/Search.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		final HttpSession session = request.getSession(true);

		final BeanTableHelper<Person> ptable = new BeanTableHelper<Person>("persons", "casesTable", Person.class);

		final BeanTableHelper<Case> table = new BeanTableHelper<Case>("cases", "casesTable", Case.class);

		final BeanTableHelper<Conviction> ctable = new BeanTableHelper<Conviction>("conviction", "casesTable", Conviction.class);

		final String filter = request.getParameter("filter");

		if (filter != null) {

			if (filter.equals("title")) {

				session.setAttribute("results", table);
				final String title = request.getParameter("title");
				table.addBeanColumn("Title", "title");
				table.addBeanColumn("Case Description", "descr");
				table.addBeanColumn("Date", "dateString");
				table.addBeanColumn("Time", "timeString");
				table.addBeanColumn("Location", "loc");
				table.addBeanColumn("Category", "cat");
				table.addBeanColumn("Open", "open");
				table.addLinkColumn("", "View Case", "Case?id=", "idcase");

				table.addObjects(this.dbInterface.searchByTitle(title));

			}

			if (filter.equals("name")) {

				session.setAttribute("results", ptable);
				final String name = request.getParameter("name");
				ptable.addBeanColumn("First Name", "firstname");
				ptable.addBeanColumn("Last Name", "lastname");
				ptable.addBeanColumn("Date of Birth", "bdateString");
				ptable.addLinkColumn("", "View Person", "Person?id=", "idperson");
				
				ptable.addObjects(this.dbInterface.searchByName(name));

			} else if (filter.equals("category")) {

				session.setAttribute("results", ctable);
				final String name = request.getParameter("category");
				ctable.addBeanColumn("Type", "type");
				ctable.addBeanColumn("Start Date", "dateString");
				ctable.addBeanColumn("End Date", "enddateString");
				ctable.addBeanColumn("Case Title", "casetitle");
				ctable.addLinkColumn("", "View Case", "Case?id=", "idcase");
				ctable.addBeanColumn("Convict First Name", "firstname");
				ctable.addBeanColumn("Convict Last Name", "lastname");
				ctable.addLinkColumn("", "View Person", "Person?id=", "idperson");
				
				ctable.addObjects(this.dbInterface.searchByCategory(name));

			} else if (filter.equals("date")) {

				session.setAttribute("results", ctable);
				ctable.addBeanColumn("Type", "type");
				ctable.addBeanColumn("Start Date", "dateString");
				ctable.addBeanColumn("End Date", "enddateString");
				ctable.addBeanColumn("Case Title", "casetitle");
				ctable.addLinkColumn("", "View Case", "Case?id=", "idcase");
				ctable.addBeanColumn("Convict First Name", "firstname");
				ctable.addBeanColumn("Convict Last Name", "lastname");
				ctable.addLinkColumn("", "View Person", "Person?id=", "idperson");
				
				ctable.addObjects(this.dbInterface.searchByDate(request.getParameter("date")));
			}
		}

		this.getServletContext().getRequestDispatcher("/Search.jsp").forward(request, response);

	}
}
