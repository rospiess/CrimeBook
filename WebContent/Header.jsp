<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="ch.ethz.inf.dbproject.model.User"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@page import="ch.ethz.inf.dbproject.UserServlet"%>

<% final User u = (User) session.getAttribute(UserManagement.SESSION_USER); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

	<head>
	    <link href="style.css" rel="stylesheet" type="text/css">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Crimebook</title>
	</head>

	<body>

		<!-- Header -->

		<table id="masterTable" cellpadding="0" cellspacing="0">
			<tr>
				<th id="masterHeader" colspan="2">
					<h1>Crimebook</h1>
					Project by François, Lukas and Robin <br> <br> &nbsp;
					<%
if (u != null) {
	// User is logged in. He can add a comment
%>
					You are currently logged in as 
				<%= session.getAttribute(UserServlet.SESSION_USER_DETAILS) %> <%
}
%>
				</th>

			</tr>
			<tr id="masterContent">

				<td id="masterContentMenu">

					<div class="menuDiv1"></div>
					<div class="menuDiv1"><a href="Home">Home</a></div>
					<div class="menuDiv1"><a href="Cases">All cases</a></div>
					<div class="menuDiv2"><a href="Cases?filter=open">Open</a></div>
					<div class="menuDiv2"><a href="Cases?filter=closed">Closed</a></div>
					<div class="menuDiv2"><a href="Cases?filter=recent">Recent</a></div>
					<div class="menuDiv2"><a href="Cases?filter=oldest">Oldest Unsolved</a></div>
					<div class="menuDiv1">Categories</div>
					<div onmouseover="document.getElementsByClassName('personal subcategories')[0].style.display = 'block'; document.getElementsByClassName('property subcategories')[0].style.display = 'none';">
					<div class="menuDiv2 personal"><a href="Cases?category=personal">Personal Crimes</a></div>
					<div class="personal subcategories">
					<div class="menuDiv3 personal"><a href="Cases?category=assault">Assault</a></div>
					<div class="menuDiv3 personal"><a href="Cases?category=murder">Murder</a></div>
					<div class="menuDiv3 personal"><a href="Cases?category=kidnapping">Kidnapping</a></div>
					<div class="menuDiv3 personal"><a href="Cases?category=otherpers">Other</a></div>
					</div>
					</div>
					<div onmouseover="document.getElementsByClassName('property subcategories')[0].style.display = 'block'; document.getElementsByClassName('personal subcategories')[0].style.display = 'none';">
					<div class="menuDiv2 property"><a href="Cases?category=property">Property Crimes</a></div>
					<div class="property subcategories">
					<div class="menuDiv3 property"><a href="Cases?category=theft">Theft</a></div>
					<div class="menuDiv3 property"><a href="Cases?category=fraud">Fraud</a></div>
					<div class="menuDiv3 property"><a href="Cases?category=burglary">Burglary</a></div>
					<div class="menuDiv3 property"><a href="Cases?category=otherprop">Other</a></div>
					</div>
					</div>
					<div class="menuDiv1"><a href="PersonsOfInterest">Persons of Interest</a></div>
					<div class="menuDiv1"><a href="Search">Search</a></div>
					<div class="menuDiv1"><a href="Statistics">Statistics</a></div>
					<%if(u != null) {%>
					<div class="menuDiv1"><a href="User">User Profile</a></div>
					<%}else  {%>					
					<div class="menuDiv1"><a href="User">Login</a></div>
					<%} %>
				</td>

				<td id="masterContentPlaceholder">

