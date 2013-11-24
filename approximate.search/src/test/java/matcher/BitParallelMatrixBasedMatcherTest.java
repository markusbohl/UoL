package matcher;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Set;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.ImmutableSet;

@RunWith(JUnitParamsRunner.class)
public class BitParallelMatrixBasedMatcherTest {

	private static final Set<Character> ALPHABET = ImmutableSet.of('A', 'C', 'G', 'T');

	private ApproximateMatcher matcher;

	@Before
	public void setUp() throws Exception {
		matcher = new BitParallelMatrixBasedMatcher(ALPHABET);
	}

	@Test
	@Parameters
	public void findMatches(final String text, final String pattern, final int allowedErrors, final int matchingPos) {
		final List<Integer> matchingPositions = matcher.search(text, pattern, allowedErrors);

		assertThat(matchingPositions, hasItem(matchingPos));
	}

	Object parametersForFindMatches() {
		return $($("AGCGGCT", "AGTCG", 1, 4));
	}
}
