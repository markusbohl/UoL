package search;

import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

import common.datastructure.ReferenceIndexStructure;
import common.preparation.StringProvider;

public class Search {

	public static void main(final String[] args) {
		final Injector injector = Guice.createInjector(new SearchModule());
		final String referenceSequence = loadReferenceSequence(injector);
		final ReferenceIndexStructure indexStructure = loadReferenceIndexStructure(injector);
		indexStructure.init(referenceSequence);

		final ApproximateSearchAlgorithm algorithm = injector.getInstance(ApproximateSearchAlgorithm.class);
		final Set<Integer> results = algorithm.search("AAA", 1);
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
