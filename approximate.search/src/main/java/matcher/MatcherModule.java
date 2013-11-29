package matcher;

import com.google.inject.AbstractModule;

public class MatcherModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ApproximateMatcher.class).to(BitParallelMatrixBasedMatcher.class);
	}
}
