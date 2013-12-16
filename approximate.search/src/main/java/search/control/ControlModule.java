package search.control;

import com.google.inject.AbstractModule;

public class ControlModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ApproximateSearchAlgorithm.class).to(CompressedSequenceSearchAlgorithm.class);
	}
}
