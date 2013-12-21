package compression;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.common.collect.ImmutableSet;
import common.datastructure.HasIndexAndLength;
import common.datastructure.ReferenceIndexStructure;

public class ReferentialCompressionTest {

	private static final int MIN_RELATIVE_MATCH_LENGTH = 5;

	private CompressionAlgorithm<String, String> compression;

	private final Set<Character> ALLOWED_ALPHABET = ImmutableSet.of('A', 'C', 'G', 'T', 'N');

	@Mock
	private ReferenceIndexStructure indexStructure;
	@Mock
	private HasIndexAndLength longestCommonSubstring1;
	@Mock
	private HasIndexAndLength longestCommonSubstring2;
	@Mock
	private HasIndexAndLength longestCommonSubstring3;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		compression = new ReferentialCompressionAlgorithm(indexStructure, MIN_RELATIVE_MATCH_LENGTH, ALLOWED_ALPHABET);
	}

	@Test
	public void emptyStringReturnsEmptyString() {
		final String compressedResult = compression.compress("");

		assertThat(compressedResult, is(""));
	}

	@Test
	public void encodeNonBaseSymbolsAsRawEntry() {
		final String compressedResult = compression.compress("XXXXX");

		assertThat(compressedResult, is("R(XXXXX)"));
	}

	@Test
	public void encodeShortSubsequencesAsRawEntry() {
		final String compressedResult = compression.compress("ACGT");

		assertThat(compressedResult, is("R(ACGT)"));
	}

	@Test
	public void encodeShortSubsequencesAsRawEntryEvenIfTheyExistInTheReferenceSequence() {
		when(longestCommonSubstring1.getIndex()).thenReturn(0);
		when(longestCommonSubstring1.getLength()).thenReturn(4);
		when(longestCommonSubstring2.getIndex()).thenReturn(-1);
		when(longestCommonSubstring2.getLength()).thenReturn(0);
		when(indexStructure.findLongestPrefixSuffixMatch("ACGTATTG")).thenReturn(longestCommonSubstring1);
		when(indexStructure.findLongestPrefixSuffixMatch("ATTG")).thenReturn(longestCommonSubstring2);

		final String compressedResult = compression.compress("ACGTATTG");

		assertThat(compressedResult, is("R(ACGT)R(ATTG)"));
	}

	@Test
	public void encodeRelativeMatchesAsSuch() {
		when(longestCommonSubstring1.getIndex()).thenReturn(0);
		when(longestCommonSubstring1.getLength()).thenReturn(8);
		when(indexStructure.findLongestPrefixSuffixMatch("ACGTACGT")).thenReturn(longestCommonSubstring1);

		final String compressedResult = compression.compress("ACGTACGT");

		assertThat(compressedResult, is("RM(0,8)"));
	}

	@Test
	public void compress() {
		when(longestCommonSubstring1.getIndex()).thenReturn(-1);
		when(longestCommonSubstring1.getLength()).thenReturn(0);
		when(indexStructure.findLongestPrefixSuffixMatch("NNCGTACGTACGTTTGCCAGTAAAGCCT")).thenReturn(
				longestCommonSubstring1);
		when(longestCommonSubstring2.getIndex()).thenReturn(5);
		when(longestCommonSubstring2.getLength()).thenReturn(10);
		when(indexStructure.findLongestPrefixSuffixMatch("TACGTACGTTTGCCAGTAAAGCCT")).thenReturn(longestCommonSubstring2);
		when(longestCommonSubstring3.getIndex()).thenReturn(18);
		when(longestCommonSubstring3.getLength()).thenReturn(11);
		when(indexStructure.findLongestPrefixSuffixMatch("TGCCAGTAAAGCCT")).thenReturn(longestCommonSubstring3);

		final String compressedResult = compression.compress("NNCGTACGTACGTTTGCCAGTAAAGCCT");

		assertThat(compressedResult, is("R(NNCG)RM(5,10)RM(18,11)R(CCT)"));
	}
}
