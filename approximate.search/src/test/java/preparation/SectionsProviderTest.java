package preparation;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;

import datastructure.ReferenceIndexStructure;
import entity.ReferenceSequenceSection;
import entity.SectionWithOffset;
import entity.SequenceSection;

public class SectionsProviderTest {

	private SectionsProvider sectionsProvider;

	private int patternLength;

	private SectionWithOffset seqSection;

	private SectionWithOffset refSeqSection;

	private SectionWithOffset overlappingArea;

	private List<SectionWithOffset> sequenceSections;

	@Mock
	private CompressedSequenceParser parser;
	@Mock
	private StringReader reader;
	@Mock
	private OverlappingStringBuilder overlappingStringBuilder;
	@Mock
	private ReferenceIndexStructure indexStructure;

	@Before
	public void setUp() throws Exception {
		initMocks(this);

		patternLength = 5;
		seqSection = new SequenceSection(10, "ATAGAC");
		refSeqSection = new ReferenceSequenceSection(0, indexStructure, 2, 10);
		overlappingArea = new SequenceSection(8, "AGACGGTT");
		sequenceSections = Arrays.asList(refSeqSection, seqSection);

		when(reader.read()).thenReturn("RM(2,10)R(ATAGAC");
		when(parser.getAllEntries()).thenReturn(sequenceSections);
		when(parser.getRawEntries()).thenReturn(Arrays.asList(seqSection));
		when(parser.getRelativeMatchEntries()).thenReturn(Arrays.asList(refSeqSection));
		when(overlappingStringBuilder.getOverlappingAreas()).thenReturn(Arrays.asList(overlappingArea));

		sectionsProvider = new SectionsProvider(reader, parser, overlappingStringBuilder, patternLength);
	}

	@Test
	public void testInitialization() {
		final InOrder inOrder = Mockito.inOrder(parser, overlappingStringBuilder);
		inOrder.verify(parser).parse("RM(2,10)R(ATAGAC");
		inOrder.verify(overlappingStringBuilder).feed(sequenceSections, 5);
	}

	@Test
	public void getRawEntries() {
		final List<SectionWithOffset> rawEntries = sectionsProvider.getRawEntries();

		assertThat(rawEntries, hasSize(1));
		assertThat(rawEntries, hasItem(seqSection));
	}

	@Test
	public void getRelativeMatchEntries() {
		final List<SectionWithOffset> relMatchEntries = sectionsProvider.getRelativeMatchEntries();

		assertThat(relMatchEntries, hasSize(1));
		assertThat(relMatchEntries, hasItem(refSeqSection));
	}

	@Test
	public void getOverlappingAreas() {
		final List<SectionWithOffset> overlappingAreas = sectionsProvider.getOverlappingAreas();

		assertThat(overlappingAreas, hasSize(1));
		assertThat(overlappingAreas, hasItem(overlappingArea));
	}
}
