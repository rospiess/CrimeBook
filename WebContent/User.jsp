<%@page import="ch.ethz.inf.dbproject.UserServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>


<% 
if ((Boolean) session.getAttribute(UserServlet.SESSION_USER_LOGGED_IN)) {
	// User is logged in. Display the details:
%>
<%if((Boolean)session.getAttribute("ChangingPassword")){ %><font color="#FF0000">
<%=session.getAttribute("FailedChanging")
	%></font>
<form action="User" method="post">
	<input type="hidden" name="action" value="submitchange" />
	<table>
		<tr>
			<th align="left">Old Password</th>
			<td><input type="password" name="old" value="" /></td>
		</tr>
		<tr>
			<th align="left">New Password</th>
			<td><input type="password" name="new" value="" /></td>
		</tr>
		<tr>
			<th align="left">Confirm New Password</th>
			<td><input type="password" name="confirmnew" value="" /></td>
		</tr>
		<tr>
			<th colspan="2">
				<input  type="submit" value="Change Password" />
			</th>
		</tr>
	</table>
	</form>

<%} else if((Boolean)session.getAttribute("AddingPerson")){ %>

<form action="User" method="post">
	<input type="hidden" name="action" value="submitadd" />
	<table>
		<tr>
			<th align="left">First Name</th>
			<td><input type="text" name="firstname" value="" /></td>
		</tr>
		<tr>
			<th align="left">Last Name</th>
			<td><input type="text" name="lastname" value="" /></td>
		</tr>
		<tr>
			<th align="left">Date of Birth (yyyy-mm-dd)</th>
			<td><input type="date" name="date" value="" /></td>
		</tr>
		<tr>
			<th colspan="2">
				<input  type="submit" value="Submit Data" />
			</th>
		</tr>
	</table>
	</form>

<%} else if((Boolean)session.getAttribute("OpeningCase")){ %>
<form action="User" method="post">
	<input type="hidden" name="action" value="submitopen" />
	<table>
		<tr>
			<th align="left">Case Title</th>
			<td><input type="text" name="title" value="" /></td>
		</tr>
		<tr>
			<th align="left">Description</th>
			<td><input type="text" name="description" value="" /></td>
		</tr>
		<tr>
			<th align="left">Date (yyyy-mm-dd)</th>
			<td><input type="date" name="date" value="" /></td>
		</tr>
		<tr>
			<th align="left">Time (hh:mm:ss)</th>
			<td><input type="time" name="time" value="" /></td>
		</tr>
		<tr>
			<th align="left">Street</th>
			<td><input type="text" name="street" value="" /></td>
			<th align="left">StreetNo</th>
			<td><input type="text" name="streetno" value="" /></td>		
		</tr>
		<tr>
			<th align="left">ZipCode</th>
			<td><input type="text" name="zipcode" value="" /></td>	
			<th align="left">City</th>
			<td><input type="text" name="city" value="" /></td>	
			<th align="left">Country</th>
			<td><input type="text" name="country" value="" /></td>	
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
		<tr>
		
			<th colspan="2">
				<input  type="submit" value="Submit Data" />
			</th>
		</tr>
	</table>
	</form>
	<%}else{ %> 
	<h3> <%= session.getAttribute("LatestAction") %> </h3><hr/>  What would you like to do?<br> <br> 
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


<%}
//TODO: Display cases opened by the user

//TODO: Add possibility to create new case (requires a form) 
	
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
	<br> <br> <br>
	<h3>Registration</h3><hr/><font color="#FF0000">
	<%=session.getAttribute("RegistrationStatus")
	%> </font>
	<form action="User" method="get">
	<input type="hidden" name="action" value="register" />
	<table>
		<tr>
			<th align="left">Username</th>
			<td><input type="text" name="regusername" value="" /></td>
		</tr>
		<tr>
			<th align="left">Password</th>
			<td><input type="password" name="regpassword" value="" /></td>
		</tr>
		<tr>
			<th>Confirm Password</th>
			<td><input type="password" name="regpassword2" value="" /></td>
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