package preparation;

public class NonOverlappingPartitioner implements Partitioner {

	@Override
	public String[] partition(final String string, final int noOfParts) {
		final String[] parts = new String[noOfParts];
		final int avrgPartLength = string.length() / noOfParts;

		for (int i = 0; i < parts.length; i++) {
			final int offset = i * avrgPartLength;
			if (isLastElement(parts, i)) {
				parts[i] = string.substring(offset, string.length());
			} else {
				parts[i] = string.substring(offset, offset + avrgPartLength);
			}
		}

		return parts;
	}

	private boolean isLastElement(final String[] parts, final int i) {
		return i + 1 == parts.length;
	}
}
