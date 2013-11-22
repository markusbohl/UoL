package control;

import javax.inject.Inject;

import preparation.SequenceProvider;
import datastructure.ReferenceIndexStructure;

public class Initialization {

	private final SequenceProvider sequenceProvider;
	private final ReferenceIndexStructure referenceIndexStructure;

	@Inject
	Initialization(final SequenceProvider sequenceProvider, final ReferenceIndexStructure referenceIndexStructure) {
		this.sequenceProvider = sequenceProvider;
		this.referenceIndexStructure = referenceIndexStructure;
	}

	public void init() {
		this.referenceIndexStructure.init(sequenceProvider.getSequence());
	}

	public ReferenceIndexStructure getReferenceIndexStructure() {
		return referenceIndexStructure;
	}
}
