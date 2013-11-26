package preparation;

import java.util.List;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

import entity.SectionWithOffset;

public class SectionsProvider {

	private final List<SectionWithOffset> rawEntries;
	private final List<SectionWithOffset> relativeMatchEntries;
	private final List<SectionWithOffset> overlappingAreas;

	@Inject
	SectionsProvider(final StringReader reader, final CompressedSequenceParser parser,
			final OverlappingStringBuilder overlappingStringBuilder, @Assisted final int patternLength) {
		parser.parse(reader.read());
		overlappingStringBuilder.feed(parser.getAllEntries(), patternLength);

		rawEntries = parser.getRawEntries();
		relativeMatchEntries = parser.getRelativeMatchEntries();
		overlappingAreas = overlappingStringBuilder.getOverlappingAreas();
	}

	public List<SectionWithOffset> getRawEntries() {
		return rawEntries;
	}

	public List<SectionWithOffset> getRelativeMatchEntries() {
		return relativeMatchEntries;
	}

	public List<SectionWithOffset> getOverlappingAreas() {
		return overlappingAreas;
	}
}