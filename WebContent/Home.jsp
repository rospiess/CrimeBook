<%@ page import="ch.ethz.inf.dbproject.model.User" %>
<%@ page import="ch.ethz.inf.dbproject.HomeServlet" %>
<%@ page import="ch.ethz.inf.dbproject.util.UserManagement" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="Header.jsp" %>
<div style="width: 101.5%; height: 104%; margin: -26px 0px -10px -10px; background-clip: padding-box; background-image: url('./ETH_image.png'); background-size: auto 100%; text-shadow:3px 3px 10px #FFFFFF, 3px 3px 5px #FFFFFF,0px 0px 5px #FFFFFF;">
<div style="text-align: center;">
<%
final User user = (User) session.getAttribute(UserManagement.SESSION_USER);

if (user != null) {
	// There is a user logged in! Display a greeting!
%>
	<h2><br><br>Welcome back, <%=user.getUsername()%>!</h2>
<%
} else {
	// No user logged in.%>
	<h2><br><br>Welcome!</h2>
<%
}
%>

<h3>See all available <a href="Cases">cases</a> and <a href="PersonsOfInterest">persons of interest</a>.</h3>
</div>
</div>
<%@ include file="Footer.jsp" %>