package entity;

import datastructure.ReferenceIndexStructure;

public class ReferenceSequenceSection implements SectionWithOffset {

	private final ReferenceIndexStructure indexStructure;
	private final int offset;
	private final int length;

	public ReferenceSequenceSection(final ReferenceIndexStructure indexStructure, final int offset, final int length) {
		this.indexStructure = indexStructure;
		this.offset = offset;
		this.length = length;
	}

	@Override
	public String getContent() {
		return indexStructure.substring(getOffset(), length);
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public String getFirstNCharacters(final int n) {
		return indexStructure.substring(getOffset(), n);
	}

	@Override
	public String getLasttNCharacters(final int n) {
		final int endIndex = getOffset() + getLength();
		return indexStructure.substring(endIndex - n, endIndex);
	}
}
