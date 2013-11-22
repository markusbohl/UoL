package reader;

import static java.util.Arrays.asList;
import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import datastructure.ReferenceIndexStructure;
import entity.ReferenceSequenceSection;
import entity.SectionWithOffset;
import entity.SequenceSection;

@RunWith(JUnitParamsRunner.class)
public class CompressedSequenceParserTest {

	private CompressedSequenceParser parser;

	@Mock
	private ReferenceIndexStructure indexStructure;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		parser = new CompressedSequenceParser(indexStructure);
	}

	@Test
	public void shouldReturnEmtpyListWhenNoRawEntriesExist() throws Exception {
		parser.parse("");

		assertThat(parser.getRawEntries(), is(empty()));
		assertThat(parser.getRelativeMatchEntries(), is(empty()));
	}

	@Test
	@Parameters
	public void parseRawEntries(final String string, final List<SectionWithOffset> expectedSections) {
		parser.parse(string);

		assertEqualEntries(parser.getRawEntries(), expectedSections);
	}

	Object parametersForParseRawEntries() {
		return $($("R(abc)", asList(new SequenceSection(0, "abc"))),
				$("R(abc)R(def)", asList(new SequenceSection(0, "abc"), new SequenceSection(3, "def"))),
				$("R(abc)RM(2,5)R(def)", asList(new SequenceSection(0, "abc"), new SequenceSection(8, "def"))),
				$("RM(2,7)R(abc)RM(15,5)R(def)", asList(new SequenceSection(7, "abc"), new SequenceSection(15, "def"))));
	}

	@Test
	@Parameters
	public void parseRelativeMatchEntries(final String string, final List<SectionWithOffset> expectedSections) {
		when(indexStructure.substring(anyInt(), anyInt())).thenReturn("anyContent");
		parser.parse(string);

		assertEqualEntries(parser.getRelativeMatchEntries(), expectedSections);
	}

	Object parametersForParseRelativeMatchEntries() {
		final ReferenceIndexStructure ris = mock(ReferenceIndexStructure.class);
		when(ris.substring(anyInt(), anyInt())).thenReturn("anyContent");

		return $(
				$("RM(2,5)", asList(new ReferenceSequenceSection(0, ris, 2, 5))),
				$("RM(2,5)RM(7,10)",
						asList(new ReferenceSequenceSection(0, ris, 2, 5), new ReferenceSequenceSection(5, ris, 7, 10))),
				$("RM(2,5)R(abc)RM(7,10)",
						asList(new ReferenceSequenceSection(0, ris, 2, 5), new ReferenceSequenceSection(8, ris, 7, 10))),
				$("R(abc)RM(2,5)R(def)RM(7,10)",
						asList(new ReferenceSequenceSection(3, ris, 2, 5), new ReferenceSequenceSection(11, ris, 7, 10))));
	}

	@Test
	public void verifySettingOfCorrectRefIndex() {
		parser.parse("RM(2,5)");
		parser.getRelativeMatchEntries().get(0).getContent();

		verify(indexStructure).substring(2, 5);
	}

	private static void assertEqualEntries(final List<SectionWithOffset> actualSections,
			final List<SectionWithOffset> expectedSections) {

		assertThat(actualSections.size(), is(expectedSections.size()));

		for (int i = 0; i < expectedSections.size(); i++) {
			final SectionWithOffset actual = actualSections.get(i);
			final SectionWithOffset expected = expectedSections.get(i);

			assertThat(actual.getContent(), is(expected.getContent()));
			assertThat(actual.getLength(), is(expected.getLength()));
			assertThat(actual.getOffset(), is(expected.getOffset()));
		}
	}
}
