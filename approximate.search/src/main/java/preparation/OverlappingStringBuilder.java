package preparation;

import java.util.LinkedList;
import java.util.List;

import entity.SectionWithOffset;
import entity.SequenceSection;

public class OverlappingStringBuilder {

	private final List<SectionWithOffset> overlappingAreas = new LinkedList<>();

	public void feed(final List<SectionWithOffset> sequenceSections, final int patternLength) {
		final int halfLength = patternLength - 1;
		final int areaLength = 2 * halfLength;

		for (int i = 0; i < sequenceSections.size() - 1; i++) {
			final String firstHalf = buildFirstHalf("", sequenceSections, i, halfLength);
			final String overlappingString = buildOverlappingString(firstHalf, sequenceSections, i + 1, areaLength);
			final int offset = sequenceSections.get(i + 1).getOffset() - firstHalf.length();

			overlappingAreas.add(new SequenceSection(offset, overlappingString));
		}
	}

	private String buildFirstHalf(String stringSoFar, final List<SectionWithOffset> sections, final int index,
			final int halfLength) {
		final int noOfCharsToRead = halfLength - stringSoFar.length();
		final SectionWithOffset currentSection = sections.get(index);
		stringSoFar = currentSection.getLastNCharacters(noOfCharsToRead) + stringSoFar;

		if (stringSoFar.length() < halfLength && index > 0) {
			return buildFirstHalf(stringSoFar, sections, index - 1, halfLength);
		}
		return stringSoFar;
	}

	private String buildOverlappingString(String stringSoFar, final List<SectionWithOffset> sections, final int index,
			final int length) {
		final int noOfCharsToRead = length - stringSoFar.length();
		final SectionWithOffset currentSection = sections.get(index);
		stringSoFar = stringSoFar + currentSection.getFirstNCharacters(noOfCharsToRead);

		if (stringSoFar.length() < length && isNotLastElement(sections, index)) {
			return buildOverlappingString(stringSoFar, sections, index + 1, length);
		}
		return stringSoFar;
	}

	private boolean isNotLastElement(final List<SectionWithOffset> sequenceSections, final int i) {
		return i + 1 != sequenceSections.size();
	}

	public List<SectionWithOffset> getOverlappingAreas() {
		return overlappingAreas;
	}
}