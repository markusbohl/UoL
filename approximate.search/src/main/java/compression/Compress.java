package compression;

import java.io.BufferedWriter;
import java.io.IOException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import common.Initialization;
import common.preparation.SequenceToCompressProvider;

public class Compress {

	public static void main(final String[] args) throws IOException {
		final Injector injector = Guice.createInjector(new CompressionModule());
		final Initialization initialization = injector.getInstance(Initialization.class);
		System.out.println("### start initialization ###");
		initialization.init();
		final ReferentialCompression compression = injector.getInstance(ReferentialCompression.class);
		final BufferedWriter writer = injector.getInstance(CompressedSequenceBufferedWriter.class);
		final SequenceToCompressProvider sequenceToCompressProvider = injector
				.getInstance(SequenceToCompressProvider.class);
		System.out.println("### start compression ###");
		final String compressedResult = compression.compress(sequenceToCompressProvider.provide());
		System.out.println("### write result ###");
		writer.append(compressedResult);
		writer.close();
		System.out.println("### finished ###");
	}
}
