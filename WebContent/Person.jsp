<%@page import="ch.ethz.inf.dbproject.model.User"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>
<% final User user = (User) session.getAttribute(UserManagement.SESSION_USER); %>

<h1>Person Details</h1>

<%=session.getAttribute("caseTable")%>


<%
if (user != null) {
	// User is logged in. He can add a comment
%>
<br>
	<form action="Person" method="post">
		<input type="hidden" name="action" value="add_comment" />
		<input type="hidden" name="user_id" value="<%= user.getUserid() %>" />
		Add Comment
		<br />
		<textarea rows="4" cols="50" name="comment"></textarea>
		<br />
		<input type="submit" value="Submit" />
	</form>
<%
}
%>
<h1>Comments</h1>
<%=session.getAttribute("commentTable")
%>
<h1>Convictions</h1>
<%=session.getAttribute("convictionTable")
%>
<h1>Involved in following cases</h1>
<%=session.getAttribute("involvedTable")
%>
<%@ include file="Footer.jsp"%>
