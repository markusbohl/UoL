package common.preparation;

import java.io.BufferedReader;
import java.io.IOException;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class FileContentProvider implements StringProvider {

	private final String filePath;
	private final BufferedFileReaderFactory bufferedFileReaderFactory;

	@AssistedInject
	FileContentProvider(@Assisted final String filePath, final BufferedFileReaderFactory bufferedFileReaderFactory) {
		this.filePath = filePath;
		this.bufferedFileReaderFactory = bufferedFileReaderFactory;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		String line;
		try (BufferedReader bufferedReader = bufferedFileReaderFactory.create(filePath)) {
			while ((line = bufferedReader.readLine()) != null) {
				builder.append(line);
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

		return builder.toString();
	}
}
