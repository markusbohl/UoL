package common.preparation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

public class SequenceToCompressBufferedFastaFileReader extends BufferedReader {

	@Inject
	SequenceToCompressBufferedFastaFileReader(@Named("sequence.to.compress.file.path") final String filePath)
			throws IOException {
		super(new FileReader(filePath));
	}
}
