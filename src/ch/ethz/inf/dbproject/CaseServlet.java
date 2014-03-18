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
import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		final HttpSession session = request.getSession(true);

		final String idString = request.getParameter("id");
		if (idString == null) {
			this.getServletContext().getRequestDispatcher("/Cases").forward(request, response);
		}

		try {

			final Integer id = Integer.parseInt(idString);
			final Case aCase = this.dbInterface.getCaseById(id);

			
			/*******************************************************
			 * Construct a table to present all properties of a case
			 *******************************************************/
			final BeanTableHelper<Case> table = new BeanTableHelper<Case>(
					"cases" 		/* The table html id property */,
					"casesTable" /* The table html class property */,
					Case.class 	/* The class of the objects (rows) that will be displayed */
			);

			// Add columns to the new table

			/*
			 * Column 1: The name of the item (This will probably have to be changed)
			 */
			table.addBeanColumn("Case Description", "description");

			/*
			 * Columns 2 & 3: Some random fields. These should be replaced by i.e. funding progress, or time remaining
			 */
			table.addBeanColumn("Test Field2", "field2");
			table.addBeanColumn("Test Integer Field 3", "field3");

			table.addObject(aCase);
			table.setVertical(true);			

			session.setAttribute("caseTable", table);			
			
		} catch (final Exception ex) {
			ex.printStackTrace();
			this.getServletContext().getRequestDispatcher("/Cases.jsp").forward(request, response);
		}

		this.getServletContext().getRequestDispatcher("/Case.jsp").forward(request, response);
	}
}