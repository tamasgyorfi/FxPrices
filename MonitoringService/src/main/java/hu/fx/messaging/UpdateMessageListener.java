package hu.fx.messaging;

import hu.fx.backing.ReportMessage;
import hu.fx.backing.ReportMessages;
import hu.fx.backing.Service;
import hu.fx.backing.Services;
import hu.fx.datetime.DateTimeProvider;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateMessageListener implements MessageListener {

	private static final String SEVERITY = "severity";
	private static final String TIMESTAMP = "timestamp";
	private static final String MESSAGE = "message";
	private static final String SENDER = "sender";
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateMessageListener.class);

	private Services services;
	private ReportMessages messages;
	private DateTimeProvider dateTimeProvider;

	public UpdateMessageListener(Services services, ReportMessages messages, DateTimeProvider dateTimeProvider) {
		this.services = services;
		this.messages = messages;
		this.dateTimeProvider = dateTimeProvider;
	}

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Message received: {}", message);
		handleMessage(message);
	}

	private void handleMessage(Message message) {
		if (message instanceof TextMessage) {
			handleStatusMessage(message);
		} else if (message instanceof MapMessage) {
			try {
				handleReportMessage((MapMessage) message);
			} catch (JMSException e) {
				LOGGER.error("Error while processing JMS text message. ", e);
			}
		}
	}

	private void handleReportMessage(MapMessage message) throws JMSException {
		String sender = message.getStringProperty(SENDER);
		String payload = message.getString(MESSAGE);
		String timeStamp = message.getString(TIMESTAMP);
		String severity = message.getString(SEVERITY);

		messages.add(new ReportMessage(sender, payload, dateTimeProvider.stringToInstant(timeStamp), severity, dateTimeProvider.now()));
	}

	private void handleStatusMessage(Message message) {
		try {
			String sender = message.getStringProperty(SENDER);

			services.add(new Service(sender, dateTimeProvider));
		} catch (JMSException e) {
			LOGGER.error("Error while processing JMS map message. ", e);
		}
	}
}
