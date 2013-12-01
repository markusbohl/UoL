package control;

import matcher.MatcherModule;
import preparation.PreparationModule;

import com.google.inject.AbstractModule;

import datastructure.DatastructureModule;

public class ControlModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new MatcherModule());
		install(new PreparationModule());
		install(new DatastructureModule());
	}
}
