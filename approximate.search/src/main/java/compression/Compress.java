package compression;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

import common.datastructure.ReferenceIndexStructure;
import common.preparation.StringProvider;
import common.preparation.StringProviderFactory;

public class Compress {

	public static void main(final String[] args) throws IOException {
		final Injector injector = Guice.createInjector(new CompressionModule());
		final StringProviderFactory stringProviderFactory = getStringProviderFactory(injector);
		final String referenceFilePath = getReferenceFilePath(injector);
		final String referenceSequence = getFastaContentFromFile(referenceFilePath, stringProviderFactory);
		final ReferenceIndexStructure indexStructure = getReferenceIndexStructure(injector);
		indexStructure.init(referenceSequence);

		final ReferentialCompressionAlgorithm compressionAlgorithm = getCompressionAlgorithm(injector);
		final String sequenceToCompressFilePath = getSequenceToCompressFilePath(injector);
		final String sequenceToCompress = getFastaContentFromFile(sequenceToCompressFilePath, stringProviderFactory);
		final String compressedResult = compressionAlgorithm.compress(sequenceToCompress);
		final BufferedWriter bufferedWriter = getCompressedSequenceWriter(injector);
		bufferedWriter.write(compressedResult);
		bufferedWriter.close();
	}

	private static StringProviderFactory getStringProviderFactory(final Injector injector) {
		return injector.getInstance(StringProviderFactory.class);
	}

	private static String getFastaContentFromFile(final String referenceFilePath,
			final StringProviderFactory stringProviderFactory) {
		final StringProvider referenceSequenceProvider = stringProviderFactory.createFromFastaFile(referenceFilePath);

		return referenceSequenceProvider.toString();
	}

	private static String getReferenceFilePath(final Injector injector) {
		return injector.getInstance(Key.get(String.class, Names.named("reference.sequence.file.path")));
	}

	private static String getSequenceToCompressFilePath(final Injector injector) {
		return injector.getInstance(Key.get(String.class, Names.named("sequence.to.compress.file.path")));
	}

	private static CompressedSequenceBufferedWriter getCompressedSequenceWriter(final Injector injector) {
		return injector.getInstance(CompressedSequenceBufferedWriter.class);
	}

	private static ReferenceIndexStructure getReferenceIndexStructure(final Injector injector) {
		return injector.getInstance(ReferenceIndexStructure.class);
	}

	private static ReferentialCompressionAlgorithm getCompressionAlgorithm(final Injector injector) {
		return injector.getInstance(ReferentialCompressionAlgorithm.class);
	}
}
