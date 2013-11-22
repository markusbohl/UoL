package preparation;

public class QGramPartitioner implements Partitioner {

	@Override
	public String[] partition(final String string, final int length) {
		if (argumentsAreIllegal(string, length)) {
			throw new IllegalArgumentException();
		}
		final String[] qGrams = new String[string.length() - length + 1];

		for (int i = 0; i < qGrams.length; i++) {
			qGrams[i] = string.substring(i, i + length);
		}

		return qGrams;
	}

	private boolean argumentsAreIllegal(final String string, final int length) {
		return string == null || string.isEmpty() || length < 1;
	}
}
