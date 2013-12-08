package preparation;

public interface SectionsProviderFactory {

	SectionsProvider createSectionsProviderFor(int patternLength, int allowedErrors);
}
