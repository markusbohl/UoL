package entity;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class SectionTest {

	private static final int START_INDEX = 5;
	private static final int END_INDEX = 10;

	private Section section;

	@Before
	public void setUp() throws Exception {
		section = new Section(START_INDEX, END_INDEX);
	}

	@Test
	public void getStartIndex() {
		assertThat(section.getStartIndex(), is(START_INDEX));
	}

	@Test
	public void getEndIndex() {
		assertThat(section.getEndIndex(), is(END_INDEX));
	}
}
