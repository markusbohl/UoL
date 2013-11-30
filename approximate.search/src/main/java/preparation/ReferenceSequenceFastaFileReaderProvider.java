package preparation;

import javax.inject.Inject;
import javax.inject.Named;

public class ReferenceSequenceFastaFileReaderProvider extends AbstractBufferedFileReaderProvider {

	@Inject
	ReferenceSequenceFastaFileReaderProvider(@Named("reference.sequence.file.path") final String filePath) {
		super(filePath);
	}
}
