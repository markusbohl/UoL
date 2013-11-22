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
public class QGramPartitionerTest {

	private Partitioner partitioner;

	@Before
	public void setUp() throws Exception {
		partitioner = new QGramPartitioner();
	}

	@Test
	@Parameters
	public void partition(final String string, final int length, final String[] qGrams) {
		assertThat(partitioner.partition(string, length), arrayContaining(qGrams));
	}

	Object parametersForPartition() {
		return $($("abc", 3, new String[] { "abc" }), //
				$("abc", 2, new String[] { "ab", "bc" }), //
				$("abc", 1, new String[] { "a", "b", "c" }), //
				$("abcd", 3, new String[] { "abc", "bcd" }));
	}

	@Test(expected = IllegalArgumentException.class)
	@Parameters
	public void illegalArgumentsThrowException(final String string, final int length) {
		partitioner.partition(string, length);
	}

	Object parametersForIllegalArgumentsThrowException() {
		return $($(null, 1), $("", 1), $("abc", 0), $("abc", -1));
	}
}
