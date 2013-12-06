package preparation;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import datastructure.ReferenceIndexStructure;
import entity.Section;

public class NeighborhoodIdentifierTest {

	private static final int ALLOWED_ERRORS = 2;

	private static final String PATTERN = "pattern";

	private NeighborhoodIdentifier neighborhoodIdentifier;

	@Mock
	private ReferenceIndexStructure indexStructure;
	@Mock
	private Partitioner partitioner;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		when(partitioner.partition(PATTERN, ALLOWED_ERRORS + 1)).thenReturn(new String[] { "pa", "tt", "ern" });

		neighborhoodIdentifier = new NeighborhoodIdentifier(indexStructure, partitioner);
	}

	@Test
	public void determineNeighborhoodForFirstSubpattern() {
		when(indexStructure.indicesOf("pa")).thenReturn(Arrays.asList(0, 10));

		final List<Section> neighberhoodAreas = neighborhoodIdentifier.identifiyAreasFor(PATTERN, ALLOWED_ERRORS);

		assertThat(neighberhoodAreas, hasSize(2));
		assertThat(neighberhoodAreas.get(0).getStartIndex(), is(0));
		assertThat(neighberhoodAreas.get(0).getEndIndex(), is(9));
		assertThat(neighberhoodAreas.get(1).getStartIndex(), is(10));
		assertThat(neighberhoodAreas.get(1).getEndIndex(), is(19));
	}

	@Test
	public void determineNeighborhoodAreaForMiddleSubpattern() {
		when(indexStructure.indicesOf("tt")).thenReturn(Arrays.asList(0, 4, 10));

		final List<Section> neighberhoodAreas = neighborhoodIdentifier.identifiyAreasFor(PATTERN, ALLOWED_ERRORS);

		assertThat(neighberhoodAreas, hasSize(3));
		assertThat(neighberhoodAreas.get(0).getStartIndex(), is(0));
		assertThat(neighberhoodAreas.get(0).getEndIndex(), is(5));
		assertThat(neighberhoodAreas.get(1).getStartIndex(), is(0));
		assertThat(neighberhoodAreas.get(1).getEndIndex(), is(11));
		assertThat(neighberhoodAreas.get(2).getStartIndex(), is(6));
		assertThat(neighberhoodAreas.get(2).getEndIndex(), is(17));
	}

	@Test
	public void determineNeighborhoodAreaForLastSubpattern() {
		when(indexStructure.indicesOf("ern")).thenReturn(Arrays.asList(0, 1, 2, 3, 10));

		final List<Section> neighberhoodAreas = neighborhoodIdentifier.identifiyAreasFor(PATTERN, ALLOWED_ERRORS);

		assertThat(neighberhoodAreas, hasSize(3));
		assertThat(neighberhoodAreas.get(0).getStartIndex(), is(0));
		assertThat(neighberhoodAreas.get(0).getEndIndex(), is(5));
		assertThat(neighberhoodAreas.get(1).getStartIndex(), is(0));
		assertThat(neighberhoodAreas.get(1).getEndIndex(), is(7));
		assertThat(neighberhoodAreas.get(2).getStartIndex(), is(4));
		assertThat(neighberhoodAreas.get(2).getEndIndex(), is(15));
	}
}
