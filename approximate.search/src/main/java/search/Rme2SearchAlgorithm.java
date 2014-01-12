package search;

import java.util.List;

import javax.inject.Inject;

import search.entity.ReferencedSectionWithOffset;
import search.matcher.ApproximateMatcher;
import search.preparation.SectionsProviderFactory;

public class Rme2SearchAlgorithm extends AbstractCompressedSequenceSearchAlgorithm {

	@Inject
	Rme2SearchAlgorithm(final SectionsProviderFactory sectionsProviderFactory,
			final ApproximateMatcher approximateMatcher) {
		super(sectionsProviderFactory, approximateMatcher);
	}

	@Override
	protected final List<ReferencedSectionWithOffset> prepare(final List<ReferencedSectionWithOffset> relMatchEntries,
			final String pattern, final int allowedErrors) {
		return relMatchEntries;
	}
}
