package hu.fx.presentation.controller;

import hu.fx.presentation.wsgateway.WebServiceGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
	
	@Autowired
	private WebServiceGateway webServiceGateway;

	@RequestMapping(value = "/providerSelector", method = RequestMethod.GET)
	public ModelAndView hello() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
		return mav;
	}

	@RequestMapping(value = "/vendors", method = RequestMethod.GET)
	public @ResponseBody String getVendors() {
		return webServiceGateway.getAllProviders();
	}

	@RequestMapping(value = "/mainpage", method = RequestMethod.GET)
	public ModelAndView goToMainPage(@RequestParam("provider") String provider) {
		return new ModelAndView("main");
	}

}
