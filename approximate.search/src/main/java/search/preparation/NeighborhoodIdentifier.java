package search.preparation;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import common.datastructure.ReferenceIndexStructure;

import search.entity.Section;

public class NeighborhoodIdentifier {

	private final Partitioner partitioner;
	private final ReferenceIndexStructure indexStructure;

	@Inject
	NeighborhoodIdentifier(final ReferenceIndexStructure indexStructure, final Partitioner partitioner) {
		this.indexStructure = indexStructure;
		this.partitioner = partitioner;
	}

	public List<Section> identifiyAreasFor(final String pattern, final int allowedErrors) {
		final List<Section> neighborhoodAreas = new LinkedList<>();
		final String[] subpatterns = partitioner.partition(pattern, allowedErrors + 1);
		final int patternLength = pattern.length();

		int localOffset = 0;
		for (int i = 0; i < subpatterns.length; i++) {
			final String subpattern = subpatterns[i];
			final int subpatternLength = subpattern.length();
			final List<Integer> indicesOfSubpattern = indexStructure.indicesOf(subpattern);

			for (final Integer index : indicesOfSubpattern) {
				if (i == 0) {
					final int endIndex = index + patternLength + allowedErrors;
					neighborhoodAreas.add(new Section(index, endIndex));
				} else {
					if (localOffset - index > allowedErrors) {
						continue;
					}
					final int startIndex = Math.max(0, index - localOffset - allowedErrors);
					final int remainingAllowedErrors = Math.min(allowedErrors, index - localOffset + allowedErrors);
					final int endIndex = index + (patternLength - localOffset) + remainingAllowedErrors;
					neighborhoodAreas.add(new Section(startIndex, endIndex));
				}
			}
			localOffset += subpatternLength;
		}

		return neighborhoodAreas;
	}
}
