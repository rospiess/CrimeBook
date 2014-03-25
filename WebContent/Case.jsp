<%@page import="ch.ethz.inf.dbproject.model.User"%>
<%@page import="ch.ethz.inf.dbproject.model.Case"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>
<% final User user = (User) session.getAttribute(UserManagement.SESSION_USER);
final Case caze = (Case) session.getAttribute("CurrentCase"); %>

<h1>Case Details</h1>

<%=session.getAttribute("caseTable")%>


<%if(user != null){
	if(caze.getOpen()){
%><form action="Case" method="get">
	<input type="hidden" name="action" value="close" />
	<table>
		<tr>
			<th colspan="2">
				<input onclick="document.location.reload(true)" type="submit" value="Close Case" />
			</th>
		</tr>
	</table>
	</form>
<%} else { %>
<form action="Case" method="get">
	<input type="hidden" name="action" value="open" />
	<table>
		<tr>
			<th colspan="2">
				<input onclick="document.location.reload(true)" type="submit" value="Reopen Case" />
			</th>
		</tr>
	</table>
	</form>

<%}} %>
<br>

<% 
if (user != null) {
	// User is logged in. He can add a comment
%> <br>
	<form action="Case" method="get">
		<input type="hidden" name="action" value="add_comment" />
		<input type="hidden" name="user_id" value="<%= user.getUserid() %>" />
		Add Comment
		<br />
		<textarea rows="4" cols="50" name="comment"></textarea>
		<br />
		<input onclick="document.location.reload(true)" type="submit" value="Submit" />
	</form>
<%
}
%>
<h1>Comments</h1>
<%=session.getAttribute("commentTable")

%>
<%if(caze.getOpen()){ %>
<h1>Suspects</h1>
<%} else { %>
<h1>Perpetrators</h1>
<%} %>
<%=session.getAttribute("suspectTable")
%>
<h1>Witnesses</h1>
<%=session.getAttribute("witnessTable")
%>
<h1>Convictions</h1>
<%=session.getAttribute("convictionTable")
%>

<%@ include file="Footer.jsp"%>
