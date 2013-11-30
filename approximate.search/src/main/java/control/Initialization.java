package control;

import javax.inject.Inject;
import javax.inject.Named;

import preparation.StringProvider;
import datastructure.ReferenceIndexStructure;

public class Initialization {

	private final StringProvider sequenceProvider;
	private final ReferenceIndexStructure referenceIndexStructure;

	@Inject
	Initialization(@Named("reference.sequence") final StringProvider sequenceProvider,
			final ReferenceIndexStructure referenceIndexStructure) {
		this.sequenceProvider = sequenceProvider;
		this.referenceIndexStructure = referenceIndexStructure;
	}

	public void init() {
		this.referenceIndexStructure.init(sequenceProvider.provide());
	}

	public ReferenceIndexStructure getReferenceIndexStructure() {
		return referenceIndexStructure;
	}
}
