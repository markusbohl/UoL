package preparation;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class PreparationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(OverlappingAreaBuilder.class);
		bind(CompressedSequenceParser.class);
		bind(Partitioner.class).to(NonOverlappingPartitioner.class);
		bind(StringProvider.class).to(ReferenceSequenceProvider.class);
		install(new FactoryModuleBuilder().build(SectionsProviderFactory.class));
	}
}
