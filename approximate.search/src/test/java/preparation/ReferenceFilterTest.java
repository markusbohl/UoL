package preparation;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import datastructure.ReferenceIndexStructure;
import entity.ReferencedSectionWithOffset;
import entity.Section;

@RunWith(JUnitParamsRunner.class)
public class ReferenceFilterTest {

	private ReferenceFilter filter;

	private List<ReferencedSectionWithOffset> referencedSections;

	@Mock
	private ReferenceIndexStructure indexStructure;
	@Mock
	private ReferencedSectionWithOffset referencedSection1;
	@Mock
	private ReferencedSectionWithOffset referencedSection2;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		when(referencedSection1.getRefIndex()).thenReturn(5);
		when(referencedSection1.getLength()).thenReturn(20);
		when(referencedSection1.getOffset()).thenReturn(207);
		when(referencedSection2.getRefIndex()).thenReturn(15);
		when(referencedSection2.getLength()).thenReturn(20);
		when(referencedSection2.getOffset()).thenReturn(84);

		referencedSections = Arrays.asList(referencedSection1, referencedSection2);

		filter = new ReferenceFilter(indexStructure);
	}

	@Test
	public void filter() {
		final Section potentialMatchSection = new Section(5, 15);
		final List<Section> potentialMatchSections = Arrays.asList(potentialMatchSection);

		final List<ReferencedSectionWithOffset> filterSections = filter.filter(referencedSections,
				potentialMatchSections, 6);

		assertThat(filterSections, hasSize(1));
		final ReferencedSectionWithOffset filteredSection = filterSections.get(0);
		assertThat(filteredSection.getRefIndex(), is(5));
		assertThat(filteredSection.getLength(), is(10));
		assertThat(filteredSection.getOffset(), is(207));
	}

	@Test
	@Parameters
	public void considerMinLength(final Section potentialMatchSection) {
		final List<Section> potentialMatchSections = Arrays.asList(potentialMatchSection);

		final List<ReferencedSectionWithOffset> filterSections = filter.filter(referencedSections,
				potentialMatchSections, 6);

		assertThat(filterSections, is(empty()));
	}

	Object[] parametersForConsiderMinLength() {
		return $($(new Section(0, 10)), $(new Section(30, 40)));
	}

	@Test
	@Parameters
	public void adjustOffset(final Section potentialMatchSection, final int refIndex, final int length, final int offset) {
		final List<Section> potentialMatchSections = Arrays.asList(potentialMatchSection);

		final List<ReferencedSectionWithOffset> filterSections = filter.filter(referencedSections,
				potentialMatchSections, 6);

		assertThat(filterSections, hasSize(1));
		final ReferencedSectionWithOffset filteredSection = filterSections.get(0);
		assertThat(filteredSection.getRefIndex(), is(refIndex));
		assertThat(filteredSection.getLength(), is(length));
		assertThat(filteredSection.getOffset(), is(offset));
	}

	Object[] parametersForAdjustOffset() {
		return $($(new Section(20, 30), 20, 10, 89));// , $(new Section(25, 40), 25, 10, 94));
	}

	@Test
	public void doNotAdjustOffset() {
		final Section potentialMatchSection = new Section(3, 15);
		final List<Section> potentialMatchSections = Arrays.asList(potentialMatchSection);

		final List<ReferencedSectionWithOffset> filterSections = filter.filter(referencedSections,
				potentialMatchSections, 6);

		assertThat(filterSections, hasSize(1));
		final ReferencedSectionWithOffset filteredSection = filterSections.get(0);
		assertThat(filteredSection.getRefIndex(), is(5));
		assertThat(filteredSection.getLength(), is(10));
		assertThat(filteredSection.getOffset(), is(207));
	}
}
