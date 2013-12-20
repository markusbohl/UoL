package common.datastructure;

import java.util.List;

import common.datastructure.SuffixTree.Match;

public class SuffixTreeWrapper implements ReferenceIndexStructure {

	private String string;
	private SuffixTree<String> suffixTree;

	@Override
	public void init(final String sequence) {
		if (sequence == null) {
			throw new IllegalArgumentException("sequence must not be null");
		}
		string = sequence;
		suffixTree = new SuffixTree<>(sequence);
	}

	@Override
	public List<Integer> indicesOf(final String substring) {
		if (suffixTree == null) {
			throw new IllegalStateException("index structure has not been initialized");
		}
		return suffixTree.indicesOf(substring);
	}

	@Override
	public String substring(final int beginIndex, final int length) {
		if (string == null) {
			throw new IllegalStateException("index structure has not been initialized");
		}
		return string.substring(beginIndex, beginIndex + length);
	}

	@Override
	public HasIndexAndLength findLongestPrefixSuffixMatch(final String otherString) {
		if (suffixTree == null) {
			throw new IllegalStateException("index structure has not been initialized");
		}

		final Match longestCommonSubString = suffixTree.findLongestCommonSubString(otherString);

		return new IndexAndLength(longestCommonSubString.getIndex(), longestCommonSubString.getLength());
	}
}
