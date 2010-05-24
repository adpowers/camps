 <html>
      <head>
      		<!-- Temporary Style Sheet -->
      		<!-- Replace with <link rel="stylesheet" href="CAMPS.css"> -->
      		<style>
				body,td,a,div,.p{font-family:arial,sans-serif}
				div,td{color:#000000}
				a:link,.w,.w a:link{color:#0000cc}
				a:visited{color:#551a8b}
				a:active{color:#ff0000}
				
				.map {z-index:0;
					 cursor:move} 
				.overlay {z-index:250} 
				.control {z-index:350} 
				
			</style>
          <!-- The fully-qualified module name -->
          <meta name='gwt:module' content='com.cse403.CAMPS'>
		
			<!-- Andrew's JS files						   -->
		<script src="control.js" type="text/javascript"></script>
	  	<script src="autocomplete.js" type="text/javascript"></script>
          
        <script type="text/javascript">
		<%
		
		out.println ("function init2() {");
			String name = request.getParameter("start");
			if(name != null && !name.equals("")){
				out.println( "addNode(\""+name+"\");" );
			}
		out.println ("}");
		%>
		</script>
          
          <!-- Properties can be specified to influence deferred binding -->
          <meta name='gwt:property' content='locale=en_UK'>

          <!-- Title -->
          <title>UW Campus Map</title>
          
	</head>
	<body>
	<body onload="init();">
	
	<div id="control" style="width: 300px;">
		<img src="images/CAMPS_Logo2.png" />
		<br>
		<p><b>Type a location to search:</b></p>
		
		<input type="text" id="searchField" size="30" onkeydown="return keyPress(event);" /><input style="display: inline" type="button" onclick="getNode()" value="Go" />
		<div id="autocomplete"></div><div id="warnings"></div>
		
		<table id="directions"><tr id="totalRow"><td></td><td style="border-top: 1px solid black"></td><td></td></tr></table>
		
		<div id="transtypes"></div>
		
		<span style="position: absolute; left: 5px; bottom: 5 px;"><a href="CampsWiki.htm">Help</a> <a href="about.html">About CAMPS</a></span>
	
	</div>
	
	<div id="map">
	</div>
		<!--                                            -->
		<!-- This script is required bootstrap stuff.   -->
		<!-- You can put it in the HEAD, but startup    -->
		<!-- is slightly faster if you include it here. -->
		<!--                                            -->
		<script language="javascript" src="gwt.js"></script>

		<!-- OPTIONAL: include this if you want history support -->
		<iframe id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>


	</body>
</html>
