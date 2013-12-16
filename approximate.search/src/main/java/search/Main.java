package search;

import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Initialization;

public class Main {

	public static void main(final String[] args) {
		final Injector injector = Guice.createInjector(new SearchModule());
		final Initialization initialization = injector.getInstance(Initialization.class);
		initialization.init();

		final ApproximateSearchAlgorithm algorithm = injector.getInstance(ApproximateSearchAlgorithm.class);
		final Set<Integer> results = algorithm.search("AAA", 1);
		print(results);
	}

	private static void print(final Set<Integer> results) {
		for (final Integer integer : results) {
			System.out.println(integer);
		}
	}
}
