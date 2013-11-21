package entity;

import static entity.SectionType.RAW;
import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
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

	@Test
	@Parameters
	public void getFirstNCharacters(final Integer n, final String expectedResult) throws Exception {
		assertThat(sequenceSection.getFirstNCharacters(n), is(expectedResult));
	}

	Object parametersForGetFirstNCharacters() {
		return $($(0, ""), $(1, "c"), $(2, "co"), $(3, "con"), $(4, "cont"), $(5, "conte"), $(6, "conten"),
				$(7, "content"), $(8, "content"));
	}

	@Test
	@Parameters
	public void getLastNCharacters(final Integer n, final String expectedResult) throws Exception {
		assertThat(sequenceSection.getLasttNCharacters(n), is(expectedResult));
	}

	Object parametersForGetLastNCharacters() {
		return $($(0, ""), $(1, "t"), $(2, "nt"), $(3, "ent"), $(4, "tent"), $(5, "ntent"), $(6, "ontent"),
				$(7, "content"), $(8, "content"));
	}
}
