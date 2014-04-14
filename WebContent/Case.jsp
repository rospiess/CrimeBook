<%@ page import="ch.ethz.inf.dbproject.model.User" %>
<%@ page import="ch.ethz.inf.dbproject.model.Case" %>
<%@ page import="ch.ethz.inf.dbproject.model.Conviction" %>
<%@ page import="ch.ethz.inf.dbproject.util.UserManagement" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="Header.jsp" %>
<% final User user = (User) session.getAttribute(UserManagement.SESSION_USER);
final Case caze = (Case) session.getAttribute("CurrentCase"); 
final Conviction con = (Conviction) session.getAttribute("CurrentCon");
%>

<%if ((Boolean)session.getAttribute("EditingCase")){ %>
	<h1>Edit Case Detail</h1>

		<form action="Case" method="post" style="display:inline">
			<table cellpadding = "0" cellspacing = "0" class = "casesTable">
				<tbody>
					<tr>
						<th>Title</th>
						<td><input type="text" name="title" maxlength="45" value="<%= caze.getTitle() %>" /></td>
					</tr>
					<tr>
						<th>Description</th>
						<td><textarea name="descr" cols="40" rows = "4" maxlength="160"/><%= caze.getDescr() %></textarea></td>
					</tr>
					<tr>
						<th>Date</th>
						<td><input type="date" name="date" value="<%= caze.getDateString() %>" /></td>
					</tr>
					<tr>
						<th>Time</th>
						<td><input type="time" name="time" value="<%= caze.getTimeString() %>"/></td>
					</tr>
					<tr>
						<th>Street</th>
						<td><input type="text" name="street" maxlength="45" value="<%= caze.getAddress().getStreet() %>"/> </td>
					</tr>
					<tr>
						<th>Street Number</th>
						<td><input type="text" name="streetno" maxlength="9" value="<%= caze.getAddress().getStreetNoString() %>"/> </td>
					</tr>
					<tr>
						<th>Zip Code</th>
						<td><input type="text" name="zipcode" maxlength="9" value="<%= caze.getAddress().getZipCodeString() %>"/> </td>
					</tr>
					<tr>
						<th>City</th>
						<td><input type="text" name="city" maxlength="45" value="<%= caze.getAddress().getCity() %>"/> </td>
					</tr>
					<tr>
						<th>Country</th>
						<td><input type="text" name="country" maxlength="45" value="<%= caze.getAddress().getCountry() %>"/> </td>
					</tr>
					<tr>
						<th align="left">Category</th>
						<td><input type="hidden" name="cat" value="category" />
							<select name="category">
							<optgroup label="Personal Crimes">
								<option value = "Assault" <%if(caze.getCat().equals("Assault")){ %> selected <%} %>>Assault</option>
								<option value = "Murder" <%if(caze.getCat().equals("Murder")){ %> selected <%} %> >Murder</option>
								<option value = "Kidnapping" <%if(caze.getCat().equals("Kidnapping")){ %> selected <%} %>>Kidnapping</option>
								<option value = "OtherPers" <%if(caze.getCat().equals("OtherPers")){ %> selected <%} %>>Other personal crimes</option>
							</optgroup>
							<optgroup label="Property Crimes">
								<option value = "Theft" <%if(caze.getCat().equals("Theft")){ %> selected <%} %>>Theft</option>
								<option value = "Fraud" <%if(caze.getCat().equals("Fraud")){ %> selected <%} %>>Fraud</option>
								<option value = "Burglary" <%if(caze.getCat().equals("Burglary")){ %> selected <%} %>>Burglary</option>
								<option value = "OtherProp" <%if(caze.getCat().equals("OtherProp")){ %> selected <%} %>>Other property crimes</option>
							</optgroup>
							</select>
						</td>
					</tr>
					
				</tbody>
				<tr>
				
		</table>
				<br>
			<input type="hidden" name="action" value="submitEdit" />
			<input type="submit" value="Submit" />
		</form>
		
		

		<form action="Case" method="get" style="display:inline; margin-left: 3em;">
			<input type="hidden" name="action" value="cancelEdit" />
			<input type="submit" value="Cancel" />
		</form>	


	
<%}else if((Boolean)session.getAttribute("ConvDate")){ %>

	<form action="Case" method="post">
	<input type="hidden" name="action" value="submitdates" />
	<table>
		<tr>
			<th align="left">Start date:</th>
			<td><input type="date" name="begindate" value= "<%=con.getDate() %>" /></td>
		</tr>
		<tr>
			<th align="left">End date:</th>
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
	
	<%= session.getAttribute("caseTable") %>
	
	
	<%if(user != null){
		if(caze.getOpen()){	
	%>
	<table>
		<tr>
		<th colspan="20">
			<form action="Case" method="get">
				<input type="hidden" name="action" value="editDetails" />
				<input type="submit" value="Edit" />
			</form>	
		</th>
		<th colspan="20">
			<form action="Case" method="post">
				<input type="hidden" name="action" value="close" />
				<input onclick="document.location.reload(true)" type="submit" value="Close Case" />
			</form>
		</th>
		<th colspan="20">
			<form action="Case" method="post">
				<input type="hidden" name="action" value="delete" />
				<input type="submit" value="Delete Case" />
			</form>
		</th>
		</tr>
	</table>
	
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
		<h1>Comments</h1>
	<%=session.getAttribute("commentTable")
	
	%>
	<% 
	if (user != null) {
		// User is logged in. He can add a comment
	%>
		<form action="Case" method="post">
			<input type="hidden" name="action" value="add_comment" />
			<input type="hidden" name="user_id" value="<%= user.getUserid() %>" />
			<h2>Add Comment</h2>
			<% String defaultComment = "Enter your annotations to the case here ..."; %>
			<textarea rows="4" cols="50" name="comment" onclick="txta=document.getElementsByTagName('textarea')[0]; if(txta.value=='<%=defaultComment%>'){txta.value='';}" onblur="txta=document.getElementsByTagName('textarea')[0]; if(txta.value==''){txta.value='<%=defaultComment%>';}"><%=defaultComment%></textarea>
			<br />
			<input onclick="document.location.reload(true)" type="submit" value="Submit" />
		</form>
	<%
	}
	%>

	<%if(caze.getOpen()){ %>
	<h1>Suspects</h1>
	<%} else { %>
	<h1>Perpetrators</h1>
	<%} %>
	<%= session.getAttribute("suspectTable") %>
	<h1>Victims</h1>
	<%= session.getAttribute("victimTable") %>
	<h1>Witnesses</h1>
	<%= session.getAttribute("witnessTable") %>
	<%if(!caze.getOpen()){ %>
	<h1>Convictions</h1>
	<%= session.getAttribute("convictionTable") %>
	<%} %>

	<% if (user != null && caze.getOpen()){ %>
		<h2>Add a Person to this case</h2>
		
		<form action="Case" method = "post">
			Add <%= session.getAttribute("personSelect") %> as a 
			<select name="role">
			<optgroup label="Role">
				<option value = "Suspect">Suspect</option>
				<option value = "Witness">Witness</option>
				<option value = "Victim">Victim</option>
			</optgroup>
			</select>
		
		
			<input type="hidden" name="action" value="link_person"/>
			<input type="submit" value = "OK"/>
		</form>
		
	<%} %>
<%} %>

<%@ include file="Footer.jsp"%>
