package ch.ethz.inf.dbproject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.User;
import ch.ethz.inf.dbproject.util.UserManagement;

@WebServlet(description = "Page that displays the user login / logout options.", urlPatterns = { "/User" })
public final class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();

	public final static String SESSION_USER_LOGGED_IN = "userLoggedIn";
	public final static String SESSION_USER_DETAILS = "userDetails";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
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
		final User loggedUser = UserManagement
				.getCurrentlyLoggedInUser(session);

		session.setAttribute("FailedLogin", "");
		session.setAttribute("Registration", "");

		if (loggedUser == null) {
			// Not logged in!
			session.setAttribute(SESSION_USER_LOGGED_IN, false);
		} else {
			// Logged in
			session.setAttribute(SESSION_USER_LOGGED_IN, true);
		}

		final String action = request.getParameter("action");
		if (action != null && action.trim().equals("login")//Login
				&& loggedUser == null) {

			final String username = request.getParameter("username");
			// Note: It is really not safe to use HTML get method to send
			// passwords.
			// However for this project, security is not a requirement.
			final String password = request.getParameter("password");
			if (password.equals(dbInterface.getPassword(username))) {
				User u = new User(1, username, password);
				session.setAttribute("FailedLogin", "");
				session.setAttribute(SESSION_USER_LOGGED_IN, true);
				session.setAttribute(SESSION_USER_DETAILS, username);
				session.setAttribute(UserManagement.SESSION_USER, u);
			}
			else //wrong password
				session.setAttribute("FailedLogin", "Invalid Username or Password");

		}
		else if  (action != null && action.trim().equals("logout")//Logout
				&& loggedUser != null) {
			session.setAttribute(SESSION_USER_LOGGED_IN, false);
			session.setAttribute(SESSION_USER_DETAILS, null);
			session.setAttribute(UserManagement.SESSION_USER, null);
		}
		else if (action != null && action.trim().equals("register")//Registration
				&& loggedUser == null) {
			final String username = request.getParameter("regusername");
			final String password = request.getParameter("regpassword");
			final String confirmpassword = request.getParameter("regpassword2");
			if(username.equals(""))
				session.setAttribute("Registration", "Please enter a username");
			else if(password.equals(""))
				session.setAttribute("Registration", "Please enter a password");
			else if(dbInterface.getNameIsTaken(username))
				session.setAttribute("Registration", "Username \""+username +"\" already in use");
			else if(!password.equals(confirmpassword))
				session.setAttribute("Registration", "Passwords have to agree");			
			else
			{
				dbInterface.insertUser(username, password);
				session.setAttribute("Registration", "Registration of \""+ username +"\" was successfull");
			}
			
		}

		// Finally, proceed to the User.jsp page which will renden the profile
		this.getServletContext().getRequestDispatcher("/User.jsp")
				.forward(request, response);

	}

}
