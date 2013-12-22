package search;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import search.entity.SectionWithOffset;
import search.matcher.ApproximateMatcher;
import search.preparation.NeighborhoodIdentifier;
import search.preparation.ReferenceFilter;
import search.preparation.SectionsProviderFactory;

public class CompressedSequenceSearchAlgorithm extends AbstractCompressedSequenceSearchAlgorithm {

	private final ApproximateMatcher approximateMatcher;

	@Inject
	CompressedSequenceSearchAlgorithm(final SectionsProviderFactory sectionsProviderFactory,
			final NeighborhoodIdentifier neighborhoodIdentifier, final ReferenceFilter referenceFilter,
			final ApproximateMatcher approximateMatcher) {

		super(sectionsProviderFactory, neighborhoodIdentifier, referenceFilter);
		this.approximateMatcher = approximateMatcher;
	}

	@Override
	protected final List<Integer> searchInSections(final List<? extends SectionWithOffset> sections,
			final String pattern, final int allowedErrors) {
		final List<Integer> matchingPositions = new LinkedList<>();

		for (final SectionWithOffset section : sections) {
			final String text = section.getContent();
			final int offset = section.getOffset();

			matchingPositions.addAll(searchInSection(text, pattern, allowedErrors, offset));
		}
		return matchingPositions;
	}

	private List<Integer> searchInSection(final String text, final String pattern, final int allowedErrors,
			final int offset) {
		return approximateMatcher.search(text, pattern, allowedErrors, offset);
	}
}