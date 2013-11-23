package preparation;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import datastructure.ReferenceIndexStructure;
import entity.ReferenceSequenceSection;
import entity.SectionWithOffset;
import entity.SequenceSection;

public class SequenceSectionsProviderTest {

	private SequenceSectionsProvider sectionsProvider;

	@Mock
	private ReferenceIndexStructure indexStructure;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		sectionsProvider = new SequenceSectionsProvider();
	}

	@Test
	public void getRawEntries() {
		final SectionWithOffset rawSection1 = new SequenceSection(0, "abcd");
		final SectionWithOffset rawSection2 = new SequenceSection(4, "efg");
		final SectionWithOffset refSection1 = new ReferenceSequenceSection(7, indexStructure, 2, 5);
		final SectionWithOffset rawSection3 = new SequenceSection(12, "xyz");

		sectionsProvider.feed(Arrays.asList(rawSection1, rawSection2, refSection1, rawSection3), 3);
		final List<SectionWithOffset> rawEntries = sectionsProvider.getRawEntries();

		assertThat(rawEntries, hasItems(rawSection1, rawSection2, rawSection3));
		assertThat(rawEntries, not(hasItem(refSection1)));
	}

	@Test
	public void getRelativeMatchEntries() {
		final SectionWithOffset refSection1 = new ReferenceSequenceSection(0, indexStructure, 2, 5);
		final SectionWithOffset refSection2 = new ReferenceSequenceSection(5, indexStructure, 12, 7);
		final SectionWithOffset rawSection1 = new SequenceSection(12, "xyz");
		final SectionWithOffset refSection3 = new ReferenceSequenceSection(15, indexStructure, 2, 5);

		sectionsProvider.feed(Arrays.asList(refSection1, refSection2, rawSection1, refSection3), 3);
		final List<SectionWithOffset> refEntries = sectionsProvider.getRelativeMatchEntries();

		assertThat(refEntries, hasItems(refSection1, refSection2, refSection3));
		assertThat(refEntries, not(hasItem(rawSection1)));
	}

	@Test
	public void getOverlappingAreas() {
		final SectionWithOffset section1 = mock(SectionWithOffset.class);
		final SectionWithOffset section2 = mock(SectionWithOffset.class);
		final SectionWithOffset section3 = mock(SectionWithOffset.class);
		when(section1.getLastNCharacters(2)).thenReturn("ab");
		when(section2.getFirstNCharacters(2)).thenReturn("cd");
		when(section2.getLastNCharacters(2)).thenReturn("12");
		when(section3.getFirstNCharacters(2)).thenReturn("34");
		when(section1.getOffset()).thenReturn(0);
		when(section1.getLength()).thenReturn(10);
		when(section2.getOffset()).thenReturn(10);
		when(section2.getLength()).thenReturn(5);
		final int patternLength = 3;

		sectionsProvider.feed(Arrays.asList(section1, section2, section3), patternLength);
		final List<SectionWithOffset> overlappingAreas = sectionsProvider.getOverlappingAreas();

		assertThat(overlappingAreas, hasSize(2));
		assertThat(overlappingAreas.get(0).getContent(), is("abcd"));
		assertThat(overlappingAreas.get(0).getOffset(), is(8));
		assertThat(overlappingAreas.get(0).getLength(), is(4));
		assertThat(overlappingAreas.get(1).getContent(), is("1234"));
		assertThat(overlappingAreas.get(1).getOffset(), is(13));
		assertThat(overlappingAreas.get(1).getLength(), is(4));
	}
}
