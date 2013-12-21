package compression;

public interface CompressionAlgorithm<I, O> {

	O compress(I input);
}