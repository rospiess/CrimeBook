<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="Header.jsp" %>

<h1>Search</h1>

<hr/>

<form method="get" action="Search">
<div>
	<input type="hidden" name="filter" value="title" />
	Search Case By Title:
	<input type="text" name="title" />
	<input type="submit" value="Search" title="Search by Title" />
</div>
</form>

<hr/>

<form method="get" action="Search">
<div>
	<input type="hidden" name="filter" value="description" />
	Search Person By Name:
	<input type="text" name="description" />
	<input type="submit" value="Search" title="Search by Description" />
</div>
</form>

<hr/>

<form method="get" action="Search">
<div>
	<input type="hidden" name="filter" value="category" />
	<label>Search Conviction By Type:</label>
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
	</select>
	<input type="submit" value="Search" title="Search by Type of Conviction" />
</div>
</form>

<hr/>

<form method="get" action="Search">
<div>
	<input type="hidden" name="filter" value="date" />
	Search Conviction By Date:
	<input type="date" name="date" />
	<input type="submit" value="Search" title="Search by Date of Conviction" />
</div>
</form>

<hr/>
<%if (session.getAttribute("results")!=null){ %>
<%= session.getAttribute("results") %>
<%} %>


<%@ include file="Footer.jsp" %>
