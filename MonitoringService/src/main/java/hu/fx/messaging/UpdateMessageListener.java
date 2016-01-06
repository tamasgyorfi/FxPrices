package hu.fx.messaging;

import hu.fx.domain.ReportMessages;
import hu.fx.domain.ReportMessage;
import hu.fx.domain.Service;
import hu.fx.domain.Services;

import java.time.Instant;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateMessageListener implements MessageListener {

	private Services services;
	private ReportMessages messages;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UpdateMessageListener.class);

	public UpdateMessageListener(Services services, ReportMessages messages) {
		this.services = services;
		this.messages = messages;
	}

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Message received: {}", message);
		handleMessage(message);
	}

	private void handleMessage(Message message) {
		if (message instanceof TextMessage) {
			handleStatusUpdate(message);
		} else if (message instanceof MapMessage) {
			try {
				handleReportMessage((MapMessage)message);
			} catch (JMSException e) {
				LOGGER.error("Error while processing JMS message. ", e);
			}
		}
	}

	private void handleReportMessage(MapMessage message) throws JMSException {
		String sender = message.getStringProperty("sender");
		String payload = message.getString("message");
		String timeStamp = message.getString("timestamp");
		String severity = message.getString("severity");
		
		messages.add(new ReportMessage(sender, payload, timeStamp, severity));
	}

	private void handleStatusUpdate(Message message) {
		try {
			String sender = message.getStringProperty("sender");
			Instant instant = Instant.now();

			services.add(new Service(sender, instant));
		} catch (JMSException e) {
			LOGGER.error("Error while processing JMS message. ", e);
		}
	}
}
