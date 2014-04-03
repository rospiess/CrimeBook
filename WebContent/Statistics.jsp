<%@page import="ch.ethz.inf.dbproject.StatisticsServlet"%>
<%@page import="ch.ethz.inf.dbproject.model.User"%>
<%@page import="ch.ethz.inf.dbproject.HomeServlet"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@page import="ch.ethz.inf.dbproject.model.Pair"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<% 
final List<Pair<String,Integer>> pairCrimesCat = (List<Pair<String,Integer>>) session.getAttribute("crimeCatStats");
String categories = "['";
String values = "[";
for (int i = 0; i < pairCrimesCat.size(); i++){
	categories = categories + pairCrimesCat.get(i).getL() + "'" + ", '";
	values = values + pairCrimesCat.get(i).getR() + ", ";
}
categories = categories.substring(0, categories.length()-3) + "]";
values = values.substring(0, values.length()-2) + "]";

final List<Pair<String,Integer>> pairsPOIInv = (List<Pair<String,Integer>>) session.getAttribute("poiInvStats");
String firstname = "['";
String involvements = "[";
for (int i = 0; i < pairsPOIInv.size(); i++){
	firstname = firstname + pairsPOIInv.get(i).getL() + "'" + ", '";
	involvements = involvements + pairsPOIInv.get(i).getR() + ", ";
}
firstname = firstname.substring(0, firstname.length()-3) + "]";
involvements = involvements.substring(0, involvements.length()-2) + "]";


%>
<div style="width:30%; border: thin solid; float: left;">
	<h3 style="width: 50%; margin: 0 auto; white-space:nowrap;"> <span>Percentage of crimes by category</span> </h3>
	<svg width="100%" height="300px" id="crimeCatChart" xmlns="http://www.w3.org/2000/svg" viewbox="-100 -100 400 400">
	  <style type="text/css">
	    path:hover {
	      opacity: 0.8;
	    }
	  </style>
	</svg>
</div>

<div style="width:30%; border: thin solid; float: left; margin-left: 10px;">
	<h3 style="width: 50%; margin: 0 auto; white-space:nowrap;"> <span>Percentage of involvements by POI</span> </h3>
	<svg width="100%" height="300px" id="poiInvChart" xmlns="http://www.w3.org/2000/svg" viewbox="-100 -100 400 400">
	  <style type="text/css">
	    path:hover {
	      opacity: 0.8;
	    }
	  </style>
	</svg>
</div>

<script>

function toPercent(values){
	var sum = values.reduce(function(a, b) { return a + b; }, 0);
	for (var i=0; i<values.length; i++)
        values[i] = values[i]*100/sum;
	return values;
}

function toStringArr(categories){
	return categories;
}

function clickHandler() {
    alert("You clicked the pie chart.");
}

function makeSVG(tag, attrs) {
    var el= document.createElementNS('http://www.w3.org/2000/svg', tag);
    for (var k in attrs)
        if (attrs.hasOwnProperty(k)) el.setAttribute(k, attrs[k]);
    return el;
}

var categories = toStringArr(<%=categories%>);
var firstname = toStringArr(<%=firstname%>);

function drawArcs(svg_layout, pieData, legendData, type){
    var total = pieData.reduce(function (accu, that) { return that + accu; }, 0);
    var sectorAngleArr = pieData.map(function (v) { return 360 * v / total; });

    var startAngle = 0;
    var endAngle = 0;
    for (var i=0; i<sectorAngleArr.length; i++){
        startAngle = endAngle;
        endAngle = startAngle + sectorAngleArr[i];

        var x1,x2,y1,y2 ;
		
		radius = 100;
        x1 = parseInt(Math.round(radius + radius*Math.cos(Math.PI*startAngle/180)));
        y1 = parseInt(Math.round(radius + radius*Math.sin(Math.PI*startAngle/180)));

        x2 = parseInt(Math.round(radius + radius*Math.cos(Math.PI*endAngle/180)));
        y2 = parseInt(Math.round(radius + radius*Math.sin(Math.PI*endAngle/180)));
        
        var legend= document.createElementNS('http://www.w3.org/2000/svg', 'text');
		legend.setAttribute('fill', 'black');
		legend.innerHTML = legendData[i];
		svg_layout.appendChild(legend);
		var tlength = legend.getComputedTextLength();
		legend.setAttribute('y',parseInt(Math.round(radius + radius*1.5*Math.sin(Math.PI*(startAngle+sectorAngleArr[i]/2)/180))));
		legend.setAttribute('x',parseInt(Math.round(radius - tlength/2 + radius*1.5*Math.cos(Math.PI*(startAngle+sectorAngleArr[i]/2)/180))));

        var d = "M"+radius+","+radius+"  L" + x1 + "," + y1 + "  A"+radius+","+radius+" 0 " + ((endAngle-startAngle > 180) ? 1 : 0) + ",1 " + x2 + "," + y2 + " z";
        var c = 360;
		var colors = ["rgb(111,173,12)", "rgb(128,183,40)", "rgb(145,194,69)", "rgb(179,213,126)", "rgb(196,223,156)", "rgb(216,238,186)"]
        var arc = makeSVG("path", {d: d, fill: colors[i%colors.length], id: i});
        svg_layout.appendChild(arc);
        if (type == "crimeCatChart") arc.onclick = function(){alert(Math.round(pieData[this.id]*100)/100 + " % of the crimes commited are listed under the category " + legendData[this.id] + ".");}; // Optional onclick handler
        if (type == "poiInvChart") arc.onclick = function(){alert("Person of interest \""+ legendData[this.id] + "\" accounts for " + Math.round(pieData[this.id]*100)/100 + " % of the involvements in cases." );};
    }
}

drawArcs(document.getElementById("crimeCatChart"), toPercent(<%=values%>), categories, "crimeCatChart"); // here is the pie chart data
drawArcs(document.getElementById("poiInvChart"), toPercent(<%=involvements%>), firstname, "poiInvChart");
</script>

<%
if (true) {
	//
	/*
		# Number of cases per year
		SELECT Title, YEAR(endDate) AS year, COUNT(*) AS numberOfCrimes FROM cases, conviction WHERE cases.idCase = conviction.idCase GROUP BY YEAR(conviction.endDate) ORDER BY year DESC;
		# Number of cases by category
		SELECT Title, CatName, COUNT(*) FROM cases GROUP BY CatName;
		# Number of cases per SuperCat
		SELECT Title, category.SuperCat, COUNT(*) FROM cases, category WHERE cases.CatName = category.CatName GROUP BY category.SuperCat;
		
		#Average of age of POI
		SELECT AVG(TIMESTAMPDIFF(YEAR,DateOfBirth,CURDATE())) FROM personOfInterest;
		
		#Average age of convicted people
		SELECT AVG(TIMESTAMPDIFF(YEAR,DateOfBirth,CURDATE())) FROM personOfInterest AS p, conviction AS c WHERE p.idPersonOfINterest = c.idpersonofinterest;
		#Average age by crime type:
		SELECT AVG(TIMESTAMPDIFF(YEAR,DateOfBirth,CURDATE())) FROM personOfInterest AS p, conviction AS conv, cases WHERE p.idPersonOfINterest = conv.idpersonofinterest and cases.idCase = conv.idCase GROUP BY CatName;
		#Total number of case note and person notes by a user
		SELECT allNotes.UserName, COUNT(*) FROM (
				(
				SELECT u.UserName FROM user AS u, notecase AS nc WHERE u.UserName = nc.UserName
				) UNION ALL (
				SELECT u.UserName FROM user AS u, noteperson AS np WHERE u.UserName =  np.UserName
				)
			) AS allNotes GROUP BY allNotes.UserName;
		
		# Number of involvments by POI and role
		SELECT FirstName, role, COUNT(role) FROM PersonOfInterest AS POI, involved AS inv WHERE POI.idPersonOfInterest = inv.idPerson GROUP BY POI.idPersonOfInterest, role ORDER BY role, FirstName;
		# Simply all involvments by POI
		select FirstName, count(*) from PersonOfInterest as POI, involved as inv WHERE POI.idPersonOfInterest = inv.idPerson GROUP BY POI.idPersonOfInterest;
		
	*/
%>

<%
}
%>

<%@ include file="Footer.jsp" %>