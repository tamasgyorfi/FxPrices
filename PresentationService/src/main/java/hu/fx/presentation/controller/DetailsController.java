package hu.fx.presentation.controller;

import hu.fx.presentation.currencydecoder.CurrenciesDataProvider;
import hu.fx.presentation.currencydecoder.model.CurrencyData;
import hu.fx.presentation.wsgateway.WebServiceGateway;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DetailsController {

	@Autowired
	private CurrenciesDataProvider currenciesDataProvider;
	@Autowired
	private WebServiceGateway webServiceGateway;

	@RequestMapping(path = "/details", method = RequestMethod.GET)
	public ModelAndView details(@RequestParam("currency") String currency) {

		return new ModelAndView("details");
	}

	@RequestMapping(path = "/details/getCurrencyDetails", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> currencyDetails(@RequestParam("currency") String currency) {
		return createModel(currency);
	}

	private Map<String, String> createModel(String currency) {

		Map<String, String> model = new HashMap<>();
		String currency1 = currency.split("-")[0];
		String currency2 = currency.split("-")[1];

		CurrencyData ccy1 = currenciesDataProvider.getCurrencyData(currency1);
		CurrencyData ccy2 = currenciesDataProvider.getCurrencyData(currency2);

		model.put("ccy1_ccy", ccy1.getCurrencyCode());
		model.put("ccy1_name", ccy1.getCurrencyName());
		model.put("ccy1_countries", ccy1.getUsingCountries().toString());

		model.put("ccy2_ccy", ccy2.getCurrencyCode());
		model.put("ccy2_name", ccy2.getCurrencyName());
		model.put("ccy2_countries", ccy2.getUsingCountries().toString());

		return model;
	}
}
