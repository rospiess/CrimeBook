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
String categories = "[";
String values = "[";
for (int i = 0; i < pairs.size(); i++){
	categories = categories + pairs.get(i).getL() + ", ";
	values = values + pairs.get(i).getR() + ", ";
}
categories = categories.substring(0, pairs.size()-1) + "]";
values = values.substring(0, pairs.size()-1) + "]";


%>

<svg width="50%" height="50%" id="piechartid" xmlns="http://www.w3.org/2000/svg" viewbox="0 0 400 400">
  <style type="text/css">
    path:hover {
      opacity: 0.8;
    }
  </style>
</svg>
<script>
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

        var d = "M"+radius+","+radius+"  L" + x1 + "," + y1 + "  A"+radius+","+radius+" 0 " + ((endAngle-startAngle > 180) ? 1 : 0) + ",1 " + x2 + "," + y2 + " z";
        var c = 360;
		var colors = ["rgb(111,173,12)", "rgb(128,183,40)", "rgb(145,194,69)", "rgb(179,213,126)", "rgb(196,223,156)", "rgb(216,238,186)"]
        var arc = makeSVG("path", {d: d, fill: colors[i%colors.length]});
        svg_layout.appendChild(arc);
        arc.onclick = clickHandler; // Optional onclick handler
		
		var legend= document.createElementNS('http://www.w3.org/2000/svg', 'text');
		legend.setAttribute('fill', 'black');
		legend.innerHTML = <%=pairs.get(1).getL()%>;
		legend.setAttribute('y',x2);
		legend.setAttribute('x',y2);
		svg_layout.appendChild(legend);
    }
}

drawArcs(document.getElementById("piechartid"), <%=values%>); // here is the pie chart data
</script>

<%@ include file="Footer.jsp" %>