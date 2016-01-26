package hu.fx.presentation.wsgateway;

import hu.fx.presentation.ConnectionConstants;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@Scope("prototype")
public class WebServiceGateway {

	private Map<String, String> parameters;

	public WebServiceGateway(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public String getAllProviders() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForEntity(
				getConnectionString(ConnectionConstants.PERSISTENCE_SERVER_HOST, ConnectionConstants.PERSISTENCE_SERVER_PORT,
						"/providers"), String.class).getBody();
	}

	public String getAllPrices() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForEntity(
				getConnectionString(ConnectionConstants.PRICE_SERVER_HOST, ConnectionConstants.PRICE_SERVER_PORT,
						"/getCurrency?ccy1=all"), String.class).getBody();
	}

	public String getCurrencyPairPrices(String ccy1, String ccy2) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForEntity(
				getConnectionString(ConnectionConstants.PRICE_SERVER_HOST, ConnectionConstants.PRICE_SERVER_PORT,
						"/getCurrency?ccy1="+ccy1+"&ccy2="+ccy2), String.class).getBody();
	}

	private String getConnectionString(String host, String port, String serviceEndpoint) {
		return "http://" + parameters.get(host) + ":" + parameters.get(port) + serviceEndpoint;
	}
}
