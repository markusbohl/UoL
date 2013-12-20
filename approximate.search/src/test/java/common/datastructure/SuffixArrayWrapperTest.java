package common.datastructure;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class SuffixArrayWrapperTest extends ReferenceIndexStructureTest {

	public SuffixArrayWrapperTest() {
		super(new SuffixArrayWrapper());
	}

	@Test
	public void findLongestPrefixSuffixMatchOnlyWithRespectToSearchString() {
		indexStructure.init("AAAACAAAA");

		final HasIndexAndLength indexAndLength = indexStructure.findLongestPrefixSuffixMatch("AC");

		assertThat(indexAndLength.getIndex(), is(3));
		assertThat(indexAndLength.getLength(), is(2));
	}
}
