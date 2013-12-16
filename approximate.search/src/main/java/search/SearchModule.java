package search;

import search.control.ControlModule;
import search.matcher.MatcherModule;
import search.preparation.PreparationModule;

import com.google.inject.AbstractModule;
import common.ConfigurationModule;
import common.datastructure.DatastructureModule;

public class SearchModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new ConfigurationModule());
		install(new MatcherModule());
		install(new PreparationModule());
		install(new DatastructureModule());
		install(new ControlModule());
	}
}
