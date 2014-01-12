package search;

import search.matcher.MatcherModule;
import search.preparation.PreparationModule;

import com.google.inject.AbstractModule;
import common.ConfigurationModule;
import common.datastructure.DatastructureModule;
import common.preparation.ReferenceSequenceModule;

public class SearchModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new ConfigurationModule("/configuration/reference.properties"));
		install(new ConfigurationModule("/configuration/search.properties"));
		install(new MatcherModule());
		install(new PreparationModule());
		install(new DatastructureModule());
		install(new ReferenceSequenceModule());
		bind(ApproximateSearchAlgorithm.class).to(Rme1SearchAlgorithm.class);
	}
}
