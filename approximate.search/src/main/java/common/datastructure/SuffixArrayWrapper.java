package common.datastructure;

import java.util.LinkedList;
import java.util.List;

import org.jsuffixarrays.Algorithm;
import org.jsuffixarrays.SuffixArrays;
import org.jsuffixarrays.SuffixData;

public class SuffixArrayWrapper implements ReferenceIndexStructure {

	private String sequence;
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

		final SuffixData suffixData = SuffixArrays.createWithLCP(otherString, Algorithm.SAIS.getInstance());
		final int[] lcp = suffixData.getLCP();
		int longestMatchIndex = -1;
		int longestMatchLength = 0;

		int i = 0;
		while (i < sequence.length()) {
			int j = 0;
			final int partialMatchIndex = i;
			int partialMatchLength = 0;
			while (j < otherString.length() && i < sequence.length()) {
				if (sequence.charAt(i) == otherString.charAt(j)) {
					i++;
					j++;
					partialMatchLength++;

					if (partialMatchLength > longestMatchLength) {
						longestMatchIndex = partialMatchIndex;
						longestMatchLength = partialMatchLength;
					}
				} else {
					if (partialMatchLength > 1) {
						i += partialMatchLength - lcp[partialMatchLength - 1];
					} else {
						i++;
					}
					break;
				}
			}
		}

		return new IndexAndLength(longestMatchIndex, longestMatchLength);
	}
}
