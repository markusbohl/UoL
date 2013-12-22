package search;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import search.entity.ReferencedSectionWithOffset;
import search.entity.Section;
import search.entity.SectionWithOffset;
import search.preparation.NeighborhoodIdentifier;
import search.preparation.ReferenceFilter;
import search.preparation.SectionsProvider;
import search.preparation.SectionsProviderFactory;

public abstract class AbstractCompressedSequenceSearchAlgorithm implements ApproximateSearchAlgorithm {

	private final SectionsProviderFactory sectionsProviderFactory;
	private final NeighborhoodIdentifier neighborhoodIdentifier;
	private final ReferenceFilter referenceFilter;
	private SectionsProvider sectionsProvider;

	@Inject
	AbstractCompressedSequenceSearchAlgorithm(final SectionsProviderFactory sectionsProviderFactory,
			final NeighborhoodIdentifier neighborhoodIdentifier, final ReferenceFilter referenceFilter) {
		this.sectionsProviderFactory = sectionsProviderFactory;
		this.neighborhoodIdentifier = neighborhoodIdentifier;
		this.referenceFilter = referenceFilter;
	}

	@Override
	public Set<Integer> search(final String pattern, final int allowedErrors) {
		final SortedSet<Integer> matchingPositions = new TreeSet<>();
		final int patternLength = pattern.length();
		final int minSectionLength = patternLength - allowedErrors;

		sectionsProvider = sectionsProviderFactory.createSectionsProviderFor(patternLength, allowedErrors);

		matchingPositions.addAll(matchesInRelativeMatchEntries(pattern, allowedErrors, minSectionLength));
		matchingPositions.addAll(matchesInRawSections(pattern, allowedErrors));
		matchingPositions.addAll(matchesInOverlappingAreas(pattern, allowedErrors));

		return matchingPositions;
	}

	protected List<Integer> matchesInRelativeMatchEntries(final String pattern, final int allowedErrors,
			final int minSectionLength) {
		final List<Section> neighborhoodAreas = neighborhoodIdentifier.identifiyAreasFor(pattern, allowedErrors);
		final List<ReferencedSectionWithOffset> referencedSections = sectionsProvider.getRelativeMatchEntries();
		final List<ReferencedSectionWithOffset> filteredSections = referenceFilter.filter(referencedSections,
				neighborhoodAreas, minSectionLength);

		return searchInSections(filteredSections, pattern, allowedErrors);
	}

	protected List<Integer> matchesInRawSections(final String pattern, final int allowedErrors) {
		final List<SectionWithOffset> rawEntries = sectionsProvider.getRawEntries();

		return searchInSections(rawEntries, pattern, allowedErrors);
	}

	protected List<Integer> matchesInOverlappingAreas(final String pattern, final int allowedErrors) {
		final List<SectionWithOffset> overlappingAreas = sectionsProvider.getOverlappingAreas();

		return searchInSections(overlappingAreas, pattern, allowedErrors);
	}

	protected abstract List<Integer> searchInSections(final List<? extends SectionWithOffset> sections,
			final String pattern, final int allowedErrors);
}