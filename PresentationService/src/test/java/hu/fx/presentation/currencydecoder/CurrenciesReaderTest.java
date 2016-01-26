package hu.fx.presentation.currencydecoder;

import static org.junit.Assert.assertEquals;
import hu.fx.presentation.currencydecoder.CurrenciesReader;
import hu.fx.presentation.currencydecoder.model.CurrenciesDetails;
import hu.fx.presentation.currencydecoder.model.CurrencyData;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CurrenciesReaderTest {

	private final List<String> TEST_DATA = Arrays.asList("AED,United Arab Emirates dirham,United Arab Emirates",
			"AFN,Afghanafghani,Afghanistan,Some place else",
			"ALL,Albania",
			"AMD,Armenian dram,Armenia");
	
	private CurrenciesReader sut = new CurrenciesReader() {
		@Override
		protected List<String> load() {
			return TEST_DATA;
		}
	};
	
	@Test
	public void currencyReader_shouldReadCompleteCurrencyLinesOnly() {
		CurrenciesDetails expectedResult = new CurrenciesDetails();
		expectedResult.addCurrency(new CurrencyData("AED", "United Arab Emirates dirham", "United Arab Emirates"));
		expectedResult.addCurrency(new CurrencyData("AFN", "Afghanafghani", "Afghanistan", "Some place else"));
		expectedResult.addCurrency(new CurrencyData("AMD", "Armenian dram", "Armenia"));
		
		CurrenciesDetails actualResult = sut.readCurrenciesData();
		assertEquals(expectedResult, actualResult);
	}

}
