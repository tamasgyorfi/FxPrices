package hu.fx.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ReportMessages {

	private List<ReportMessage> messages = Collections.synchronizedList(new LinkedList<ReportMessage>());
	
	public void add(ReportMessage message) {
		messages.add(message);
	}
	
	public List<ReportMessage> getMessages() {
		return new ArrayList<ReportMessage>(Collections.unmodifiableList(messages));
	}
}
