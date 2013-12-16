package compression;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.inject.Named;

public class CompressedSequenceBufferedWriter extends BufferedWriter {

	CompressedSequenceBufferedWriter(@Named("compressed.sequence.file.path") final String fileName) throws IOException {
		super(new FileWriter(fileName));
	}
}
