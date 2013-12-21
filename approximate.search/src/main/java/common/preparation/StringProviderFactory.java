package common.preparation;

import javax.inject.Named;

import search.preparation.StringProvider;

public interface StringProviderFactory {

	StringProvider createFromFile(String filePath);

	@Named("fasta")
	StringProvider createFromFastaFile(String filePath);
}
