package common;

import java.io.IOException;
import java.util.Properties;

import search.preparation.PreparationModule;

import com.google.inject.AbstractModule;
import com.google.inject.ProvisionException;
import com.google.inject.name.Names;

public class ConfigurationModule extends AbstractModule {

	@Override
	protected void configure() {
		Names.bindProperties(binder(), getProperties());
	}

	private Properties getProperties() {
		final Properties properties = new Properties();
		try {
			properties.load(PreparationModule.class.getResourceAsStream("/configuration/config.properties"));
			return properties;
		} catch (final IOException e) {
			throw new ProvisionException(e.getMessage());
		}
	}
}
