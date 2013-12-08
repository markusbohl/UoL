package preparation;

import java.util.LinkedList;
import java.util.List;

import entity.SectionWithOffset;
import entity.SequenceSection;

public class OverlappingAreaBuilder {

	private final List<SectionWithOffset> overlappingAreas = new LinkedList<>();

	public List<SectionWithOffset> getOverlappingAreas() {
		return overlappingAreas;
	}

	public void feed(final List<SectionWithOffset> sequenceSections, final int patternLength, final int allowedErrors) {
		final int lengthPerSection = patternLength + allowedErrors - 1;

		for (int i = 0; i < sequenceSections.size() - 1; i++) {
			final String firstPart = buildFirstPart("", sequenceSections, i, lengthPerSection);
			final String overlappingString = buildOverlappingString(firstPart, sequenceSections, i + 1,
					lengthPerSection);
			final int offset = sequenceSections.get(i + 1).getOffset() - firstPart.length();

			overlappingAreas.add(new SequenceSection(offset, overlappingString));
		}
	}

	private String buildFirstPart(final String stringSoFar, final List<SectionWithOffset> sections, final int index,
			final int lengthPerSection) {
		final int noOfCharsToRead = lengthPerSection - stringSoFar.length();
		final String lastCharsOfSection = readLastCharsOfSection(sections, index, noOfCharsToRead);
		final String currentString = lastCharsOfSection + stringSoFar;

		if (isFirstPartTooShort(currentString, lengthPerSection, index)) {
			return buildFirstPart(currentString, sections, index - 1, lengthPerSection);
		}
		return currentString;
	}

	private String readLastCharsOfSection(final List<SectionWithOffset> sections, final int index,
			final int noOfCharsToRead) {
		final SectionWithOffset currentSection = sections.get(index);
		final String lastCharsOfSection = currentSection.getLastNCharacters(noOfCharsToRead);
		return lastCharsOfSection;
	}

	private boolean isFirstPartTooShort(final String firstPart, final int lengthPerSection, final int currentIndex) {
		return firstPart.length() < lengthPerSection && currentIndex > 0;
	}

	private String buildOverlappingString(final String stringSoFar, final List<SectionWithOffset> sections,
			final int index, final int remainingLength) {
		final String firstCharsOfSection = readFirstCharsOfSection(sections, index, remainingLength);
		final String currentString = stringSoFar + firstCharsOfSection;
		final int substringLength = firstCharsOfSection.length();

		if (isRemainingPartToShort(substringLength, remainingLength, sections, index)) {
			return buildOverlappingString(currentString, sections, index + 1, remainingLength - substringLength);
		}
		return currentString;
	}

	private String readFirstCharsOfSection(final List<SectionWithOffset> sections, final int index,
			final int noOfCharsToRead) {
		final SectionWithOffset currentSection = sections.get(index);
		final String beginningOfSection = currentSection.getFirstNCharacters(noOfCharsToRead);
		return beginningOfSection;
	}

	private boolean isRemainingPartToShort(final int substringLength, final int remainingLength,
			final List<SectionWithOffset> sections, final int index) {
		return substringLength < remainingLength && isNotLastElement(sections, index);
	}

	private boolean isNotLastElement(final List<SectionWithOffset> sections, final int currentIndex) {
		return currentIndex + 1 != sections.size();
	}
}