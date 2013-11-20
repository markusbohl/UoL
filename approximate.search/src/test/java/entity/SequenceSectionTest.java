package entity;

import static entity.SectionType.RAW;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class SequenceSectionTest {

	private SequenceSection sequenceSection;

	@Before
	public void setUp() throws Exception {
		sequenceSection = new SequenceSection(RAW, "content", 5);
	}

	@Test
	public void getContent() throws Exception {
		assertThat(sequenceSection.getContent(), is("content"));
	}

	@Test
	public void getLength() throws Exception {
		assertThat(sequenceSection.getLength(), is("content".length()));
	}

	@Test
	public void getOffset() throws Exception {
		assertThat(sequenceSection.getOffset(), is(5));
	}

	@Test
	public void getType() throws Exception {
		assertThat(sequenceSection.getSectionType(), is(RAW));
	}
}
