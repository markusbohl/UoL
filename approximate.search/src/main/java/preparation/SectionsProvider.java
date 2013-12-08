package preparation;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.inject.assistedinject.Assisted;

import entity.ReferencedSectionWithOffset;
import entity.SectionWithOffset;

public class SectionsProvider {

	private final List<SectionWithOffset> rawEntries;
	private final List<SectionWithOffset> overlappingAreas;
	private final List<ReferencedSectionWithOffset> relativeMatchEntries;

	@Inject
	SectionsProvider(@Named("compressed.sequence") final StringProvider provider,
			final CompressedSequenceParser parser, final OverlapBuilder overlapBuilder,
			@Assisted("patternLength") final int patternLength, @Assisted("allowedErrors") final int allowedErrors) {
		parser.parse(provider.provide());
		overlapBuilder.feed(parser.getAllEntries(), patternLength, allowedErrors);

		rawEntries = parser.getRawEntries();
		relativeMatchEntries = parser.getRelativeMatchEntries();
		overlappingAreas = overlapBuilder.getOverlappingAreas();
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
