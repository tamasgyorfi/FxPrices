package hu.fx.presentation.currencydecoder.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CurrenciesDetails {

	private Map<String, CurrencyData> currencies = new HashMap<>();

	public void addCurrency(CurrencyData currencyData) {
		currencies.put(currencyData.getCurrencyCode(), currencyData);
	}
	
	public Optional<CurrencyData> getCurrency(String currencyCode) {
		return Optional.ofNullable(currencies.get(currencyCode));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currencies == null) ? 0 : currencies.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurrenciesDetails other = (CurrenciesDetails) obj;
		if (currencies == null) {
			if (other.currencies != null)
				return false;
		} else if (!currencies.equals(other.currencies))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CurrenciesDetails [currencies=" + currencies + "]";
	}
}
