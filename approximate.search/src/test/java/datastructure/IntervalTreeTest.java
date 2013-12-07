package datastructure;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import datastructure.IntervalTree.IntervalData;

@RunWith(JUnitParamsRunner.class)
public class IntervalTreeTest {

	@Test
	@Parameters
	public void findIntersectedElements(final long start, final long end) throws Exception {
		final IntervalData<String> intervalData = data(5, 15, "0123456789");
		final IntervalTree<String> intervalTree = new IntervalTree<>(intervalDataList(intervalData));

		final List<IntervalData<String>> intersectedElements = intervalTree.queryIntersectedElements(start, end);

		assertThat(intersectedElements, hasSize(1));
		assertThat(intersectedElements, hasItem(intervalData));
	}

	Object[] parametersForFindIntersectedElements() {
		return $($(0L, 10L), $(5L, 15L), $(10L, 20L), $(8L, 12L), $(0L, 20L));
	}

	@Test
	@Parameters
	public void doNotFindNonIntersectedElements(final long start, final long end) throws Exception {
		final IntervalData<String> intervalData = data(5, 15, "0123456789");
		final IntervalTree<String> intervalTree = new IntervalTree<>(intervalDataList(intervalData));

		final List<IntervalData<String>> intersectedElements = intervalTree.queryIntersectedElements(start, end);

		assertThat(intersectedElements, hasSize(0));
	}

	Object[] parametersForDoNotFindNonIntersectedElements() {
		return $($(0L, 4L), $(0L, 5L), $(15L, 20L), $(16L, 20L));
	}

	@Test
	@Parameters
	public void queryIntersectedElements(final List<IntervalData<String>> intervals, final long start, final long end,
			final IntervalData<String>[] intersectedElements) {
		final IntervalTree<String> intervalTree = new IntervalTree<>(intervals);

		final List<IntervalData<String>> result = intervalTree.queryIntersectedElements(start, end);

		assertThat(result, hasSize(intersectedElements.length));
		assertThat(result, hasItems(intersectedElements));
	}

	Object[] parametersForQueryIntersectedElements() {
		final IntervalData<String> data1 = data(0, 5, "01234");
		final IntervalData<String> data2 = data(3, 7, "3456");
		final IntervalData<String> data3 = data(5, 15, "56789abcde");
		final IntervalData<String> data4 = data(8, 12, "89abc");
		final IntervalData<String> data5 = data(13, 17, "ABCD");
		final IntervalData<String> data6 = data(15, 20, "VWXYZ");
		final List<IntervalData<String>> dataList = intervalDataList(data1, data2, data3, data4, data5, data6);

		return $($(dataList, 0L, 10L, new IntervalData[] { data1, data2, data3, data4 }),
				$(dataList, 12L, 20L, new IntervalData[] { data3, data5, data6 }),
				$(dataList, 8L, 12L, new IntervalData[] { data3, data4 }),
				$(dataList, 0L, 20L, new IntervalData[] { data1, data2, data3, data4, data5, data6 }));
	}

	private final IntervalData<String> data(final int start, final int end, final String value) {
		return new IntervalData<>(start, end, value);
	}

	@SafeVarargs
	private final List<IntervalData<String>> intervalDataList(final IntervalData<String>... intervalData) {
		return Arrays.asList(intervalData);
	}

	@Test
	public void getIntervalDataValue() {
		final String value = "0123456789";
		final IntervalData<String> intervalData = data(5, 15, value);
		final IntervalTree<String> intervalTree = new IntervalTree<>(intervalDataList(intervalData));

		final List<IntervalData<String>> result = intervalTree.queryIntersectedElements(5, 15);

		assertThat(result.get(0).getObject(), is(value));
	}
}
