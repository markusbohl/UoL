package preparation;

import java.util.LinkedList;
import java.util.List;

import entity.SectionWithOffset;
import entity.SequenceSection;

public class SequenceSectionsProvider {

	private final List<SectionWithOffset> rawEntries = new LinkedList<>();
	private final List<SectionWithOffset> refEntries = new LinkedList<>();
	private final List<SectionWithOffset> overlappingAreas = new LinkedList<>();

	public void feed(final List<SectionWithOffset> sequenceSections, final int patternLength) {
		for (int i = 0; i < sequenceSections.size(); i++) {
			final SectionWithOffset sectionWithOffset = sequenceSections.get(i);
			if (sectionWithOffset instanceof SequenceSection) {
				rawEntries.add(sectionWithOffset);
			} else {
				refEntries.add(sectionWithOffset);
			}
			if (notLastElement(sequenceSections, i)) {
				final int n = patternLength - 1;
				final String firstHalf = sectionWithOffset.getLastNCharacters(n);
				final String secondHalf = sequenceSections.get(i + 1).getFirstNCharacters(n);
				final int offset = sectionWithOffset.getOffset() + sectionWithOffset.getLength() - n;
				overlappingAreas.add(new SequenceSection(offset, firstHalf + secondHalf));
			}
		}
	}

	private boolean notLastElement(final List<SectionWithOffset> sequenceSections, final int i) {
		return i + 1 != sequenceSections.size();
	}

	public List<SectionWithOffset> getRawEntries() {
		return rawEntries;
	}

	public List<SectionWithOffset> getRelativeMatchEntries() {
		return refEntries;
	}

	public List<SectionWithOffset> getOverlappingAreas() {
		return overlappingAreas;
	}
}