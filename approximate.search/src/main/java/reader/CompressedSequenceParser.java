package reader;

import java.util.ArrayList;
import java.util.List;

import entity.SequenceSection;

public class CompressedSequenceParser {

	private final ArrayList<SequenceSection> rawEntries = new ArrayList<>();

	public void parse(final String string, final int minLength) {
		int i = 0;
		int offset = 0;
		while (i < string.length()) {
			if (string.charAt(i) == '(') {
				if (string.charAt(i - 1) == 'R') {
					final int beginIndex = i + 1;
					final int endIndex = string.indexOf(')', i + 2);
					final String content = string.substring(beginIndex, endIndex);
					if (content.length() >= minLength) {
						addRawEntry(content, offset);
					}
					i = endIndex;
					offset += content.length();
				} else {
					final int beginIndex = string.indexOf(',', i + 2) + 1;
					final int endIndex = string.indexOf(')', i + 3);
					i = endIndex;
					offset += Integer.valueOf(string.substring(beginIndex, endIndex));
				}
			} else {
				i++;
			}
		}
	}

	private void addRawEntry(final String content, final int offset) {
		rawEntries.add(new SequenceSection(content, offset));
	}

	public List<SequenceSection> getRawEntries() {
		return rawEntries;
	}

}
