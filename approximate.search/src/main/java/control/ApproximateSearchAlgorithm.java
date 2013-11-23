package control;

import java.util.List;

public interface ApproximateSearchAlgorithm {

	List<Integer> search(String pattern, int allowedErrors);
}
