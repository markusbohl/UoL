package search.datastructure;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import search.datastructure.SuffixTree.Match;

@RunWith(JUnitParamsRunner.class)
public class SuffixTreeTest {

	@Test
	@Parameters
	public void indicesOf(final String string, final String query, final Integer... indices) {
		final SuffixTree<String> suffixTree = new SuffixTree<>(string);

		final List<Integer> result = suffixTree.indicesOf(query);

		assertThat(result, hasSize(indices.length));
		assertThat(result, hasItems(indices));
	}

	Object[] parametersForIndicesOf() {
		return $($("AGCTAGCT", "N", new Integer[] {}), //
				$("AGCTAGCT", "AN", new Integer[] {}),//
				$("AGCTAGCT", "AGCTAGCT", new Integer[] { 0 }),//
				$("AGCTAGCT", "A", new Integer[] { 0, 4 }),//
				$("AGCTAGCT", "G", new Integer[] { 1, 5 }),//
				$("AGCTAGCT", "C", new Integer[] { 2, 6 }),//
				$("AGCTAGCT", "T", new Integer[] { 3, 7 }),//
				$("AGCTAGCT", "AGCT", new Integer[] { 0, 4 })//
		);
	}

	@Test
	@Parameters
	public void findLongestCommonSubstring(final String string, final String otherString, final Match expectedMatch) {
		final SuffixTree<String> suffixTree = new SuffixTree<>(string);

		final Match actualMatch = suffixTree.findLongestCommonSubString(otherString);

		assertThat(actualMatch, is(expectedMatch));
	}

	Object[] parametersForFindLongestCommonSubstring() {
		return $($("AGCTAGCT", "N", new Match(-1, 0)),//
				$("AGCTAGCT", "AGC", new Match(0, 3)),//
				$("AGCTAGCT", "AGCN", new Match(0, 3)),//
				$("AGCTAGCT", "TAGCT", new Match(3, 5)),//
				$("AGCTNAGCTT", "AGCTT", new Match(5, 5)),//
				$("AGCTNAGCTT", "AGCTTN", new Match(5, 5)),//
				$("AGCTNTTNAGCTTN", "AGCTT", new Match(8, 5)),//
				$("AGCTAGCT", "CTAG", new Match(2, 4)),//
				$("AGCTAGCT", "CTAGN", new Match(2, 4))//
		);
	}
}
