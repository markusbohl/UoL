package preparation;

import java.util.LinkedList;
import java.util.List;

import entity.SectionWithOffset;
import entity.SequenceSection;

public class OverlappingStringBuilder {

	private final List<SectionWithOffset> overlappingAreas = new LinkedList<>();

	public List<SectionWithOffset> getOverlappingAreas() {
		return overlappingAreas;
	}

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

	private String buildFirstHalf(final String stringSoFar, final List<SectionWithOffset> sections, final int index,
			final int halfLength) {
		final int noOfCharsToRead = halfLength - stringSoFar.length();
		final SectionWithOffset currentSection = sections.get(index);
		final String currentString = currentSection.getLastNCharacters(noOfCharsToRead) + stringSoFar;

		if (currentString.length() < halfLength && index > 0) {
			return buildFirstHalf(currentString, sections, index - 1, halfLength);
		}
		return currentString;
	}

	private String buildOverlappingString(final String stringSoFar, final List<SectionWithOffset> sections,
			final int index, final int length) {
		final int noOfCharsToRead = length - stringSoFar.length();
		final SectionWithOffset currentSection = sections.get(index);
		final String currentString = stringSoFar + currentSection.getFirstNCharacters(noOfCharsToRead);

		if (currentString.length() < length && isNotLastElement(sections, index)) {
			return buildOverlappingString(currentString, sections, index + 1, length);
		}
		return currentString;
	}

	private boolean isNotLastElement(final List<SectionWithOffset> sequenceSections, final int i) {
		return i + 1 != sequenceSections.size();
	}
}