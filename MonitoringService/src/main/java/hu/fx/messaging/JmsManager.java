package hu.fx.messaging;

import hu.staticdataservice.client.HttpClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsManager {

	private static final String MONITORING_DESTINATION = "jms.monitoring.destination";
	private static final String JMS_BROKER_ENDPOINT = "jms.broker.endpoint";
	private HttpClient client;
	private Map<String, String> parameters;
	private MessageListener listener;
	private ActiveMQConnectionFactory connectionFactory;
	private Connection connection;

	public JmsManager(HttpClient client, MessageListener listener) {
		this.client = client;
		this.listener = listener;
	}

	@PostConstruct
	public void postConstruct() {

		try {
			parameters = client.getParametersFor("default",
					new ArrayList<String>(Arrays.asList(JMS_BROKER_ENDPOINT, MONITORING_DESTINATION)));
			connectionFactory = new ActiveMQConnectionFactory(parameters.get(JMS_BROKER_ENDPOINT));
			connection = connectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(parameters.get(MONITORING_DESTINATION));
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PreDestroy
	public void tearDown() {
		try {
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
