package hu.fx.datetime;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateTimeProvider {

	public Instant now() {
		return Instant.now();
	}
	
	public long betweenDateAndNow(Instant date, ChronoUnit timeUnit) {
		return timeUnit.between(date, now());
	}
	
	public Instant stringToInstant(String value) {
		return new Date(Long.parseLong(value)).toInstant();
	}
}
