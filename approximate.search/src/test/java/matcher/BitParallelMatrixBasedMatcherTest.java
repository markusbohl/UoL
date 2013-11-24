package matcher;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
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

	private static final Set<Character> ALPHABET = ImmutableSet.of('A', 'C', 'G', 'T', 'N');

	private ApproximateMatcher matcher;

	@Before
	public void setUp() throws Exception {
		matcher = new BitParallelMatrixBasedMatcher(ALPHABET);
	}

	@Test
	@Parameters
	public void findMatches(final String text, final String pattern, final int allowedErrors,
			final Integer[] expectedPositions) {
		final List<Integer> actualPositions = matcher.search(text, pattern, allowedErrors);

		assertThat(actualPositions, hasSize(expectedPositions.length));
		assertThat(actualPositions, hasItems(expectedPositions));
	}

	Object parametersForFindMatches() {
		return $($("NNNNNNN", "AAA", 0, new Integer[] {}), $("NNAAANN", "AAA", 0, new Integer[] { 4 }),
				$("AANNN", "AAA", 1, new Integer[] { 1, 2 }), $("NNNAA", "AAA", 1, new Integer[] { 4 }),
				$("AGCGGCT", "AGTCG", 1, new Integer[] { 3 }), $("AGCGGCT", "AGTCG", 2, new Integer[] { 2, 3, 4 }),
				$("AGCGGCTAGCGGCT", "AGTCG", 1, new Integer[] { 3, 10 }));
	}
}
