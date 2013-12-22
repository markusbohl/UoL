package compression;

import java.text.MessageFormat;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import common.datastructure.HasIndexAndLength;
import common.datastructure.ReferenceIndexStructure;

public class ReferentialCompressionAlgorithm implements CompressionAlgorithm<String, String> {

	private final ReferenceIndexStructure indexStructure;
	private final int minRelativeMatchLength;
	private final Set<Character> allowedAlphabet;

	@Inject
	ReferentialCompressionAlgorithm(final ReferenceIndexStructure indexStructure,
			@Named("min.relative.match.length") final int minRelativeMatchLength, final Set<Character> allowedAlphabet) {
		this.indexStructure = indexStructure;
		this.minRelativeMatchLength = minRelativeMatchLength;
		this.allowedAlphabet = allowedAlphabet;
	}

	@Override
	public String compress(final String sequence) {
		final StringBuilder result = new StringBuilder();

		int index = 0;
		while (index < sequence.length()) {
			final String remainingSequence = sequence.substring(index);
			final HasIndexAndLength longestPrefixSuffixMatch = indexStructure
					.findLongestPrefixSuffixMatch(remainingSequence);

			if (matchWithMinimumLengthExists(longestPrefixSuffixMatch)) {
				index += longestPrefixSuffixMatch.getLength();
				final String ref = encodeRef(longestPrefixSuffixMatch);
				result.append(ref);
			} else {
				final String rawString = rawString(sequence, index);
				index += rawString.length();
				final String raw = encodeRaw(rawString);
				result.append(raw);
			}
		}

		return result.toString();
	}

	private boolean matchWithMinimumLengthExists(final HasIndexAndLength longestPrefixSuffixMatch) {
		if (longestPrefixSuffixMatch == null) {
			return false;
		}

		return longestPrefixSuffixMatch.getLength() >= minRelativeMatchLength;
	}

	private String rawString(final String sequence, int index) {
		final StringBuilder raw = new StringBuilder();
		while (index < sequence.length()
				&& (rawMatchIsStillShortEnough(raw) || charIsNotAllowed(sequence.charAt(index)))) {
			raw.append(sequence.charAt(index));
			index++;
		}

		return raw.toString();
	}

	private boolean rawMatchIsStillShortEnough(final StringBuilder raw) {
		return raw.length() < minRelativeMatchLength - 1;
	}

	private boolean charIsNotAllowed(final char currentCharacter) {
		return !allowedAlphabet.contains(currentCharacter);
	}

	private String encodeRaw(final String raw) {
		return MessageFormat.format("R({0})", raw);
	}

	private String encodeRef(final HasIndexAndLength refData) {
		return MessageFormat.format("RM({0,number,#},{1,number,#})", refData.getIndex(), refData.getLength());
	}
}
