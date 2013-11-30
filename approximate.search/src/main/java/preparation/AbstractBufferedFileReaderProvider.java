package preparation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

import javax.inject.Provider;

import com.google.inject.CreationException;
import com.google.inject.spi.Message;

public abstract class AbstractBufferedFileReaderProvider implements Provider<BufferedReader> {

	private final String filePath;

	AbstractBufferedFileReaderProvider(final String filePath) {
		this.filePath = filePath;
	}

	@Override
	public BufferedReader get() {
		try {
			return new BufferedReader(new FileReader(filePath));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			throw new CreationException(Arrays.asList(new Message(e.getMessage())));
		}
	}
}