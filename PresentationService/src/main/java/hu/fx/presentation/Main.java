package hu.fx.presentation;

import hu.fx.presentation.config.ConfigReader;
import hu.fx.presentation.currencydecoder.CurrenciesDataProvider;
import hu.fx.presentation.currencydecoder.CurrenciesReader;
import hu.fx.presentation.wsgateway.WebServiceGateway;
import hu.staticdataservice.client.HttpClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootApplication
@EnableWebMvc
public class Main extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(Main.class);
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/html/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
		return factory;
	}

	@Bean(name="remoteProperties")
	public Map<String, String> httpClient() {
		ConfigReader reader = new ConfigReader();

		String host = reader.getParam("staticdata.server.host");
		Integer port = Integer.parseInt(reader.getParam("staticdata.server.port"));
		return new HttpClient(host, port).getParametersFor(
				"default",
				new ArrayList<String>(Arrays.asList(ConnectionConstants.PRICE_SERVER_HOST, ConnectionConstants.PRICE_SERVER_PORT,
						ConnectionConstants.PERSISTENCE_SERVER_HOST, ConnectionConstants.PERSISTENCE_SERVER_PORT)));
	}

	@Bean
	public WebServiceGateway webServiceGateway(@Value("#{remoteProperties}")Map<String, String> client) {
		return new WebServiceGateway(client);
	}

	@Bean
	public CurrenciesDataProvider provider() {
		return new CurrenciesDataProvider(new CurrenciesReader());
	}
}
