package search.datastructure;

import java.util.List;

public interface ReferenceIndexStructure {

	void init(String sequence);

	List<Integer> indicesOf(String substring);

	String substring(int beginIndex, int length);
}
