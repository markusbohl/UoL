package search.datastructure;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import search.datastructure.SuffixTree.Match;

public class MatchTest {

	private static final int INDEX = 79;
	private static final int LENGTH = 24;

	private Match match;

	@Before
	public void setUp() throws Exception {
		match = new Match(INDEX, LENGTH);
	}

	@Test
	public void getIndex() {
		assertThat(match.getIndex(), is(INDEX));
	}

	@Test
	public void getLength() {
		assertThat(match.getLength(), is(LENGTH));
	}

	@Test
	public void equalsIsTrue() {
		assertTrue(match.equals(new Match(INDEX, LENGTH)));
	}

	@Test
	public void equalsIsFalse() {
		assertFalse(match.equals(new Match(0, 0)));
	}

	@Test
	public void hashCodeIsEqual() {
		assertThat(match.hashCode(), is(new Match(INDEX, LENGTH).hashCode()));
	}

	@Test
	public void hashCodeIsNotEqual() {
		assertThat(match.hashCode(), is(not(new Match(0, 0).hashCode())));
	}
}
