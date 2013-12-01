package datastructure;

import java.util.List;

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
}
