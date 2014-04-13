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
    	
    	List<Pair<String,Integer>> poiInvolv = dbInterface.getStatInvolvements();
    	session.setAttribute("poiInvStats", poiInvolv);
    	
    	List<Pair<Integer,Integer>> crimesAyear = dbInterface.getStatCrimesPerYear();
    	session.setAttribute("crimesAyear", crimesAyear);
    	
    	List<Pair<Integer,Integer>> convictionsAyear = dbInterface.getStatConvictionsPerYear();
    	session.setAttribute("convictionsAyear", convictionsAyear);
    	
    	List<Pair<String,Integer>> crimeNbrSuperCat = dbInterface.getStatSuperCategories();
    	session.setAttribute("crimeSuperCatStats", crimeNbrSuperCat);
    	
    	this.getServletContext().getRequestDispatcher("/Statistics.jsp").forward(request, response);	        
	}
}
