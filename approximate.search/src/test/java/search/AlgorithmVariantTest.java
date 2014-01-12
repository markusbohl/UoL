package search;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class AlgorithmVariantTest {

	@Test
	public void testVariants() {
		assertThat(AlgorithmVariant.get(1), is(AlgorithmVariant.Rme1_RawOA_1));
		assertThat(AlgorithmVariant.get(2), is(AlgorithmVariant.Rme1_RawOA_2));
		assertThat(AlgorithmVariant.get(3), is(AlgorithmVariant.Rme2_RawOA_1));
		assertThat(AlgorithmVariant.get(4), is(AlgorithmVariant.Rme2_RawOA_2));
	}
}
