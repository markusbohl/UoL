package control;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
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

	private ApproximateSearchAlgorithm algorithm;

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
	@Mock
	private SectionWithOffset section1;
	@Mock
	private SectionWithOffset section2;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		when(partitioner.partition(eq(PATTERN), anyInt())).thenReturn(new String[] { "pa", "tt", "ern" });
		when(sectionsProviderFactory.createSectionsProviderFor(anyInt())).thenReturn(sectionProvider);
		when(section1.getContent()).thenReturn("content1");
		when(section1.getOffset()).thenReturn(23);
		when(section2.getContent()).thenReturn("content2");
		when(section2.getOffset()).thenReturn(44);

		algorithm = new CompressedSequenceSearchAlgorithm(sectionsProviderFactory, partitioner, indexStructure, matcher);
	}

	@Test
	public void createSectionsProviderForGivenPatternLength() {
		algorithm.search(PATTERN, 2);

		verify(sectionsProviderFactory).createSectionsProviderFor(PATTERN.length());
	}

	@Test
	public void searchIndexStructureForSubpatterns() {
		algorithm.search(PATTERN, ALLOWED_ERRORS);

		verify(indexStructure).indicesOf("pa");
		verify(indexStructure).indicesOf("tt");
		verify(indexStructure).indicesOf("ern");
	}

	@Test
	public void searchForMatchesInRawEntries() {
		when(matcher.search("content1", PATTERN, ALLOWED_ERRORS, 23)).thenReturn(Arrays.asList(30, 40));
		when(matcher.search("content2", PATTERN, ALLOWED_ERRORS, 44)).thenReturn(Arrays.asList(50));
		when(sectionProvider.getRawEntries()).thenReturn(Arrays.asList(section1, section2));

		final List<Integer> matchingPositions = algorithm.search(PATTERN, ALLOWED_ERRORS);

		assertThat(matchingPositions, hasSize(3));
		assertThat(matchingPositions, hasItems(30, 40, 50));
	}

	@Test
	public void searchForMatchesInOverlappingAreas() {
		when(matcher.search("content1", PATTERN, ALLOWED_ERRORS, 23)).thenReturn(Arrays.asList(30, 40));
		when(matcher.search("content2", PATTERN, ALLOWED_ERRORS, 44)).thenReturn(Arrays.asList(50));
		when(sectionProvider.getOverlappingAreas()).thenReturn(Arrays.asList(section1, section2));

		final List<Integer> matchingPositions = algorithm.search(PATTERN, ALLOWED_ERRORS);

		assertThat(matchingPositions, hasSize(3));
		assertThat(matchingPositions, hasItems(30, 40, 50));
	}

	@Test
	public void determineNeighborhoodAreaForFirstSubpattern() {
		when(indexStructure.indicesOf("pa")).thenReturn(Arrays.asList(0, 10));
		when(indexStructure.indicesOf("tt")).thenReturn(Collections.<Integer> emptyList());
		when(indexStructure.indicesOf("ern")).thenReturn(Collections.<Integer> emptyList());

		algorithm.search(PATTERN, ALLOWED_ERRORS);

		verify(indexStructure).substring(0, PATTERN_LENGTH + ALLOWED_ERRORS);
		verify(indexStructure).substring(10, PATTERN_LENGTH + ALLOWED_ERRORS);
	}

	@Test
	public void determineNeighborhoodAreaForMiddleSubpattern1() {
		when(indexStructure.indicesOf("pa")).thenReturn(Collections.<Integer> emptyList());
		when(indexStructure.indicesOf("tt")).thenReturn(Arrays.asList(0, 10));
		when(indexStructure.indicesOf("ern")).thenReturn(Collections.<Integer> emptyList());

		algorithm.search(PATTERN, ALLOWED_ERRORS);

		verify(indexStructure).substring(0, 7);
		verify(indexStructure).substring(6, 11);
	}

	@Test
	public void determineNeighborhoodAreaForMiddleSubpattern2() {
		when(partitioner.partition("longerpattern", 4)).thenReturn(new String[] { "lon", "ger", "pat", "tern" });
		when(indexStructure.indicesOf("lon")).thenReturn(Collections.<Integer> emptyList());
		when(indexStructure.indicesOf("ger")).thenReturn(Collections.<Integer> emptyList());
		when(indexStructure.indicesOf("pat")).thenReturn(Arrays.asList(0));
		when(indexStructure.indicesOf("tern")).thenReturn(Collections.<Integer> emptyList());

		algorithm.search("longerpattern", 3);

		verify(indexStructure, never()).substring(anyInt(), anyInt());
	}

	@Test
	public void determineNeighborhoodAreaForLastSubpattern() {
		when(indexStructure.indicesOf("pa")).thenReturn(Collections.<Integer> emptyList());
		when(indexStructure.indicesOf("tt")).thenReturn(Collections.<Integer> emptyList());
		when(indexStructure.indicesOf("ern")).thenReturn(Arrays.asList(0, 10));

		algorithm.search(PATTERN, ALLOWED_ERRORS);

		verify(indexStructure).substring(4, 11);
	}

	@Test
	public void searchForMatchesInRelativeMatchEntries() throws Exception {
		when(partitioner.partition(anyString(), anyInt())).thenReturn(new String[] { "lon", "ger", "pat", "tern" });
		when(indexStructure.indicesOf("lon")).thenReturn(Arrays.asList(0));
		when(indexStructure.indicesOf("ger")).thenReturn(Arrays.asList(2, 5));
		when(indexStructure.indicesOf("pat")).thenReturn(Arrays.asList(10));
		when(indexStructure.indicesOf("tern")).thenReturn(Arrays.asList(15));
		when(indexStructure.substring(anyInt(), anyInt())).thenReturn("substring1", "substring2", "substring3",
				"substring4", "substring5");
		when(matcher.search(eq("substring1"), anyString(), anyInt(), anyInt())).thenReturn(Arrays.asList(10));
		when(matcher.search(eq("substring2"), anyString(), anyInt(), anyInt())).thenReturn(Arrays.asList(20));
		when(matcher.search(eq("substring3"), anyString(), anyInt(), anyInt())).thenReturn(Arrays.asList(30));
		when(matcher.search(eq("substring4"), anyString(), anyInt(), anyInt())).thenReturn(Arrays.asList(40));
		when(matcher.search(eq("substring5"), anyString(), anyInt(), anyInt())).thenReturn(Arrays.asList(50));

		final List<Integer> matchingPositions = algorithm.search("longerpattern", 3);

		assertThat(matchingPositions, hasSize(5));
		assertThat(matchingPositions, hasItems(10, 20, 30, 40, 50));
	}
}
