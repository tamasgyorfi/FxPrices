package hu.fx.presentation.currencydecoder.model;

import java.util.Arrays;
import java.util.List;

public class CurrencyData {

	private final String currencyCode;
	private final String currencyName;
	private final List<String> usingCountries;

	public CurrencyData(String currencyCode, String currencyName, String... usingCountries) {
		this.currencyCode = currencyCode;
		this.currencyName = currencyName;
		this.usingCountries = Arrays.asList(usingCountries);
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public List<String> getUsingCountries() {
		return usingCountries;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
		result = prime * result + ((currencyName == null) ? 0 : currencyName.hashCode());
		result = prime * result + ((usingCountries == null) ? 0 : usingCountries.hashCode());
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
		CurrencyData other = (CurrencyData) obj;
		if (currencyCode == null) {
			if (other.currencyCode != null)
				return false;
		} else if (!currencyCode.equals(other.currencyCode))
			return false;
		if (currencyName == null) {
			if (other.currencyName != null)
				return false;
		} else if (!currencyName.equals(other.currencyName))
			return false;
		if (usingCountries == null) {
			if (other.usingCountries != null)
				return false;
		} else if (!usingCountries.equals(other.usingCountries))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CurrencyData [currencyCode=" + currencyCode + ", currencyName=" + currencyName + ", usingCountries="
				+ usingCountries + "]";
	}
}