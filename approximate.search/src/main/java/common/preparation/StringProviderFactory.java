package common.preparation;

import search.preparation.StringProvider;

public interface StringProviderFactory {

	StringProvider createFromFile(String filePath);
}
