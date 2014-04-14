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

final List<Pair<Integer,Integer>> pairsCrimesYear = (List<Pair<Integer,Integer>>) session.getAttribute("crimesAyear");
String years = "[";
String crimes = "[";
for (int i = 0; i < pairsCrimesYear.size(); i++){
	years = years + pairsCrimesYear.get(i).getL() + ", ";
	crimes = crimes + pairsCrimesYear.get(i).getR() + ", ";
}
years = years.substring(0, years.length()-2) + "]";
crimes = crimes.substring(0, crimes.length()-2) + "]";

final List<Pair<Integer,Integer>> pairsConvictionsYear = (List<Pair<Integer,Integer>>) session.getAttribute("convictionsAyear");
String yearsConv = "[";
String convictions = "[";
for (int i = 0; i < pairsConvictionsYear.size(); i++){
	yearsConv = yearsConv + pairsConvictionsYear.get(i).getL() + ", ";
	convictions = convictions + pairsConvictionsYear.get(i).getR() + ", ";
}
yearsConv = yearsConv.substring(0, yearsConv.length()-2) + "]";
convictions = convictions.substring(0, convictions.length()-2) + "]";

final List<Pair<String,Integer>> pairSuperCrimesCat = (List<Pair<String,Integer>>) session.getAttribute("crimeSuperCatStats");
String categoriesSuper = "['";
String valuesSuper = "[";
for (int i = 0; i < pairSuperCrimesCat.size(); i++){
	categoriesSuper = categoriesSuper + pairSuperCrimesCat.get(i).getL() + "'" + ", '";
	valuesSuper = valuesSuper + pairSuperCrimesCat.get(i).getR() + ", ";
}
categoriesSuper = categoriesSuper.substring(0, categoriesSuper.length()-3) + "]";
valuesSuper = valuesSuper.substring(0, valuesSuper.length()-2) + "]";

final List<Pair<String,Integer>> pairAgesCat = (List<Pair<String,Integer>>) session.getAttribute("people_average_ages");
String categoriesAges = "['";
String averageAges = "[";
for (int i = 0; i < pairAgesCat.size(); i++){
	categoriesAges = categoriesAges + pairAgesCat.get(i).getL() + "'" + ", '";
	averageAges = averageAges + pairAgesCat.get(i).getR() + ", ";
}
categoriesAges = categoriesAges.substring(0, categoriesAges.length()-3) + "]";
averageAges = averageAges.substring(0, averageAges.length()-2) + "]";
%>

<div id="overlayCont" style="text-align: center;">
	<div id="overlay" style="position:absolute; left:220px; top:115px; width: 100%; height: 100%; background-color: black; opacity:0.8; display: none;" onclick="this.parentNode.style.display = 'none'">
	</div>
	<div id="zoomed" style="position: absolute; left: 20%; width: 75%; height: 75%; float: center; opacity:1; background-color: white; display: none;" onclick="this.parentNode.style.display = 'none'">
	</div>
</div>

<div id="crimeCat" style="width:30%; height: 320px; border: thin solid; float: left; text-align: center;"  onclick="zoom('crimeCat');">
	<h3 style="margin: 0 auto;">Percentage of crimes by category</h3>
	<svg width="100%" height="300px" id="crimeCatChart" xmlns="http://www.w3.org/2000/svg" viewbox="-100 -100 400 400">
	  <style type="text/css">
	    path:hover {
	      opacity: 0.8;
	    }
	  </style>
	</svg>
</div>

<div id="poiInv" style="width:30%; height: 320px; border: thin solid; float: left; margin-left: 10px; text-align: center;"  onclick="zoom('poiInv');">
	<h3 style="margin: 0 auto;">Percentage of involvements by POI</h3>
	<svg width="100%" height="300px" id="poiInvChart" xmlns="http://www.w3.org/2000/svg" viewbox="-100 -100 400 400">
	  <style type="text/css">
	    path:hover {
	      opacity: 0.8;
	    }
	  </style>
	</svg>
</div>

<div id="crimeAyear" style="width:30%; height: 320px; border: thin solid; float: left; margin-left: 10px; text-align: center;" onclick="zoom('crimeAyear');">
	<h3 style="margin: 0 auto;">Crimes per year</h3>
	<svg width="100%" height="300px" id="crimeAyearChart" viewBox="-40 80 1400 1000">

	</svg>
</div>

<div id="convictionAyear" style="width:30%; height: 320px; border: thin solid; float: left; margin-top: 10px; text-align: center;" onclick="zoom('convictionAyear');">
	<h3 style="margin: 0 auto;">Convictions per year</h3>
	<svg width="100%" height="300px" id="convictionAyearChart" viewBox="-40 80 1400 1000">

	</svg>
</div>

<div id="crimeSuperCat" style="width:30%; height: 320px; border: thin solid; float: left;  margin-top: 10px;  margin-left: 10px;  text-align: center;"  onclick="zoom('crimeSuperCat');">
	<h3 style="margin: 0 auto;">Personal vs property crimes by percents</h3>
	<svg width="100%" height="300px" id="crimeSuperCatChart" xmlns="http://www.w3.org/2000/svg" viewbox="-100 -100 400 400">
	  <style type="text/css">
	    path:hover {
	      opacity: 0.8;
	    }
	  </style>
	</svg>
</div>

<div id="averageAges" style="width:30%; height: 320px; border: thin solid; float: left; margin-top: 10px; margin-left: 10px; text-align: center;" onclick="zoom('averageAges');">
	<h3 style="margin: 0 auto;">Average ages by person categories</h3>
	<svg width="100%" height="300px" id="averageAgesChart" viewBox="-40 80 1400 1000">

	</svg>
</div>

<script>

var crimes = (<%=crimes%>);
var years = (<%=years%>);

var convictions = (<%=convictions%>);
var yearsConv = (<%=yearsConv%>);

var colors = ["rgb(111,173,12)", "rgb(128,183,40)", "rgb(145,194,69)", "rgb(179,213,126)", "rgb(196,223,156)", "rgb(216,238,186)"];

function zoom(divTozoom){
	var htmlCode = document.getElementById(divTozoom).innerHTML;
	htmlCode = htmlCode.slice(0);
	htmlCode = htmlCode.replace("height=\"300px\"", "height=\"90%\"");
	htmlCode = htmlCode.replace("viewBox=\"-40 80", "viewBox=\"0 50"); // resituate the svg when zoomed
	htmlCode = htmlCode.replace(/id=\".*?\"/g, ''); // regexp: delete all id's in the copied innerHTML to avoid rendering conflicts
	document.getElementById('zoomed').innerHTML = htmlCode; // the the innerHTML of the zoomed svg
	document.getElementById('overlayCont').style.display = 'block'; // display the needed overlay divs
	document.getElementById('zoomed').style.display = 'block';
	document.getElementById('overlay').style.display = 'block';
}

function createLineChart(){
	makeLineChart(document.getElementById("crimeAyearChart"), years, crimes, ["Crimes", "Years"]);
	makeLineChart(document.getElementById("convictionAyearChart"), yearsConv, convictions, ["Convictions", "Years"]);
	return false;
}

function makeLineChart(svg_layout, xAxisData, valuesData, legendData){
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
	xLabel.textContent = legendData[1];
	gLegend.appendChild(xLabel);
	var yLabel= makeNodeWithAtt("text", {style: "font-size: 150%;", x: 0, y: 110});
	yLabel.textContent = legendData[0];
	gLegend.appendChild(yLabel);
	
	
	var sorted = valuesData.slice(0).sort();
	var maxV = sorted[sorted.length-1];
	var maxL;
	var min = sorted[0];
	var step = Math.ceil(maxV/5);
	for (var i=0; i<5; i++){
		var vLabel = makeNodeWithAtt("text", {x: 45, y: 860 - i*150});
		maxL = (i+1)*step;
		vLabel.textContent = maxL;
		gLegend.appendChild(vLabel);
	}
	
	for (var i=0; i<xAxisData.length; i++){
		var hLabel = makeNodeWithAtt("text", {x: 80 + i*250, y: 1050});
		hLabel.textContent = xAxisData[xAxisData.length-1-i];
		gLegend.appendChild(hLabel);
	}
	
	// coords
	var unit = 750/maxL;
	// legend: value of last point
	var lastpoint = (1000 - valuesData[0]*unit);
	var endValue = makeNodeWithAtt("text", {x: 1150, y: lastpoint});
	endValue.textContent = valuesData[0];
	gLegend.appendChild(endValue);
	
	svg_layout.appendChild(gLegend);
	
	// Graph
	var coords = "100 ?.?, 350 ?.?, 600 ?.?, 850 ?.?, 1100 ?.?";
	for (var i=0; i<valuesData.length; i++){
		coords = coords.replace("?.?",(1000 - valuesData[valuesData.length-1-i]*unit));
	}
	var polyline = makeNodeWithAtt("polyline", {filter: "url(#drop-shadow)", points: coords, style: "stroke:red; stroke-width: 3; fill : none;"});
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

function makeNodeWithAtt(tag, attrs) {
    var el= document.createElementNS('http://www.w3.org/2000/svg', tag);
    for (var k in attrs)
        if (attrs.hasOwnProperty(k)) el.setAttribute(k, attrs[k]);
    return el;
}

var categories = (<%=categories%>);
var firstname = (<%=firstname%>);

function drawArcs(svg_layout, pieData, legendData, type){
    var total = pieData.reduce(function (accu, that) { return that + accu; }, 0); // The reduce() method applies a function against an accumulator and each value of the array (from left-to-right) has to reduce it to a single value.
    var sectorAngleArr = pieData.map(function (v) { return 360 * v / total; }); //The map() method creates a new array with the results of calling a provided function on every element in this array.

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
		legend.textContent = legendData[i]; // legend.innerHTML works totaly fine, but not so on funny Chrome ;-]
		svg_layout.appendChild(legend);
		var tlength = legend.getComputedTextLength();
		legend.setAttribute('y',parseInt(Math.round(radius + radius*1.5*Math.sin(Math.PI*(startAngle+sectorAngleArr[i]/2)/180))));
		legend.setAttribute('x',parseInt(Math.round(radius - tlength/2 + radius*1.5*Math.cos(Math.PI*(startAngle+sectorAngleArr[i]/2)/180))));

        var d = "M"+radius+","+radius+"  L" + x1 + "," + y1 + "  A"+radius+","+radius+" 0 " + ((endAngle-startAngle > 180) ? 1 : 0) + ",1 " + x2 + "," + y2 + " z";
        var c = 360;
        var arc = makeNodeWithAtt("path", {d: d, fill: colors[i%colors.length], id: i});
		if (type == "crimeCatChart") arc.setAttribute('title', Math.round(pieData[i]*100)/100 + " % of the crimes commited are listed under the category " + legendData[i] + ".");
		if (type == "poiInvChart") arc.setAttribute('title', "Person of interest \""+ legendData[i] + "\" accounts for " + Math.round(pieData[i]*100)/100 + " % of the involvements in cases.");
		var tooltip= document.createElementNS('http://www.w3.org/2000/svg', 'title');
		if (type == "crimeCatChart") tooltip.textContent = Math.round(pieData[i]*100)/100 + " % of the crimes commited are listed under the category " + legendData[i] + ".";
		if (type == "poiInvChart") tooltip.textContent = "Person of interest \""+ legendData[i] + "\" accounts for " + Math.round(pieData[i]*100)/100 + " % of the involvements in cases.";
		if (type == "crimeSuperCatChart") tooltip.textContent = Math.round(pieData[i]*100)/100 + " % of the crimes commited are listed under the  super category " + legendData[i] + ".";
		arc.appendChild(tooltip);
		svg_layout.appendChild(arc);
        if (type == "crimeCatChart") arc.onclick = function(){alert(Math.round(pieData[this.id]*100)/100 + " % of the crimes commited are listed under the category " + legendData[this.id] + ".");}; // alternative onclick handler as chrome doesn't support tooltip on svg elements
        if (type == "poiInvChart") arc.onclick = function(){alert("Person of interest \""+ legendData[this.id] + "\" accounts for " + Math.round(pieData[this.id]*100)/100 + " % of the involvements in cases.");};
    }
}

function barChart(svg_layout, xAxisData, barData, legendData, type){
	// Axes
	var gxAxis = makeNodeWithAtt("g", {style: "stroke-width:3; stroke:black"});
	var xAxis = makeNodeWithAtt("path", {d: "M 105 1000 L 1095 1000 Z"});
	gxAxis.appendChild(xAxis);
	svg_layout.appendChild(gxAxis);
	
	// Guiding lines
	var gHoriz = makeNodeWithAtt("g", {filter: "url(#drop-shadow)", style: "fill:none; stroke:#B0B0B0; stroke-width:2.3; stroke-dasharray:2.3 4.7;"});
	for (var i=0; i<5; i++){
		var horiz = makeNodeWithAtt("path", {d: "M 100 " + (250 + i*150) + " L 1100 " + (250 + i*150) + " Z"});
		gHoriz.appendChild(horiz);
	}
	svg_layout.appendChild(gHoriz);
	
	// Legends
	var gLegend = makeNodeWithAtt("g", {style: "font-size: 250%;"});
	
	var xLabel= makeNodeWithAtt("text", {x: 1180, y: 1000});
	xLabel.textContent = legendData[1];
	gLegend.appendChild(xLabel);
	var yLabel= makeNodeWithAtt("text", {style: "font-size: 150%;", x: 0, y: 110});
	yLabel.textContent = legendData[0];
	gLegend.appendChild(yLabel);
	
	var sorted = barData.slice(0).sort();
	var maxV = sorted[sorted.length-1];
	var maxL;
	var min = sorted[0];
	var step = Math.ceil(maxV/5);
	for (var i=0; i<5; i++){
		var vLabel = makeNodeWithAtt("text", {x: 45, y: 860 - i*150});
		maxL = (i+1)*step;
		vLabel.textContent = maxL;
		gLegend.appendChild(vLabel);
	}
	
	var unit = 750/maxL;
	
	// Text labels
	for (var i=0; i<xAxisData.length; i++){
		var hLabel = makeNodeWithAtt("text", {'text-anchor': 'middle', x: 190 + i*210, y: 1050});
		hLabel.textContent = xAxisData[xAxisData.length-1-i];
		gLegend.appendChild(hLabel);
	}
	
	svg_layout.appendChild(gLegend);
	
	for (var i=0; i<barData.length; i++){
		var bar = makeNodeWithAtt("rect", {x: 150 + i*210, y: 1000 - barData[barData.length-1-i]*unit, width: 80, height: barData[barData.length-1-i]*unit, fill: colors[(colors.length-i)%colors.length]});
		var tooltip= document.createElementNS('http://www.w3.org/2000/svg', 'title');
		if (type == "averageAgesChart") tooltip.textContent = "The average age of people listed under \""+ xAxisData[i] + "\" is " + Math.round(barData[barData.length-1-i]*100)/100 + " years.";
		bar.appendChild(tooltip);
		svg_layout.appendChild(bar);
	} 
	
}

drawArcs(document.getElementById("crimeCatChart"), toPercent(<%=values%>), categories, "crimeCatChart");
drawArcs(document.getElementById("poiInvChart"), toPercent(<%=involvements%>), firstname, "poiInvChart");
drawArcs(document.getElementById("crimeSuperCatChart"), toPercent(<%=valuesSuper%>), <%=categoriesSuper%>, "crimeSuperCatChart");
createLineChart();
barChart(document.getElementById("averageAgesChart"), <%=categoriesAges%>, <%=averageAges%>, ["Age", "Categories"], "averageAgesChart");
</script>

<%
// ideas for additional queries
	/*
	
		# Would have been nice, but hard to visualize: 
		#Number of involvments by grouped by role and then by POI
		SELECT role, CONCAT(CONCAT(FirstName, ' '), LastName), COUNT(role) FROM PersonOfInterest AS POI, involved AS inv WHERE POI.idPersonOfInterest = inv.idPerson GROUP BY POI.idPersonOfInterest, role ORDER BY role, FirstName;
		

#Total number of case note and person notes by a user
SELECT allNotes.UserName, COUNT(*) FROM (
		(
		SELECT u.UserName FROM user AS u, notecase AS nc WHERE u.UserName = nc.UserName
		) UNION ALL (
		SELECT u.UserName FROM user AS u, noteperson AS np WHERE u.UserName =  np.UserName
		)
	) AS allNotes GROUP BY allNotes.UserName;

# Simply all involvments by POI
SELECT FirstName, LastName, COUNT(*) AS amount FROM PersonOfInterest AS POI, involved AS inv WHERE POI.idPersonOfInterest = inv.idPerson GROUP BY POI.idPersonOfInterest;
select FirstName, count(*) from PersonOfInterest as POI, involved as inv WHERE POI.idPersonOfInterest = inv.idPerson GROUP BY POI.idPersonOfInterest;

		# Number of cases per year
		SELECT YEAR(endDate) AS year, COUNT(*) AS numberOfCrimes FROM cases, conviction WHERE cases.idCase = conviction.idCase GROUP BY YEAR(conviction.endDate) ORDER BY year DESC;
		# of convictions per year
		SELECT YEAR(beginDate) AS year, COUNT(*) AS numberOfCrimes FROM conviction GROUP BY year ORDER BY year DESC LIMIT 5;
		# Number of cases by category
		SELECT CatName, COUNT(*) FROM cases GROUP BY CatName;
		# Number of cases per SuperCat
		SELECT category.SuperCat, COUNT(*) FROM cases, category WHERE cases.CatName = category.CatName GROUP BY category.SuperCat;
		
		#Average of age of POI
		SELECT AVG(TIMESTAMPDIFF(YEAR,DateOfBirth,CURDATE())) FROM personOfInterest;
		
		#Average age of convicted people
		SELECT AVG(TIMESTAMPDIFF(YEAR,DateOfBirth,CURDATE())) FROM personOfInterest AS p, conviction AS c WHERE p.idPersonOfINterest = c.idpersonofinterest;
		#Average age by crime type:
		SELECT AVG(TIMESTAMPDIFF(YEAR,DateOfBirth,CURDATE())) FROM personOfInterest AS p, conviction AS conv, cases WHERE p.idPersonOfINterest = conv.idpersonofinterest and cases.idCase = conv.idCase GROUP BY CatName;
		
	*/
%>

<%@ include file="Footer.jsp" %>
