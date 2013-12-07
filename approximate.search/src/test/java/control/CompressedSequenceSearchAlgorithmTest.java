package control;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import matcher.ApproximateMatcher;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import preparation.NeighborhoodIdentifier;
import preparation.ReferenceFilter;
import preparation.SectionsProvider;
import preparation.SectionsProviderFactory;
import datastructure.ReferenceIndexStructure;
import entity.ReferencedSectionWithOffset;
import entity.Section;
import entity.SectionWithOffset;

public class CompressedSequenceSearchAlgorithmTest {

	private static final int ALLOWED_ERRORS = 2;
	private static final String PATTERN = "pattern";
	private static final int MIN_LENGTH = PATTERN.length() - ALLOWED_ERRORS;

	private ApproximateSearchAlgorithm algorithm;

	@Mock
	private ReferenceIndexStructure indexStructure;
	@Mock
	private SectionsProviderFactory sectionsProviderFactory;
	@Mock
	private NeighborhoodIdentifier neighborhoodIdentifier;
	@Mock
	private ReferenceFilter referenceFilter;
	@Mock
	private ApproximateMatcher approximateMatcher;
	@Mock
	private SectionsProvider sectionProvider;
	@Mock
	private SectionWithOffset section1;
	@Mock
	private SectionWithOffset section2;
	@Mock
	private ReferencedSectionWithOffset referencedSection1;
	@Mock
	private ReferencedSectionWithOffset referencedSection2;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		when(sectionsProviderFactory.createSectionsProviderFor(anyInt())).thenReturn(sectionProvider);
		when(section1.getContent()).thenReturn("content1");
		when(section1.getOffset()).thenReturn(23);
		when(section2.getContent()).thenReturn("content2");
		when(section2.getOffset()).thenReturn(44);
		when(referencedSection1.getContent()).thenReturn("content1");
		when(referencedSection1.getOffset()).thenReturn(23);
		when(referencedSection2.getContent()).thenReturn("content2");
		when(referencedSection2.getOffset()).thenReturn(44);

		algorithm = new CompressedSequenceSearchAlgorithm(approximateMatcher, sectionsProviderFactory,
				neighborhoodIdentifier, referenceFilter);
	}

	@Test
	public void createSectionsProviderForGivenPatternLength() {
		algorithm.search(PATTERN, ALLOWED_ERRORS);

		verify(sectionsProviderFactory).createSectionsProviderFor(PATTERN.length());
	}

	@Test
	public void filterIdentifiedNeighborhoodAreas() {
		final List<Section> neighborhoodAreas = Arrays.asList(new Section(5, 16));
		final List<ReferencedSectionWithOffset> relativeMatchEntries = Arrays.asList(referencedSection1,
				referencedSection2);
		when(sectionProvider.getRelativeMatchEntries()).thenReturn(relativeMatchEntries);
		when(neighborhoodIdentifier.identifiyAreasFor(PATTERN, ALLOWED_ERRORS)).thenReturn(neighborhoodAreas);

		algorithm.search(PATTERN, ALLOWED_ERRORS);

		verify(referenceFilter).filter(relativeMatchEntries, neighborhoodAreas, MIN_LENGTH);
	}

	@Test
	public void searchForMatchesInRelativeMatchEntries() {
		final List<ReferencedSectionWithOffset> filteredSections = Arrays
				.asList(referencedSection1, referencedSection2);
		when(referenceFilter.filter(anyListOf(ReferencedSectionWithOffset.class), anyListOf(Section.class), anyInt()))
				.thenReturn(filteredSections);
		when(indexStructure.substring(5, 10)).thenReturn("content1");
		when(indexStructure.substring(10, 10)).thenReturn("content2");
		when(approximateMatcher.search("content1", PATTERN, ALLOWED_ERRORS, 23)).thenReturn(Arrays.asList(30));
		when(approximateMatcher.search("content2", PATTERN, ALLOWED_ERRORS, 44)).thenReturn(Arrays.asList(50, 51));

		final List<Integer> matchingPositions = algorithm.search(PATTERN, ALLOWED_ERRORS);

		assertThat(matchingPositions, hasSize(3));
		assertThat(matchingPositions, hasItems(30, 50, 51));
	}

	@Test
	public void searchForMatchesInRawEntries() {
		when(approximateMatcher.search("content1", PATTERN, ALLOWED_ERRORS, 23)).thenReturn(Arrays.asList(30, 40));
		when(approximateMatcher.search("content2", PATTERN, ALLOWED_ERRORS, 44)).thenReturn(Arrays.asList(50));
		when(sectionProvider.getRawEntries()).thenReturn(Arrays.asList(section1, section2));

		final List<Integer> matchingPositions = algorithm.search(PATTERN, ALLOWED_ERRORS);

		assertThat(matchingPositions, hasSize(3));
		assertThat(matchingPositions, hasItems(30, 40, 50));
	}

	@Test
	public void searchForMatchesInOverlappingAreas() {
		when(approximateMatcher.search("content1", PATTERN, ALLOWED_ERRORS, 23)).thenReturn(Arrays.asList(30, 40));
		when(approximateMatcher.search("content2", PATTERN, ALLOWED_ERRORS, 44)).thenReturn(Arrays.asList(50));
		when(sectionProvider.getOverlappingAreas()).thenReturn(Arrays.asList(section1, section2));

		final List<Integer> matchingPositions = algorithm.search(PATTERN, ALLOWED_ERRORS);

		assertThat(matchingPositions, hasSize(3));
		assertThat(matchingPositions, hasItems(30, 40, 50));
	}
}
