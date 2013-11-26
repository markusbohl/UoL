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
		final int patternLength = pattern.length();
		final int typicalSearchLength = patternLength + 2 * allowedErrors;
		final String[] partition = partitioner.partition(pattern, allowedErrors);

		for (int i = 0; i < partition.length; i++) {
			final String subPattern = partition[i];
			final int subPatternLength = subPattern.length();
			for (final Integer posOfSubpatternInIndex : indexStructure.indicesOf(subPattern)) {
				if (i == 0) {
					indexStructure.substring(posOfSubpatternInIndex, patternLength + allowedErrors);
				} else if (i < partition.length - 1) {
					final int posOfSubpatternInPattern = i * subPatternLength;
					final int beginIndex = posOfSubpatternInIndex - posOfSubpatternInPattern - allowedErrors;
					if (beginIndex >= 0) {
						indexStructure.substring(beginIndex, typicalSearchLength);
					} else if (beginIndex + posOfSubpatternInPattern + allowedErrors >= 0) {
						final int length = patternLength - posOfSubpatternInPattern + allowedErrors;
						indexStructure.substring(0, length);
					}
				} else {
					final int beginIndex = posOfSubpatternInIndex - (patternLength - subPatternLength) - allowedErrors;
					if (beginIndex >= 0) {
						indexStructure.substring(beginIndex, typicalSearchLength);
					}
				}
			}
		}

		sectionsProvider = sectionsProviderFactory.createSectionsProviderFor(patternLength);

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
		return approximateMatcher.search(section.getContent(), pattern, allowedErrors, section.getOffset());
	}
}