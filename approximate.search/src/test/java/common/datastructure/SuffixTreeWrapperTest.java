package common.datastructure;

import junitparams.Parameters;

import org.junit.Test;

public class SuffixTreeWrapperTest extends ReferenceIndexStructureTest {

	public SuffixTreeWrapperTest() {
		super(new SuffixTreeWrapper());
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void initRequiredBeforeInvokingFindLongestCommonSubstring() {
		super.initRequiredBeforeInvokingFindLongestCommonSubstring();
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	@Parameters
	public void findLongestPrefixSuffixMatch(final String referenceSequence, final String otherSequence,
			final HasIndexAndLength indexAndLength) {
		super.findLongestPrefixSuffixMatch(referenceSequence, otherSequence, indexAndLength);
	}
}