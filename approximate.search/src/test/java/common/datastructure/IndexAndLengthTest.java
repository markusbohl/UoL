package common.datastructure;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class IndexAndLengthTest {

	private static final int INDEX = 3;
	private static final int LENGTH = 8;

	private HasIndexAndLength indexAndLength;

	@Before
	public void setUp() throws Exception {
		indexAndLength = new IndexAndLength(INDEX, LENGTH);
	}

	@Test
	public void getIndex() {
		assertThat(indexAndLength.getIndex(), is(INDEX));
	}

	@Test
	public void getLength() throws Exception {
		assertThat(indexAndLength.getLength(), is(LENGTH));
	}
}
