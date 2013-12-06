package preparation;

import static org.hamcrest.Matchers.empty;
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
import entity.SectionWithOffset;

public class ReferenceFilterTest {

	private ReferenceFilter filter;

	private List<SectionWithOffset> referencedSections;

	@Mock
	private ReferenceIndexStructure indexStructure;
	@Mock
	private SectionWithOffset referencedSection1;
	@Mock
	private SectionWithOffset referencedSection2;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		when(referencedSection1.getOffset()).thenReturn(5);
		when(referencedSection1.getLength()).thenReturn(20);
		when(referencedSection2.getOffset()).thenReturn(15);
		when(referencedSection2.getLength()).thenReturn(20);

		referencedSections = Arrays.asList(referencedSection1, referencedSection2);

		filter = new ReferenceFilter();
		// final Section potentialMatchSection2 = new Section(5, 15);
		// final Section potentialMatchSection3 = new Section(20, 30);
	}

	@Test
	public void verifyMinLength() {
		final Section potentialMatchSection1 = new Section(0, 10);
		final List<Section> potentialMatchSections = Arrays.asList(potentialMatchSection1);

		final List<Section> filterSections = filter.filter(referencedSections, potentialMatchSections, 6);

		assertThat(filterSections, is(empty()));
	}
}
