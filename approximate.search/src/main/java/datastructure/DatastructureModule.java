package datastructure;

import com.google.inject.AbstractModule;

public class DatastructureModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ReferenceIndexStructure.class).to(SuffixTreeWrapper.class).asEagerSingleton();
	}
}
