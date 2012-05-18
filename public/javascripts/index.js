function evalSparql(){
	var qtext = $('#querytext').get(0).value;
	var url = '/sparql?query=' + encodeURIComponent(qtext);
	$.get(url, function(data){
		$('#sparqlresults').html(data);
	});
}

function addUrlResToQuery(res){
	var curText = $('#querytext').get(0).value;
	var newText = curText + "<" + res + ">";
	console.log(newText);
	$('#querytext').val(newText);
}