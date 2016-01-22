$(document).on({
	ajaxStart : function() {
		$("#spinner_box").fadeIn("slow", "linear")
	},
	ajaxStop : function() {
		$("#spinner_box").fadeOut("slow", "linear")
	}
});

$(document).ready(function() {

	var currentPage = 0;
	var allPages = 0;
	var CURRENCIES_PER_PAGE = 20;

	var getParameterByName = function(name) {
		name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
		return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	}

	var toPage = function(page) {
		$('.pagination').fadeTo("fast", 1);
		$('#pag_button' + page).fadeTo("fast", 0.5);
		$("#itemContainer").children().hide();
		$('#itemContainer').children().slice(page * CURRENCIES_PER_PAGE, page * CURRENCIES_PER_PAGE + CURRENCIES_PER_PAGE).show();
	}

	var setupPagination = function() {
		$("#itemContainer").children().hide();
		$('#itemContainer').children().slice(0, CURRENCIES_PER_PAGE).show();

		for ( var i = 0; i <= allPages; i++) {
			$("#next").before("<button class='pagination' id='pag_button" + i + "'>" + (i + 1) + "</button>")
		}

		$('#pag_button0').fadeTo("fast", 0.5);
		$('.pagination').click(function() {
			var id = $(this).attr('id');
			var pageNr = id.substring("pag_button".length, id.length);
			toPage(pageNr);
		})

		$("#prev").click(function() {
			if (currentPage !== 0) {
				currentPage -= 1;
				toPage(currentPage);
			}
		});

		$("#next").click(function() {
			if (currentPage !== allPages) {
				currentPage += 1;
				toPage(currentPage);
			}
		});
	}

	var display = function(quotes) {
		allPages = Math.floor(quotes.length / CURRENCIES_PER_PAGE);
		for ( var i = 0; i < quotes.length; i++) {
			var currencyPair = quotes[i].ccy1 + " / " + quotes[i].ccy2;
			$("#placeholder").before("<div id='" + currencyPair + "' class='currency-button'><br/>" + currencyPair + "<br/> price: " + quotes[i].price + "</p>");
		}

		$(".currency-button").mouseenter(function() {
			$(this).fadeTo("fast", 1);
		});

		$(".currency-button").mouseleave(function() {
			$(this).fadeTo("fast", 0.5);
		});

		setupPagination();
	}

	var vendor = getParameterByName("provider");
	$("#itemContainer").before("<h2 class='h2' id='header'>Most fresh currencies for provider: " + vendor + " </h2>");

	$("#refresh").click(function() {
		location.reload();
	});

	$('html,body').css('cursor','default');
	
	$.ajax({
		type : "GET",
		url : "allPricesForSource.html",
		contentType : "application/json",
		dataType : "json",
		success : function(resp) {
			if (typeof resp.quotes[vendor] !== "undefined") {
				display(resp.quotes[vendor]);
			}
		},
		error : function(resp) {
			alert(JSON.stringify(resp));
			var x = "";
			for (key in resp) {
				x += key + " = " + resp[key] + "\n";
			}
			$("#error_box").fadeIn("slow", "linear");
			$("#error_ok").click(function() {
				$("#error_box").fadeOut("slow", "linear");
			})
		}
	});
})