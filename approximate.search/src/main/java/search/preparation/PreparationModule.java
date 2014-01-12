package search.preparation;

import static search.AlgorithmVariant.Rme1_RawOA_1;
import static search.AlgorithmVariant.Rme2_RawOA_1;
import search.AlgorithmVariant;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class PreparationModule extends AbstractModule {

	private final AlgorithmVariant variant;

	public PreparationModule(final AlgorithmVariant variant) {
		this.variant = variant;
	}

	@Override
	protected void configure() {
		bind(ReferenceFilter.class);
		bind(CompressedSequenceParser.class);
		bind(Partitioner.class).to(NonOverlappingPartitioner.class);
		install(new FactoryModuleBuilder().build(SectionsProviderFactory.class));

		if (Rme1_RawOA_1 == variant || Rme2_RawOA_1 == variant) {
			bind(OverlapBuilder.class).to(PerSectionOverlapBuilder.class);
		} else {
			bind(OverlapBuilder.class).to(RawSectionsIncludingOverlapBuilder.class);
		}
	}
}
