$(document).on({
	ajaxStart : function() {
		$("#spinner_box").fadeIn("slow", "linear")
	},
	ajaxStop : function() {
		$("#spinner_box").fadeOut("slow", "linear")
	}
});

$(document).ready(function() {
	var getParameterByName = function(name) {
		name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
		return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	}

	$.ajax({
		type : "GET",
		url : "details/getCurrencyDetails?currency=" + getParameterByName("currency"),
		contentType : "application/json",
		dataType : "json",
		success : function(resp) {

			$("#details").after("<p >" + resp.ccy1_ccy + "/" + resp.ccy2_ccy + "</p>");
			$("#details").after("<p >" + resp.ccy1_name + "/" + resp.ccy2_name + "</p>");
		}
	});
})