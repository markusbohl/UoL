package search.entity;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import common.datastructure.ReferenceIndexStructure;

import search.entity.ReferenceSequenceSection;
import search.entity.ReferencedSectionWithOffset;

public class ReferenceSequenceSectionTest {

	private ReferencedSectionWithOffset sequenceSection;

	@Mock
	private ReferenceIndexStructure indexStructure;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		sequenceSection = new ReferenceSequenceSection(2, indexStructure, 5, 17);
	}

	@Test
	public void getContent() {
		when(indexStructure.substring(5, 17)).thenReturn("reference-content");

		assertThat(sequenceSection.getContent(), is("reference-content"));
	}

	@Test
	public void getLength() {
		assertThat(sequenceSection.getLength(), is(17));
	}

	@Test
	public void getFirstNCharactersDelegatesToIndexStructure() {
		when(indexStructure.substring(5, 4)).thenReturn("refe");

		assertThat(sequenceSection.getFirstNCharacters(4), is("refe"));
	}

	@Test
	public void getLastNCharactersDelegatesToIndexStructure() {
		when(indexStructure.substring(5 + 17 - 4, 4)).thenReturn("tent");

		assertThat(sequenceSection.getLastNCharacters(4), is("tent"));
	}

	@Test
	public void getRefIndex() {
		assertThat(sequenceSection.getRefIndex(), is(5));
	}
}
