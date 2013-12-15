package search.datastructure;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import search.datastructure.SuffixTree.TreePrinter;

@RunWith(JUnitParamsRunner.class)
public class SuffixTreeTest {

	@Test
	@Parameters
	public void indicesOf(final String string, final String query, final Integer... indices) {
		final SuffixTree<String> suffixTree = new SuffixTree<>(string);
		TreePrinter.printNode(suffixTree);

		final List<Integer> result = suffixTree.indicesOf(query);
		assertThat(result, hasSize(indices.length));
		assertThat(result, hasItems(indices));
	}

	Object[] parametersForIndicesOf() {
		return $($("AGCTAGCT", "N", new Integer[] {}), //
				$("AGCTAGCT", "AN", new Integer[] {}),//
				$("AGCTAGCT", "A", new Integer[] { 0, 4 }),//
				$("AGCTAGCT", "G", new Integer[] { 1, 5 }),//
				$("AGCTAGCT", "C", new Integer[] { 2, 6 }),//
				$("AGCTAGCT", "T", new Integer[] { 3, 7 }),//
				$("AGCTAGCT", "AGCT", new Integer[] { 0, 4 })//
		);
	}
}
