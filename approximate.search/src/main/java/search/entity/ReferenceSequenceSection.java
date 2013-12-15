package search.entity;

import search.datastructure.ReferenceIndexStructure;

public class ReferenceSequenceSection implements ReferencedSectionWithOffset {

	private final ReferenceIndexStructure indexStructure;
	private final int offset;
	private final int refIndex;
	private final int refLength;

	public ReferenceSequenceSection(final int offset, final ReferenceIndexStructure indexStructure, final int refIndex,
			final int refLength) {
		this.offset = offset;
		this.indexStructure = indexStructure;
		this.refIndex = refIndex;
		this.refLength = refLength;
	}

	@Override
	public int getRefIndex() {
		return refIndex;
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
	public String getLastNCharacters(final int n) {
		final int endIndex = refIndex + refLength;
		return indexStructure.substring(endIndex - n, n);
	}
}
