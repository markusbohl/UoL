package search;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import search.matcher.ApproximateMatcher;

public class NaiveStringSearchAlgorithmTest {

	private static final String TEXT = "text";
	private static final String PATTERN = "pattern";
	private static final int ALLOWED_ERRORS = 2;

	private ApproximateSearchAlgorithm algorithm;

	@Mock
	private ApproximateMatcher approximateMatcher;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		algorithm = new NaiveStringSearchAlgorithm(TEXT, approximateMatcher);
	}

	@Test
	public void initApproximateMatcher() {
		algorithm.search(PATTERN, ALLOWED_ERRORS);

		verify(approximateMatcher).init(PATTERN, ALLOWED_ERRORS);
	}

	@Test
	public void searchText() {
		when(approximateMatcher.search(TEXT, PATTERN, ALLOWED_ERRORS, 0)).thenReturn(Arrays.asList(736));

		final Set<Integer> result = algorithm.search(PATTERN, ALLOWED_ERRORS);

		assertThat(result, hasSize(1));
		assertThat(result, hasItem(736));
	}
}
