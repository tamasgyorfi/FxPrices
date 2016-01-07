package hu.fx.messaging;

import static org.junit.Assert.assertEquals;
import hu.fx.backing.ReportMessage;
import hu.fx.backing.ReportMessages;
import hu.fx.backing.Service;
import hu.fx.backing.Services;
import hu.fx.datetime.DateTimeProvider;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.TextMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UpdateMessageListenerTest {

	@Mock
	private TextMessage textMessage;
	@Mock
	private MapMessage mapMessage;
	@Mock
	private DateTimeProvider dateTimeProvider;
	
	private Services services = new Services();
	private ReportMessages messages = new ReportMessages();
	private UpdateMessageListener sut;
	private Instant now = Instant.parse("2015-12-03T10:15:30.00Z");
	private Instant sendTime = now.minus(4, ChronoUnit.SECONDS);
	
	@Before
	public void setup() {
		 sut = new UpdateMessageListener(services, messages, dateTimeProvider);
	}
	
	@Test
	public void serviceUpdateMessage_shouldBeReflectedInServicesObject_whenTextMessageReceived() throws JMSException {
		Mockito.when(textMessage.getStringProperty("sender")).thenReturn("service");
		Mockito.when(dateTimeProvider.now()).thenReturn(now);
		sut.onMessage(textMessage);
		
		List<Service> allServices = services.getAllServices();
		assertEquals(1, allServices.size());
		assertEquals("service - 2015-12-03T10:15:30Z", allServices.get(0).getServiceDetails());
	}

	@Test
	public void reportMessage_shouldBeReflectedInReportMessagesObject_whenMapMessageReceived() throws JMSException {
		Mockito.when(mapMessage.getStringProperty("sender")).thenReturn("service");
		Mockito.when(mapMessage.getString("message")).thenReturn("db load error");
		Mockito.when(mapMessage.getString("timestamp")).thenReturn("999");
		Mockito.when(mapMessage.getString("severity")).thenReturn("error");
		
		Mockito.when(dateTimeProvider.now()).thenReturn(now);
		Mockito.when(dateTimeProvider.stringToInstant("999")).thenReturn(sendTime);
		sut.onMessage(mapMessage);
		
		List<ReportMessage> reportMessages = messages.getMessages();
		assertEquals(1, reportMessages.size());

		ReportMessage reportMessage = reportMessages.get(0);
		assertEquals("service", reportMessage.getSender());
		assertEquals("db load error", reportMessage.getMessage());
		assertEquals("error", reportMessage.getSeverity());
		assertEquals(now, reportMessage.getReceivedOn());
		assertEquals(sendTime, reportMessage.getTimestamp());
	}

}
