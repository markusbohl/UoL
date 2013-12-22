package search;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import search.entity.SectionWithOffset;
import search.entity.SequenceSection;
import search.matcher.ApproximateMatcher;

public class SectionMatcherCallableTest {

	private Callable<List<Integer>> callable;

	private final SectionWithOffset section = new SequenceSection(19, "content");
	private final String pattern = "pattern";
	private final int allowedErrors = 2;

	@Mock
	private ApproximateMatcher matcher;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		callable = new SectionMatcherCallable(section, pattern, allowedErrors, matcher);
	}

	@Test
	public void returnSearchResultFromMatcher() throws Exception {
		final List<Integer> indices = Arrays.asList(37, 230);
		final String text = section.getContent();
		final int offset = section.getOffset();
		when(matcher.search(text, pattern, allowedErrors, offset)).thenReturn(indices);

		final List<Integer> result = callable.call();

		assertThat(result, is(indices));
	}
}
