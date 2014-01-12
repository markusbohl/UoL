package search;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
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
import search.entity.SectionWithOffset;
import search.matcher.ApproximateMatcher;
import search.preparation.SectionsProvider;
import search.preparation.SectionsProviderFactory;

public abstract class ApproximateSearchAlgorithmTest {

	private static final int ALLOWED_ERRORS = 2;
	private static final String PATTERN = "pattern";

	private ApproximateSearchAlgorithm algorithm;

	@Mock
	private SectionsProviderFactory sectionsProviderFactory;
	@Mock
	private ApproximateMatcher approximateMatcher;
	@Mock
	private SectionsProvider sectionsProvider;
	@Mock
	private SectionWithOffset section1;
	@Mock
	private SectionWithOffset section2;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		when(sectionsProviderFactory.createFor(anyInt(), anyInt())).thenReturn(sectionsProvider);
		when(section1.getContent()).thenReturn("content1");
		when(section1.getOffset()).thenReturn(23);
		when(section2.getContent()).thenReturn("content2");
		when(section2.getOffset()).thenReturn(44);

		algorithm = algorithm();
	}

	@Test
	public void createSectionsProviderForGivenPatternLength() {
		algorithm.search(PATTERN, ALLOWED_ERRORS);

		verify(sectionsProviderFactory).createFor(PATTERN.length(), ALLOWED_ERRORS);
	}

	@Test
	public void searchForMatchesInRawEntries() {
		when(approximateMatcher.search("content1", PATTERN, ALLOWED_ERRORS, 23)).thenReturn(Arrays.asList(30, 40));
		when(approximateMatcher.search("content2", PATTERN, ALLOWED_ERRORS, 44)).thenReturn(Arrays.asList(50));
		when(sectionsProvider.getRawEntries()).thenReturn(Arrays.asList(section1, section2));

		final Set<Integer> matchingPositions = algorithm.search(PATTERN, ALLOWED_ERRORS);

		assertThat(matchingPositions, hasSize(3));
		assertThat(matchingPositions, hasItems(30, 40, 50));
	}

	@Test
	public void searchForMatchesInOverlappingAreas() {
		when(approximateMatcher.search("content1", PATTERN, ALLOWED_ERRORS, 23)).thenReturn(Arrays.asList(30, 40));
		when(approximateMatcher.search("content2", PATTERN, ALLOWED_ERRORS, 44)).thenReturn(Arrays.asList(50));
		when(sectionsProvider.getOverlappingAreas()).thenReturn(Arrays.asList(section1, section2));

		final Set<Integer> matchingPositions = algorithm.search(PATTERN, ALLOWED_ERRORS);

		assertThat(matchingPositions, hasSize(3));
		assertThat(matchingPositions, hasItems(30, 40, 50));
	}

	private AbstractCompressedSequenceSearchAlgorithm algorithm() {
		return new AbstractCompressedSequenceSearchAlgorithm(sectionsProviderFactory, approximateMatcher) {

			@Override
			protected List<ReferencedSectionWithOffset> prepare(
					final List<ReferencedSectionWithOffset> relMatchEntries, final String pattern,
					final int allowedErrors) {
				// not relevant to test in this class
				return null;
			}
		};
	}
}
