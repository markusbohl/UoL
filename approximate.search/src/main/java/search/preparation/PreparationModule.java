package search.preparation;

import java.io.BufferedReader;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

public class PreparationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ReferenceFilter.class);
		bind(CompressedSequenceParser.class);
		bind(Partitioner.class).to(NonOverlappingPartitioner.class);
		bind(OverlapBuilder.class).to(RawSectionsIncludingOverlapBuilder.class);
		// bind(OverlapBuilder.class).to(PerSectionOverlapBuilder.class);
		bind(StringProvider.class).annotatedWith(Names.named("reference.sequence")).to(ReferenceSequenceProvider.class);
		bind(BufferedReader.class).annotatedWith(Names.named("reference.sequence")).toProvider(
				ReferenceSequenceFastaFileReaderProvider.class);
		bind(StringProvider.class).annotatedWith(Names.named("compressed.sequence")).to(
				CompressedSequenceProvider.class);
		bind(BufferedReader.class).annotatedWith(Names.named("compressed.sequence")).toProvider(
				CompressedSequenceBufferedFileReaderProvider.class);
		install(new FactoryModuleBuilder().build(SectionsProviderFactory.class));
	}
}
