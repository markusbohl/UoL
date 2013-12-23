package search.preparation;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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

import search.entity.ReferenceSequenceSection;
import search.entity.ReferencedSectionWithOffset;
import search.entity.SectionWithOffset;
import search.entity.SequenceSection;

import common.datastructure.ReferenceIndexStructure;
import common.preparation.StringProvider;
import common.preparation.StringProviderFactory;

public class SectionsProviderTest {

	private static final String FILE_PATH = "filePath";

	private SectionsProvider sectionsProvider;

	private int patternLength;

	private int allowedErrors;

	private SectionWithOffset seqSection;

	private ReferencedSectionWithOffset refSeqSection;

	private SectionWithOffset overlappingArea;

	private List<SectionWithOffset> sequenceSections;

	@Mock
	private CompressedSequenceParser parser;
	@Mock
	private StringProvider stringProvider;
	@Mock
	private OverlapBuilder overlapBuilder;
	@Mock
	private ReferenceIndexStructure indexStructure;
	@Mock
	private StringProviderFactory stringProviderFactory;

	@Before
	public void setUp() throws Exception {
		initMocks(this);

		patternLength = 5;
		allowedErrors = 1;
		seqSection = new SequenceSection(10, "ATAGAC");
		refSeqSection = new ReferenceSequenceSection(0, indexStructure, 2, 10);
		overlappingArea = new SequenceSection(8, "AGACGGTT");
		sequenceSections = Arrays.asList(refSeqSection, seqSection);

		when(stringProviderFactory.createFromFile(FILE_PATH)).thenReturn(stringProvider);
		when(stringProvider.toString()).thenReturn("RM(2,10)R(ATAGAC");
		when(parser.getAllEntries()).thenReturn(sequenceSections);
		when(parser.getRawEntries()).thenReturn(Arrays.asList(seqSection));
		when(parser.getRelativeMatchEntries()).thenReturn(Arrays.asList(refSeqSection));
		when(overlapBuilder.getOverlappingAreas()).thenReturn(Arrays.asList(overlappingArea));

		sectionsProvider = new SectionsProvider(stringProviderFactory, FILE_PATH, parser, overlapBuilder,
				patternLength, allowedErrors);
	}

	@Test
	public void testInitialization() {
		final InOrder inOrder = Mockito.inOrder(parser, overlapBuilder);
		inOrder.verify(parser).parse("RM(2,10)R(ATAGAC");
		inOrder.verify(overlapBuilder).feed(sequenceSections, 5, allowedErrors);
	}

	@Test
	public void getRawEntriesWhenRawSectionsNotIncludedInOverlapAreas() {
		final List<SectionWithOffset> rawEntries = sectionsProvider.getRawEntries();

		assertThat(rawEntries, hasSize(1));
		assertThat(rawEntries, hasItem(seqSection));
	}

	@Test
	public void getEmptyListWhenRawSectionsFullyIncludedInOverlapAreas() {
		when(overlapBuilder.rawSectionsFullyIncluded()).thenReturn(true);
		final SectionsProvider secProvider = new SectionsProvider(stringProviderFactory, FILE_PATH, parser,
				overlapBuilder, patternLength, allowedErrors);

		final List<SectionWithOffset> rawEntries = secProvider.getRawEntries();

		assertThat(rawEntries, is(empty()));
	}

	@Test
	public void getRelativeMatchEntries() {
		final List<ReferencedSectionWithOffset> relMatchEntries = sectionsProvider.getRelativeMatchEntries();

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
