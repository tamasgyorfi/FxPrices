package hu.fx.config;

import hu.fx.backing.ReportMessages;
import hu.fx.backing.Services;
import hu.fx.datetime.DateTimeProvider;
import hu.fx.messaging.JmsManager;
import hu.fx.messaging.UpdateMessageListener;
import hu.staticdataservice.client.HttpClient;

import java.util.Properties;

import javax.faces.webapp.FacesServlet;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.sun.faces.config.ConfigureListener;

@SpringBootApplication
public class Main extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Main.class);
	}

	@Bean
	@Qualifier("properties")
	public Properties getProperties(ConfigReader configReader) {
		return configReader.getProperties();
	}

	@Bean
	public HttpClient httpClient(@Qualifier("properties")Properties properties) {
		return new HttpClient(properties.getProperty("staticdata.server.host"), Integer.valueOf(properties
				.getProperty("staticdata.server.port")));
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		FacesServlet servlet = new FacesServlet();
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet, "*.jsf");
		return servletRegistrationBean;
	}

	@Bean
	public ServletListenerRegistrationBean<ConfigureListener> jsfConfigureListener() {
		return new ServletListenerRegistrationBean<ConfigureListener>(new ConfigureListener());
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
		return factory;
	}

	@Bean
	public ConfigReader configReader() {
		return new ConfigReader();
	}
	
	@Bean
	public Services services() {
		return new Services();
	}
	
	@Bean(name="messages")
	public ReportMessages messages() {
		return new ReportMessages();
	}
	
	@Bean
	public DateTimeProvider dateTimeProvider() {
		return new DateTimeProvider();
	}
	
	@Bean
	public JmsManager jmsManager(HttpClient httpClient, Services services, ReportMessages messages, DateTimeProvider dateTimeProvider) {
		return new JmsManager(httpClient, new UpdateMessageListener(services, messages, dateTimeProvider));
	}
}	
