package control;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import preparation.CompressedSequenceParser;
import preparation.Partitioner;
import preparation.StringReader;
import datastructure.ReferenceIndexStructure;

public class CompressedSequenceSearchAlgorithmTest {

	private ApproximateSearchAlgorithm searchAlgorithm;

	@Mock
	private StringReader comprSeqReader;
	@Mock
	private CompressedSequenceParser parser;
	@Mock
	private Partitioner partitioner;
	@Mock
	private ReferenceIndexStructure indexStructure;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		searchAlgorithm = new CompressedSequenceSearchAlgorithm(comprSeqReader, parser, partitioner, indexStructure);
		when(partitioner.partition(eq("pattern"), anyInt())).thenReturn(new String[] { "pa", "tt", "ern" });
	}

	@Test
	public void readAndParseCompressedSequence() {
		when(comprSeqReader.read()).thenReturn("compressedSequence");

		searchAlgorithm.search("pattern", 2);

		verify(parser).parse("compressedSequence");
	}

	@Test
	public void searchIndexStructureForSubpatterns() {
		searchAlgorithm.search("pattern", 2);

		verify(indexStructure).indicesOf("pa");
		verify(indexStructure).indicesOf("tt");
		verify(indexStructure).indicesOf("ern");
	}
}
