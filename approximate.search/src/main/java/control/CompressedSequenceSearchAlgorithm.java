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

		matchingPositions.addAll(matchesInReference(pattern, allowedErrors));
		matchingPositions.addAll(matchesInRawSection(pattern, allowedErrors));
		matchingPositions.addAll(matchesInOverlappingAres(pattern, allowedErrors));

		return matchingPositions;
	}

	private List<Integer> matchesInReference(final String pattern, final int allowedErrors) {
		final List<Integer> matchingPositions = new LinkedList<>();
		final int patternLength = pattern.length();
		final int typicalSearchLength = patternLength + 2 * allowedErrors;
		final String[] partition = partitioner.partition(pattern, allowedErrors + 1);

		for (int i = 0; i < partition.length; i++) {
			final String subPattern = partition[i];
			final int subPatternLength = subPattern.length();
			final int posOfSubpatternInPattern = i * subPatternLength;

			for (final Integer posOfSubpatternInIndex : indexStructure.indicesOf(subPattern)) {
				if (isFirstSubpattern(i)) {
					final int length = patternLength + allowedErrors;
					matchingPositions
							.addAll(matchesInReference(posOfSubpatternInIndex, length, pattern, allowedErrors));
				} else if (isNotLastSubpattern(partition, i)) {
					final int beginIndex = posOfSubpatternInIndex - posOfSubpatternInPattern - allowedErrors;
					matchingPositions
							.addAll(matchesInReference(beginIndex, typicalSearchLength, pattern, allowedErrors));

					if (beginIndex >= 0) {
						matchingPositions.addAll(matchesInReference(beginIndex, typicalSearchLength, pattern,
								allowedErrors));
					} else if (posOfSubpatternInPattern <= allowedErrors) {
						final int length = patternLength - posOfSubpatternInPattern + allowedErrors;
						matchingPositions.addAll(matchesInReference(0, length, pattern, allowedErrors));
					}
				} else {
					final int beginIndex = posOfSubpatternInIndex - (patternLength - subPatternLength) - allowedErrors;
					if (beginIndex >= 0) {
						matchingPositions.addAll(matchesInReference(beginIndex, typicalSearchLength, pattern,
								allowedErrors));
					}
				}
			}
		}
		return matchingPositions;
	}

	private List<Integer> matchesInReference(final int beginIndex, final int length, final String pattern,
			final int allowedErrors) {
		if (beginIndex < 0) {
			if (beginIndex + allowedErrors < 0) {

			}
		}
		final String substring = indexStructure.substring(beginIndex, length);
		return approximateMatcher.search(substring, pattern, allowedErrors, 0);
	}

	private boolean isNotLastSubpattern(final String[] partition, final int index) {
		return index < partition.length - 1;
	}

	private boolean isFirstSubpattern(final int index) {
		return index == 0;
	}

	private List<Integer> matchesInOverlappingAres(final String pattern, final int allowedErrors) {
		final List<Integer> matchingPositions = new LinkedList<>();
		for (final SectionWithOffset overlappingArea : sectionsProvider.getOverlappingAreas()) {
			matchingPositions.addAll(matchesInSection(overlappingArea, pattern, allowedErrors));
		}
		return matchingPositions;
	}

	private List<Integer> matchesInRawSection(final String pattern, final int allowedErrors) {
		final List<Integer> matchingPositions = new LinkedList<>();
		for (final SectionWithOffset rawSection : sectionsProvider.getRawEntries()) {
			matchingPositions.addAll(matchesInSection(rawSection, pattern, allowedErrors));
		}
		return matchingPositions;
	}

	private List<Integer> matchesInSection(final SectionWithOffset section, final String pattern,
			final int allowedErrors) {
		return approximateMatcher.search(section.getContent(), pattern, allowedErrors, section.getOffset());
	}
}