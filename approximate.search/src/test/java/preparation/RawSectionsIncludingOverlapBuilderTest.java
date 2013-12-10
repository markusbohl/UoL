package preparation;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import datastructure.ReferenceIndexStructure;
import entity.ReferencedSectionWithOffset;
import entity.SectionWithOffset;
import entity.SequenceSection;

@RunWith(JUnitParamsRunner.class)
public class RawSectionsIncludingOverlapBuilderTest {

	private OverlapBuilder overlapBuilder;

	@Mock
	private ReferenceIndexStructure indexStructure;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		overlapBuilder = new RawSectionsIncludingOverlapBuilder();
	}

	@Test
	@Parameters
	public void getOverlappingAreas(final List<SectionWithOffset> sequenceSections, final int patternLength,
			final int allowedErrors, final List<SectionWithOffset> expectedValues) {
		overlapBuilder.feed(sequenceSections, patternLength, allowedErrors);

		final List<SectionWithOffset> overlappingAreas = overlapBuilder.getOverlappingAreas();

		assertEqualValuesIn(overlappingAreas, expectedValues);
	}

	Object[] parametersForGetOverlappingAreas() {
		final int patternLength = 3;
		final int allowedErrors = 1;
		final int overlap = patternLength + allowedErrors - 1;

		return $(
				$(listOf(rawSection(0, "abcde"), refSection(5, "123456789", overlap)), patternLength, allowedErrors,
						listOf(rawSection(0, "abcde123"))), //

				$(listOf(refSection(0, "123456789", overlap), rawSection(9, "abcde")), patternLength, allowedErrors,
						listOf(rawSection(6, "789abcde"))), //

				$(listOf(refSection(0, "123456789", overlap), rawSection(9, "abcdefghi"),
						refSection(18, "ABCD", overlap)), patternLength, allowedErrors,
						listOf(rawSection(6, "789abcdefghiABC"))), //

				$(listOf(refSection(0, "123456789", overlap), rawSection(9, "abcdefghi"), rawSection(18, "_z_z_z_"),
						refSection(25, "ABCD", overlap)), patternLength, allowedErrors,
						listOf(rawSection(6, "789abcdefghi_z_z_z_ABC"))), //

				$(listOf(refSection(0, "123456789", overlap), rawSection(9, "abcdefghi"), rawSection(18, "_z_z_z_")),
						patternLength, allowedErrors, listOf(rawSection(6, "789abcdefghi_z_z_z_"))), //

				$(listOf(rawSection(0, "123456789"), rawSection(9, "abcdefghi"), rawSection(18, "_z_z_z_")),
						patternLength, allowedErrors, listOf(rawSection(0, "123456789abcdefghi_z_z_z_"))), //

				$(listOf(rawSection(0, "123456789"), rawSection(9, "abcdefghi"), refSection(18, "_z_z_z_", overlap)),
						patternLength, allowedErrors, listOf(rawSection(0, "123456789abcdefghi_z_"))) //
		);
	}

	@Test
	@Parameters
	public void mindLengthOfSectionsWhenBuildingOverlappingAreas(final List<SectionWithOffset> sequenceSections,
			final int patternLength, final int allowedErrors, final List<SectionWithOffset> expectedValues) {
		overlapBuilder.feed(sequenceSections, patternLength, allowedErrors);

		final List<SectionWithOffset> overlappingAreas = overlapBuilder.getOverlappingAreas();

		assertEqualValuesIn(overlappingAreas, expectedValues);
	}

	Object[] parametersForMindLengthOfSectionsWhenBuildingOverlappingAreas() {
		final int patternLength = 6;
		final int allowedErrors = 1;
		final int overlap = patternLength + allowedErrors - 1;

		return $(
				$(listOf(refSection(0, "12345", overlap), refSection(5, "abcde", overlap),
						refSection(10, "ABCDE", overlap)), patternLength, allowedErrors,
						listOf(rawSection(0, "12345abcdeA"), rawSection(4, "5abcdeABCDE"))),

				$(listOf(refSection(0, "12345", overlap), rawSection(5, "abcde"), refSection(10, "ABCDE", overlap)),
						patternLength, allowedErrors, listOf(rawSection(0, "12345abcdeABCDE"))),

				$(listOf(refSection(0, "12", overlap), rawSection(2, "ab"), refSection(4, "ABCDE", overlap)),
						patternLength, allowedErrors, listOf(rawSection(0, "12abABCDE"))),

				$(listOf(rawSection(0, "12345"), rawSection(5, "abcde"), refSection(10, "ABCDE", overlap)),
						patternLength, allowedErrors, listOf(rawSection(0, "12345abcdeABCDE"))),

				$(listOf(rawSection(0, "12"), rawSection(2, "ab"), refSection(4, "ABC", overlap)), patternLength,
						allowedErrors, listOf(rawSection(0, "12abABC"))),

				$(listOf(rawSection(0, "12345"), rawSection(5, "abcde"), refSection(10, "ABC", overlap),
						rawSection(13, "zzzzz"), rawSection(18, "00000")), patternLength, allowedErrors,
						listOf(rawSection(0, "12345abcdeABCzzz"), rawSection(7, "cdeABCzzzzz00000"))),

				$(listOf(refSection(0, "12345", overlap), refSection(5, "abcde", overlap), rawSection(10, "ABCD")),
						patternLength, allowedErrors, listOf(rawSection(0, "12345abcdeA"), rawSection(4, "5abcdeABCD"))),//

				$(listOf(refSection(0, "12345", overlap), refSection(5, "abcde", overlap),
						rawSection(10, "ABCDEFGHIJ"), refSection(20, "zzzzz", overlap)), patternLength, allowedErrors,
						listOf(rawSection(0, "12345abcdeA"), rawSection(4, "5abcdeABCDEFGHIJzzzzz")))//
		);
	}

	private List<SectionWithOffset> listOf(final SectionWithOffset... sections) {
		return Arrays.asList(sections);
	}

	private SequenceSection rawSection(final int offset, final String content) {
		return new SequenceSection(offset, content);
	}

	private ReferencedSectionWithOffset refSection(final int offset, final String content, final int overlap) {
		final ReferencedSectionWithOffset refSection = mock(ReferencedSectionWithOffset.class);
		when(refSection.getContent()).thenReturn(content);
		when(refSection.getOffset()).thenReturn(offset);
		when(refSection.getLength()).thenReturn(content.length());
		for (int i = 0; i <= overlap; i++) {
			when(refSection.getFirstNCharacters(i)).thenReturn(firstNCharsOf(content, i));
			when(refSection.getLastNCharacters(i)).thenReturn(lastNCharsOf(content, i));
		}

		return refSection;
	}

	private String firstNCharsOf(final String content, final int n) {
		if (n > content.length()) {
			return content;
		}
		return content.substring(0, n);
	}

	private String lastNCharsOf(final String content, final int n) {
		final int contentLength = content.length();
		if (n > contentLength) {
			return content;
		}
		return content.substring(contentLength - n, contentLength);
	}

	private void assertEqualValuesIn(final List<SectionWithOffset> actualValues,
			final List<SectionWithOffset> expectedValues) {
		assertThat(actualValues.size(), is(expectedValues.size()));
		for (int i = 0; i < actualValues.size(); i++) {
			final SectionWithOffset actual = actualValues.get(i);
			final SectionWithOffset expected = expectedValues.get(i);
			assertThat(actual.getContent(), is(expected.getContent()));
			assertThat(actual.getOffset(), is(expected.getOffset()));
			assertThat(actual.getLength(), is(expected.getLength()));
		}
	}

	@Test
	public void rawSectionsFullyIncluded() {
		assertThat(overlapBuilder.rawSectionsFullyIncluded(), is(true));
	}
}
