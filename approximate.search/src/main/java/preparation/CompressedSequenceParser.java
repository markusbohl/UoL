package preparation;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import datastructure.ReferenceIndexStructure;
import entity.ReferenceSequenceSection;
import entity.ReferencedSectionWithOffset;
import entity.SectionWithOffset;
import entity.SequenceSection;

public class CompressedSequenceParser {

	private static final int MIN_OFFSET_TO_COMMA = 2;
	private static final int MIN_OFFSET_TO_END_OF_R_ENTRY = 2;
	private static final int MIN_OFFSET_TO_END_OF_RM_ENTRY = 3;
	private final ReferenceIndexStructure indexStructure;
	private final List<SectionWithOffset> rawEntries = new LinkedList<>();
	private final List<ReferencedSectionWithOffset> relativeMatchEntries = new LinkedList<>();
	private final List<SectionWithOffset> allEntries = new LinkedList<>();

	@Inject
	CompressedSequenceParser(final ReferenceIndexStructure indexStructure) {
		this.indexStructure = indexStructure;
	}

	public void parse(final String string) {
		int i = 0;
		int offset = 0;
		while (i < string.length()) {
			if (string.charAt(i) == '(') {
				if (string.charAt(i - 1) == 'R') {
					final int beginIndex = i + 1;
					final int endIndex = string.indexOf(')', i + MIN_OFFSET_TO_END_OF_R_ENTRY);
					final String content = string.substring(beginIndex, endIndex);
					addRawEntry(offset, content);
					i = endIndex;
					offset += content.length();
				} else {
					final int indexOfComma = string.indexOf(',', i + MIN_OFFSET_TO_COMMA);
					final int indexOfRightParenthesis = string.indexOf(')', i + MIN_OFFSET_TO_END_OF_RM_ENTRY);
					final int referenceIndex = Integer.valueOf(string.substring(i + 1, indexOfComma));
					final int referenceLength = Integer.valueOf(string.substring(indexOfComma + 1,
							indexOfRightParenthesis));
					final int endIndex = indexOfRightParenthesis;
					addRelativeMatchEntry(offset, referenceIndex, referenceLength);
					i = endIndex;
					offset += referenceLength;
				}
			} else {
				i++;
			}
		}
	}

	private void addRawEntry(final int offset, final String content) {
		final SequenceSection section = new SequenceSection(offset, content);

		allEntries.add(section);
		rawEntries.add(section);
	}

	private void addRelativeMatchEntry(final int offset, final int referenceIndex, final int referenceLength) {
		final ReferenceSequenceSection section = new ReferenceSequenceSection(offset, indexStructure, referenceIndex,
				referenceLength);

		allEntries.add(section);
		relativeMatchEntries.add(section);
	}

	public List<SectionWithOffset> getRawEntries() {
		return rawEntries;
	}

	public List<ReferencedSectionWithOffset> getRelativeMatchEntries() {
		return relativeMatchEntries;
	}

	public List<SectionWithOffset> getAllEntries() {
		return allEntries;
	}
}
