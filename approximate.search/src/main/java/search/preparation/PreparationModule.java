package search.preparation;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class PreparationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ReferenceFilter.class);
		bind(CompressedSequenceParser.class);
		bind(Partitioner.class).to(NonOverlappingPartitioner.class);
		bind(OverlapBuilder.class).to(RawSectionsIncludingOverlapBuilder.class);
		// bind(OverlapBuilder.class).to(PerSectionOverlapBuilder.class);
		install(new FactoryModuleBuilder().build(SectionsProviderFactory.class));
	}
}
