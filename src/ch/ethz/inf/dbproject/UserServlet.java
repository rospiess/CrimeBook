package ch.ethz.inf.dbproject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.Address;
import ch.ethz.inf.dbproject.model.Case;
import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.DatastoreInterfaceSimpleDatabase;
import ch.ethz.inf.dbproject.model.User;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

@WebServlet(description = "Page that displays the user login / logout options.", urlPatterns = { "/User" })
public final class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterfaceSimpleDatabase dbInterface = new DatastoreInterfaceSimpleDatabase();

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
		session.setAttribute("RegistrationStatus", "");
		session.setAttribute("Error", "");
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
					User u = new User(username);
					session.setAttribute(SESSION_USER_LOGGED_IN, true);
					session.setAttribute(SESSION_USER_DETAILS, username);
					session.setAttribute(UserManagement.SESSION_USER, u);
					session.setAttribute("LatestAction", "Authentication successful");

					response.sendRedirect(request.getRequestURL().toString());return;
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
			String s = (String) session.getAttribute("LatestAction");
			if(!(s != null && s.startsWith("Opened")))
			session.setAttribute("LatestAction", "Welcome "+loggedUser.getUsername());
			session.setAttribute(SESSION_USER_LOGGED_IN, true);
			
			final BeanTableHelper<Case> table = new BeanTableHelper<Case>(
					"cases" 		/* The table html id property */,
					"casesTable" /* The table html class property */,
					Case.class 	/* The class of the objects (rows) that will bedisplayed */
			);


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

//			table.addObjects(dbInterface.getCasesByUser(loggedUser.getUsername()));
			session.setAttribute("UserCases",table);


		
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

		request.setCharacterEncoding("UTF-8"); // needed in order to allow for most of special printable UTF-8 char like Umlaut in casenote and personnote
		response.setContentType("text/plain; charset=utf-8"); // And just in case we do it as well for the response
		response.setCharacterEncoding("UTF-8");
		
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
				session.setAttribute("Error", "Incorrect Old Password");
			else if (newpassword.equals(""))
				session.setAttribute("Error",
						"Please enter a new password");
			else if (!newpassword.equals(confirmnew))
				session.setAttribute("Error",
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
			
			String errorlog = "";
			String firstname = request.getParameter("firstname");
			String lastname = request.getParameter("lastname");
			String date = request.getParameter("date");
			
			if (!firstname.isEmpty()){
			if (!lastname.isEmpty()){
			
			try{
				java.sql.Date.valueOf(date);
			}catch(IllegalArgumentException e){
				date = "0001-01-01"; // default = unknown value
				errorlog = errorlog.concat(", date of birth unknown");
			}
			
			session.setAttribute("AddingPerson", false);
			this.dbInterface.addNewPerson(firstname, lastname, date);
			session.setAttribute("LatestAction", "Added new Person successfully"+errorlog);
			}
			else
				session.setAttribute("Error", "Last Name cannot be empty");
			}
			else
			session.setAttribute("Error", "First Name cannot be empty");
		}

		
		else if (action != null && action.trim().equals("submitopen"))// Open new Case
		{
			final String title = request.getParameter("title");
			if (title != ""){
				
				String errorlog = ""; // Error messages for the user
				
				final String descr = request.getParameter("description");
				
				final String catname = request.getParameter("category");
				final String street = request.getParameter("street");
				final String city = request.getParameter("city");
				final String country = request.getParameter("country");
				
				// Handle invalid inputs or null values
				
				String date = request.getParameter("date");
				try{
					java.sql.Date.valueOf(date);
				}catch(IllegalArgumentException e){
					date = null; // default = unknown value
					errorlog = errorlog.concat(", date unkown");
				}
				
				
				String time = request.getParameter("time");
				try{
					if (time != null && time.length() == 5){
						//For browser which only have hh:mm (e.g. Chrome)
						time = time.concat(":00");
					}
					java.sql.Time.valueOf(time);
				}catch(IllegalArgumentException e){
					time = null;
					errorlog = errorlog.concat(", time unkown");
				}
				
				int streetno;
				String t_stno = request.getParameter("streetno");
				if (t_stno != null && t_stno != ""){
					try{
						streetno = Integer.parseInt(t_stno);
					}catch(NumberFormatException e){
						streetno = -1; // Unknown
						errorlog = errorlog.concat(", street No invalid");
					}
				}
				else{
					streetno = -1; // Unknown
					errorlog = errorlog.concat(", street No unkown");
				}
					
				
				int zipcode;
				String t_zip = request.getParameter("zipcode");
				if (t_zip != null && t_zip != ""){
					try{
						zipcode = Integer.parseInt(t_zip);
					}catch(NumberFormatException e){
						zipcode = -1; // Unknown
						errorlog = errorlog.concat(", zip code invalid");
					}
				}
				else{
					zipcode = -1; // Unknown
					errorlog = errorlog.concat(", zip code unkown");
				}
				
				final Address address = new Address(country, city, street, zipcode,
						streetno);
				
				this.dbInterface.openNewCase(title, descr, date, time, address,
						catname, loggedUser.getUsername());
				session.setAttribute("LatestAction", "Opened new Case successfully"+errorlog);
				session.setAttribute("OpeningCase", false);
				response.setHeader("Refresh", "0");
			}
			else				
				session.setAttribute("Error", "Case Title cannot be empty");
		}
		// Finally, proceed to the User.jsp page which will renden the profile
				this.getServletContext().getRequestDispatcher("/User.jsp")
						.forward(request, response);
	}

}
