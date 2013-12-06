package control;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import matcher.ApproximateMatcher;
import preparation.NeighborhoodIdentifier;
import preparation.ReferenceFilter;
import preparation.SectionsProvider;
import preparation.SectionsProviderFactory;
import datastructure.ReferenceIndexStructure;
import entity.Section;
import entity.SectionWithOffset;

public class CompressedSequenceSearchAlgorithm implements ApproximateSearchAlgorithm {

	private final ApproximateMatcher approximateMatcher;
	private final SectionsProviderFactory sectionsProviderFactory;
	private final NeighborhoodIdentifier neighborhoodIdentifier;
	private final ReferenceFilter referenceFilter;
	private SectionsProvider sectionsProvider;
	private final ReferenceIndexStructure indexStructure;

	@Inject
	CompressedSequenceSearchAlgorithm(final ApproximateMatcher approximateMatcher,
			final SectionsProviderFactory sectionsProviderFactory, final NeighborhoodIdentifier neighborhoodIdentifier,
			final ReferenceFilter referenceFilter, final ReferenceIndexStructure indexStructure) {
		this.sectionsProviderFactory = sectionsProviderFactory;
		this.neighborhoodIdentifier = neighborhoodIdentifier;
		this.referenceFilter = referenceFilter;
		this.approximateMatcher = approximateMatcher;
		this.indexStructure = indexStructure;
	}

	@Override
	public List<Integer> search(final String pattern, final int allowedErrors) {
		final List<Integer> matchingPositions = new LinkedList<>();

		sectionsProvider = sectionsProviderFactory.createSectionsProviderFor(pattern.length());

		final int minLength = pattern.length() - allowedErrors;
		final List<Section> neighborhoodAreas = neighborhoodIdentifier.identifiyAreasFor(pattern, allowedErrors);
		final List<SectionWithOffset> referencedSections = sectionsProvider.getRelativeMatchEntries();
		final List<Section> filteredSections = referenceFilter.filter(referencedSections, neighborhoodAreas, minLength);
		final List<SectionWithOffset> rawEntries = sectionsProvider.getRawEntries();
		final List<SectionWithOffset> overlappingAreas = sectionsProvider.getOverlappingAreas();

		matchingPositions.addAll(matchesInReferenceSections(filteredSections, pattern, allowedErrors));
		matchingPositions.addAll(matchesInSections(rawEntries, pattern, allowedErrors));
		matchingPositions.addAll(matchesInSections(overlappingAreas, pattern, allowedErrors));

		return matchingPositions;
	}

	private List<Integer> matchesInReferenceSections(final List<Section> sections, final String pattern,
			final int allowedErrors) {
		final List<Integer> matchingPositions = new LinkedList<>();

		for (final Section section : sections) {
			final int startIndex = section.getStartIndex();
			final int length = section.getEndIndex() - startIndex;
			final String text = indexStructure.substring(startIndex, length);

			matchingPositions.addAll(matchesInSection(text, pattern, allowedErrors, startIndex));
		}

		return matchingPositions;
	}

	private List<Integer> matchesInSections(final List<SectionWithOffset> sections, final String pattern,
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