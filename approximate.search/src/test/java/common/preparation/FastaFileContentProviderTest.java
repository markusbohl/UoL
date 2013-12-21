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

public class FastaFileContentProviderTest {

	private static final String FILE_PATH = "filePath";

	private StringProvider provider;

	@Mock
	private BufferedFileReaderFactory bufferedFileReaderFactory;
	@Mock
	private BufferedReader bufferedReader;

	@Before
	public void setUp() throws Exception {
		initMocks(this);

		provider = new FastaFileContentProvider(FILE_PATH, bufferedFileReaderFactory);
	}

	@Test
	public void readLinesFromBufferedReader() throws IOException {
		when(bufferedFileReaderFactory.create(FILE_PATH)).thenReturn(bufferedReader);
		when(bufferedReader.readLine()).thenReturn(">dna", "AAAA", "CCCC", "TTTT", "GGGG", null);

		final String content = provider.toString();

		assertThat(content, is("AAAACCCCTTTTGGGG"));
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
