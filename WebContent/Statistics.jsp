<%@page import="ch.ethz.inf.dbproject.StatisticsServlet"%>
<%@page import="ch.ethz.inf.dbproject.model.User"%>
<%@page import="ch.ethz.inf.dbproject.HomeServlet"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@page import="ch.ethz.inf.dbproject.model.Pair"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<% 
final List<Pair<String,Integer>> pairs = (List<Pair<String,Integer>>) session.getAttribute("myStats");
String categories = "['";
String values = "[";
for (int i = 0; i < pairs.size(); i++){
	categories = categories + pairs.get(i).getL() + "'" + ", '";
	values = values + pairs.get(i).getR() + ", ";
}
categories = categories.substring(0, categories.length()-3) + "]";
values = values.substring(0, values.length()-2) + "]";


%>

<svg width="50%" height="50%" id="piechartid" xmlns="http://www.w3.org/2000/svg" viewbox="-50 -50 400 400">
  <style type="text/css">
    path:hover {
      opacity: 0.8;
    }
  </style>
</svg>
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

function drawArcs(svg_layout, pieData){
    var total = pieData.reduce(function (accu, that) { return that + accu; }, 0);
    var sectorAngleArr = pieData.map(function (v) { return 360 * v / total; });
    
    var categories = toStringArr(<%=categories%>);

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
		legend.innerHTML = categories[i];
		legend.setAttribute('y',parseInt(Math.round(radius + radius*1.35*Math.sin(Math.PI*endAngle/180))));
		legend.setAttribute('x',parseInt(Math.round(radius + radius*1.35*Math.cos(Math.PI*endAngle/180))));
		svg_layout.appendChild(legend);

        var d = "M"+radius+","+radius+"  L" + x1 + "," + y1 + "  A"+radius+","+radius+" 0 " + ((endAngle-startAngle > 180) ? 1 : 0) + ",1 " + x2 + "," + y2 + " z";
        var c = 360;
		var colors = ["rgb(111,173,12)", "rgb(128,183,40)", "rgb(145,194,69)", "rgb(179,213,126)", "rgb(196,223,156)", "rgb(216,238,186)"]
        var arc = makeSVG("path", {d: d, fill: colors[i%colors.length]});
        svg_layout.appendChild(arc);
        arc.onclick = clickHandler; // Optional onclick handler
    }
}

drawArcs(document.getElementById("piechartid"), toPercent(<%=values%>)); // here is the pie chart data
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