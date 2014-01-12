package search;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import search.entity.ReferencedSectionWithOffset;
import search.entity.SectionWithOffset;
import search.matcher.ApproximateMatcher;
import search.preparation.SectionsProvider;
import search.preparation.SectionsProviderFactory;

public abstract class AbstractCompressedSequenceSearchAlgorithm implements ApproximateSearchAlgorithm {

	private final SectionsProviderFactory sectionsProviderFactory;
	private final ApproximateMatcher approximateMatcher;

	@Inject
	AbstractCompressedSequenceSearchAlgorithm(final SectionsProviderFactory sectionsProviderFactory,
			final ApproximateMatcher approximateMatcher) {
		this.sectionsProviderFactory = sectionsProviderFactory;
		this.approximateMatcher = approximateMatcher;
	}

	protected final ApproximateMatcher getApproximateMatcher() {
		return approximateMatcher;
	}

	@Override
	public Set<Integer> search(final String pattern, final int allowedErrors) {
		final SortedSet<Integer> matchingPositions = new TreeSet<>();
		final int patternLength = pattern.length();

		final SectionsProvider sectionsProvider = sectionsProviderFactory.createFor(patternLength, allowedErrors);
		final List<SectionWithOffset> rawEntries = rawEntries(sectionsProvider);
		final List<SectionWithOffset> overlappingAreas = overlappingAreas(sectionsProvider);
		final List<ReferencedSectionWithOffset> relEntries = relEntries(sectionsProvider);
		final List<ReferencedSectionWithOffset> preparedRelEntries = prepare(relEntries, pattern, allowedErrors);

		matchingPositions.addAll(matchesIn(preparedRelEntries, pattern, allowedErrors));
		matchingPositions.addAll(matchesIn(rawEntries, pattern, allowedErrors));
		matchingPositions.addAll(matchesIn(overlappingAreas, pattern, allowedErrors));

		return matchingPositions;
	}

	protected abstract List<ReferencedSectionWithOffset> prepare(List<ReferencedSectionWithOffset> relMatchEntries,
			final String pattern, final int allowedErrors);

	protected List<ReferencedSectionWithOffset> relEntries(final SectionsProvider sectionsProvider) {
		return sectionsProvider.getRelativeMatchEntries();
	}

	protected List<SectionWithOffset> rawEntries(final SectionsProvider sectionsProvider) {
		return sectionsProvider.getRawEntries();
	}

	protected List<SectionWithOffset> overlappingAreas(final SectionsProvider sectionsProvider) {
		return sectionsProvider.getOverlappingAreas();
	}

	protected List<Integer> matchesIn(final List<? extends SectionWithOffset> sections, final String pattern,
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
}