package search;

import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import search.matcher.ApproximateMatcher;

public class NaiveStringSearchAlgorithm implements ApproximateSearchAlgorithm {

	private final String text;
	private final ApproximateMatcher approximateMatcher;

	@Inject
	NaiveStringSearchAlgorithm(final String text, final ApproximateMatcher approximateMatcher) {
		this.text = text;
		this.approximateMatcher = approximateMatcher;
	}

	@Override
	public Set<Integer> search(final String pattern, final int allowedErrors) {
		approximateMatcher.init(pattern, allowedErrors);
		return new TreeSet<>(approximateMatcher.search(text, pattern, allowedErrors, 0));
	}
}
