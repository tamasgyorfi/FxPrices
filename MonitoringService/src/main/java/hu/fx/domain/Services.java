package hu.fx.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Services {

	private Set<Service> services = new ConcurrentSkipListSet<>();
	private static final Logger LOGGER= LoggerFactory.getLogger(Services.class);

	public void add(Service service) {
		LOGGER.info("Adding: {}", service );
		if (services.contains(service)) {
			services.remove(service);
		}
		services.add(service);
		LOGGER.info("Services now: {}", services);
	}
	
	public List<Service> getAllServices() {
		return new ArrayList<Service>(Collections.unmodifiableSet(services));
	}
}
