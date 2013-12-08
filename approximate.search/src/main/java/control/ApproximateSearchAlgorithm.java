package control;

import java.util.Set;

public interface ApproximateSearchAlgorithm {

	Set<Integer> search(String pattern, int allowedErrors);
}
