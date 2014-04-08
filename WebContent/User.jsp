<%@page import="ch.ethz.inf.dbproject.UserServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>


<% 
if ((Boolean) session.getAttribute(UserServlet.SESSION_USER_LOGGED_IN)) {
	// User is logged in. Display the details:
%>
<%if((Boolean)session.getAttribute("ChangingPassword")){ %>
<h3>Change your Password</h3><hr/>
<font color="#FF0000">
<%=session.getAttribute("FailedChanging")
	%></font>
	
	<form action="User" method="post" style="display:inline">
	<table>

		<tr>
			<th align="left">Old Password</th>
			<td><input type="password" name="old" maxlength="60" value="" /></td>
		</tr>
		<tr>
			<th align="left">New Password</th>
			<td><input type="password" name="new" maxlength="60" value="" /></td>
		</tr>
		<tr>
			<th align="left">Confirm New Password</th>
			<td><input type="password" name="confirmnew" maxlength="60" value="" /></td>
		</tr>

	</table>
	
		<br>
			<input type="hidden" name="action" value="submitchange" />
			<input type="submit" value="Change Password" />
		</form>
		
		

		<form action="User" method="get" style="display:inline; margin-left: 3em;">
			<input type="submit" value="Cancel" />
		</form>	


<%} else if((Boolean)session.getAttribute("AddingPerson")){ %>
<h3>Add a new Person of Interest</h3><hr/>
<form action="User" method="post" style="display:inline">
	<table>
		<tr>
			<th align="left">First Name</th>
			<td><input type="text" name="firstname" maxlength="45" value="" /></td>
		</tr>
		<tr>
			<th align="left">Last Name</th>
			<td><input type="text" name="lastname" maxlength="45" value="" /></td>
		</tr>
		<tr>
			<th align="left">Date of Birth</th>
			<td><input type="date" name="date" value="" /></td>
		</tr>
	</table>
	
		<br>
			<input type="hidden" name="action" value="submitadd" />
			<input type="submit" value="Submit Data" />
		</form>
		
		

		<form action="User" method="get" style="display:inline; margin-left: 3em;">
			<input type="submit" value="Cancel" />
		</form>	

<%} else if((Boolean)session.getAttribute("OpeningCase")){ %>
<h3>Add a new Case</h3><hr/>
<form action="User" method="post" style="display:inline">
	<table>
		<tr>
			<th align="left">Case Title</th>
			<td><input type="text" name="title" maxlength="45" value="" /></td>
		</tr>
		<tr>
			<th align="left">Description</th>
			<td><input type="text" name="description" maxlength="160" value="" /></td>
		</tr>
		<tr>
			<th align="left">Date</th>
			<td><input type="date" name="date" value="" /></td>
		</tr>
		<tr>
			<th align="left">Time</th>
			<td><input type="time" name="time" value="" /></td>
		</tr>
		<tr>
			<th align="left">Street</th>
			<td><input type="text" name="street" maxlength="45" value="" /></td>
			<th align="left">StreetNo</th>
			<td><input type="text" name="streetno" maxlength="9" value="" /></td>		
		</tr>
		<tr>
			<th align="left">ZipCode</th>
			<td><input type="text" name="zipcode" maxlength="9" value="" /></td>	
			<th align="left">City</th>
			<td><input type="text" name="city" maxlength="45" value="" /></td>	
			<th align="left">Country</th>
			<td><input type="text" name="country" maxlength="45" value="" /></td>	
		</tr>
		<tr>
		<th align="left">Category</th>
		<td><input type="hidden" name="cat" value="category" />
		<select name="category">
		<optgroup label="Personal Crimes">
			<option value = "Assault">Assault</option>
			<option value = "Murder">Murder</option>
			<option value = "Kidnapping">Kidnapping</option>
			<option value = "OtherPers">Other personal crimes</option>
		</optgroup>
		<optgroup label="Property Crimes">
			<option value = "Theft">Theft</option>
			<option value = "Fraud">Fraud</option>
			<option value = "Burglary">Burglary</option>
			<option value = "OtherProp">Other property crimes</option>
		</optgroup>
	</select></td>
	</tr>
	</table>
	
		<br>
			<input type="hidden" name="action" value="submitopen" />
			<input type="submit" value="Submit Data" />
		</form>
		
		

		<form action="User" method="get" style="display:inline; margin-left: 3em;">
			<input type="submit" value="Cancel" />
		</form>	
	<%}else{ %> 
	<h2> <%= session.getAttribute("LatestAction") %> </h2><hr/>  What would you like to do?<br> <br> 
<table>
		<tr>
			<th colspan="2">
				<form action="User" method="get">
				<input type="hidden" name="action" value="open" />
					<input  type="submit" value="Open new Case" />
				</form>
			</th>

			<th colspan="2">
				<form action="User" method="get">
				<input type="hidden" name="action" value="add" />
					<input type="submit" value="Add new Person" />
				</form>
			</th>
			
			<th colspan="2">
				<form action="User" method="get">
				<input type="hidden" name="action" value="change" />
					<input type="submit" value="Change Password" />
				</form>
			</th>
			
			<th colspan="2">
				<form action="User" method="get">
				<input type="hidden" name="action" value="logout" />
					<input type="submit" value="Logout" />
				</form>
			</th>
		</tr>
	</table>
<h2>Your Cases</h2>
<%=session.getAttribute("UserCases")
	%>
<%}
//TODO: Display cases opened by the user

	
} else {
	%> 
<h3>Login</h3><hr/><font color="#FF0000">
<%=session.getAttribute("FailedLogin")
	%> </font>
	<form action="User" method="get">
	<input type="hidden" name="action" value="login" />
	<table>
		<tr>
			<th>Username</th>
			<td><input type="text" name="username" maxlength="45" value="" /></td>
		</tr>
		<tr>
			<th>Password</th>
			<td><input type="password" name="password" maxlength="60" value="" /></td>
		</tr>
		<tr>
			<th colspan="2">
				<input type="submit" value="Login" />
			</th>
		</tr>
	</table>
	</form>
	<br> <br> <br>
	<h3>Registration</h3><hr/><font color="#FF0000">
	<%=session.getAttribute("RegistrationStatus")
	%> </font>
	<form action="User" method="get">
	<input type="hidden" name="action" value="register" />
	<table>
		<tr>
			<th align="left">Username</th>
			<td><input type="text" name="regusername" maxlength="45" value="" /></td>
		</tr>
		<tr>
			<th align="left">Password</th>
			<td><input type="password" name="regpassword" maxlength="60" value="" /></td>
		</tr>
		<tr>
			<th>Confirm Password</th>
			<td><input type="password" name="regpassword2" maxlength="60" value="" /></td>
		</tr>
		<tr>
			<th colspan="2">
				<input type="submit" value="Submit" />
			</th>
		</tr>
	</table>
	</form>
<%
}
%>

<%@ include file="Footer.jsp" %>