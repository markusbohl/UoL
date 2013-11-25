package control;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import matcher.ApproximateMatcher;
import preparation.Partitioner;
import preparation.SectionsProvider;
import preparation.SectionsProviderFactory;
import datastructure.ReferenceIndexStructure;
import entity.SectionWithOffset;

public class CompressedSequenceSearchAlgorithm implements ApproximateSearchAlgorithm {

	private final Partitioner partitioner;
	private final ReferenceIndexStructure indexStructure;
	private final ApproximateMatcher approximateMatcher;
	private final SectionsProviderFactory sectionsProviderFactory;
	private SectionsProvider sectionsProvider;

	@Inject
	CompressedSequenceSearchAlgorithm(final SectionsProviderFactory sectionsProviderFactory,
			final Partitioner partitioner, final ReferenceIndexStructure indexStructure,
			final ApproximateMatcher approximateMatcher) {
		this.sectionsProviderFactory = sectionsProviderFactory;
		this.partitioner = partitioner;
		this.indexStructure = indexStructure;
		this.approximateMatcher = approximateMatcher;
	}

	@Override
	public List<Integer> search(final String pattern, final int allowedErrors) {
		final List<Integer> matchingPositions = new LinkedList<>();
		sectionsProvider = sectionsProviderFactory.createSectionsProviderFor(pattern.length());

		for (final String subpattern : partitioner.partition(pattern, allowedErrors)) {
			indexStructure.indicesOf(subpattern);
		}

		for (final SectionWithOffset rawSection : sectionsProvider.getRawEntries()) {
			matchingPositions.addAll(matchesInSection(rawSection, pattern, allowedErrors));
		}

		for (final SectionWithOffset overlappingArea : sectionsProvider.getOverlappingAreas()) {
			matchingPositions.addAll(matchesInSection(overlappingArea, pattern, allowedErrors));
		}

		return matchingPositions;
	}

	private List<Integer> matchesInSection(final SectionWithOffset section, final String pattern,
			final int allowedErrors) {
		final String content = section.getContent();
		final int offset = section.getOffset();
		return approximateMatcher.search(content, pattern, allowedErrors, offset);
	}
}
