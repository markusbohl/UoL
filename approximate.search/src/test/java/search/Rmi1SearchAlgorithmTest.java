package search;

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
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import search.entity.ReferencedSectionWithOffset;
import search.entity.Section;
import search.matcher.ApproximateMatcher;
import search.preparation.NeighborhoodIdentifier;
import search.preparation.ReferenceFilter;
import search.preparation.SectionsProvider;
import search.preparation.SectionsProviderFactory;

import common.datastructure.ReferenceIndexStructure;

public class Rmi1SearchAlgorithmTest {

	protected static final int ALLOWED_ERRORS = 2;
	protected static final String PATTERN = "pattern";
	protected static final int MIN_LENGTH = PATTERN.length() - ALLOWED_ERRORS;

	protected ApproximateSearchAlgorithm algorithm;

	@Mock
	private ReferenceIndexStructure indexStructure;
	@Mock
	private SectionsProviderFactory sectionsProviderFactory;
	@Mock
	private ApproximateMatcher approximateMatcher;
	@Mock
	private NeighborhoodIdentifier neighborhoodIdentifier;
	@Mock
	private ReferenceFilter referenceFilter;
	@Mock
	protected SectionsProvider sectionProvider;
	@Mock
	protected ReferencedSectionWithOffset referencedSection1;
	@Mock
	protected ReferencedSectionWithOffset referencedSection2;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		when(sectionsProviderFactory.createFor(anyInt(), anyInt())).thenReturn(sectionProvider);
		when(referencedSection1.getContent()).thenReturn("content1");
		when(referencedSection1.getOffset()).thenReturn(23);
		when(referencedSection2.getContent()).thenReturn("content2");
		when(referencedSection2.getOffset()).thenReturn(44);
		when(sectionProvider.getRelativeMatchEntries()).thenReturn(
				Arrays.asList(referencedSection1, referencedSection2));

		algorithm = new Rme1SearchAlgorithm(sectionsProviderFactory, neighborhoodIdentifier, referenceFilter,
				approximateMatcher);
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

		final Set<Integer> matchingPositions = algorithm.search(PATTERN, ALLOWED_ERRORS);

		assertThat(matchingPositions, hasSize(3));
		assertThat(matchingPositions, hasItems(30, 50, 51));
	}
}
