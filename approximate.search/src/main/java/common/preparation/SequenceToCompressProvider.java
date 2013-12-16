package common.preparation;

import java.io.BufferedReader;

import javax.inject.Inject;
import javax.inject.Named;

public class SequenceToCompressProvider extends AbstractLineFilteringStringProvider {

	@Inject
	SequenceToCompressProvider(@Named("sequence.to.compress") final BufferedReader bufferedReader) {
		super(bufferedReader);
	}

	@Override
	protected boolean accept(final String line) {
		return !line.startsWith(">");
	}
}
