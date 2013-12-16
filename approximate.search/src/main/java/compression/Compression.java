package compression;

public interface Compression<I, O> {

	O compress(I input);
}