package control;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import matcher.ApproximateMatcher;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import preparation.Partitioner;
import preparation.SectionsProvider;
import preparation.SectionsProviderFactory;
import datastructure.ReferenceIndexStructure;
import entity.SectionWithOffset;

public class CompressedSequenceSearchAlgorithmTest {

	private static final int ALLOWED_ERRORS = 2;

	private static final String PATTERN = "pattern";

	private static final int PATTERN_LENGTH = PATTERN.length();

	private ApproximateSearchAlgorithm searchAlgorithm;

	@Mock
	private SectionsProviderFactory sectionsProviderFactory;
	@Mock
	private Partitioner partitioner;
	@Mock
	private ReferenceIndexStructure indexStructure;
	@Mock
	private ApproximateMatcher matcher;
	@Mock
	private SectionsProvider sectionProvider;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		searchAlgorithm = new CompressedSequenceSearchAlgorithm(sectionsProviderFactory, partitioner, indexStructure,
				matcher);
		when(partitioner.partition(eq(PATTERN), anyInt())).thenReturn(new String[] { "pa", "tt", "ern" });
		when(sectionsProviderFactory.createSectionsProviderFor(anyInt())).thenReturn(sectionProvider);
	}

	@Test
	public void createSectionsProviderForGivenPatternLength() {
		searchAlgorithm.search(PATTERN, 2);

		verify(sectionsProviderFactory).createSectionsProviderFor(PATTERN.length());
	}

	@Test
	public void searchIndexStructureForSubpatterns() {
		searchAlgorithm.search(PATTERN, ALLOWED_ERRORS);

		verify(indexStructure).indicesOf("pa");
		verify(indexStructure).indicesOf("tt");
		verify(indexStructure).indicesOf("ern");
	}

	@Test
	public void searchForMatchesInRawEntries() {
		final SectionWithOffset section1 = mock(SectionWithOffset.class);
		final SectionWithOffset section2 = mock(SectionWithOffset.class);
		when(section1.getContent()).thenReturn("content1");
		when(section1.getOffset()).thenReturn(23);
		when(section2.getContent()).thenReturn("content2");
		when(section2.getOffset()).thenReturn(44);
		when(sectionProvider.getRawEntries()).thenReturn(Arrays.asList(section1, section2));
		when(matcher.search("content1", PATTERN, ALLOWED_ERRORS, 23)).thenReturn(Arrays.asList(30, 40));
		when(matcher.search("content2", PATTERN, ALLOWED_ERRORS, 44)).thenReturn(Arrays.asList(50));

		final List<Integer> matchingPositions = searchAlgorithm.search(PATTERN, ALLOWED_ERRORS);

		assertThat(matchingPositions, hasSize(3));
		assertThat(matchingPositions, hasItems(30, 40, 50));
	}

	@Test
	public void searchForMatchesInOverlappingAreas() {
		final SectionWithOffset section1 = mock(SectionWithOffset.class);
		final SectionWithOffset section2 = mock(SectionWithOffset.class);
		when(section1.getContent()).thenReturn("content1");
		when(section1.getOffset()).thenReturn(23);
		when(section2.getContent()).thenReturn("content2");
		when(section2.getOffset()).thenReturn(44);
		when(sectionProvider.getOverlappingAreas()).thenReturn(Arrays.asList(section1, section2));
		when(matcher.search("content1", PATTERN, ALLOWED_ERRORS, 23)).thenReturn(Arrays.asList(30, 40));
		when(matcher.search("content2", PATTERN, ALLOWED_ERRORS, 44)).thenReturn(Arrays.asList(50));

		final List<Integer> matchingPositions = searchAlgorithm.search(PATTERN, ALLOWED_ERRORS);

		assertThat(matchingPositions, hasSize(3));
		assertThat(matchingPositions, hasItems(30, 40, 50));
	}

	@Test
	public void determineNeighborhoodAreaForFirstSubpattern() {
		when(indexStructure.indicesOf("pa")).thenReturn(Arrays.asList(0, 10));
		when(indexStructure.indicesOf("tt")).thenReturn(Collections.<Integer> emptyList());
		when(indexStructure.indicesOf("ern")).thenReturn(Collections.<Integer> emptyList());

		searchAlgorithm.search(PATTERN, ALLOWED_ERRORS);

		verify(indexStructure).substring(0, PATTERN_LENGTH + ALLOWED_ERRORS);
		verify(indexStructure).substring(10, PATTERN_LENGTH + ALLOWED_ERRORS);
	}

	@Test
	public void determineNeighborhoodAreaForLastSubpattern() {
		when(indexStructure.indicesOf("pa")).thenReturn(Collections.<Integer> emptyList());
		when(indexStructure.indicesOf("tt")).thenReturn(Collections.<Integer> emptyList());
		when(indexStructure.indicesOf("ern")).thenReturn(Arrays.asList(0, 10));

		searchAlgorithm.search(PATTERN, ALLOWED_ERRORS);

		verify(indexStructure).substring(4, 11);
	}

	@Test
	public void determineNeighborhoodAreaForMiddleSubpattern() {
		when(indexStructure.indicesOf("pa")).thenReturn(Collections.<Integer> emptyList());
		when(indexStructure.indicesOf("tt")).thenReturn(Arrays.asList(0, 10));
		when(indexStructure.indicesOf("ern")).thenReturn(Collections.<Integer> emptyList());

		searchAlgorithm.search(PATTERN, ALLOWED_ERRORS);

		verify(indexStructure).substring(0, 7);
		verify(indexStructure).substring(6, 11);
	}
}
