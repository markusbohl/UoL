package entity;

public class SequenceSection implements SectionWithOffset {

	private final int offset;
	private final String content;

	public SequenceSection(final int offset, final String content) {
		this.offset = offset;
		this.content = content;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public int getLength() {
		return content.length();
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public String getFirstNCharacters(final int n) {
		if (n >= getLength()) {
			return content;
		}
		return content.substring(0, n);
	}

	@Override
	public String getLasttNCharacters(final int n) {
		if (n >= getLength()) {
			return content;
		}
		return content.substring(getLength() - n, getLength());
	}
}
