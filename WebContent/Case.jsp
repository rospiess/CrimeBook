<%@page import="ch.ethz.inf.dbproject.model.User"%>
<%@page import="ch.ethz.inf.dbproject.model.Case"%>
<%@page import="ch.ethz.inf.dbproject.model.Conviction"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>
<% final User user = (User) session.getAttribute(UserManagement.SESSION_USER);
final Case caze = (Case) session.getAttribute("CurrentCase"); 
final Conviction con = (Conviction) session.getAttribute("CurrentCon");
%>

<%if((Boolean)session.getAttribute("ConvDate")){ %>

	<form action="Case" method="post">
	<input type="hidden" name="action" value="submitdates" />
	<table>
		<tr>
			<th align="left">Start date (yyyy-mm-dd)</th>
			<td><input type="date" name="begindate" value= "<%=con.getDate() %>" /></td>
		</tr>
		<tr>
			<th align="left">End date (yyyy-mm-dd)</th>
			<td><input type="date" name="enddate" value="<%=con.getEnddate()%>" /></td>
		</tr>
		<tr>
			<th colspan="2">
				<input  type="submit" value="Submit Dates" />
			</th>
		</tr>
	</table>
	</form>

<%}
else
{%>
	
	<h1>Case Details</h1>
	
	<%=session.getAttribute("caseTable")%>
	
	
	<%if(user != null){
		if(caze.getOpen()){
	%><form action="Case" method="post">
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
	<form action="Case" method="post">
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
		<form action="Case" method="post">
			<input type="hidden" name="action" value="add_comment" />
			<input type="hidden" name="user_id" value="<%= user.getUserid() %>" />
			Add Comment
			<br />
			<% String defaultComment = "Enter your annotations to the case here ..."; %>
			<textarea rows="4" cols="50" name="comment" onclick="txta=document.getElementsByTagName('textarea')[0]; if(txta.value=='<%=defaultComment%>'){txta.value='';}" onblur="txta=document.getElementsByTagName('textarea')[0]; if(txta.value==''){txta.value='<%=defaultComment%>';}"><%=defaultComment%></textarea>
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
	<%if(!caze.getOpen()){ %>
	<h1>Convictions</h1>
	<%=session.getAttribute("convictionTable")
	%>
	<%} %>

<%} %>

<%@ include file="Footer.jsp"%>
