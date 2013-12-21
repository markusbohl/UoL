package search;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.common.collect.ImmutableSet;

public class SearchTest {

	private Search search;

	@Mock
	private ApproximateSearchAlgorithm searchAlgorithm;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		search = new Search(searchAlgorithm);
	}

	@Test
	public void doSearchFor() {
		final String query = "query";
		final int allowedErrors = 2;
		final Set<Integer> expectedResult = ImmutableSet.of(4, 18);
		when(searchAlgorithm.search(query, allowedErrors)).thenReturn(expectedResult);

		final Set<Integer> result = search.doSearchFor(query, 2);

		assertThat(result, is(expectedResult));
	}
}
