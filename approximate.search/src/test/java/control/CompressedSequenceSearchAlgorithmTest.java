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
		when(partitioner.partition(eq("pattern"), anyInt())).thenReturn(new String[] { "pa", "tt", "ern" });
		when(sectionsProviderFactory.createSectionsProviderFor(anyInt())).thenReturn(sectionProvider);
	}

	@Test
	public void createSectionsProviderForGivenPatternLength() {
		searchAlgorithm.search("pattern", 2);

		verify(sectionsProviderFactory).createSectionsProviderFor("pattern".length());
	}

	@Test
	public void searchIndexStructureForSubpatterns() {
		searchAlgorithm.search("pattern", 2);

		verify(indexStructure).indicesOf("pa");
		verify(indexStructure).indicesOf("tt");
		verify(indexStructure).indicesOf("ern");
	}

	@Test
	public void searchForMatchesInRawEntries() {
		final int allowedErrors = 2;
		final SectionWithOffset section1 = mock(SectionWithOffset.class);
		final SectionWithOffset section2 = mock(SectionWithOffset.class);
		when(section1.getContent()).thenReturn("content1");
		when(section1.getOffset()).thenReturn(23);
		when(section2.getContent()).thenReturn("content2");
		when(section2.getOffset()).thenReturn(44);
		when(sectionProvider.getRawEntries()).thenReturn(Arrays.asList(section1, section2));
		when(matcher.search("content1", "pattern", allowedErrors, 23)).thenReturn(Arrays.asList(30, 40));
		when(matcher.search("content2", "pattern", allowedErrors, 44)).thenReturn(Arrays.asList(50));

		final List<Integer> matchingPositions = searchAlgorithm.search("pattern", allowedErrors);

		assertThat(matchingPositions, hasSize(3));
		assertThat(matchingPositions, hasItems(30, 40, 50));
	}

	@Test
	public void searchForMatchesInOverlappingAreas() {
		final int allowedErrors = 2;
		final SectionWithOffset section1 = mock(SectionWithOffset.class);
		final SectionWithOffset section2 = mock(SectionWithOffset.class);
		when(section1.getContent()).thenReturn("content1");
		when(section1.getOffset()).thenReturn(23);
		when(section2.getContent()).thenReturn("content2");
		when(section2.getOffset()).thenReturn(44);
		when(sectionProvider.getOverlappingAreas()).thenReturn(Arrays.asList(section1, section2));
		when(matcher.search("content1", "pattern", allowedErrors, 23)).thenReturn(Arrays.asList(30, 40));
		when(matcher.search("content2", "pattern", allowedErrors, 44)).thenReturn(Arrays.asList(50));

		final List<Integer> matchingPositions = searchAlgorithm.search("pattern", allowedErrors);

		assertThat(matchingPositions, hasSize(3));
		assertThat(matchingPositions, hasItems(30, 40, 50));
	}

}
