package hu.fx.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

	public Properties getProperties() {
		Properties properties = new Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("WEB-INF/props_default.properties");
		
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		
		return properties;
	}
}
