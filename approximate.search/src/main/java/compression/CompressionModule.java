package compression;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import common.ConfigurationModule;
import common.datastructure.DatastructureModule;

public class CompressionModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new ConfigurationModule());
		install(new DatastructureModule());

		bind(new TypeLiteral<Set<Character>>() {
		}).toInstance(ImmutableSet.of('A', 'C', 'G', 'T', 'N'));

		bind(ReferentialCompression.class);
	}
}
