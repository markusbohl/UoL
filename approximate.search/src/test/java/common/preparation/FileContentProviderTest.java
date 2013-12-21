package common.preparation;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import search.preparation.StringProvider;

public class FileContentProviderTest {

	private static final String FILE_PATH = "filePath";

	private StringProvider provider;

	@Mock
	private BufferedFileReaderFactory bufferedFileReaderFactory;
	@Mock
	private BufferedReader bufferedReader;

	@Before
	public void setUp() throws Exception {
		initMocks(this);

		provider = new FileContentProvider(FILE_PATH, bufferedFileReaderFactory);
	}

	@Test
	public void readLinesFromBufferedReader() throws IOException {
		when(bufferedFileReaderFactory.create(FILE_PATH)).thenReturn(bufferedReader);
		when(bufferedReader.readLine()).thenReturn("aaaaaaaaaa", "1111111111", "BBBBBBBBBB", null);

		final String content = provider.toString();

		assertThat(content, is("aaaaaaaaaa1111111111BBBBBBBBBB"));
	}

	@Test(expected = RuntimeException.class)
	public void throwsRuntimeExceptionWhenFileNotFound() {
		when(bufferedFileReaderFactory.create("fileDoesNotExist")).thenThrow(new FileNotFoundException());

		provider.toString();
	}

	@Test(expected = RuntimeException.class)
	public void throwsRuntimeExceptionWhenIOExceptionDuringReadOccurs() throws IOException {
		when(bufferedFileReaderFactory.create(FILE_PATH)).thenReturn(bufferedReader);
		when(bufferedReader.readLine()).thenThrow(new IOException());

		provider.toString();
	}
}
