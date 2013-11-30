package preparation;

import javax.inject.Inject;
import javax.inject.Named;

public class CompressedSequenceBufferedFileReaderProvider extends AbstractBufferedFileReaderProvider {

	@Inject
	CompressedSequenceBufferedFileReaderProvider(@Named("compressed.sequence.file.path") final String filePath) {
		super(filePath);
	}
}
