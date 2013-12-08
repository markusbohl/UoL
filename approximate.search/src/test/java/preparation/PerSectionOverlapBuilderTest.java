package preparation;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import entity.SectionWithOffset;
import entity.SequenceSection;

public class PerSectionOverlapBuilderTest {

	private OverlapBuilder sectionsProvider;

	@Before
	public void setUp() throws Exception {
		sectionsProvider = new PerSectionOverlapBuilder();
	}

	@Test
	public void getOverlappingAreas() {
		final SectionWithOffset section1 = new SequenceSection(0, "abcd");
		final SectionWithOffset section2 = new SequenceSection(4, "123456789");
		final SectionWithOffset section3 = new SequenceSection(13, "efgh");
		final int patternLength = 3;
		final int allowedErrors = 1;

		sectionsProvider.feed(Arrays.asList(section1, section2, section3), patternLength, allowedErrors);
		final List<SectionWithOffset> overlappingAreas = sectionsProvider.getOverlappingAreas();

		assertThat(overlappingAreas, hasSize(2));
		assertThat(overlappingAreas.get(0).getContent(), is("bcd123"));
		assertThat(overlappingAreas.get(0).getOffset(), is(1));
		assertThat(overlappingAreas.get(1).getContent(), is("789efg"));
		assertThat(overlappingAreas.get(1).getOffset(), is(10));
	}

	@Test
	public void mindLengthOfSectionsWhenBuildingOverlappingAreas() {
		final SectionWithOffset section1 = new SequenceSection(0, "abcd");
		final SectionWithOffset section2 = new SequenceSection(4, "efgh");
		final SectionWithOffset section3 = new SequenceSection(8, "ijkl");
		final SectionWithOffset section4 = new SequenceSection(12, "mnop");
		final int patternLength = 6;
		final int allowedErrors = 1;

		sectionsProvider.feed(Arrays.asList(section1, section2, section3, section4), patternLength, allowedErrors);
		final List<SectionWithOffset> overlappingAreas = sectionsProvider.getOverlappingAreas();

		assertThat(overlappingAreas, hasSize(3));

		assertThat(overlappingAreas.get(0).getContent(), is("abcdefghij"));
		assertThat(overlappingAreas.get(0).getOffset(), is(0));
		assertThat(overlappingAreas.get(1).getContent(), is("cdefghijklmn"));
		assertThat(overlappingAreas.get(1).getOffset(), is(2));
		assertThat(overlappingAreas.get(2).getContent(), is("ghijklmnop"));
		assertThat(overlappingAreas.get(2).getOffset(), is(6));
	}
}
