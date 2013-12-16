package search.datastructure;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SuffixTreeWrapperTest {

	private ReferenceIndexStructure indexStructure;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		indexStructure = new SuffixTreeWrapper();
	}

	@Test(expected = IllegalArgumentException.class)
	public void initRequiresStringInstance() {
		indexStructure.init(null);
	}

	@Test(expected = IllegalStateException.class)
	public void initRequiredBeforeInvokingIndicesOf() {
		indexStructure.indicesOf("AC");
	}

	@Test(expected = IllegalStateException.class)
	public void initRequiredBeforeInvokingSubstring() {
		indexStructure.substring(1, 3);
	}

	@Test(expected = IllegalStateException.class)
	public void initRequiredBeforeInvokingFindLongestCommonSubstring() {
		indexStructure.findLongestCommonSubstring("AC");
	}

	@Test
	public void indicesOf() {
		indexStructure.init("ACTAC");

		final List<Integer> indices = indexStructure.indicesOf("AC");

		assertThat(indices, hasSize(2));
		assertThat(indices, hasItems(0, 3));
	}

	@Test
	public void substring() {
		indexStructure.init("ACTAC");

		assertThat(indexStructure.substring(1, 3), is("CTA"));
	}

	@Test
	public void findLongestCommonSubstring() {
		indexStructure.init("ACTAC");

		final HasIndexAndLength longestCommonSubstring = indexStructure.findLongestCommonSubstring("TAC");

		assertThat(longestCommonSubstring.getIndex(), is(2));
		assertThat(longestCommonSubstring.getLength(), is(3));
	}
}