package search.preparation;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import search.entity.ReferencedSectionWithOffset;
import search.entity.SectionWithOffset;

import com.google.inject.assistedinject.Assisted;

public class SectionsProvider {

	private final List<SectionWithOffset> rawEntries;
	private final List<SectionWithOffset> overlappingAreas;
	private final List<ReferencedSectionWithOffset> relativeMatchEntries;

	@Inject
	SectionsProvider(@Named("compressed.sequence") final StringProvider stringProvider,
			final CompressedSequenceParser parser, final OverlapBuilder overlapBuilder,
			@Assisted("patternLength") final int patternLength, @Assisted("allowedErrors") final int allowedErrors) {

		init(stringProvider, parser, overlapBuilder, patternLength, allowedErrors);

		overlappingAreas = overlapBuilder.getOverlappingAreas();
		relativeMatchEntries = parser.getRelativeMatchEntries();
		rawEntries = determineRawEntries(parser, overlapBuilder.rawSectionsFullyIncluded());
	}

	private void init(final StringProvider provider, final CompressedSequenceParser parser,
			final OverlapBuilder overlapBuilder, final int patternLength, final int allowedErrors) {
		parser.parse(provider.toString());
		overlapBuilder.feed(parser.getAllEntries(), patternLength, allowedErrors);
	}

	private List<SectionWithOffset> determineRawEntries(final CompressedSequenceParser parser,
			final boolean rawSectionInOverlappingAreas) {
		if (rawSectionInOverlappingAreas) {
			return Collections.emptyList();
		}
		return parser.getRawEntries();
	}

	public List<SectionWithOffset> getRawEntries() {
		return rawEntries;
	}

	public List<ReferencedSectionWithOffset> getRelativeMatchEntries() {
		return relativeMatchEntries;
	}

	public List<SectionWithOffset> getOverlappingAreas() {
		return overlappingAreas;
	}
}
