package common.datastructure;

import java.util.LinkedList;
import java.util.List;

import org.jsuffixarrays.Algorithm;
import org.jsuffixarrays.SuffixArrays;
import org.jsuffixarrays.SuffixData;

public class SuffixArrayWrapper implements ReferenceIndexStructure {

	private String sequence;
	private int[] lcp;
	private int[] suffixArray;

	@Override
	public void init(final String sequence) {
		if (sequence == null) {
			throw new IllegalArgumentException("sequence must not be null");
		}

		this.suffixArray = SuffixArrays.create(sequence, Algorithm.SAIS.getInstance());
		this.sequence = sequence;
	}

	@Override
	public List<Integer> indicesOf(final String substring) {
		if (sequence == null) {
			throw new IllegalStateException("index structure has not been initialized");
		}

		return indicesOf(substring, 0, suffixArray.length);
	}

	private List<Integer> indicesOf(final String query, final int min, final int max) {
		final List<Integer> indices = new LinkedList<>();

		if (min <= max && min < suffixArray.length) {
			final int midpoint = midpoint(min, max);
			if (sequence.charAt(suffixArray[midpoint]) < query.charAt(0)) {
				indices.addAll(indicesOf(query, midpoint + 1, max));
			} else if (sequence.charAt(suffixArray[midpoint]) > query.charAt(0)) {
				indices.addAll(indicesOf(query, min, midpoint - 1));
			} else {
				if (substringStartsAtIndex(query, suffixArray[midpoint])) {
					indices.add(suffixArray[midpoint]);
					indices.addAll(findConsecutiveMatches(query, midpoint + 1, true));
					indices.addAll(findConsecutiveMatches(query, midpoint - 1, false));
				} else if (sequence.substring(suffixArray[midpoint]).compareTo(query) < 0) {
					indices.addAll(indicesOf(query, midpoint + 1, max));
				} else {
					indices.addAll(indicesOf(query, min, midpoint - 1));
				}
			}
		}

		return indices;
	}

	private int midpoint(final int min, final int max) {
		return min + ((max - min) / 2);
	}

	private boolean substringStartsAtIndex(final String substring, final int index) {
		if (sequence.length() >= index + substring.length()) {
			for (int i = 0; i < substring.length(); i++) {
				if (sequence.charAt(index + i) != substring.charAt(i)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	private List<Integer> findConsecutiveMatches(final String query, int saIndex, final boolean forwards) {
		final List<Integer> indices = new LinkedList<>();

		boolean run = true;
		while (run && saIndex >= 0 && saIndex < suffixArray.length) {
			final int index = suffixArray[saIndex];
			if (sequence.substring(index).startsWith(query)) {
				indices.add(index);
				saIndex = forwards ? saIndex + 1 : saIndex - 1;
			} else {
				run = false;
			}
		}

		return indices;
	}

	@Override
	public String substring(final int beginIndex, final int length) {
		if (sequence == null) {
			throw new IllegalStateException("index structure has not been initialized");
		}
		return sequence.substring(beginIndex, beginIndex + length);
	}

	@Override
	public HasIndexAndLength findLongestPrefixSuffixMatch(final String otherString) {
		if (sequence == null) {
			throw new IllegalStateException("index structure has not been initialized");
		}

		final String string = sequence + " " + otherString;
		final SuffixData suffixData = SuffixArrays.createWithLCP(string, Algorithm.SAIS.getInstance());
		this.lcp = suffixData.getLCP();
		this.suffixArray = suffixData.getSuffixArray();
		final int boundaryIndex = sequence.length();
		int maxLength = -1;
		int maxLengthIndex = -1;

		for (int i = 1; i < lcp.length; i++) {
			if (lcp[i] > maxLength && isIndexWithinBoundaries(boundaryIndex, i)) {
				maxLength = lcp[i];
				maxLengthIndex = suffixArray[i];
			}
		}

		return new IndexAndLength(maxLengthIndex, maxLength);
	}

	private boolean isIndexWithinBoundaries(final int boundaryIndex, final int i) {
		return suffixArray[i] < boundaryIndex && suffixArray[i - 1] > boundaryIndex;
	}
}
