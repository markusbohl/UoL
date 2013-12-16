package search.preparation;

import java.io.BufferedReader;

import javax.inject.Inject;
import javax.inject.Named;

import common.preparation.AbstractLineFilteringStringProvider;

public class CompressedSequenceProvider extends AbstractLineFilteringStringProvider {

	@Inject
	CompressedSequenceProvider(@Named("compressed.sequence") final BufferedReader bufferedReader) {
		super(bufferedReader);
	}
}
