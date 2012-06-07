function evalSparql(){
	$('#sparqlResultsTable').html('<p>Fetching SPARQL results ...</p>');
	var qtext = $('#querytext').get(0).value;
	var url = '/sparql?query=' + encodeURIComponent(qtext);
	$.get(url, function(data){
		$('#sparqlResultsTable').html(data);
	});
}

function getQueryProfile(qProfile){
	$('#queryProfileList').html('<p>Fetching query profile ...</p>');
	var url = '/qprofile?id=' + qProfile;
	$.get(url, function(data){
		$('#queryProfileList').html(data);
	});
}

function addUrlResToQuery(res){
	var textArea = $('#querytext').get(0);
	var insertText = "<" + res + ">";
	insertTextAtCursor(textArea, insertText);
}

function insertTextAtCursor(el, text) {
    var val = el.value, endIndex, range;
    if (typeof el.selectionStart != "undefined" && typeof el.selectionEnd != "undefined") {
        endIndex = el.selectionEnd;
        el.value = val.slice(0, endIndex) + text + val.slice(endIndex);
        el.selectionStart = el.selectionEnd = endIndex + text.length;
    } else if (typeof document.selection != "undefined" && typeof document.selection.createRange != "undefined") {
        el.focus();
        range = document.selection.createRange();
        range.collapse(false);
        range.text = text;
        range.select();
    }
}
