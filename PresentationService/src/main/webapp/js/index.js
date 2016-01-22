$(document).on({
	ajaxStart : function() {
		$("#spinner_box").fadeIn("slow", "linear")
	},
	ajaxStop : function() {
		$("#spinner_box").fadeOut("slow", "linear")
	}
});

$(document).ready(function() {
	var visible = false;

	$.ajax({
		type : "GET",
		url : "vendors.html",
		contentType : "application/json",
		dataType : "json",
		success : function(resp) {
			var vendors = resp.providers;
			for ( var i = 0; i < vendors.length; i++) {
				$("#placeholder").before("<div id='" + i + "' class='menuitem' align='center'>" + vendors[i] + "</div>");
			}

			$(".menuitem").mouseenter(function() {
				if (visible) {
					$(this).fadeTo("fast", 1);
				}
			})

			$(".menuitem").mouseleave(function() {
				if (visible) {
					$(this).fadeTo("fast", 0.5);
				}
			})

			$(".menuitem").click(function() {
				var id = $(this).attr('id');
				$("#dropdown span").text(vendors[id]);
				$(".menuitem").fadeOut("slow", "linear");
				visible = false;
			})

			$("#dropdown").click(function() {
				$(".menuitem").fadeToggle("slow", "linear");
				visible = true;
			})

			$("#go").click(function() {
				var source = $("#dropdown").text();
				window.location.href='/mainpage?provider='+source;
			})
		},
		error : function(resp) {
			$("#error_box").fadeIn("slow", "linear");
			$("#error_ok").click(function() {
				$("#error_box").fadeOut("slow", "linear");
			})
		}
	});
})