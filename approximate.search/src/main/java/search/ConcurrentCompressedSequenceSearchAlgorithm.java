package search;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.inject.Named;

import search.entity.SectionWithOffset;
import search.matcher.ApproximateMatcher;
import search.preparation.NeighborhoodIdentifier;
import search.preparation.ReferenceFilter;
import search.preparation.SectionsProviderFactory;

public class ConcurrentCompressedSequenceSearchAlgorithm extends AbstractCompressedSequenceSearchAlgorithm {

	private final ApproximateMatcher approximateMatcher;
	private final ExecutorService threadPool;

	@Inject
	ConcurrentCompressedSequenceSearchAlgorithm(final SectionsProviderFactory sectionsProviderFactory,
			final NeighborhoodIdentifier neighborhoodIdentifier, final ReferenceFilter referenceFilter,
			final ApproximateMatcher approximateMatcher, @Named("number.of.search.threads") final int numOfThreads) {

		super(sectionsProviderFactory, neighborhoodIdentifier, referenceFilter);
		this.approximateMatcher = approximateMatcher;
		this.threadPool = Executors.newFixedThreadPool(numOfThreads);
	}

	@Override
	protected final List<Integer> searchInSections(final List<? extends SectionWithOffset> sections,
			final String pattern, final int allowedErrors) {
		final List<Integer> matchingPositions = new LinkedList<>();
		final List<Future<List<Integer>>> futures = new LinkedList<>();

		for (final SectionWithOffset section : sections) {
			final SectionMatcherCallable callable = new SectionMatcherCallable(section, pattern, allowedErrors,
					approximateMatcher);
			final Future<List<Integer>> future = threadPool.submit(callable);
			futures.add(future);
		}

		for (final Future<List<Integer>> future : futures) {
			try {
				matchingPositions.addAll(future.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		return matchingPositions;
	}
}
