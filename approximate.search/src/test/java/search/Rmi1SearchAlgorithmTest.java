package search;

import search.matcher.ApproximateMatcher;
import search.preparation.NeighborhoodIdentifier;
import search.preparation.ReferenceFilter;
import search.preparation.SectionsProviderFactory;

public class Rmi1SearchAlgorithmTest extends ApproximateSearchAlgorithmTest {

	@Override
	protected ApproximateSearchAlgorithm createAlgorithmToTest(final ApproximateMatcher approximateMatcher,
			final SectionsProviderFactory sectionsProviderFactory, final NeighborhoodIdentifier neighborhoodIdentifier,
			final ReferenceFilter referenceFilter) {
		return new Rme1SearchAlgorithm(sectionsProviderFactory, neighborhoodIdentifier,
				referenceFilter, approximateMatcher);
	}
}
