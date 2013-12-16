package search.preparation;

import javax.inject.Inject;
import javax.inject.Named;

import common.preparation.AbstractBufferedFileReaderProvider;

public class CompressedSequenceBufferedFileReaderProvider extends AbstractBufferedFileReaderProvider {

	@Inject
	CompressedSequenceBufferedFileReaderProvider(@Named("compressed.sequence.file.path") final String filePath) {
		super(filePath);
	}
}
