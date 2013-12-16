package compression;

import java.text.MessageFormat;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import common.datastructure.HasIndexAndLength;
import common.datastructure.ReferenceIndexStructure;

public class ReferentialCompression implements Compression<String, String> {

	private final ReferenceIndexStructure indexStructure;
	private final int minRelativeMatchLength;
	private final Set<Character> allowedAlphabet;

	@Inject
	ReferentialCompression(final ReferenceIndexStructure indexStructure,
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
			final HasIndexAndLength longestCommonSubstring = indexStructure
					.findLongestCommonSubstring(remainingSequence);

			if (commonSubstringWithMinimumLengthExists(longestCommonSubstring)) {
				index += longestCommonSubstring.getLength();
				result.append(encodeRef(longestCommonSubstring));
			} else {
				final String rawString = rawString(sequence, index);
				index += rawString.length();
				result.append(encodeRaw(rawString));
			}
		}

		return result.toString();
	}

	private boolean commonSubstringWithMinimumLengthExists(final HasIndexAndLength longestCommonSubstring) {
		if (longestCommonSubstring == null) {
			return false;
		}

		final boolean hasMinimumLenth = longestCommonSubstring.getLength() >= minRelativeMatchLength;
		return hasMinimumLenth;
	}

	private String rawString(final String sequence, int index) {
		final StringBuilder raw = new StringBuilder();
		while (index < sequence.length() && (rawMatchIsStillShort(raw) || charIsNotAllowed(sequence.charAt(index)))) {
			raw.append(sequence.charAt(index));
			index++;
		}

		return raw.toString();
	}

	private boolean rawMatchIsStillShort(final StringBuilder raw) {
		return raw.length() < minRelativeMatchLength - 1;
	}

	private boolean charIsNotAllowed(final char currentCharacter) {
		return !allowedAlphabet.contains(currentCharacter);
	}

	private String encodeRaw(final String raw) {
		return MessageFormat.format("R({0})", raw);
	}

	private String encodeRef(final HasIndexAndLength refData) {
		return MessageFormat.format("RM({0},{1})", refData.getIndex(), refData.getLength());
	}
}
