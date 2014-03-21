<%@page import="ch.ethz.inf.dbproject.UserServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h2>Your Account</h2>

<% 
if ((Boolean) session.getAttribute(UserServlet.SESSION_USER_LOGGED_IN)) {
	// User is logged in. Display the details:
%>
	
<%= session.getAttribute(UserServlet.SESSION_USER_DETAILS) %>
	


<%
//TODO: Display cases opened by the user

//TODO: Add possibility to create new case (requires a form) 
	
} else {
	// User not logged in. Display the login form.
%>

	<form action="User" method="get">
	<input type="hidden" name="action" value="login" />
	<table>
		<tr>
			<th>Username</th>
			<td><input type="text" name="username" value="" /></td>
		</tr>
		<tr>
			<th>Password</th>
			<td><input type="password" name="password" value="" /></td>
		</tr>
		<tr>
			<th colspan="2">
				<input type="submit" value="Login" />
			</th>
		</tr>
	</table>
	</form>

<%
}
%>

<%@ include file="Footer.jsp" %>