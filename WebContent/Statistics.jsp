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


<div style="width:30%; border: thin solid; float: left; margin-left: 10px;">
	<h3 style="width: 50%; margin: 0 auto; white-space:nowrap;"> <span>Crimes per year</span> </h3>
	<svg width="400" height="300" viewBox="50 20 350 350">
		<desc>BEI Database Event Tracker</desc>
		<linearGradient id="blueGrad">
		<stop offset="0" style="stop-color: indigo;"></stop>
		<stop offset="25%" style="stop-color: blue"></stop> 
		<stop offset="100%" style="stop-color: white;"></stop>
		</linearGradient> 
		<filter id="drop-shadow"> 
			<feGaussianBlur in="" stdDeviation="5" result="blur"></feGaussianBlur> 
			<feOffset id="depth" in="blur" dx="4" dy="4" result="offsetBlur"></feOffset> 
			<feMerge> <feMergeNode in="offsetBlur"></feMergeNode> 
		<feMergeNode in="SourceGraphic"></feMergeNode> 
		</feMerge>
		</filter> 
		<g transform="scale(0.32)">
		<g> 
		<g filter="url(#drop-shadow)">
		<text x="50" y="35" style="font-size:30; fill : white">
		Database Event Tracker Graph
		</text>
		</g>
		<!-- Now Draw the main X and Y axis -->
		<g style="stroke-width:5; stroke:black">
		<!-- X Axis -->
				<path d="M 40 1100 L 1040 1100 Z"></path>
		</g>
		<!-- Now add some dashes in as a guide -->
		<g filter="url(#drop-shadow)" style="fill:none; stroke:#B0B0B0; stroke-width:2; stroke-dasharray:2 4;text-anchor:end; font-size:15">
			<path d="M 42 1000.1085776330076 L 1040 1000.1085776330076 Z"></path>
		 	<text style="fill:black; stroke:none" x="36" y="1000.1085776330076">276</text>
			<path d="M 42 900.2171552660152 L 1040 900.2171552660152 Z"></path>
		 	<text style="fill:black; stroke:none" x="36" y="900.2171552660152">552</text>
			<path d="M 42 800.3257328990228 L 1040 800.3257328990228 Z"></path>
		 	<text style="fill:black; stroke:none" x="36" y="800.3257328990228">828</text>
			<path d="M 42 700.4343105320304 L 1040 700.4343105320304 Z"></path>
		 	<text style="fill:black; stroke:none" x="36" y="700.4343105320304">1104</text>
			<path d="M 42 600.542888165038 L 1040 600.542888165038 Z"></path>
		 	<text style="fill:black; stroke:none" x="36" y="600.542888165038">1380</text>
			<path d="M 42 500.6514657980456 L 1040 500.6514657980456 Z"></path>
		 	<text style="fill:black; stroke:none" x="36" y="500.6514657980456">1656</text>
			<path d="M 42 400.7600434310532 L 1040 400.7600434310532 Z"></path>
		 	<text style="fill:black; stroke:none" x="36" y="400.7600434310532">1932</text>
			<path d="M 42 300.8686210640608 L 1040 300.8686210640608 Z"></path>
		 	<text style="fill:black; stroke:none" x="36" y="300.8686210640608">2208</text>
			<path d="M 42 200.9771986970684 L 1040 200.9771986970684 Z"></path>
		 	<text style="fill:black; stroke:none" x="36" y="200.9771986970684">2484</text>
			<path d="M 42 101.08577633007599 L 1040 101.08577633007599 Z"></path>
		 	<text style="fill:black; stroke:none" x="36" y="101.08577633007599">2760</text>
		 	<text style="fill:black; stroke:none; font-size: larger;" x="105" y="80">Event Count</text>
			<path d="M 42 100.0 L 1040 100.0 Z"></path>
		</g>
		<g filter="url(#drop-shadow)">
		 	<path d="M 1040 1095 L 1040 100 Z"></path>
		 	<text style="fill:black; stroke:none" x="1030" y="1125">250</text>
		 	<path d="M 520 1095 L 520 100 Z"></path>
		 	<text style="fill:black; stroke:none" x="510" y="1125">125</text>
		 	<path d="M 260 1095 L 260 100 Z"></path>
		 	<text style="fill:black; stroke:none" x="250" y="1125">62</text>
		 	<path d="M 780.0 1095 L 780.0 100 Z"></path>
		 	<text style="fill:black; stroke:none" x="770.0" y="1125">186</text>
		 	<text style="fill:black; stroke:none; font-size: larger;" x="520" y="1140">Reading Count</text>
		</g>
		<polyline filter="url(#drop-shadow)" points=" 44 922.6565327542526, 332 803.6916395222584, 644 449.6199782844734, 760 375.38545059717694, 872 240.4270720231632, 940 173.56496561708286, 1040 150.0," style="stroke:red; stroke-width: 3; fill : none;"></polyline>
		 	<text style="fill:black; stroke:none" x="1040" y="115.0">2763</text>
		</g>
		</g>
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
		if (type == "crimeCatChart") arc.setAttribute('title', Math.round(pieData[i]*100)/100 + " % of the crimes commited are listed under the category " + legendData[i] + ".");
		if (type == "poiInvChart") arc.setAttribute('title', "Person of interest \""+ legendData[i] + "\" accounts for " + Math.round(pieData[i]*100)/100 + " % of the involvements in cases.");
        svg_layout.appendChild(arc);
        if (type == "crimeCatChart") arc.onclick = function(){alert(Math.round(pieData[this.id]*100)/100 + " % of the crimes commited are listed under the category " + legendData[this.id] + ".");}; // Optional onclick handler
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
		SELECT YEAR(endDate) AS year, COUNT(*) AS numberOfCrimes FROM cases, conviction WHERE cases.idCase = conviction.idCase GROUP BY YEAR(conviction.endDate) ORDER BY year DESC;
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