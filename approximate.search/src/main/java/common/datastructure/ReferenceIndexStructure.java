package common.datastructure;

import java.util.Set;

public interface ReferenceIndexStructure {

	void init(String sequence);

	Set<Integer> indicesOf(String substring);

	String substring(int beginIndex, int length);

	HasIndexAndLength findLongestPrefixSuffixMatch(String otherString);
}
