package common.preparation;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

public class BufferedFileReaderTest {

	private static final String FILE_PATH = "src/test/resources/resources/bufferedFileReaderTest.txt";

	@Test
	public void readLineFromFile() throws IOException {
		String result;
		try (BufferedFileReader bufferedFileReader = new BufferedFileReader(FILE_PATH)) {
			result = bufferedFileReader.readLine();
		}

		assertThat(result, is("works!"));
	}
}
