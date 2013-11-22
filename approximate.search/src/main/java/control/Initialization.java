package control;

import javax.inject.Inject;

import preparation.StringReader;
import datastructure.ReferenceIndexStructure;

public class Initialization {

	private final StringReader sequenceProvider;
	private final ReferenceIndexStructure referenceIndexStructure;

	@Inject
	Initialization(final StringReader sequenceProvider, final ReferenceIndexStructure referenceIndexStructure) {
		this.sequenceProvider = sequenceProvider;
		this.referenceIndexStructure = referenceIndexStructure;
	}

	public void init() {
		this.referenceIndexStructure.init(sequenceProvider.read());
	}

	public ReferenceIndexStructure getReferenceIndexStructure() {
		return referenceIndexStructure;
	}
}
