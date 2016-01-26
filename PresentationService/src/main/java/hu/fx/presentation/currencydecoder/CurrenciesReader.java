package hu.fx.presentation.currencydecoder;

import hu.fx.presentation.currencydecoder.model.CurrenciesDetails;
import hu.fx.presentation.currencydecoder.model.CurrencyData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CurrenciesReader {

	private static final String CSV_SEPARATOR = ",";
	private static final int MINIMAL_LENGTH = 3;

	protected List<String> load() throws IOException {
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("WEB-INF/currencies.csv");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
		List<String> lines = new ArrayList<>();

		String line;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}

		return lines;
	}

	public CurrenciesDetails readCurrenciesData() {
		CurrenciesDetails details = new CurrenciesDetails();
		try {
			load().stream()
				.map(line -> getCurrencyDetails(line))
				.filter(line -> line.isPresent())
				.map(line -> line.get())
				.forEach(line -> details.addCurrency(line));
		} catch (IOException e) {
			return details;
		}
		return details;
	}

	private Optional<CurrencyData> getCurrencyDetails(String line) {
		String[] currencyLine = line.split(CSV_SEPARATOR);
		if (currencyLine.length < MINIMAL_LENGTH) {
			return Optional.empty();
		}

		return Optional.of(new CurrencyData(currencyLine[0], currencyLine[1], Arrays.copyOfRange(currencyLine, 2,
				currencyLine.length)));
	}
}
