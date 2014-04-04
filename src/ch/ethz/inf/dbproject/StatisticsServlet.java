package ch.ethz.inf.dbproject;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.Pair;

/**
 * Servlet implementation class Statistics
 */
@WebServlet(description = "The statitics page of the crime database", urlPatterns = { "/Statistics" })
public final class StatisticsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();
		
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StatisticsServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	final HttpSession session = request.getSession(true);
    	
    	List<Pair<String,Integer>> crimeNbrCat = dbInterface.getStatCategories();
    	session.setAttribute("crimeCatStats", crimeNbrCat);
//    	TODO: implement a getStatPerson function in DatastoreInterface
    	List<Pair<String,Integer>> crimeNbrPer = dbInterface.getStatPersons();//not correct right now
    	session.setAttribute("poiInvStats", crimeNbrPer);
    	this.getServletContext().getRequestDispatcher("/Statistics.jsp").forward(request, response);	        
	}
}