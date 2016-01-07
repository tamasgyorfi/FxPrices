package hu.fx.backing;

import java.time.Instant;

public class ReportMessage {

	private final String sender;
	private final String message;
	private final Instant timestamp;
	private final Instant receivedOn;
	private final String severity;

	public ReportMessage(String sender, String message, Instant timestamp, String severity, Instant receivedOn) {
		this.sender = sender;
		this.message = message;
		this.severity = severity;
		this.timestamp = timestamp;
		this.receivedOn = receivedOn;
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Instant getTimestamp() {
		return timestamp;
	}
	
	public Instant getReceivedOn() {
		return receivedOn;
	}
	
	public String getSeverity() {
		return severity;
	}
}
