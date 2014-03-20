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
			final List<Comment> clist = this.dbInterface.getCommentsById(id,"case");
			
			/*******************************************************
			 * Construct a table to present all properties of a case
			 *******************************************************/
			final BeanTableHelper<Case> table = new BeanTableHelper<Case>(
					"case" 		/* The table html id property */,
					"casesTable" /* The table html class property */,
					Case.class 	/* The class of the objects (rows) that will be displayed */
			);
			
			table.addBeanColumn("Case ID", "idcase");
			table.addBeanColumn("Title", "title");
			table.addBeanColumn("Case Description", "descr");
			table.addBeanColumn("Date", "date");
			table.addBeanColumn("Time", "time");
			table.addBeanColumn("Location", "loc");
			table.addBeanColumn("Category", "cat");
			table.addBeanColumn("Open", "open");
			
			table.addObject(aCase);
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
			
			ctable.addObjects(clist);		

			session.setAttribute("commentTable", ctable);	
			
		} catch (final Exception ex) {
			ex.printStackTrace();
			this.getServletContext().getRequestDispatcher("/Cases.jsp").forward(request, response);
		}

		this.getServletContext().getRequestDispatcher("/Case.jsp").forward(request, response);
	}
}
