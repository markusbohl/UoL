package search;

import search.matcher.ApproximateMatcher;
import search.preparation.NeighborhoodIdentifier;
import search.preparation.ReferenceFilter;
import search.preparation.SectionsProviderFactory;

public class ConcurrentCompressedSequenceSearchAlgorithmTest extends ApproximateSearchAlgorithmTest {

	private static final int NUM_OF_THREADS = 2;

	@Override
	protected ApproximateSearchAlgorithm createAlgorithmToTest(final ApproximateMatcher approximateMatcher,
			final SectionsProviderFactory sectionsProviderFactory, final NeighborhoodIdentifier neighborhoodIdentifier,
			final ReferenceFilter referenceFilter) {
		return new ConcurrentCompressedSequenceSearchAlgorithm(sectionsProviderFactory, neighborhoodIdentifier,
				referenceFilter, approximateMatcher, NUM_OF_THREADS);
	}
}
