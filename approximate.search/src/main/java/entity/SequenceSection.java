package entity;

public class SequenceSection {

	private final String content;
	private final int offset;
	private final SectionType sectionType;

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

}
