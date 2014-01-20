package search.matcher;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class MatcherModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(new TypeLiteral<Set<Character>>() {
		}).toInstance(ImmutableSet.of('A', 'C', 'G', 'T', 'N'));
		bind(ApproximateMatcher.class).to(BitParallelMatrixBasedMatcher.class).asEagerSingleton();
		// bind(ApproximateMatcher.class).to(EmptyMatcher.class).asEagerSingleton();
	}
}
