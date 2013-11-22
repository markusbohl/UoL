package reader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import datastructure.ReferenceIndexStructure;
import entity.ReferenceSequenceSection;
import entity.SectionWithOffset;
import entity.SequenceSection;

public class CompressedSequenceParser {

	private final ReferenceIndexStructure indexStructure;
	private final ArrayList<SectionWithOffset> rawEntries = new ArrayList<>();
	private final ArrayList<SectionWithOffset> relativeMatchEntries = new ArrayList<>();

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
					final int endIndex = string.indexOf(')', i + 2);
					final String content = string.substring(beginIndex, endIndex);
					addRawEntry(offset, content);
					i = endIndex;
					offset += content.length();
				} else {
					final int indexOfComma = string.indexOf(',', i + 2);
					final int indexOfRightParenthesis = string.indexOf(')', i + 3);
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
		rawEntries.add(new SequenceSection(offset, content));
	}

	private void addRelativeMatchEntry(final int offset, final int referenceIndex, final int referenceLength) {
		relativeMatchEntries.add(new ReferenceSequenceSection(offset, indexStructure, referenceIndex, referenceLength));
	}

	public List<SectionWithOffset> getRawEntries() {
		return rawEntries;
	}

	public List<SectionWithOffset> getRelativeMatchEntries() {
		return relativeMatchEntries;
	}
}
