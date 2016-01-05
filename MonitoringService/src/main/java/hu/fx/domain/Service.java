package hu.fx.domain;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Service implements Comparable<Service> {

	private static final String IMG_GREEN = "img/green.png";
	private static final String IMG_RED = "img/red.png";
	private static final String IMG_YELLOW = "img/yellow.png";
	private static final String IMG_UNKNOWN = "img/unknown.png";
	
	private static final long SECONDS_TO_WARNING = 5;
	private static final long SECONDS_TO_ERROR = 10;
	
	private String name;
	private Instant lastUpdate;
	
	public Service(String name) {
		this.name = name;
	}

	public Service(String name, Instant lastUpdate) {
		this(name);
		this.lastUpdate = lastUpdate;
	}

	public String getServiceDetails() {
		if (lastUpdate == null) {
			return name + " - NO UPDATE RECEIVED YET";
		}
		return name + " - " + lastUpdate.toString();
	}

	public Service setLastUpdate(Instant lastUpdate) {
		return new Service(name, lastUpdate);
	}

	public String getStatus() {

		if (lastUpdate == null) {
			return IMG_UNKNOWN;
		}
		
		Instant now = Instant.now();
		long between = ChronoUnit.SECONDS.between(lastUpdate, now);
		if (between > SECONDS_TO_WARNING && between < SECONDS_TO_ERROR) {
			return IMG_YELLOW;
		} else if (between > SECONDS_TO_ERROR) {
			return IMG_RED;
		} 
		
		return IMG_GREEN;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Service other = (Service) obj;
		if (lastUpdate == null) {
			if (other.lastUpdate != null)
				return false;
		} else if (!lastUpdate.equals(other.lastUpdate))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int compareTo(Service other) {
		return this.name.compareTo(other.name);
	}

	@Override
	public String toString() {
		return "Service [name=" + name + ", lastUpdate=" + lastUpdate + "]";
	}
}
