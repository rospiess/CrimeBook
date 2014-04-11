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
<div style="width:30%; border: thin solid; float: left; text-align: center;">
	<h3 style="margin: 0 auto;">Percentage of crimes by category</h3>
	<svg width="100%" height="300px" id="crimeCatChart" xmlns="http://www.w3.org/2000/svg" viewbox="-100 -100 400 400">
	  <style type="text/css">
	    path:hover {
	      opacity: 0.8;
	    }
	  </style>
	</svg>
</div>

<div style="width:30%; border: thin solid; float: left; margin-left: 10px; text-align: center;">
	<h3 style="margin: 0 auto;">Percentage of involvements by POI</h3>
	<svg width="100%" height="300px" id="poiInvChart" xmlns="http://www.w3.org/2000/svg" viewbox="-100 -100 400 400">
	  <style type="text/css">
	    path:hover {
	      opacity: 0.8;
	    }
	  </style>
	</svg>
</div>


<div style="width:30%; border: thin solid; float: left; margin-left: 10px; text-align: center;">
	<h3 style="margin: 0 auto;">Crimes per year</h3>
	<svg width="100%" height="300px" id="crimeAyearChart" viewBox="150 50 1000 1050">

	</svg>
</div>

<script>

function createLineChart(years, values){
	makeLineChart(document.getElementById("crimeAyearChart"), years, values);
	return false;
}

function makeLineChart(svg_layout, yearData, valuesData, legendData){
	// blur filter
	var filter = makeNodeWithAtt("filter", {id: "drop-shadow"});
	var feGaussianBlur = makeNodeWithAtt("feGaussianBlur", {in: "", stdDeviation: 5, result: "blur"});
	filter.appendChild(feGaussianBlur);
	var feOffset = makeNodeWithAtt("feOffset", {id: "depth", dy: 4, dx: 4, result: "offsetBlur"});
	filter.appendChild(feOffset);
	var feMerge = document.createElementNS('http://www.w3.org/2000/svg', 'feMerge');
	var feMergeNode = makeNodeWithAtt("feMergeNode", {in: "offsetBlur"});
	feMerge.appendChild(feMergeNode);
	var feMergeNode2 = makeNodeWithAtt("feMergeNode", {in: "SourceGraphic"});
	feMerge.appendChild(feMergeNode2);
	filter.appendChild(feMerge);
	svg_layout.appendChild(filter);
	
	// Axis
	var gxAxis = makeNodeWithAtt("g", {style: "stroke-width:3; stroke:black"});
	var xAxis = makeNodeWithAtt("path", {d: "M 105 1005 L 1095 1005 Z"});
	gxAxis.appendChild(xAxis);
	svg_layout.appendChild(gxAxis);
	
	var gHoriz = makeNodeWithAtt("g", {filter: "url(#drop-shadow)", style: "fill:none; stroke:#B0B0B0; stroke-width:2.3; stroke-dasharray:2.3 4.7;"});
	for (var i=0; i<5; i++){
		var horiz = makeNodeWithAtt("path", {d: "M 100 " + (250 + i*150) + " L 1100 " + (250 + i*150) + " Z"});
		gHoriz.appendChild(horiz);
	}
	svg_layout.appendChild(gHoriz);
	
	var gVert = makeNodeWithAtt("g", {style: "fill:none; stroke:#101010; stroke-width:2;"});
	for (var i=0; i<5; i++){
		var vert = makeNodeWithAtt("path", {d: "M " + (100 + i*250) + " 1000 L " + (100 + i*250) + " 980 Z"});
		gVert.appendChild(vert);
	}
	svg_layout.appendChild(gVert);
	
	// Legends
	var gLegend = makeNodeWithAtt("g", {style: "font-size: 250%;"});
	
	var xLabel= makeNodeWithAtt("text", {style: "font-size: 150%;", x: 1180, y: 1000});
	xLabel.innerHTML = "Year";
	gLegend.appendChild(xLabel);
	var yLabel= makeNodeWithAtt("text", {style: "font-size: 150%;", x: 0, y: 110});
	yLabel.innerHTML = "Crimes";
	gLegend.appendChild(yLabel);
	
	for (var i=0; i<5; i++){
		var vLabel = makeNodeWithAtt("text", {x: 45, y: 260 + i*150});
		vLabel.innerHTML = 100 - i * 20;
		gLegend.appendChild(vLabel);
	}
	
	for (var i=0; i<5; i++){
		var hLabel = makeNodeWithAtt("text", {x: 80 + i*250, y: 1050});
		hLabel.innerHTML = 2010 + i;
		gLegend.appendChild(hLabel);
	}
	
	var lastpoint = 150;
	var endValue = makeNodeWithAtt("text", {x: 1150, y: lastpoint});
	endValue.innerHTML = 1100-lastpoint;
	gLegend.appendChild(endValue);
	
	svg_layout.appendChild(gLegend);
	
	// Graph
	var polyline = makeNodeWithAtt("polyline", {filter: "url(#drop-shadow)", points: "100 922, 232 803, 444 449, 560 375, 672 440, 840 323, 1100 250", style: "stroke:red; stroke-width: 3; fill : none;"});
	svg_layout.appendChild(polyline);
	
	return false;
}

function getE(integerValue)
{
    return (integerValue+"").length-1;
}

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

function makeNodeWithAtt(tag, attrs) {
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
        var arc = makeNodeWithAtt("path", {d: d, fill: colors[i%colors.length], id: i});
		if (type == "crimeCatChart") arc.setAttribute('title', Math.round(pieData[i]*100)/100 + " % of the crimes commited are listed under the category " + legendData[i] + ".");
		if (type == "poiInvChart") arc.setAttribute('title', "Person of interest \""+ legendData[i] + "\" accounts for " + Math.round(pieData[i]*100)/100 + " % of the involvements in cases.");
        svg_layout.appendChild(arc);
        if (type == "crimeCatChart") arc.onclick = function(){alert(Math.round(pieData[this.id]*100)/100 + " % of the crimes commited are listed under the category " + legendData[this.id] + ".");}; // Optional onclick handler
    }
}

drawArcs(document.getElementById("crimeCatChart"), toPercent(<%=values%>), categories, "crimeCatChart"); // here is the pie chart data
drawArcs(document.getElementById("poiInvChart"), toPercent(<%=involvements%>), firstname, "poiInvChart");
createLineChart();
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
