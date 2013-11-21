package reader;

import static entity.SectionType.RAW;
import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import entity.SequenceSection;

@RunWith(JUnitParamsRunner.class)
public class CompressedSequenceParserTest {

	private CompressedSequenceParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new CompressedSequenceParser();
	}

	@Test
	public void shouldReturnEmtpyListWhenNoRawEntriesExist() throws Exception {
		parser.parse("", 0);

		assertThat(parser.getRawEntries(), is(empty()));
	}

	@Test
	@Parameters
	public void parseRawEntries(final String string, final List<SequenceSection> expectedSections) {
		parser.parse(string, 3);

		assertEqualEntries(parser.getRawEntries(), expectedSections);
	}

	Object[] parametersForParseRawEntries() {
		return $(
				$("R(abc)", Arrays.asList(new SequenceSection(RAW, "abc", 0))), //
				$("R(abc)R(def)", Arrays.asList(new SequenceSection(RAW, "abc", 0), new SequenceSection(RAW, "def", 3))), //
				$("R(abc)RM(2,5)R(def)",
						Arrays.asList(new SequenceSection(RAW, "abc", 0), new SequenceSection(RAW, "def", 8))), //
				$("RM(2,7)R(abc)RM(15,5)R(def)",
						Arrays.asList(new SequenceSection(RAW, "abc", 7), new SequenceSection(RAW, "def", 15))) //
		);
	}

	@Test
	@Parameters
	public void mindMinLengthWhenParsingRawEntries(final int minLength, final List<SequenceSection> expectedSections) {
		parser.parse("R(a)R(ab)R(abc)R(abcd)", minLength);

		assertEqualEntries(parser.getRawEntries(), expectedSections);
	}

	Object[] parametersForMindMinLengthWhenParsingRawEntries() {
		return $(
				$(5, Collections.<SequenceSection> emptyList()), //
				$(4, Arrays.asList(new SequenceSection(RAW, "abcd", 6))), //
				$(3, Arrays.asList(new SequenceSection(RAW, "abc", 3), new SequenceSection(RAW, "abcd", 6))), //
				$(2, Arrays.asList(new SequenceSection(RAW, "ab", 1), new SequenceSection(RAW, "abc", 3),
						new SequenceSection(RAW, "abcd", 6))), //
				$(1, Arrays.asList(new SequenceSection(RAW, "a", 0), new SequenceSection(RAW, "ab", 1),
						new SequenceSection(RAW, "abc", 3), new SequenceSection(RAW, "abcd", 6))), //
				$(0, Arrays.asList(new SequenceSection(RAW, "a", 0), new SequenceSection(RAW, "ab", 1),
						new SequenceSection(RAW, "abc", 3), new SequenceSection(RAW, "abcd", 6))) //
		);
	}

	private static void assertEqualEntries(final List<SequenceSection> actualSections,
			final List<SequenceSection> expectedSections) {

		assertThat(actualSections.size(), is(expectedSections.size()));

		for (int i = 0; i < expectedSections.size(); i++) {
			final SequenceSection actual = actualSections.get(i);
			final SequenceSection expected = expectedSections.get(i);

			assertThat(actual.getSectionType(), is(expected.getSectionType()));
			assertThat(actual.getContent(), is(expected.getContent()));
			assertThat(actual.getLength(), is(expected.getLength()));
			assertThat(actual.getOffset(), is(expected.getOffset()));
		}
	}
}
