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
			final CompressedSequenceParser parser, final OverlappingAreaBuilder overlappingAreaBuilder,
			@Assisted final int patternLength, @Assisted final int allowedErrors) {
		parser.parse(provider.provide());
		overlappingAreaBuilder.feed(parser.getAllEntries(), patternLength, allowedErrors);

		rawEntries = parser.getRawEntries();
		relativeMatchEntries = parser.getRelativeMatchEntries();
		overlappingAreas = overlappingAreaBuilder.getOverlappingAreas();
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
