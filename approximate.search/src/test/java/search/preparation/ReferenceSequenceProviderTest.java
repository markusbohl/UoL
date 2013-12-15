package search.preparation;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.BufferedReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import search.preparation.ReferenceSequenceProvider;
import search.preparation.StringProvider;

public class ReferenceSequenceProviderTest {

	private StringProvider sequenceProvider;

	@Mock
	private BufferedReader reader;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		sequenceProvider = new ReferenceSequenceProvider(reader);
	}

	@Test
	public void provideReferenceSequenceAsString() throws IOException {
		final String line1 = ">16 dna:chromosome chromosome:GRCh37:16:1:90354753:1 REF";
		final String line2 = "TAACCCTAACCCTAACCCTAACCCTAACCCTAACCGACCCTCACCCTCACCCTAACCACA";
		final String line3 = "TGAGCAATGTGGGTGTTATATTTTAGCTGTCATGGGTGCATTAGGAATGCTGCATTTGTG";
		when(reader.readLine()).thenReturn(line1, line2, line3, null);

		final String providedSequence = sequenceProvider.provide();

		assertThat(providedSequence, is(line2 + line3));
	}
}
