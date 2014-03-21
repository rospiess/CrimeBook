<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Search</h1>

<hr/>

<form method="get" action="Search">
<div>
	<input type="hidden" name="filter" value="description" />
	Search By Name:
	<input type="text" name="description" />
	<input type="submit" value="Search" title="Search by Description" />
</div>
</form>

<hr/>

<form method="get" action="Search">
<div>
	<input type="hidden" name="filter" value="category" />
	Search By Type of Conviction:
	<input type="text" name="category" />
	<input type="submit" value="Search" title="Search by Type of Conviction" />
</div>
</form>

<hr/>

<form method="get" action="Search">
<div>
	<input type="hidden" name="filter" value="date" />
	Search By Date of Conviction(yyyy-mm-dd):
	<input type="text" name="date" />
	<input type="submit" value="Search" title="Search by Date of Conviction" />
</div>
</form>

<hr/>

<%= 
session.getAttribute("results") 
%>

<hr/>

<%@ include file="Footer.jsp" %>
