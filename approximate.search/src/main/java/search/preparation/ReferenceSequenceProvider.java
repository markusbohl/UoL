package search.preparation;

import java.io.BufferedReader;

import javax.inject.Inject;
import javax.inject.Named;

public class ReferenceSequenceProvider extends AbstractLineFilteringStringProvider {

	@Inject
	ReferenceSequenceProvider(@Named("reference.sequence") final BufferedReader bufferedReader) {
		super(bufferedReader);
	}

	protected boolean accept(final String line) {
		return !line.startsWith(">");
	}
}
