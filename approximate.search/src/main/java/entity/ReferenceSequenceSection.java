package entity;

import datastructure.ReferenceIndexStructure;

public class ReferenceSequenceSection implements SectionWithOffset {

	private final ReferenceIndexStructure indexStructure;
	private final int offset;
	private final int refLength;
	private final int refIndex;

	public ReferenceSequenceSection(final int offset, final ReferenceIndexStructure indexStructure, final int refIndex,
			final int refLength) {
		this.offset = offset;
		this.indexStructure = indexStructure;
		this.refIndex = refIndex;
		this.refLength = refLength;
	}

	@Override
	public String getContent() {
		return indexStructure.substring(refIndex, refLength);
	}

	@Override
	public int getLength() {
		return refLength;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public String getFirstNCharacters(final int n) {
		return indexStructure.substring(refIndex, n);
	}

	@Override
	public String getLasttNCharacters(final int n) {
		final int endIndex = refIndex + refLength;
		return indexStructure.substring(endIndex - n, endIndex);
	}
}
