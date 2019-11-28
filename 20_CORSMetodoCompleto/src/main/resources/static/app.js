/**
 * 
 */

$(document).ready(function(){
	
	$.ajax({
		url: 'http://localhost:8080/producto/',
		type: 'GET',
		success: function(data) {
			var html = "";
			$.each(data, function(index, value){
				html += '<tr>';
				html += '<td>'+ value.id+'</td>';
				html += '<td>'+ value.nombre+'</td>';
				html += '<td>'+ value.categoria+'</td>';
				html += '</tr>';
			});
			$("#productos-table-body").append(html);
		},
		error: function(error) {
			console.log("Error: " + error);
		}
	});
	
});