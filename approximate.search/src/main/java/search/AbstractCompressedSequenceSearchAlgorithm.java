package search;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import search.entity.SectionWithOffset;
import search.matcher.ApproximateMatcher;
import search.preparation.NeighborhoodIdentifier;
import search.preparation.ReferenceFilter;
import search.preparation.SectionsProvider;
import search.preparation.SectionsProviderFactory;

public abstract class AbstractCompressedSequenceSearchAlgorithm implements ApproximateSearchAlgorithm {

	private final SectionsProviderFactory sectionsProviderFactory;
	protected SectionsProvider sectionsProvider;
	private final ApproximateMatcher approximateMatcher;

	@Inject
	AbstractCompressedSequenceSearchAlgorithm(final SectionsProviderFactory sectionsProviderFactory,
			final ApproximateMatcher approximateMatcher, final NeighborhoodIdentifier neighborhoodIdentifier,
			final ReferenceFilter referenceFilter) {
		this.sectionsProviderFactory = sectionsProviderFactory;
		this.approximateMatcher = approximateMatcher;
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

	protected abstract List<Integer> matchesInRelativeMatchEntries(final String pattern, final int allowedErrors,
			final int minSectionLength);

	protected List<Integer> matchesInRawSections(final String pattern, final int allowedErrors) {
		final List<SectionWithOffset> rawEntries = sectionsProvider.getRawEntries();
		return searchInSections(rawEntries, pattern, allowedErrors);
	}

	protected List<Integer> matchesInOverlappingAreas(final String pattern, final int allowedErrors) {
		final List<SectionWithOffset> overlappingAreas = sectionsProvider.getOverlappingAreas();
		return searchInSections(overlappingAreas, pattern, allowedErrors);
	}

	protected List<Integer> searchInSections(final List<? extends SectionWithOffset> sections, final String pattern,
			final int allowedErrors) {
		final List<Integer> matchingPositions = new LinkedList<>();

		for (final SectionWithOffset section : sections) {
			final String text = section.getContent();
			final int offset = section.getOffset();

			final List<Integer> matchesInSection = approximateMatcher.search(text, pattern, allowedErrors, offset);
			matchingPositions.addAll(matchesInSection);
		}
		return matchingPositions;
	}

	protected ApproximateMatcher getApproximateMatcher() {
		return approximateMatcher;
	}
}