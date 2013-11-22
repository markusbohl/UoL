package reader;

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

import entity.SectionWithOffset;
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
	public void parseRawEntries(final String string, final List<SectionWithOffset> expectedSections) {
		parser.parse(string, 3);

		assertEqualEntries(parser.getRawEntries(), expectedSections);
	}

	Object[] parametersForParseRawEntries() {
		return $(
				$("R(abc)", Arrays.asList(new SequenceSection("abc", 0))), //
				$("R(abc)R(def)", Arrays.asList(new SequenceSection("abc", 0), new SequenceSection("def", 3))), //
				$("R(abc)RM(2,5)R(def)", Arrays.asList(new SequenceSection("abc", 0), new SequenceSection("def", 8))), //
				$("RM(2,7)R(abc)RM(15,5)R(def)",
						Arrays.asList(new SequenceSection("abc", 7), new SequenceSection("def", 15))) //
		);
	}

	@Test
	@Parameters
	public void mindMinLengthWhenParsingRawEntries(final int minLength, final List<SectionWithOffset> expectedSections) {
		parser.parse("R(a)R(ab)R(abc)R(abcd)", minLength);

		assertEqualEntries(parser.getRawEntries(), expectedSections);
	}

	@Test
	public void parseRelativeMatchEntries() {
	}

	Object[] parametersForMindMinLengthWhenParsingRawEntries() {
		return $(
				$(5, Collections.<SectionWithOffset> emptyList()), //
				$(4, Arrays.asList(new SequenceSection("abcd", 6))), //
				$(3, Arrays.asList(new SequenceSection("abc", 3), new SequenceSection("abcd", 6))), //
				$(2, Arrays.asList(new SequenceSection("ab", 1), new SequenceSection("abc", 3), new SequenceSection(
						"abcd", 6))), //
				$(1, Arrays.asList(new SequenceSection("a", 0), new SequenceSection("ab", 1), new SequenceSection(
						"abc", 3), new SequenceSection("abcd", 6))), //
				$(0, Arrays.asList(new SequenceSection("a", 0), new SequenceSection("ab", 1), new SequenceSection(
						"abc", 3), new SequenceSection("abcd", 6))) //
		);
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
