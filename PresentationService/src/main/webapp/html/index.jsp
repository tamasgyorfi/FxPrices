<head>
<link rel='stylesheet' type='text/css' href='static/css/styles.css' />
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
<script type="text/javascript" src="js/index.js"></script>
</head>
<body class=".background">
	<h1 class="h1">FX Currency Prices</h1>
	<br/>
	<h2 class="h2">Please select a currency price provider from the following list:</h2>
	<div class="center">
		<div id="dropdown" class="main">
			<span>Select a vendor</span> <img alt="" src="img/down1.png" align="middle">
		</div>
		<p id="placeholder"></p>
		<button class="button" id="go">Go!</button>
	</div>
	<div class="box" id="error_box">
		<div class="center">
			<span class="h2">Unable to access backend system. Please try again later.</span>
			<br/>
			<br/>
			<br/>
			<br/>
			<button class="button" id="error_ok">Ok</button>
		</div>
	</div>
	<div class="box" id="spinner_box">
		<span class="h2">Loading, please wait...</span>
		<br/>
		<br/>
		<img src="img/spinner.gif">
	</div>
</body>