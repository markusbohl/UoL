package entity;

public class SequenceSection {

	private final SectionType sectionType;
	private final String content;
	private final int offset;

	public SequenceSection(final SectionType type, final String content, final int offset) {
		this.sectionType = type;
		this.content = content;
		this.offset = offset;
	}

	public String getContent() {
		return content;
	}

	public int getLength() {
		return content.length();
	}

	public int getOffset() {
		return offset;
	}

	public SectionType getSectionType() {
		return sectionType;
	}

	public String getFirstNCharacters(final int n) {
		if (n >= getLength()) {
			return content;
		}
		return content.substring(0, n);
	}

	public String getLasttNCharacters(final int n) {
		if (n >= getLength()) {
			return content;
		}
		return content.substring(getLength() - n, getLength());
	}
}
