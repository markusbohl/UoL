package search;

import static search.AlgorithmVariant.Rme1_RawOA_1;
import static search.AlgorithmVariant.Rme1_RawOA_2;
import search.matcher.MatcherModule;
import search.preparation.PreparationModule;

import com.google.inject.AbstractModule;
import common.ConfigurationModule;
import common.datastructure.DatastructureModule;
import common.preparation.ReferenceSequenceModule;

public class SearchModule extends AbstractModule {

	private final AlgorithmVariant variant;

	public SearchModule(final AlgorithmVariant variant) {
		this.variant = variant;
	}

	@Override
	protected void configure() {
		install(new ConfigurationModule("/configuration/reference.properties"));
		install(new ConfigurationModule("/configuration/search.properties"));
		install(new MatcherModule());
		install(new PreparationModule(variant));
		install(new DatastructureModule());
		install(new ReferenceSequenceModule());

		if (Rme1_RawOA_1 == variant || Rme1_RawOA_2 == variant) {
			bind(ApproximateSearchAlgorithm.class).to(Rme1SearchAlgorithm.class);
		} else {
			bind(ApproximateSearchAlgorithm.class).to(Rme2SearchAlgorithm.class);
		}
	}
}
