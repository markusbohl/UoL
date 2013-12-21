package common.preparation;

import java.io.BufferedReader;
import java.io.IOException;

import search.preparation.StringProvider;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class FastaFileContentProvider implements StringProvider {

	private final String filePath;
	private final BufferedFileReaderFactory bufferedFileReaderFactory;

	@AssistedInject
	FastaFileContentProvider(@Assisted final String filePath, final BufferedFileReaderFactory bufferedFileReaderFactory) {
		this.filePath = filePath;
		this.bufferedFileReaderFactory = bufferedFileReaderFactory;
	}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		try (BufferedReader bufferedReader = bufferedFileReaderFactory.create(filePath)) {
			while ((line = bufferedReader.readLine()) != null) {
				if (accept(line)) {
					stringBuilder.append(line);
				}
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

		return stringBuilder.toString();
	}

	protected boolean accept(final String line) {
		return !line.startsWith(">");
	}
}