package search;

import java.text.DateFormat;
import java.util.Date;
import java.util.Set;

import search.matcher.ApproximateMatcher;
import search.matcher.MatcherModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import common.ConfigurationModule;
import common.preparation.ReferenceSequenceModule;
import common.preparation.StringProvider;
import common.preparation.StringProviderFactory;

public class NaiveSearch {

	private static final DateFormat DATE_TIME_INSTANCE = DateFormat.getDateTimeInstance(DateFormat.SHORT,
			DateFormat.LONG);

	public static void main(final String[] args) throws InterruptedException {
		logWithTimeStamp("start");
		if (args.length != 4) {
			throw new IllegalArgumentException("application parameters missing or incomplete");
		}
		final String pattern = read("-p", args);
		final String allowedErrors = read("-k", args);

		Thread.sleep(10000);

		final Injector injector = createInjector();
		final String sequence = loadSequence(injector);
		logWithTimeStamp("sequence loaded");
		final ApproximateMatcher approximateMatcher = injector.getInstance(ApproximateMatcher.class);
		final NaiveStringSearchAlgorithm searchAlgorithm = new NaiveStringSearchAlgorithm(sequence, approximateMatcher);

		final Set<Integer> results = searchAlgorithm.search(pattern, Integer.valueOf(allowedErrors));
		log(results);
		logWithTimeStamp("finish");
	}

	private static String read(final String argument, final String[] args) {
		for (int i = 0; i < args.length - 1; i++) {
			if (args[i].equals(argument)) {
				return args[i + 1];
			}
		}
		throw new IllegalArgumentException();
	}

	private static Injector createInjector() {
		return Guice.createInjector(new MatcherModule(), new ConfigurationModule("/configuration/search.properties"),
				new ReferenceSequenceModule());
	}

	private static String loadSequence(final Injector injector) {
		final StringProviderFactory stringProviderFactory = injector.getInstance(StringProviderFactory.class);
		final String referenceFilePath = injector.getInstance(Key.get(String.class, Names.named("sequence.file.path")));
		final StringProvider referenceSequenceProvider = stringProviderFactory.createFromFastaFile(referenceFilePath);

		return referenceSequenceProvider.toString();
	}

	private static void log(final Set<Integer> results) {
		for (final Integer integer : results) {
			System.out.println(integer);
		}
	}

	public static void logWithTimeStamp(final String message) {
		System.out.println(message + ": " + DATE_TIME_INSTANCE.format(new Date()));
	}
}
