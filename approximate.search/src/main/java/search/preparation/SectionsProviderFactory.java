package search.preparation;

import com.google.inject.assistedinject.Assisted;

public interface SectionsProviderFactory {

	SectionsProvider createSectionsProviderFor(@Assisted("patternLength") int patternLength,
			@Assisted("allowedErrors") int allowedErrors);
}
