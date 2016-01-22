package hu.fx.presentation.controller;

import hu.fx.presentation.wsgateway.WebServiceGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

	@Autowired
	private WebServiceGateway webServicegateway;
	
	@RequestMapping(path="/allPricesForSource", method=RequestMethod.GET)
	public @ResponseBody String getAllPricesForSource() {
		return webServicegateway.getAllPrices();
	}
}
