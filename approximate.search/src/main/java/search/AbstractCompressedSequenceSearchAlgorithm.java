package search;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import search.entity.ReferencedSectionWithOffset;
import search.entity.Section;
import search.entity.SectionWithOffset;
import search.preparation.NeighborhoodIdentifier;
import search.preparation.ReferenceFilter;
import search.preparation.SectionsProvider;
import search.preparation.SectionsProviderFactory;

public abstract class AbstractCompressedSequenceSearchAlgorithm implements ApproximateSearchAlgorithm {

	private final SectionsProviderFactory sectionsProviderFactory;
	private final NeighborhoodIdentifier neighborhoodIdentifier;
	private final ReferenceFilter referenceFilter;
	private SectionsProvider sectionsProvider;

	@Inject
	AbstractCompressedSequenceSearchAlgorithm(final SectionsProviderFactory sectionsProviderFactory,
			final NeighborhoodIdentifier neighborhoodIdentifier, final ReferenceFilter referenceFilter) {
		this.sectionsProviderFactory = sectionsProviderFactory;
		this.neighborhoodIdentifier = neighborhoodIdentifier;
		this.referenceFilter = referenceFilter;
	}

	@Override
	public Set<Integer> search(final String pattern, final int allowedErrors) {
		final SortedSet<Integer> matchingPositions = new TreeSet<>();
		final int patternLength = pattern.length();
		final int minSectionLength = patternLength - allowedErrors;

		System.out.println("createSectionsProvider: " + SimpleDateFormat.getTimeInstance().format(new Date()));
		sectionsProvider = sectionsProviderFactory.createSectionsProviderFor(patternLength, allowedErrors);
		System.out.println("sectionsProvider created: " + SimpleDateFormat.getTimeInstance().format(new Date()));
		matchingPositions.addAll(matchesInRelativeMatchEntries(pattern, allowedErrors, minSectionLength));
		System.out.println(SimpleDateFormat.getTimeInstance().format(new Date()));
		matchingPositions.addAll(matchesInRawSections(pattern, allowedErrors));
		System.out.println(SimpleDateFormat.getTimeInstance().format(new Date()));
		matchingPositions.addAll(matchesInOverlappingAreas(pattern, allowedErrors));
		System.out.println(SimpleDateFormat.getTimeInstance().format(new Date()));
		return matchingPositions;
	}

	protected List<Integer> matchesInRelativeMatchEntries(final String pattern, final int allowedErrors,
			final int minSectionLength) {
		System.out.println("matchesInRelativeMatchEntries: " + SimpleDateFormat.getTimeInstance().format(new Date()));
		final List<Section> neighborhoodAreas = neighborhoodIdentifier.identifiyAreasFor(pattern, allowedErrors);
		System.out.println("neighborhoodAreas identified: " + SimpleDateFormat.getTimeInstance().format(new Date()));
		final List<ReferencedSectionWithOffset> referencedSections = sectionsProvider.getRelativeMatchEntries();
		System.out.println("referencedSections loaded: " + SimpleDateFormat.getTimeInstance().format(new Date()));
		final List<ReferencedSectionWithOffset> filteredSections = referenceFilter.filter(referencedSections,
				neighborhoodAreas, minSectionLength);
		System.out.println("referencedSections filtered: " + SimpleDateFormat.getTimeInstance().format(new Date()));
		return searchInSections(filteredSections, pattern, allowedErrors);
	}

	protected List<Integer> matchesInRawSections(final String pattern, final int allowedErrors) {
		final List<SectionWithOffset> rawEntries = sectionsProvider.getRawEntries();

		return searchInSections(rawEntries, pattern, allowedErrors);
	}

	protected List<Integer> matchesInOverlappingAreas(final String pattern, final int allowedErrors) {
		System.out.println("matchesInOverlappingAreas: " + SimpleDateFormat.getTimeInstance().format(new Date()));
		final List<SectionWithOffset> overlappingAreas = sectionsProvider.getOverlappingAreas();
		System.out.println("overlappingAreas loaded: " + SimpleDateFormat.getTimeInstance().format(new Date()));
		return searchInSections(overlappingAreas, pattern, allowedErrors);
	}

	protected abstract List<Integer> searchInSections(final List<? extends SectionWithOffset> sections,
			final String pattern, final int allowedErrors);
}