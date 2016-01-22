<head>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
<script type="text/javascript" src="js/main.js"></script>
<link rel='stylesheet' type='text/css' href='static/css/styles.css' />
</head>
<body class=".background">
	<h1 class="h1">FX Currency Prices</h1>
	<br />
	<div class="center" id="itemContainer">
		<p id="placeholder"></p>
	</div>
	<div class="box" id="error_box">
		<div class="center">
			<span class="h2">Unable to access backend system. Please try again later.</span> <br /> <br /> <br /> <br />
			<button class="button" id="error_ok">Ok</button>
		</div>
	</div>
	<div class="box" id="spinner_box">
		<span class="h2">Loading, please wait...</span> <br /> <br /> <img src="/img/spinner.gif">
	</div>
	<div class="holder">
		<br/>
		<button class="pagination" id="prev">&lt;Prev</button>
		<button class="pagination" id="next">Next&gt;</button>
		<br/>
		<br/>
		<button class="button" id="refresh">Refresh</button>
	</div>
</body>