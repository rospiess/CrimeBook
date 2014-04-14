<%@page import="ch.ethz.inf.dbproject.model.User"%>
<%@page import="ch.ethz.inf.dbproject.HomeServlet"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<%
final User user = (User) session.getAttribute(UserManagement.SESSION_USER);

if (user != null) {
	// There is a user logged in! Display a greeting!
%>
	<h2>Welcome back, <%=user.getUsername()%>!</h2>
<%
} else {
	// No user logged in.%>
	<h2>Welcome!</h2>
<%
}
%>

See all available <a href="Cases">cases</a> and <a href="PersonsOfInterest">persons of interest</a>.

<%@ include file="Footer.jsp" %>