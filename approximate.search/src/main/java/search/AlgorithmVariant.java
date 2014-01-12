package search;

public enum AlgorithmVariant {

	Rme1_RawOA_1(1), Rme1_RawOA_2(2), Rme2_RawOA_1(3), Rme2_RawOA_2(4);

	private final int value;

	private AlgorithmVariant(final int value) {
		this.value = value;
	}

	public static AlgorithmVariant get(final int value) {
		for (final AlgorithmVariant variant : values()) {
			if (value == variant.value) {
				return variant;
			}
		}
		return null;
	}
}
