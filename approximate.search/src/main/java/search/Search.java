package search;

import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import common.datastructure.ReferenceIndexStructure;
import common.preparation.StringProvider;
import common.preparation.StringProviderFactory;

public class Search {

	public static void main(final String[] args) {
		final Injector injector = Guice.createInjector(new SearchModule());
		final StringProviderFactory stringProviderFactory = getStringProviderFactory(injector);
		final String referenceFilePath = getReferenceFilePath(injector);
		final String referenceSequence = getFastaContentFromFile(referenceFilePath, stringProviderFactory);
		final ReferenceIndexStructure indexStructure = getReferenceIndexStructure(injector);
		indexStructure.init(referenceSequence);

		final ApproximateSearchAlgorithm algorithm = injector.getInstance(ApproximateSearchAlgorithm.class);
		final Integer allowedErrors = injector.getInstance(Key.get(Integer.class, Names.named("allowed.errors")));
		final Set<Integer> results = algorithm.search("ACTAGATGATCAAATTTA", allowedErrors);
		log(results);
	}

	private static StringProviderFactory getStringProviderFactory(final Injector injector) {
		return injector.getInstance(StringProviderFactory.class);
	}

	private static String getFastaContentFromFile(final String referenceFilePath,
			final StringProviderFactory stringProviderFactory) {
		final StringProvider referenceSequenceProvider = stringProviderFactory.createFromFastaFile(referenceFilePath);

		return referenceSequenceProvider.toString();
	}

	private static String getReferenceFilePath(final Injector injector) {
		return injector.getInstance(Key.get(String.class, Names.named("reference.sequence.file.path")));
	}

	private static ReferenceIndexStructure getReferenceIndexStructure(final Injector injector) {
		return injector.getInstance(ReferenceIndexStructure.class);
	}

	private static void log(final Set<Integer> results) {
		for (final Integer integer : results) {
			System.out.println(integer);
		}
	}
}
