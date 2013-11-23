package preparation;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class KPlusPartitionerTest {

	private Partitioner partitioner;

	@Before
	public void setUp() {
		partitioner = new KPlusPartitioner();
	}

	@Test
	@Parameters
	public void partition(final String string, final int length, final String[] parts) {
		assertThat(partitioner.partition(string, length), arrayContaining(parts));
	}

	Object parametersForPartition() {
		return $($("abcdef", 2, new String[] { "abc", "def" }), //
				$("abcdefg", 2, new String[] { "abc", "defg" }), //
				$("abcdef", 3, new String[] { "ab", "cd", "ef" }), //
				$("abcdefg", 3, new String[] { "ab", "cd", "efg" }));
	}
}
