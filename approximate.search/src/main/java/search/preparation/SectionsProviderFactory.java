package search.preparation;

import com.google.inject.assistedinject.Assisted;

public interface SectionsProviderFactory {

	SectionsProvider createFor(@Assisted("patternLength") int patternLength,
			@Assisted("allowedErrors") int allowedErrors);
}
