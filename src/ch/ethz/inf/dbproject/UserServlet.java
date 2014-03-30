package ch.ethz.inf.dbproject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.Address;
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
		session.setAttribute("FailedChanging", "");
		session.setAttribute("RegistrationStatus", "");
		session.setAttribute("OpeningCase", false);
		session.setAttribute("ChangingPassword", false);
		session.setAttribute("AddingPerson", false);
		final String action = request.getParameter("action");
		
		if (loggedUser == null) {
			// Not logged in!
			session.setAttribute(SESSION_USER_LOGGED_IN, false);
			
			if (action != null && action.trim().equals("login"))// Login
			 {

				final String username = request.getParameter("username");
				// Note: It is really not safe to use HTML get method to send
				// passwords.
				// However for this project, security is not a requirement.
				final String password = request.getParameter("password");
				if (password.equals(dbInterface.getPassword(username))) {
					User u = new User(1, username, password);
					session.setAttribute(SESSION_USER_LOGGED_IN, true);
					session.setAttribute(SESSION_USER_DETAILS, username);
					session.setAttribute(UserManagement.SESSION_USER, u);
					session.setAttribute("LatestAction", "Authentication successful");
				} else
					// wrong password
					session.setAttribute("FailedLogin",
							"Invalid Username or Password");

			}
			
			else if (action != null && action.trim().equals("register"))// Registration
			{
				final String username = request.getParameter("regusername");
				final String password = request.getParameter("regpassword");
				final String confirmpassword = request.getParameter("regpassword2");
			if (username.equals(""))
			session.setAttribute("RegistrationStatus",
					"Please enter a username");
			else if (password.equals(""))
				session.setAttribute("RegistrationStatus",
					"Please enter a password");
			else if (dbInterface.getNameIsTaken(username))
				session.setAttribute("RegistrationStatus", "Username \""
					+ username + "\" already in use");
			else if (!password.equals(confirmpassword))
				session.setAttribute("RegistrationStatus",
					"Passwords have to agree");
			else {
				dbInterface.insertUser(username, password);
				session.setAttribute("RegistrationStatus", "Registration of \""
					+ username + "\" was successful");
				}
			}
		} else {
			// Logged in
			session.setAttribute("LatestAction", "Welcome "+loggedUser.getUsername());
			session.setAttribute(SESSION_USER_LOGGED_IN, true);

		
		 if (action != null && action.trim().equals("logout")// Logout
				&& loggedUser != null) {
			session.setAttribute(SESSION_USER_LOGGED_IN, false);
			session.setAttribute(SESSION_USER_DETAILS, null);
			session.setAttribute(UserManagement.SESSION_USER, null);
		}
		else if (action != null && action.trim().equals("change"))// Switch toChanging
			session.setAttribute("ChangingPassword", true);
		
		else if (action != null && action.trim().equals("add"))// Switch to Adding
			session.setAttribute("AddingPerson", true);
		
		else if (action != null && action.trim().equals("open"))// Switch to Opening
			session.setAttribute("OpeningCase", true);
		
		}
		// Finally, proceed to the User.jsp page which will renden the profile
		this.getServletContext().getRequestDispatcher("/User.jsp")
				.forward(request, response);
		

	}
	
	protected final void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		final HttpSession session = request.getSession(true);
		final User loggedUser = UserManagement
				.getCurrentlyLoggedInUser(session);

		final String action = request.getParameter("action");
		
		if (action != null && action.trim().equals("submitchange"))// Change Password
		{
			final String old = request.getParameter("old");
			final String newpassword = request.getParameter("new");
			final String confirmnew = request.getParameter("confirmnew");
			if (!old.equals(dbInterface.getPassword(loggedUser.getUsername())))
				session.setAttribute("FailedChanging", "Incorrect Old Password");
			else if (newpassword.equals(""))
				session.setAttribute("FailedChanging",
						"Please enter a new password");
			else if (!newpassword.equals(confirmnew))
				session.setAttribute("FailedChanging",
						"Passwords have to agree");
			else
			{
				this.dbInterface.changePassword(loggedUser.getUsername(), newpassword);
				session.setAttribute("ChangingPassword", false);
				session.setAttribute("LatestAction", "Changed Password successfully");
			}
		}
		
		else if (action != null && action.trim().equals("submitadd"))// Add new Person
		{
			session.setAttribute("AddingPerson", false);
			final String firstname = request.getParameter("firstname");
			final String lastname = request.getParameter("lastname");
			final String date = request.getParameter("date");
			
			this.dbInterface.addNewPerson(firstname, lastname, date);
			session.setAttribute("LatestAction", "Added new Person successfully");
		}

		
		else if (action != null && action.trim().equals("submitopen"))// Open new Case
		{
			session.setAttribute("OpeningCase", false);
			final String title = request.getParameter("title");
			final String descr = request.getParameter("description");
			final String date = request.getParameter("date");
			final String time = request.getParameter("time");
			final String catname = request.getParameter("category");
			final String street = request.getParameter("street");
			final String city = request.getParameter("city");
			final String country = request.getParameter("country");
			final int streetno = Integer.parseInt(request
					.getParameter("streetno"));
			final int zipcode = Integer.parseInt(request
					.getParameter("zipcode"));
			final Address address = new Address(country, city, street, zipcode,
					streetno);
			
			this.dbInterface.openNewCase(title, descr, date, time, address,
					catname);
			session.setAttribute("LatestAction", "Opened new Case successfully");
		}
		// Finally, proceed to the User.jsp page which will renden the profile
				this.getServletContext().getRequestDispatcher("/User.jsp")
						.forward(request, response);
	}

}
