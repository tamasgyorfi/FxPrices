package hu.fx.messaging;

import hu.fx.domain.Service;
import hu.fx.domain.Services;

import java.time.Instant;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusUpdateListener implements MessageListener{

	private Services services;
	private static final Logger LOGGER= LoggerFactory.getLogger(StatusUpdateListener.class);

	public StatusUpdateListener(Services services) {
		this.services = services;
	}
	
	@Override
	public void onMessage(Message message) {
		LOGGER.info("Message received: {}", message);
		try {
			String sender = message.getStringProperty("sender");
			Instant instant = Instant.now();
			
			services.add(new Service(sender, instant));
		} catch (JMSException e) {
			LOGGER.error("Error while processing JMS message. ", e);
		}
	}
}
