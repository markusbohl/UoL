package common;

import java.io.IOException;
import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.ProvisionException;
import com.google.inject.name.Names;

public class ConfigurationModule extends AbstractModule {

	private final String configurationResourcePath;

	public ConfigurationModule(final String configurationResourcePath) {
		this.configurationResourcePath = configurationResourcePath;
	}

	@Override
	protected void configure() {
		Names.bindProperties(binder(), getProperties());
	}

	private Properties getProperties() {
		final Properties properties = new Properties();
		try {
			properties.load(ConfigurationModule.class.getResourceAsStream(configurationResourcePath));
			return properties;
		} catch (final IOException e) {
			throw new ProvisionException(e.getMessage());
		}
	}
}
