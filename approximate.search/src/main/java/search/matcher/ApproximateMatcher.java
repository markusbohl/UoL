package search.matcher;

import java.util.List;

public interface ApproximateMatcher {

	List<Integer> search(String text, String pattern, int allowedErrors, int offset);
}
