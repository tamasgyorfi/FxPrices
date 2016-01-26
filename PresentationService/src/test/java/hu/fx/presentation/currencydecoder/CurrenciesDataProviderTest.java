package hu.fx.presentation.currencydecoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import hu.fx.presentation.currencydecoder.model.CurrencyData;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CurrenciesDataProviderTest {

	private CurrenciesReader reader = Mockito.spy(CurrenciesReader.class);
	private CurrenciesDataProvider sut;
	private final List<String> TEST_DATA = Arrays.asList("AED,United Arab Emirates dirham,United Arab Emirates",
			"AFN,Afghanafghani,Afghanistan,Some place else", "ALL,Albania", "AMD,Armenian dram,Armenia");

	@Before
	public void setup() throws IOException {
		doReturn(TEST_DATA).when(reader).load();

		sut = new CurrenciesDataProvider(reader);
		sut.init();
	}

	@Test
	public void currencyProvider_correctlyFetchesCurrencyData_byCountryCode() {

		CurrencyData expectedCurrencyData = new CurrencyData("AFN", "Afghanafghani", "Afghanistan", "Some place else");
		CurrencyData currencyData = sut.getCurrencyData("AFN");

		assertEquals(expectedCurrencyData, currencyData);
	}

	@Test
	public void currencyProvider_returnsDefaultData_whenCurrencyCodeIsUnknown() {

		CurrencyData expectedCurrencyData = new CurrencyData("", "Unknown currency name", "Unknown usage");
		CurrencyData currencyData = sut.getCurrencyData("AFG");

		assertEquals(expectedCurrencyData, currencyData);
	}
}
