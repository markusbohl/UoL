package search.matcher;

import java.util.Collections;
import java.util.List;

public class EmptyMatcher implements ApproximateMatcher {

	@Override
	public List<Integer> search(final String text, final String pattern, final int allowedErrors, final int offset) {
		return Collections.emptyList();
	}

	@Override
	public void init(final String pattern, final int allowedErrors) {

	}
}
