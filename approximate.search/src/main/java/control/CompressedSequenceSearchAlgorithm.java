package control;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import matcher.ApproximateMatcher;
import preparation.NeighborhoodIdentifier;
import preparation.ReferenceFilter;
import preparation.SectionsProvider;
import preparation.SectionsProviderFactory;
import entity.Section;
import entity.SectionWithOffset;

public class CompressedSequenceSearchAlgorithm implements ApproximateSearchAlgorithm {

	private final ApproximateMatcher approximateMatcher;
	private final SectionsProviderFactory sectionsProviderFactory;
	private SectionsProvider sectionsProvider;
	private final NeighborhoodIdentifier neighborhoodIdentifier;
	private final ReferenceFilter referenceFilter;

	@Inject
	CompressedSequenceSearchAlgorithm(final ApproximateMatcher approximateMatcher,
			final SectionsProviderFactory sectionsProviderFactory, final NeighborhoodIdentifier neighborhoodIdentifier,
			final ReferenceFilter referenceFilter) {
		this.sectionsProviderFactory = sectionsProviderFactory;
		this.neighborhoodIdentifier = neighborhoodIdentifier;
		this.referenceFilter = referenceFilter;
		this.approximateMatcher = approximateMatcher;
	}

	@Override
	public List<Integer> search(final String pattern, final int allowedErrors) {
		final List<Integer> matchingPositions = new LinkedList<>();

		sectionsProvider = sectionsProviderFactory.createSectionsProviderFor(pattern.length());

		final List<Section> neighborhoodAreas = neighborhoodIdentifier.identifiyAreasFor(pattern, allowedErrors);
		final List<SectionWithOffset> filteredReferenceSections = referenceFilter.filter(
				sectionsProvider.getRelativeMatchEntries(), neighborhoodAreas);

		matchingPositions.addAll(matchesInSections(pattern, allowedErrors, filteredReferenceSections));
		matchingPositions.addAll(matchesInSections(pattern, allowedErrors, sectionsProvider.getRawEntries()));
		matchingPositions.addAll(matchesInSections(pattern, allowedErrors, sectionsProvider.getOverlappingAreas()));

		return matchingPositions;
	}

	private List<Integer> matchesInSections(final String pattern, final int allowedErrors,
			final List<SectionWithOffset> sections) {
		final List<Integer> matchingPositions = new LinkedList<>();
		for (final SectionWithOffset rawSection : sections) {
			matchingPositions.addAll(matchesInSection(rawSection, pattern, allowedErrors));
		}
		return matchingPositions;
	}

	private List<Integer> matchesInSection(final SectionWithOffset section, final String pattern,
			final int allowedErrors) {
		return approximateMatcher.search(section.getContent(), pattern, allowedErrors, section.getOffset());
	}
}