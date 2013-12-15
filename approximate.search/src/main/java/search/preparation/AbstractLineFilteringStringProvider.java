package search.preparation;

import java.io.BufferedReader;
import java.io.IOException;

public abstract class AbstractLineFilteringStringProvider implements StringProvider {

	private final BufferedReader bufferedReader;

	AbstractLineFilteringStringProvider(final BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}

	@Override
	public String provide() {
		final StringBuilder stringBuilder = new StringBuilder();

		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				if (accept(line)) {
					stringBuilder.append(line);
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return stringBuilder.toString();
	}

	protected boolean accept(final String line) {
		return true;
	}
}