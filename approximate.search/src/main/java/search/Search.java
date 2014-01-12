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

	/**
	 * Arguments:<br>
	 * <ul>
	 * <li><code>-p pattern</code></li>
	 * <li><code>-k allowedErrors</code></li>
	 * <li>algorithm: <code>-a 1 | 2 | 3 | 4</code>
	 * <ul>
	 * <li><code>-a 1</code> = RME_1 & RawOA_1</li>
	 * <li><code>-a 2</code> = RME_1 & RawOA_2</li>
	 * <li><code>-a 3</code> = RME_2 & RawOA_1</li>
	 * <li><code>-a 4</code> = RME_2 & RawOA_2</li>
	 * </ul>
	 * </ul>
	 */
	public static void main(final String[] args) {
		if (args.length != 6) {
			throw new IllegalArgumentException("application parameters missing or incomplete");
		}
		final String pattern = read("-p", args);
		final String allowedErrors = read("-k", args);
		final Integer variant = Integer.valueOf(read("-a", args));

		final Injector injector = Guice.createInjector(new SearchModule(AlgorithmVariant.get(variant)));
		final String referenceSequence = loadReferenceSequence(injector);
		final ReferenceIndexStructure indexStructure = getReferenceIndexStructure(injector);
		indexStructure.init(referenceSequence);

		final ApproximateSearchAlgorithm algorithm = injector.getInstance(ApproximateSearchAlgorithm.class);
		final Set<Integer> results = algorithm.search(pattern, Integer.valueOf(allowedErrors));
		log(results);
	}

	private static String read(final String argument, final String[] args) {
		for (int i = 0; i < args.length - 1; i++) {
			if (args[i].equals(argument)) {
				return args[i + 1];
			}
		}
		throw new IllegalArgumentException();
	}

	private static String loadReferenceSequence(final Injector injector) {
		final StringProviderFactory stringProviderFactory = injector.getInstance(StringProviderFactory.class);
		final String referenceFilePath = injector.getInstance(Key.get(String.class,
				Names.named("reference.sequence.file.path")));
		final StringProvider referenceSequenceProvider = stringProviderFactory.createFromFastaFile(referenceFilePath);

		return referenceSequenceProvider.toString();
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
