package common.datastructure;

public class IndexAndLength implements HasIndexAndLength {

	private final int index;
	private final int length;

	public IndexAndLength(final int index, final int length) {
		this.index = index;
		this.length = length;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public int getLength() {
		return length;
	}
}
