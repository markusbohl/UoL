package control;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import reader.SequenceProvider;
import datastructure.ReferenceIndexStructure;

public class InitializationTest {

	private Initialization initialization;

	@Mock
	private SequenceProvider sequenceProvider;
	@Mock
	private ReferenceIndexStructure referenceIndexStructure;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		initialization = new Initialization(sequenceProvider, referenceIndexStructure);
	}

	@Test
	public void shouldInitReferenceIndexStructureWithProvidedSequence() {
		when(sequenceProvider.getSequence()).thenReturn("providedString");

		initialization.init();

		verify(referenceIndexStructure).init("providedString");
	}

	@Test
	public void shouldReturnReferenceIndexStructure() {
		final ReferenceIndexStructure actualIndexStructure = initialization.getReferenceIndexStructure();

		assertThat(actualIndexStructure, is(referenceIndexStructure));
	}
}
