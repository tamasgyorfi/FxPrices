package hu.fx.domain;

import java.time.Instant;
import java.util.Date;

public class ReportMessage {

	private final String sender;
	private final String message;
	private final Instant timestamp;
	private final Instant receivedOn;
	private final String severity;

	public ReportMessage(String sender, String message, String timestamp, String severity) {
		this.sender = sender;
		this.message = message;
		this.severity = severity;
		this.timestamp = new Date(Long.parseLong(timestamp)).toInstant();
		this.receivedOn = Instant.now();
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getTimestamp() {
		return timestamp.toString();
	}
	
	public Instant getReceivedOn() {
		return receivedOn;
	}
	
	public String getSeverity() {
		return severity;
	}
}
