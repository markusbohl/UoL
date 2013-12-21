package common.preparation;

import javax.inject.Named;

public interface StringProviderFactory {

	StringProvider createFromFile(String filePath);

	@Named("fasta")
	StringProvider createFromFastaFile(String filePath);
}
