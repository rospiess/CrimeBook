package ch.ethz.inf.dbproject;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.Comment;
import ch.ethz.inf.dbproject.model.Conviction;
import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.model.Person;
import ch.ethz.inf.dbproject.model.User;
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected final void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		final HttpSession session = request.getSession(true);

		String idString = request.getParameter("id");
		if (idString == null) {
			idString = (String) session.getAttribute("Last Case");
		}

		try {

			session.setAttribute("Last Case", idString);
			final Integer id = Integer.parseInt(idString);
			final Case aCase = this.dbInterface.getCaseById(id);
			final List<Comment> clist = this.dbInterface.getCommentsById(id,
					"case");
			final List<Person> plist = this.dbInterface.getSuspectsById(id);
			final List<Person> wlist = this.dbInterface.getWitnessesById(id);
			final List<Conviction> convlist = this.dbInterface.getConvictionsByCaseId(id);
			final User loggedUser = UserManagement
					.getCurrentlyLoggedInUser(session);

			/*******************************************************
			 * Construct a table to present all properties of a case
			 *******************************************************/
			final BeanTableHelper<Case> table = new BeanTableHelper<Case>(
					"case" /* The table html id property */,
					"casesTable" /* The table html class property */, Case.class /*
																				 * The
																				 * class
																				 * of
																				 * the
																				 * objects
																				 * (
																				 * rows
																				 * )
																				 * that
																				 * will
																				 * be
																				 * displayed
																				 */
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

			ctable.addObjects(clist);

			session.setAttribute("commentTable", ctable);

			final BeanTableHelper<Person> ptable = new BeanTableHelper<Person>(
					"person", "casesTable", Person.class);

			ptable.addBeanColumn("Person ID", "idperson");
			ptable.addBeanColumn("First Name", "firstname");
			ptable.addBeanColumn("Last Name", "lastname");
			ptable.addBeanColumn("Date of Birth", "bdate");
			ptable.addLinkColumn("", "View Person", "Person?id=", "idperson");

			ptable.addObjects(plist);

			session.setAttribute("suspectTable", ptable);
			
			final BeanTableHelper<Person> wtable = new BeanTableHelper<Person>(
					"person", "casesTable", Person.class);

			wtable.addBeanColumn("Person ID", "idperson");
			wtable.addBeanColumn("First Name", "firstname");
			wtable.addBeanColumn("Last Name", "lastname");
			wtable.addBeanColumn("Date of Birth", "bdate");
			wtable.addLinkColumn("", "View Person", "Person?id=", "idperson");

			wtable.addObjects(wlist);

			session.setAttribute("witnessTable", wtable);
			
			//
			
			
			final BeanTableHelper<Conviction> convtable = new BeanTableHelper<Conviction>(
					"conviction", "convictionTable", Conviction.class);

			convtable.addBeanColumn("Conviction ID", "idcon");
			convtable.addBeanColumn("Type", "type");
			convtable.addBeanColumn("Begin Date", "date");
			convtable.addBeanColumn("End Date", "enddate");
			convtable.addBeanColumn("Convicted lastname", "lastname");
			convtable.addLinkColumn("", "View conviction", "Case?id=", "idcase");

			convtable.addObjects(convlist);

			session.setAttribute("convictionTable", convtable);
			
			//

			final String comment = request.getParameter("comment");
			final String action = request.getParameter("action");
			if (action != null && action.equals("add_comment")
					&& comment != null && !comment.isEmpty())
				this.dbInterface.insertComment(id, comment,
						loggedUser.getUsername(), "case");

		} catch (final Exception ex) {
			ex.printStackTrace();
			this.getServletContext().getRequestDispatcher("/Cases.jsp")
					.forward(request, response);
		}

		this.getServletContext().getRequestDispatcher("/Case.jsp")
				.forward(request, response);
	}
}
