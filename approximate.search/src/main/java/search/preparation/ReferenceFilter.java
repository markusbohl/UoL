package search.preparation;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import search.datastructure.IntervalTree;
import search.datastructure.ReferenceIndexStructure;
import search.datastructure.IntervalTree.IntervalData;
import search.entity.ReferenceSequenceSection;
import search.entity.ReferencedSectionWithOffset;
import search.entity.Section;

public class ReferenceFilter {

	private final ReferenceIndexStructure indexStructure;

	@Inject
	ReferenceFilter(final ReferenceIndexStructure indexStructure) {
		this.indexStructure = indexStructure;
	}

	public List<ReferencedSectionWithOffset> filter(final List<ReferencedSectionWithOffset> referencedSections,
			final List<Section> potentialMatchSections, final int minLength) {

		final LinkedList<ReferencedSectionWithOffset> filteredSections = new LinkedList<>();
		final IntervalTree<ReferencedSectionWithOffset> intervalTree = buildIntervalTreeFor(referencedSections);

		for (final Section section : potentialMatchSections) {
			final int sectionStart = section.getStartIndex();
			final int sectionEnd = section.getEndIndex();

			final List<IntervalData<ReferencedSectionWithOffset>> intersectedElements = intervalTree
					.queryIntersectedElements(sectionStart, sectionEnd);

			for (final IntervalData<ReferencedSectionWithOffset> intervalData : intersectedElements) {
				final ReferencedSectionWithOffset referencedSection = intervalData.getObject();
				final int refStartIndex = referencedSection.getRefIndex();
				final int refEndIndex = refStartIndex + referencedSection.getLength();
				final int refOffset = referencedSection.getOffset();

				if (intersected(sectionStart, sectionEnd, refStartIndex, refEndIndex)) {
					final int newStartIndex = Math.max(sectionStart, refStartIndex);
					final int newEndIndex = Math.min(sectionEnd, refEndIndex);
					final int newLength = newEndIndex - newStartIndex;
					final int newOffset = refOffset + newStartIndex - refStartIndex;

					if (newLength >= minLength) {
						filteredSections.add(createRefSectionWith(newOffset, newStartIndex, newLength));
					}
				}
			}
		}

		return filteredSections;
	}

	private ReferenceSequenceSection createRefSectionWith(final int offset, final int newStartIndex, final int newLength) {
		return new ReferenceSequenceSection(offset, indexStructure, newStartIndex, newLength);
	}

	private boolean intersected(final int sectionStartIndex, final int sectionEndIndex, final int refStartIndex,
			final int refEndIndex) {
		return refStartIndex < sectionEndIndex && refEndIndex > sectionStartIndex;
	}

	private IntervalTree<ReferencedSectionWithOffset> buildIntervalTreeFor(
			final List<ReferencedSectionWithOffset> referencedSections) {
		return new IntervalTree<>(convertToIntervalData(referencedSections));
	}

	private List<IntervalData<ReferencedSectionWithOffset>> convertToIntervalData(
			final List<ReferencedSectionWithOffset> referencedSections) {
		final List<IntervalData<ReferencedSectionWithOffset>> intervalData = new LinkedList<>();

		for (final ReferencedSectionWithOffset section : referencedSections) {
			final int start = section.getRefIndex();
			final int end = start + section.getLength();
			intervalData.add(new IntervalData<>(start, end, section));
		}

		return intervalData;
	}
}
