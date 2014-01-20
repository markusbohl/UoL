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
		super(sectionsProviderFactory, approximateMatcher);
		this.neighborhoodIdentifier = neighborhoodIdentifier;
		this.referenceFilter = referenceFilter;
	}

	@Override
	protected List<ReferencedSectionWithOffset> prepare(final List<ReferencedSectionWithOffset> relMatchEntries,
			final String pattern, final int allowedErrors) {
		final List<Section> neighborhoodAreas = neighborhoodIdentifier.identifiyAreasFor(pattern, allowedErrors);
		System.out.println("neighborhoodAreas: " + neighborhoodAreas.size());
		final int patternLength = pattern.length();
		final int minSectionLength = patternLength - allowedErrors;
		final List<ReferencedSectionWithOffset> filteredSections = referenceFilter.filter(relMatchEntries,
				neighborhoodAreas, minSectionLength);
		System.out.println("filteredSections: " + filteredSections.size());
		return filteredSections;
	}
}