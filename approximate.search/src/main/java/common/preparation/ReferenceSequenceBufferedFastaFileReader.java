package common.preparation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

public class ReferenceSequenceBufferedFastaFileReader extends BufferedReader {

	@Inject
	ReferenceSequenceBufferedFastaFileReader(@Named("reference.sequence.file.path") final String filePath)
			throws IOException {
		super(new FileReader(filePath));
	}
}
