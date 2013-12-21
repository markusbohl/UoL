package compression;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import common.ConfigurationModule;
import common.datastructure.DatastructureModule;
import common.preparation.ReferenceSequenceModule;

public class CompressionModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new ConfigurationModule("/configuration/reference.properties"));
		install(new ConfigurationModule("/configuration/compression.properties"));
		install(new DatastructureModule());
		install(new ReferenceSequenceModule());

		bind(new TypeLiteral<Set<Character>>() {
		}).toInstance(ImmutableSet.of('A', 'C', 'G', 'T', 'N'));
	}
}
