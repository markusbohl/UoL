package preparation;

import java.io.BufferedReader;
import java.io.IOException;

import javax.inject.Inject;

public class ReferenceSequenceProvider implements StringProvider {

	private final BufferedReader bufferedReader;

	@Inject
	ReferenceSequenceProvider(final BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}

	@Override
	public String provide() {
		final StringBuilder stringBuilder = new StringBuilder();

		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				if (line.startsWith(">")) {
					continue;
				}
				stringBuilder.append(line);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return stringBuilder.toString();
	}

}
