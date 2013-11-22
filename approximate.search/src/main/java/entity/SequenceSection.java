package entity;

public class SequenceSection implements SectionWithOffset {

	private final String content;
	private final int offset;

	public SequenceSection(final String content, final int offset) {
		this.content = content;
		this.offset = offset;
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
