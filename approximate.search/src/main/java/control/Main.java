package control;

import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

	public static void main(final String[] args) {
		final Injector injector = Guice.createInjector(new ControlModule());
		final Initialization initialization = injector.getInstance(Initialization.class);
		initialization.init();

		final ApproximateSearchAlgorithm algorithm = injector.getInstance(ApproximateSearchAlgorithm.class);
		final List<Integer> results = algorithm.search("AAA", 1);
		print(results);
	}

	private static void print(final List<Integer> results) {
		for (final Integer integer : results) {
			System.out.println(integer);
		}
	}
}
