var socket;
var userId;
function createConnection() {
		var wsUri = "ws://" + window.location.hostname + ":" + window.location.port + "/RoundRobin/UpdateValues";
		socket = new WebSocket(wsUri);
		socket.onmessage = onMessage;
}

function onMessage(event){
	var json = event.data;
	var jsonObj = JSON.parse(json);
	var categories = jsonObj.categories;
	var selectedNames = $("#selectedNames");
	selectedNames.empty();
	for(i=0;i<categories.length;i++){
		var wrapperCategory = $("<div></div>");
		wrapperCategory.attr("id",categories[i]+"-wrapper");
		var title = $("<h2></h2>");
		title.addClass("title");
		title.text(categories[i]);
		wrapperCategory.append(title);
		var outputDiv = createTransition(jsonObj[categories[i]]);
		wrapperCategory.append(outputDiv);
		selectedNames.append(wrapperCategory);
	}
	
}

function onClose(event){
	closeConnection();	
}

function closeConnection(){
	socket.close();
}

$(function(){
	createConnection();
});

function createTransition(text){	
	letters = text.split("");
	var lettersTag = $("<div></div>");
	lettersTag.addClass("letters")
	var outputTag = $("<div></div>")
	for (var i = 0;i<letters.length;i++){
		var spanTag = $("<span></span>");
		spanTag.addClass("letter");
		spanTag.text(letters[i]);
		lettersTag.append(spanTag);
	}
	outputTag.addClass("wrapper");
	outputTag.append(lettersTag);
	return outputTag;
}