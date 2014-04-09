<%@page import="ch.ethz.inf.dbproject.model.User"%>
<%@page import="ch.ethz.inf.dbproject.model.Person"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@page import = "java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>
<% final User user = (User) session.getAttribute(UserManagement.SESSION_USER); %>
<% final Person person = (Person) session.getAttribute("currentPerson");
Calendar bdate;
String byear,bmonth,bday;
if (person.getBdate() != null){
	bdate = Calendar.getInstance();
	bdate.setTime(person.getBdate());
	byear = Integer.toString(bdate.get(Calendar.YEAR));
	bmonth = Integer.toString(bdate.get(Calendar.MONTH)+1);
	bday = Integer.toString(bdate.get(Calendar.DAY_OF_MONTH));
}
else{
	byear = "????";
	bmonth = "??";
	bday = "??";
}
%>



<%if((Boolean)session.getAttribute("editingPerson")){ %>
	<h1>Edit Person Detail</h1>
	
	<form action="Person" method="post" style="display:inline">	
	<table>
	<tr>
		<th>
			<table cellpadding = "0" cellspacing = "0" class = "personsTable">
				<tbody>
					<tr>
						<th>First Name</th>
						<td><input type="text" name="firstname" maxlength="45" value="<%=person.getFirstname() %>" /></td>
					<tr>
						<th>Last Name</th>
						<td><input type="text" name="lastname" maxlength="45" value="<%=person.getLastname() %>" /></td>
					<tr>
						<th>Date of Birth</th>
						
						<td> 
						Year: <input type = "text" name = "birthyear" size = "4" maxlength="4" value ="<%=byear%>"/> 
						Month: <input type = "text" name = "birthmonth"  size = "2" maxlength="2" value ="<%=bmonth%>"/>
						Day: <input type = "text" name = "birthday"  size = "2" maxlength="2" value ="<%=bday%>"/>
						</td>
					
				</tbody>
			</table>
		
		</th>
	</tr>
	</table>
	
		<br>
			<input type="hidden" name="action" value="submitEdit" />
			<input type="submit" value="Submit" />
		</form>
		
		

		<form action="Person" method="get" style="display:inline; margin-left: 3em;">
			<input type="hidden" name="action" value="cancelEdit" />
			<input type="submit" value="Cancel" />
		</form>	

	
<%}else{ %>

	<h1>Person Details</h1>

	<%=session.getAttribute("personTable")%>
	
	
	
	
	
	<%
	if (user != null) {
		// User is logged in. He can edit
	%>
		<form action="Person" method="get">
			<input type="hidden" name="action" value="editDetails" />
			<input type="submit" value="Edit" />
		</form>	
		<%
	}
	%>
<h1>Comments</h1>
	<%=session.getAttribute("commentTable")
	%>
	<%if (user != null) {
		// User is logged in. He can add a comment
	%>
		<form action="Person" method="post">
			<input type="hidden" name="action" value="add_comment" />
			<input type="hidden" name="user_id" value="<%= user.getUserid() %>" />
			<h2>Add Comment</h2>
			<% String defaultComment = "Enter your comment to this person here ..."; // delete the default text on clicking into the textarea, and only the default text. Restore default text when left empty.%>
				<textarea rows="4" cols="50" name="comment" 
				onclick="txta=document.getElementsByTagName('textarea')[0]; if(txta.value=='<%=defaultComment%>'){txta.value='';}" 
				onblur="txta=document.getElementsByTagName('textarea')[0]; if(txta.value==''){txta.value='<%=defaultComment%>';}"><%=defaultComment%></textarea>
			<br />
			<input type="submit" value="Submit" />
		</form>
	<%
	}
	%>
	<h1>Convictions</h1>
	<%=session.getAttribute("convictionTable")
	%>
	<h1>Involved in following cases</h1>
	<%=session.getAttribute("involvedTable")
	%>
	
	<%if (user != null){
	%>
		<h2>Link to a case</h2>
		
		<form action="Person" method = "post">
			Link to <%=session.getAttribute("caseSelect") %> as a 
			<select name="role">
			<optgroup label="Role">
				<option value = "Suspect">Suspect</option>
				<option value = "Witness">Witness</option>
				<option value = "Victim">Victim</option>
			</optgroup>
			</select>
		
		
			<input type="hidden" name="action" value="link_case"/>
			<input type="submit" value = "OK"/>
		</form>
		
	<%} %>
<%} %>

<%@ include file="Footer.jsp"%>
