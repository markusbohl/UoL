package search;

import java.util.List;
import java.util.concurrent.Callable;

import search.entity.SectionWithOffset;
import search.matcher.ApproximateMatcher;

public class SectionMatcherCallable implements Callable<List<Integer>> {

	private final String text;
	private final String pattern;
	private final int allowedErrors;
	private final int offset;
	private final ApproximateMatcher matcher;

	SectionMatcherCallable(final SectionWithOffset section, final String pattern, final int allowedErrors,
			final ApproximateMatcher matcher) {
		this.text = section.getContent();
		this.offset = section.getOffset();
		this.pattern = pattern;
		this.allowedErrors = allowedErrors;
		this.matcher = matcher;
	}

	@Override
	public List<Integer> call() throws Exception {
		return matcher.search(text, pattern, allowedErrors, offset);
	}
}
