package search.preparation;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class NonOverlappingPartitionerTest {

	private Partitioner partitioner;

	@Before
	public void setUp() {
		partitioner = new NonOverlappingPartitioner();
	}

	@Test
	@Parameters
	public void partition(final String string, final int noOfParts, final String[] expectedParts) {
		final String[] actualParts = partitioner.partition(string, noOfParts);

		assertThat(actualParts, arrayContaining(expectedParts));
	}

	Object parametersForPartition() {
		return $($("abcdef", 1, new String[] { "abcdef" }), //
				$("abcdef", 2, new String[] { "abc", "def" }), //
				$("abcdefg", 2, new String[] { "abc", "defg" }), //
				$("abcdef", 3, new String[] { "ab", "cd", "ef" }), //
				$("abcdefg", 3, new String[] { "ab", "cd", "efg" }));
	}
}
