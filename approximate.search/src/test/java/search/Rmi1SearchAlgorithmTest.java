package search;

import search.matcher.ApproximateMatcher;
import search.preparation.NeighborhoodIdentifier;
import search.preparation.ReferenceFilter;
import search.preparation.SectionsProviderFactory;

public class CompressedSequenceSearchAlgorithmTest extends ApproximateSearchAlgorithmTest {

	@Override
	protected ApproximateSearchAlgorithm createAlgorithmToTest(final ApproximateMatcher approximateMatcher,
			final SectionsProviderFactory sectionsProviderFactory, final NeighborhoodIdentifier neighborhoodIdentifier,
			final ReferenceFilter referenceFilter) {
		return new CompressedSequenceSearchAlgorithm(sectionsProviderFactory, neighborhoodIdentifier,
				referenceFilter, approximateMatcher);
	}
}
