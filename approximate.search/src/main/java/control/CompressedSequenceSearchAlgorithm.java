package control;

import java.util.List;

import javax.inject.Inject;

import preparation.CompressedSequenceParser;
import preparation.Partitioner;
import preparation.StringReader;
import datastructure.ReferenceIndexStructure;

public class CompressedSequenceSearchAlgorithm implements ApproximateSearchAlgorithm {

	private final StringReader comprSeqReader;
	private final CompressedSequenceParser comprSeqParser;
	private final Partitioner partitioner;
	private final ReferenceIndexStructure indexStructure;

	@Inject
	CompressedSequenceSearchAlgorithm(final StringReader comprSeqReader, final CompressedSequenceParser comprSeqParser,
			final Partitioner partitioner, final ReferenceIndexStructure indexStructure) {
		this.comprSeqReader = comprSeqReader;
		this.comprSeqParser = comprSeqParser;
		this.partitioner = partitioner;
		this.indexStructure = indexStructure;
	}

	@Override
	public List<Integer> search(final String pattern, final int allowedErrors) {
		comprSeqParser.parse(comprSeqReader.read());
		for (final String subpattern : partitioner.partition(pattern, allowedErrors)) {
			indexStructure.indicesOf(subpattern);
		}
		return null;
	}

}
