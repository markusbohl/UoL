package preparation;

import java.util.LinkedList;
import java.util.List;

import entity.ReferencedSectionWithOffset;
import entity.SectionWithOffset;
import entity.SequenceSection;

public class RawSectionsIncludingOverlapBuilder implements OverlapBuilder {

	private final LinkedList<SectionWithOffset> overlappingAreas = new LinkedList<>();

	@Override
	public List<SectionWithOffset> getOverlappingAreas() {
		return overlappingAreas;
	}

	@Override
	public void feed(final List<SectionWithOffset> sections, final int patternLength, final int allowedErrors) {
		final int maxLengthPerRefSection = patternLength + allowedErrors - 1;

		for (int i = 0; i < sections.size() - 1; i++) {
			final SectionWithOffset section = sections.get(i);
			if (i == 0 || section instanceof ReferencedSectionWithOffset) {
				final String firstPart = buildFirstPart("", sections, i, maxLengthPerRefSection);
				final String secondPart = buildSecondPart("", sections, i + 1, maxLengthPerRefSection,
						maxLengthPerRefSection);
				final String content = firstPart + secondPart;
				final int nextSectionOffset = sections.get(i + 1).getOffset();
				final int firstPartLength = firstPart.length();
				final int offset = nextSectionOffset - firstPartLength;

				overlappingAreas.add(new SequenceSection(offset, content));
			}
		}
	}

	private String buildFirstPart(final String stringSoFar, final List<SectionWithOffset> sections,
			final int currentIndex, final int maxLengthPerRefSection) {
		final SectionWithOffset section = sections.get(currentIndex);

		final int noOfCharsToRead = maxLengthPerRefSection - stringSoFar.length();
		final String substring = readFirstPart(noOfCharsToRead, currentIndex, section);
		final String currentString = substring + stringSoFar;
		final int currentStringLength = currentString.length();
		if (isFirstPartTooShort(currentStringLength, maxLengthPerRefSection, currentIndex)) {
			return buildFirstPart(currentString, sections, currentIndex - 1, maxLengthPerRefSection);
		}
		return currentString;
	}

	private String readFirstPart(final int noOfCharsToRead, final int currentIndex, final SectionWithOffset section) {
		if (isFirstElementRawSection(currentIndex, section)) {
			return section.getContent();
		}
		return section.getLastNCharacters(noOfCharsToRead);
	}

	private boolean isFirstElementRawSection(final int currentIndex, final SectionWithOffset section) {
		return (currentIndex == 0) && !(section instanceof ReferencedSectionWithOffset);
	}

	private boolean isFirstPartTooShort(final int currentStringLength, final int maxLengthPerRefSection,
			final int currentIndex) {
		return currentStringLength < maxLengthPerRefSection && currentIndex > 0;
	}

	private String buildSecondPart(final String stringSoFar, final List<SectionWithOffset> sections,
			final int currentIndex, final int maxLengthPerRefSection, int remainingNoOfCharsToRead) {
		final SectionWithOffset section = sections.get(currentIndex);
		final int nextIndex = currentIndex + 1;

		if (section instanceof ReferencedSectionWithOffset) {
			final String substring = section.getFirstNCharacters(remainingNoOfCharsToRead);
			final String currentString = stringSoFar + substring;
			final int substringLength = substring.length();
			if (isSecondPartTooShort(substringLength, maxLengthPerRefSection, sections, nextIndex)) {
				remainingNoOfCharsToRead -= substringLength;
				return buildSecondPart(currentString, sections, nextIndex, maxLengthPerRefSection,
						remainingNoOfCharsToRead);
			}
			return currentString;
		}

		final String gatheredString;
		if (hasNotPassedASecondRefEntry(maxLengthPerRefSection, remainingNoOfCharsToRead)) {
			gatheredString = stringSoFar + section.getContent();
		} else {
			final String firstChars = section.getFirstNCharacters(remainingNoOfCharsToRead);
			gatheredString = stringSoFar + firstChars;
			remainingNoOfCharsToRead -= firstChars.length();
		}

		if (isLastSection(sections, nextIndex)) {
			return gatheredString;
		}

		return buildSecondPart(gatheredString, sections, nextIndex, maxLengthPerRefSection, remainingNoOfCharsToRead);
	}

	private boolean hasNotPassedASecondRefEntry(final int maxLengthPerRefSection, final int remainingNoOfCharsToRead) {
		return maxLengthPerRefSection == remainingNoOfCharsToRead;
	}

	private boolean isSecondPartTooShort(final int substringLength, final int maxLengthPerRefSection,
			final List<SectionWithOffset> sections, final int nextIndex) {
		return substringLength < maxLengthPerRefSection && nextIndex < sections.size();
	}

	private boolean isLastSection(final List<SectionWithOffset> sections, final int nextIndex) {
		return nextIndex == sections.size();
	}

	@Override
	public boolean rawSectionsFullyIncluded() {
		return true;
	}
}
