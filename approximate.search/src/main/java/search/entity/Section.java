package search.entity;

public class Section {

	private final int startIndex;
	private final int endIndex;

	public Section(final int startIndex, final int endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}
}
