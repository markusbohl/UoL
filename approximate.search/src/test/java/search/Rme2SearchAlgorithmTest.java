package search;

import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import search.entity.ReferencedSectionWithOffset;
import search.matcher.ApproximateMatcher;
import search.preparation.SectionsProviderFactory;

public class Rme2SearchAlgorithmTest {

	private static final int ALLOWED_ERRORS = 2;
	private static final String PATTERN = "pattern";

	private Rme2SearchAlgorithm algorithm;

	@Mock
	private SectionsProviderFactory sectionsProviderFactory;
	@Mock
	private ApproximateMatcher approximateMatcher;
	@Mock
	private List<ReferencedSectionWithOffset> relMatchEntries;

	@Before
	public void setUp() {
		initMocks(this);

		algorithm = new Rme2SearchAlgorithm(sectionsProviderFactory, approximateMatcher);
	}

	@Test
	public void preparesDoesNotAlterRelativeMatchEntries() {
		algorithm.prepare(relMatchEntries, PATTERN, ALLOWED_ERRORS);

		verifyZeroInteractions(relMatchEntries);
	}
}