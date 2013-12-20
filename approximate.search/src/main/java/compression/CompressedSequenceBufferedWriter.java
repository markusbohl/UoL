package compression;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

public class CompressedSequenceBufferedWriter extends BufferedWriter {

	@Inject
	CompressedSequenceBufferedWriter(@Named("compressed.sequence.file.path") final String fileName) throws IOException {
		super(new FileWriter(fileName));
	}
}
