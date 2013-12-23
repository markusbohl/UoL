package search.matcher;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

public class BitParallelMatrixBasedMatcher implements ApproximateMatcher {

	private static final long _0B1 = 0b1;
	private final Set<Character> alphabet;
	private final Map<Character, Integer> charToIndexMap;

	@Inject
	BitParallelMatrixBasedMatcher(final Set<Character> alphabet) {
		this.alphabet = alphabet;
		this.charToIndexMap = initCharToIndexMap(alphabet);
	}

	private static Map<Character, Integer> initCharToIndexMap(final Set<Character> alphabet) {
		final Map<Character, Integer> charToIndexMap = new HashMap<>();

		int index = 0;
		for (final Character character : alphabet) {
			charToIndexMap.put(character, index);
			index++;
		}

		return charToIndexMap;
	}

	@Override
	public List<Integer> search(final String text, final String pattern, final int allowedErrors, final int offset) {
		final LinkedList<Integer> matchingPositions = new LinkedList<>();
		final long[] b = initPatternBitmasks(pattern);
		long vp = ~0L;
		long vn = 0L;
		int err = pattern.length();

		for (int i = 0; i < text.length(); i++) {
			final Integer index = charToIndexMap.get(text.charAt(i));
			if (index == null) {
				continue;
			}
			long x = b[index] | vn;
			final long d0 = ((vp + (x & vp)) ^ vp) | x;
			final long hn = vp & d0;
			final long hp = vn | ~(vp | d0);
			x = hp << 1;
			vn = x & d0;
			vp = (hn << 1) | ~(x | d0);
			if ((hp & (_0B1 << pattern.length() - 1)) != 0L) {
				err++;
			} else if ((hn & (_0B1 << pattern.length() - 1)) != 0L) {
				err--;
			}
			if (err <= allowedErrors) {
				matchingPositions.add(i + offset);
			}
		}

		return matchingPositions;
	}

	private long[] initPatternBitmasks(final String pattern) {
		final long[] b = new long[alphabet.size()];

		for (int i = 0; i < pattern.length(); i++) {
			final char currentChar = pattern.charAt(i);
			final Integer currentIndex = charToIndexMap.get(currentChar);

			if (currentIndex == null) {
				continue;
			}
			b[currentIndex] = b[currentIndex] | (_0B1 << i);
		}

		return b;
	}
}
