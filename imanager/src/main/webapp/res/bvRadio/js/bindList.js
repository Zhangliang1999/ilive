

$(document).ready(function() {
	$("#addBindList").click(function() {
		$("#selectL option:selected").each(function() {
			$(this).clone().prependTo("#selectR");
		});
	});

	$("#deleteBindList").click(function() {
		$("#selectR option:selected").each(function() {
			$(this).remove();
		});
	});
});
