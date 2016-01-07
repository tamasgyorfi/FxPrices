package hu.fx.backing;

import static org.junit.Assert.assertEquals;
import hu.fx.datetime.DateTimeProvider;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTest {

	@Mock
	private DateTimeProvider dateTimeProvider;
	
	private Instant now = Instant.parse("2015-12-03T10:15:30.00Z");
	private Instant smallDelay = now.minus(4, ChronoUnit.SECONDS);
	private Instant warningDelay = now.minus(8, ChronoUnit.SECONDS);
	private Instant errorDelay = now.minus(12, ChronoUnit.SECONDS);

	@Test
	public void getStatus_shouldReturnOkImageName_whenNoTimeout() {
		Service sut = getService(smallDelay);
		assertEquals("img/green.png", sut.getStatus());
	}

	@Test
	public void getStatus_shouldReturnWarningImageName_whenWarningTimeout() {
		Service sut = getService(warningDelay);
		assertEquals("img/yellow.png", sut.getStatus());
	}

	@Test
	public void getStatus_shouldReturnErrorImageName_whenErrorTimeout() {
		Service sut = getService(errorDelay);
		assertEquals("img/red.png", sut.getStatus());
	}

	@Test
	public void getStatus_shouldReturnUnknownImageName_whenNoLastMessagePresent() {
		Service sut = new Service("service");
		assertEquals("img/unknown.png", sut.getStatus());
	}

	private Service getService(Instant delay) {
		Mockito.when(dateTimeProvider.now()).thenReturn(now);
		Mockito.when(dateTimeProvider.betweenDateAndNow(Matchers.any(Instant.class), Matchers.eq(ChronoUnit.SECONDS))).thenReturn(ChronoUnit.SECONDS.between(delay, now));
		return new Service("service", dateTimeProvider);
	}
}
