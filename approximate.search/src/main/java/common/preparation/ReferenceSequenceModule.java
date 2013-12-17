package common.preparation;

import java.io.BufferedReader;

import search.preparation.StringProvider;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class ReferenceSequenceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StringProvider.class).annotatedWith(Names.named("reference.sequence")).to(ReferenceSequenceProvider.class);
		bind(StringProvider.class).annotatedWith(Names.named("sequence.to.compress")).to(
				SequenceToCompressProvider.class);
		bind(BufferedReader.class).annotatedWith(Names.named("reference.sequence")).to(
				ReferenceSequenceBufferedFastaFileReader.class);
		bind(BufferedReader.class).annotatedWith(Names.named("sequence.to.compress")).to(
				SequenceToCompressBufferedFastaFileReader.class);
	}
}
