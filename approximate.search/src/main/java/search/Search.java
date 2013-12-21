package search;

import java.util.Set;

import javax.inject.Inject;

import search.preparation.StringProvider;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import common.datastructure.ReferenceIndexStructure;

public class Search {

	private final ApproximateSearchAlgorithm searchAlgorithm;

	@Inject
	Search(final ApproximateSearchAlgorithm searchAlgorithm) {
		this.searchAlgorithm = searchAlgorithm;
	}

	Set<Integer> doSearchFor(final String query, final int allowedErrors) {
		return searchAlgorithm.search(query, allowedErrors);
	}

	public static void main(final String[] args) {
		final Injector injector = Guice.createInjector(new SearchModule());
		final String referenceSequence = loadReferenceSequence(injector);
		final ReferenceIndexStructure indexStructure = loadReferenceIndexStructure(injector);
		indexStructure.init(referenceSequence);

		final Search search = injector.getInstance(Search.class);
		final Set<Integer> results = search.doSearchFor("AAA", 1);
		log(results);
	}

	private static String loadReferenceSequence(final Injector injector) {
		final StringProvider referenceSequenceProvider = injector.getInstance(Key.get(StringProvider.class,
				Names.named("reference.sequence")));
		return referenceSequenceProvider.toString();
	}

	private static ReferenceIndexStructure loadReferenceIndexStructure(final Injector injector) {
		return injector.getInstance(ReferenceIndexStructure.class);
	}

	private static void log(final Set<Integer> results) {
		for (final Integer integer : results) {
			System.out.println(integer);
		}
	}
}
