package search.control;

import search.datastructure.DatastructureModule;
import search.matcher.MatcherModule;
import search.preparation.PreparationModule;

import com.google.inject.AbstractModule;

public class ControlModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new MatcherModule());
		install(new PreparationModule());
		install(new DatastructureModule());
		bind(ApproximateSearchAlgorithm.class).to(CompressedSequenceSearchAlgorithm.class);
	}
}
