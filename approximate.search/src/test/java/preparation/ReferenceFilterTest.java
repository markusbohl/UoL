package preparation;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;

public class ReferenceFilterTest {

	private ReferenceFilter filter;

	@Before
	public void setUp() throws Exception {
		initMocks(this);

		filter = new ReferenceFilter();
	}
}
