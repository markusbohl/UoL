package search;

import java.util.List;

import javax.inject.Inject;

import search.entity.ReferencedSectionWithOffset;
import search.entity.Section;
import search.matcher.ApproximateMatcher;
import search.preparation.NeighborhoodIdentifier;
import search.preparation.ReferenceFilter;
import search.preparation.SectionsProviderFactory;

public class Rme1SearchAlgorithm extends AbstractCompressedSequenceSearchAlgorithm {

	private final NeighborhoodIdentifier neighborhoodIdentifier;
	private final ReferenceFilter referenceFilter;

	@Inject
	Rme1SearchAlgorithm(final SectionsProviderFactory sectionsProviderFactory,
			final NeighborhoodIdentifier neighborhoodIdentifier, final ReferenceFilter referenceFilter,
			final ApproximateMatcher approximateMatcher) {
		super(sectionsProviderFactory, approximateMatcher, neighborhoodIdentifier, referenceFilter);
		this.neighborhoodIdentifier = neighborhoodIdentifier;
		this.referenceFilter = referenceFilter;
	}

	@Override
	protected List<Integer> matchesInRelativeMatchEntries(final String pattern, final int allowedErrors,
			final int minSectionLength) {
		final List<ReferencedSectionWithOffset> referencedSections = sectionsProvider.getRelativeMatchEntries();
		final List<Section> neighborhoodAreas = neighborhoodIdentifier.identifiyAreasFor(pattern, allowedErrors);
		final List<ReferencedSectionWithOffset> filteredSections = referenceFilter.filter(referencedSections,
				neighborhoodAreas, minSectionLength);
		return searchInSections(filteredSections, pattern, allowedErrors);
	}
}