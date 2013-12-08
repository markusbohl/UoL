package control;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import matcher.ApproximateMatcher;
import preparation.NeighborhoodIdentifier;
import preparation.ReferenceFilter;
import preparation.SectionsProvider;
import preparation.SectionsProviderFactory;
import entity.ReferencedSectionWithOffset;
import entity.Section;
import entity.SectionWithOffset;

public class CompressedSequenceSearchAlgorithm implements ApproximateSearchAlgorithm {

	private final ApproximateMatcher approximateMatcher;
	private final SectionsProviderFactory sectionsProviderFactory;
	private final NeighborhoodIdentifier neighborhoodIdentifier;
	private final ReferenceFilter referenceFilter;
	private SectionsProvider sectionsProvider;

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
	public Set<Integer> search(final String pattern, final int allowedErrors) {
		final SortedSet<Integer> matchingPositions = new TreeSet<>();
		final int patternLength = pattern.length();
		final int minSectionLength = patternLength - allowedErrors;

		sectionsProvider = sectionsProviderFactory.createSectionsProviderFor(patternLength, allowedErrors);

		final List<Section> neighborhoodAreas = neighborhoodIdentifier.identifiyAreasFor(pattern, allowedErrors);
		final List<ReferencedSectionWithOffset> referencedSections = sectionsProvider.getRelativeMatchEntries();
		final List<ReferencedSectionWithOffset> filteredSections = referenceFilter.filter(referencedSections,
				neighborhoodAreas, minSectionLength);
		final List<SectionWithOffset> rawEntries = sectionsProvider.getRawEntries();
		final List<SectionWithOffset> overlappingAreas = sectionsProvider.getOverlappingAreas();

		matchingPositions.addAll(matchesInSections(filteredSections, pattern, allowedErrors));
		matchingPositions.addAll(matchesInSections(rawEntries, pattern, allowedErrors));
		matchingPositions.addAll(matchesInSections(overlappingAreas, pattern, allowedErrors));

		return matchingPositions;
	}

	private List<Integer> matchesInSections(final List<? extends SectionWithOffset> sections, final String pattern,
			final int allowedErrors) {
		final List<Integer> matchingPositions = new LinkedList<>();

		for (final SectionWithOffset section : sections) {
			final String text = section.getContent();
			final int offset = section.getOffset();

			matchingPositions.addAll(matchesInSection(text, pattern, allowedErrors, offset));
		}
		return matchingPositions;
	}

	private List<Integer> matchesInSection(final String text, final String pattern, final int allowedErrors,
			final int offset) {
		return approximateMatcher.search(text, pattern, allowedErrors, offset);
	}
}