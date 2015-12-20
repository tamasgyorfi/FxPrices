package hu.fx.domain;

import java.time.Instant;

public class Service implements Comparable<Service> {

	private String name;
	private Instant lastUpdate;

	public Service(String name) {
		this.name = name;
	}

	public Service(String name, Instant lastUpdate) {
		this(name);
		this.lastUpdate = lastUpdate;
	}

	public String getName() {
		return name;
	}

	public String getLastUpdate() {
		if (lastUpdate == null) {
			return "NO UPDATE RECEIVED YET";
		}
		return lastUpdate.toString();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
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
