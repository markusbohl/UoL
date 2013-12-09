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
				final String secondPart = buildSecondPart("", sections, i + 1, maxLengthPerRefSection);
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

		if (section instanceof ReferencedSectionWithOffset) {
			final String substring = section.getLastNCharacters(maxLengthPerRefSection - stringSoFar.length());
			final String currentString = substring + stringSoFar;
			final int currentStringLength = currentString.length();
			if (isFirstPartTooShort(currentStringLength, maxLengthPerRefSection, currentIndex)) {
				return buildFirstPart(currentString, sections, currentIndex - 1, maxLengthPerRefSection);
			}
			return currentString;
		}

		return section.getContent() + stringSoFar;
	}

	private boolean isFirstPartTooShort(final int currentStringLength, final int maxLengthPerRefSection,
			final int currentIndex) {
		return currentStringLength < maxLengthPerRefSection && currentIndex > 0;
	}

	private String buildSecondPart(final String stringSoFar, final List<SectionWithOffset> sections,
			final int currentIndex, final int maxLengthPerRefSection) {
		final SectionWithOffset section = sections.get(currentIndex);

		if (section instanceof ReferencedSectionWithOffset) {
			final String substring = section.getFirstNCharacters(maxLengthPerRefSection - stringSoFar.length());
			final String currentString = stringSoFar + substring;
			final int currentLength = currentString.length();
			if (isSecondPartTooShort(currentLength, maxLengthPerRefSection, sections, currentIndex)) {
				return buildSecondPart(currentString, sections, currentIndex + 1, maxLengthPerRefSection);
			}
			return currentString;
		}

		if (isLastSection(sections, currentIndex)) {
			return stringSoFar + section.getContent();
		}

		return section.getContent() + buildSecondPart(stringSoFar, sections, currentIndex + 1, maxLengthPerRefSection);
	}

	private boolean isSecondPartTooShort(final int currentStringLength, final int maxLengthPerRefSection,
			final List<SectionWithOffset> sections, final int currentIndex) {
		return currentStringLength < maxLengthPerRefSection && currentIndex < sections.size() - 1;
	}

	private boolean isLastSection(final List<SectionWithOffset> sections, final int currentIndex) {
		return currentIndex == sections.size() - 1;
	}

	@Override
	public boolean rawSectionsFullyIncluded() {
		return true;
	}
}
