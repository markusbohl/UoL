package common.preparation;

import java.io.BufferedReader;

public interface BufferedFileReaderFactory {

	BufferedReader create(String filePath);
}
