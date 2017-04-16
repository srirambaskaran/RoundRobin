$(function(){
	$("#categories").on("change",loadList);
	$("#createNewCategory").on("click",createNewCategory);
	$("#createNewEntry").on("click",createNewEntry);
	$("#selectNext").on("click", selectNext);
	$("#selectPrevious").on("click", selectPrev);
	$("#deleteEntry").on("click",deleteEntry);
	loadCategories();
	
});
function deleteEntry(){
	selectedCategory = $("#categories").find(":selected").text();
	selectedEntry = $("#categoryList").find(":selected").text();
	if(selectedEntry == ""){
		
	}else{
		params = {operation:"deleteEntry",entry:selectedEntry, category: selectedCategory};
		$.post("AdminUpdateServlet",params,function(data){
			var jsonObj = JSON.parse(data);
			var status = jsonObj.status; 
			if(status == true){
				list = jsonObj.list;
				var categoryList = $("#categoryList");
				categoryList.empty();
				for(var i=0;i<list.length;i++){
					option = $("<option></option>");
					option.attr("value",list[i]);
					option.text(list[i]);
					categoryList.append(option);
				}
				$("#categorySelected").text(list[parseInt(jsonObj.selected)]);
				$("#categoryRelated").show();
			}else{
				alert(jsonObj.message);
			}
		});
	}
}

function selectNext(){
	selectedCategory = $("#categories").find(":selected").text();
	params = {operation:"next",category: selectedCategory};
	$.post("AdminUpdateServlet",params,function(data){
		var jsonObj = JSON.parse(data);
		var status = jsonObj.status; 
		if(status == true){
			list = jsonObj.list;
			$("#categorySelected").text(list[parseInt(jsonObj.selected)]);
		}
	});
}
function selectPrev(){
	selectedCategory = $("#categories").find(":selected").text();
	params = {operation:"prev",category: selectedCategory};
	$.post("AdminUpdateServlet",params,function(data){
		var jsonObj = JSON.parse(data);
		var status = jsonObj.status; 
		if(status == true){
			list = jsonObj.list;
			$("#categorySelected").text(list[parseInt(jsonObj.selected)]);
		}
	});
}
function createNewEntry(){
	selectedCategory = $("#categories").find(":selected").text();
	var newEntry = $("#newEntry").val();
	if(newEntry == ""){
		alert("New category name cannot be empty");
	}else{
		params = {operation:"saveList",category:selectedCategory, entry: newEntry};
		$.post("AdminUpdateServlet",params,function(data){
			var jsonObj = JSON.parse(data);
			var status = jsonObj.status; 
			if(status == true){
				list = jsonObj.list;
				var categoryList = $("#categoryList");
				categoryList.empty();
				for(var i=0;i<list.length;i++){
					option = $("<option></option>");
					option.attr("value",list[i]);
					option.text(list[i]);
					categoryList.append(option);
				}
				$("#categorySelected").text(list[parseInt(jsonObj.selected)]);
				$("#categoryRelated").show();
				$("#newEntry").val("");
			}else{
				alert(jsonObj.message);
			}
		});
	}
	
}

function createNewCategory(){
	var newCategory = $("#newCategory").val();
	if(newCategory == ""){
		alert("New category name cannot be empty");
	}else{
		params = {operation:"createNewCategory",category:newCategory};
		$.post("AdminUpdateServlet",params,function(data){
			var jsonObj = JSON.parse(data);
			var status = jsonObj.status; 
			if(status == true){
				categories = jsonObj.categories;
				var categoriesTag = $("#categories");
				categoriesTag.empty();
				var option = $("<option/>");
				option.text("Select");
				option.attr("value","Select");
				categoriesTag.append(option);
				for(var i=0;i<categories.length;i++){
					option = $("<option></option>");
					option.attr("name",categories[i]);
					option.attr("value",categories[i]);
					option.text(categories[i]);
					categoriesTag.append(option);
				}
				$("#newCategory").val("");
			}else{
				alert("New category creation failed");
			}
		});
	}
}
function loadList(){
	selectedCategory = $("#categories").find(":selected").text();
	params = {operation:"getList",category:selectedCategory};
	var categoryList = $("#categoryList");
	categoryList.empty();
	$("#categoryRelated").show();
	$.post("AdminUpdateServlet",params,function(data){
		var jsonObj = JSON.parse(data);
		var status = jsonObj.status; 
		if(status == true){
			list = jsonObj.list;
			
			for(var i=0;i<list.length;i++){
				option = $("<option></option>");
				option.attr("value",list[i]);
				option.text(list[i]);
				categoryList.append(option);
			}
			$("#categorySelected").text(list[parseInt(jsonObj.selected)]);
			$("#categoryRelated").show();
		}else{
			alert(jsonObj.message);
		}
	});
}

function loadCategories(){
	params = {operation:"getCategories"}
	$("#categoryRelated").hide();
	$.post("AdminUpdateServlet",params,function(data){
		var jsonObj = JSON.parse(data);
		var status = jsonObj.status; 
		if(status == true){
			categories = jsonObj.categories;
			var categoriesTag = $("#categories");
			categoriesTag.empty();
			var option = $("<option/>");
			option.text("Select");
			option.attr("value","Select");
			categoriesTag.append(option);
			for(var i=0;i<categories.length;i++){
				option = $("<option></option>");
				option.attr("name",categories[i]);
				option.attr("value",categories[i]);
				option.text(categories[i]);
				categoriesTag.append(option);
			}
			
		}
	});
}