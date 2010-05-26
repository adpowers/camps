if (window.XMLHttpRequest) {
    var xmlhttp = new XMLHttpRequest;
} else if (window.ActiveXObject) {
    var xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
}

var baseURL = "servlet/CampsServlet";	// url of the servlet
var transtype = -1;								// initalize transportation type

// Pass this the table row you want to remove.
function removeNode (node) {
	// find the next and previous rows
	// figure out it is the only node, if it is at the beginning/end, or in the middle
	// remove the overlays that depend on this
	// if it is in the middle, add a new overlay from prev to next
	// if it leaves one node remaining, circle that node
	// update the total

    var t = node;
    var prev = t.previousSibling;
    var next = t.nextSibling;
    
    t.parentNode.removeChild(t);
    
    if (prev == null && next.getAttribute("id") == "totalRow") {
    	// If this is the only node, remove it's overlay.
    
        removeOverlay(node.getAttribute("id"), -1, transtype);
        
    } else if (prev == null) {
    	// If this is the first node, and there are others, remove the path and set
    	// the time field of the next row to blank.
    
        removeOverlay(node.getAttribute("id"), next.getAttribute("id"), transtype);
        
        // set first node's time to blank
        next.childNodes[1].innerHTML = "";
        
        // if we leave just one node left, create a circle
        if (next.parentNode.childNodes.length == 2) {
        	getPathInfo(next, null, false);
        }
        
    } else if (next.getAttribute("id") == "totalRow") {
    	// If this is the last node, and there are others, remove the path.
    	
        removeOverlay(prev.getAttribute("id"), node.getAttribute("id"), transtype);
        
        // if we leave just one node left, create a circle
        if (prev.parentNode.childNodes.length == 2) {
        	getPathInfo(prev, null, false);
        }
        
    } else {
        // If this is in the middle, remove the two paths that depend on this node and
        // then add a path from the node before to the node after.
        
        removeOverlay(prev.getAttribute("id"), node.getAttribute("id"), transtype);
        removeOverlay(node.getAttribute("id"), next.getAttribute("id"), transtype);
        getPathInfo(prev, next, true);
    }
    
    updateTotal();
}

// This adds the selected node to the table.
function getNode() {
    // Don't allow the user to enter the same value twice in a row.
    if (document.getElementById("searchField").value == "") {
    	return false;
    }
    
    getNodeWithString( response.results[selection] );
    
    // make it prettier by removing the value from the textfield and
    // clearing the drop down box
    document.getElementById("searchField").value = "";
    document.getElementById("autocomplete").style.display = "none";
}

function getNodeWithString( value ) {    
	// fire off xmlhttp request with the passed value
    // if it returns with a valid node, add the new item before total, add the overlay,
    // and update the total
	
    // Let the user know what is going on
    document.getElementById("warnings").innerHTML = "loading...";
    xmlhttp.open("GET", baseURL + "?method=getnode&value=" + escape(value)); 
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4) {
            var res = eval("(" + xmlhttp.responseText + ")");
            
            if (res.id == -1) {
                document.getElementById("warnings").innerHTML = "<span style=\"color:red\">Invalid node</span>";
            } else {
            
            	document.getElementById("warnings").innerHTML = "";
            
            	// Create the elements and set their attributes and any click handlers.
                var row = document.createElement("tr");
                row.setAttribute("id", res.id);
                
                var col1 = document.createElement("td");
                var col2 = document.createElement("td");
                var col3 = document.createElement("td");
                var img = document.createElement("img");
                col1.innerHTML = res.name;
                img.setAttribute("src", "images/x.png");
                img.onclick = crossPlatformRemove;
                col3.appendChild(img);           
                
                row.appendChild(col1);
                row.appendChild(col2);
                row.appendChild(col3);
                
                var total = document.getElementById("totalRow");
                
                // if there is only one node previously, remove its overlay
                if (total.parentNode.childNodes.length == 2) {
                    removeOverlay(total.parentNode.firstChild.getAttribute("id"), -1, transtype);
                }
                
                total.parentNode.insertBefore(row, total);
                
                var prev = row.previousSibling;
                if (prev != null) {
                    // if this isn't the first node, create a path overlay
                    getPathInfo(prev, row, true);
                } else {
                    // if this is the first node, create an overlay for just that node
                    getPathInfo(row, null, false);
                }
            }
        }
    }
    xmlhttp.send(null);
}

// The majority of this function is courtesy of QuicksMode.org
// http://www.quirksmode.org/js/events_properties.html
//
// This is passed a click event, determines the target element,
// and then forwards it to remove.
function crossPlatformRemove(e) {
	var targ;
	if (!e) var e = window.event;
	if (e.target) targ = e.target;
	else if (e.srcElement) targ = e.srcElement;
	if (targ.nodeType == 3) // defeat Safari bug
		targ = targ.parentNode;
	removeNode(targ.parentNode.parentNode);
}

// pass this the two table row elements you want to find a path between
// and a boolean if you want to update the total time
function getPathInfo(prev, row, update) {
    var firstID = prev.getAttribute("id");
    var secondID = -1;	// -1 is used to draw circles
    if (row != null) {
        secondID = row.getAttribute("id");
    }
    
    // Let the user know what is going on
    document.getElementById("warnings").innerHTML = "loading...";
    
    xmlhttp.open("GET", baseURL + "?method=getpathinfo&node1=" + firstID + "&node2=" + secondID + "&trans_type=" + transtype);
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4) {
            var res = eval("(" + xmlhttp.responseText + ")");
            
            // If it can't find a path (and we aren't requesting a circle), show an error,
            // otherwise set the time for this path, update the total (if requested), and
            // then add the overlay.
            if (res.time == -1 && row != null) {
            	document.getElementById("warnings").innerHTML = "<span style=\"color:red\">Can't find a path between the nodes</span>";
            } else {
            	document.getElementById("warnings").innerHTML = "";
            	
	            if (row != null) {
	                row.childNodes[1].innerHTML = res.time + " min";
	            }
	            if (update) {
	                updateTotal();
	            }
	            // add overlay
	            addOverlay(firstID, secondID, res.x, res.y, res.w, res.h, res.imageUrl);
	        }
        }
    }
    xmlhttp.send(null);
}

// Pass this five ints to add an overlay on the map.
// start - first node id (building)
// end - last node id (building)
// x - coordinate on the base map
// y - coordinate on the base map
// w - width of the image returned
// h - width of the image returned
function addOverlay(start, end, x, y, w, h, url) {
    var key = start + "-" + end + "-" + transtype;
    document.addOverlayWrapper(key, url, x, y, w, h);  
}

// Pass this three ints to remove an overlap from the map.
// start - first node id (building)
// end - last node id (building)
// trans - the transportation type over the overlay
function removeOverlay(start, end, trans) {
	var key = start + "-" + end + "-" + trans;
	document.removeOverlayWrapper(key);
}

// Prints the transportation types on the screen.
function getTransportationTypes() {
    xmlhttp.open("GET", baseURL + "?method=gettransportationtypes");
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4) {
            var res = eval("(" + xmlhttp.responseText + ")");
            var out = "";
            for (var i = 0; i < res.types.length; i++) {
            
                // set the default type to walk
                var checked = "";
                if (res.types[i].name == "Walk") {
                    checked = " checked=\"checked\"";
                    transtype = res.types[i].id;
                }
                
                out += "<input type=\"radio\" name=\"transportation\" value=\"" + res.types[i].id + "\"" + checked + " onchange=\"updateTransportationType(" + res.types[i].id + ");\" /> " + res.types[i].name + "<br />";
            }
            document.getElementById("transtypes").innerHTML = out;
        }
    }
    xmlhttp.send(null);
}

// Updates the total time.
function updateTotal() {
    var t = document.getElementById("directions").firstChild;
    var count = 0.0;
    
    // Step through the DOM and sum up the counts.
    for (var i = 0; i < t.childNodes.length - 1; i++) {
        var v = t.childNodes[i].childNodes[1].innerHTML;
        if (v != "") {
            //count += parseInt(v.substring(0, v.length - 4));
            count += parseFloat(v);
        }
    }
    
    var out = document.getElementById("totalRow").childNodes[1];
    if (count == 0) {
        out.innerHTML = "";
    } else {
        out.innerHTML = count.toPrecision(2) + " min";
    }
}

// Pass this the transportation type you want to switch to.
function updateTransportationType(newType) {

	// remove all overlays, callGetPathInfo for each pair, updateTotal;
	
    var t = document.getElementById("directions").firstChild; // table
    var old = transtype;
    transtype = newType;
    
    for (var i = 0; i < t.childNodes.length - 2; i++) {
        var row = t.childNodes[i];
        var next = t.childNodes[i+1];
        var time = (i + 1) * 500;
        
        removeOverlay(row.getAttribute("id"), next.getAttribute("id"), old);
        
        // setTimeout so they don't all update at once and break the client
        setTimeout(getPathInfo, time, row, next, true);
    }
    
    return true;
}

// Call when the page loads to set up the necessary items.
function init() {
    getTransportationTypes();
    parseURL();
}

// automatically add the nodes from the URL
function parseURL() {
	// [\\?&]		anything after a ? or &
	// ([^&#]*)		any number of characters that aren't & or #
	var getN = new RegExp( "[\\?&]n=([^&#]*)" );
	var nodelist = getN.exec( window.location.href );
	if (nodelist != null) {
		var nodes = nodelist[1].split(",");
		for(var i=1;i<=nodes.length;i++){
			var node = nodes[i-1];
			setTimeout("getNodeWithString(\""+node+"\")", i * 500);
		}
		//var i = 1;
		//for (node in nodes) {
		//	setTimeout(getNodeWithString, i * 500, node);
		//	i++;
		//}
	}
}

//used to automate node adding
function addNode(name) {
	document.getElementById("searchField").value = name;
	getAutocomplete();
	var timer = setTimeout(addNode2, 50);
}

function addNode2() {
	//getAutocomplete();
	fillWithSelection();
	getNode();
}