function evalSparql(){
	$('#sparqlresultstable').html('<p>Fetching SPARQL results ...</p>');
	var qtext = $('#querytext').get(0).value;
	var url = '/sparql?query=' + encodeURIComponent(qtext);
	$.get(url, function(data){
		$('#sparqlresultstable').html(data);
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
