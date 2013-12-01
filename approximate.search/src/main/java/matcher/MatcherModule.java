package matcher;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class MatcherModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(new TypeLiteral<Set<Character>>() {
		}).toInstance(ImmutableSet.of('A', 'C', 'G', 'T'));
		bind(ApproximateMatcher.class).to(BitParallelMatrixBasedMatcher.class);
	}
}
