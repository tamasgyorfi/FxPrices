package hu.fx.presentation.currencydecoder;

import hu.fx.presentation.currencydecoder.model.CurrenciesDetails;
import hu.fx.presentation.currencydecoder.model.CurrencyData;

import java.util.Optional;

import javax.annotation.PostConstruct;

public class CurrenciesDataProvider {

	private CurrenciesReader reader;
	private CurrenciesDetails currenciesData;
	private static final CurrencyData EMPTY_CURRENCY_DATA = new CurrencyData("", "Unknown currency name", "Unknown usage");

	public CurrenciesDataProvider(CurrenciesReader reader) {
		this.reader = reader;
	}
	
	@PostConstruct
	protected void init() {
		currenciesData = reader.readCurrenciesData();
	}
	
	public CurrencyData getCurrencyData(String currencyCode) {
		Optional<CurrencyData> currency = currenciesData.getCurrency(currencyCode);
		
		if (currency.isPresent()) {
			return currency.get();
		}
		
		return EMPTY_CURRENCY_DATA;
	}
}
