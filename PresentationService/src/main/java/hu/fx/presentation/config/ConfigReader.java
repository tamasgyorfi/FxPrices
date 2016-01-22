package hu.fx.presentation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

	private Properties props = new Properties();
	
	private void load() throws IOException {
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("WEB-INF/config.properties");
		props.load(inStream);
	}
	
	public ConfigReader() {
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
			props = null;
		}
	}
	
	public String getParam(String key) {
		if (props != null) {
			return (String) props.get(key);
		}
		
		return "";
	}
	
}
