var selection = 0;		// the users selection on the dropdown
var response;			// store the response object
var timer;				// store the timer between keypresses
var maxResults = 10;	// max number of results to display

// Is called onkeypress from the text field. This figures out what key is pressed,
// and then calls the appropriate methods (the "god class" of autocomplete).
function keyPress (event) {
    var key;
    if(window.event) { // IE
        key = event.keyCode;
    } else if(event.which) { // Netscape/Firefox/Opera
        key = event.which;
    }
    
    switch(key) {
        case 27:
        	// esc
            document.getElementById("searchField").value = "";
            updateDropDown();
            return false;
            break;
        case 38:
            // up
            selection--;
            updateDropDown();
            return false;
            break;
        case 39:
            // right;
            fillWithSelection()
            return false;
            break;
        case 40:
            // down
            selection++;
            updateDropDown();
            return false;
            break;
        case 9:
            //tab
            selection++;
            updateDropDown();
            return false;
            break;
        case 13:
            // enter
            getNode();
            break;
        default:
            clearTimeout(timer);
            timer = setTimeout(getAutocomplete, 50);
            break;
    }
    
    return true;
}

// Fires the XmlHttpRequest with the textbox value and then saves the results as response. 
function getAutocomplete() {
    xmlhttp.open("GET", baseURL + "?method=autocomplete&value=" + escape(document.getElementById("searchField").value));
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4) { 
            response = eval("(" + xmlhttp.responseText + ")");
            updateDropDown();
        }
    }
    xmlhttp.send(null);
    selection = 0;
}

// Takes the current selection and puts it in the textbox.
function fillWithSelection() {
    if (response.results.length != 0) {
        document.getElementById("searchField").value = response.results[selection];
    }
    return false;
}

// Updates the selection to the value passed. This is called during onmouseovers.
function updateSelection(value) {
    selection = value;
    updateDropDown();
}

// This steps through the returned array and prints out the dropdown box.
function updateDropDown () {
	var size = Math.min(response.results.length, maxResults);

	// Don't allow the user to send the selection pointer beyond the length of the dropdown
    selection = (selection + size) % size
    
    var dropdown = document.getElementById("autocomplete");
    var output = "<table>";
    var input = document.getElementById("searchField").value;
    
    // If the textbox is empty, hide the dropdown, otherwise show it.
    if (input == "" || response.results.length == 0) {
        dropdown.style.display = "none";
        return true;
    } else {
        dropdown.style.display = "block";
        dropdown.style.border = "1px solid black";
        dropdown.style.width = "15em";
    }

    for (var i = 0; i < size; i++) {
        if (selection == i) {
            var bgcolor = "#BBBBFF";
        } else {
            var bgcolor = "#FFFFFF";
        }
        
        var regex = new RegExp(input);
        
        var node = response.results[i].replace(regex, "<b>" + input + "</b>");
        
       output += "<tr><td style=\"width: 15em; background: " + bgcolor + "\" onmouseover=\"updateSelection(" + i + ");\" onmousedown=\"getNode();\">" + node + "</td></tr>";
    }
    output += "</table>";
    dropdown.innerHTML = output;
}