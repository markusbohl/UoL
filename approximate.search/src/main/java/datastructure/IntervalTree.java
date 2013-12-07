package datastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * An adapted version of Justin Wetherell's implementation of an interval tree - an ordered tree data structure to hold
 * intervals. It allows one to efficiently find all intervals that overlap with any given interval or point. The
 * original code can be found on <a href=
 * "https://github.com/phishman3579/java-algorithms-implementation/blob/master/src/com/jwetherell/algorithms/data_structures/IntervalTree.java"
 * >https://github.com/phishman3579/java-algorithms-implementation</a>
 * <p>
 * This version allows to return the intersected elements from queries.
 */
public class IntervalTree<O extends Object> {

	private Interval<O> root = null;

	private static final Comparator<IntervalData<?>> startComparator = new Comparator<IntervalData<?>>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(final IntervalData<?> arg0, final IntervalData<?> arg1) {
			// Compare start first
			if (arg0.start < arg1.start) {
				return -1;
			}
			if (arg1.start < arg0.start) {
				return 1;
			}
			return 0;
		}
	};

	private static final Comparator<IntervalData<?>> endComparator = new Comparator<IntervalData<?>>() {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(final IntervalData<?> arg0, final IntervalData<?> arg1) {
			// Compare end first
			if (arg0.end < arg1.end) {
				return -1;
			}
			if (arg1.end < arg0.end) {
				return 1;
			}
			return 0;
		}
	};

	/**
	 * Create interval tree from list of IntervalData objects;
	 * 
	 * @param intervals
	 *            is a list of IntervalData objects
	 */
	public IntervalTree(final List<IntervalData<O>> intervals) {
		if (intervals.size() <= 0) {
			return;
		}

		root = createFromList(intervals);
	}

	protected static final <O extends Object> Interval<O> createFromList(final List<IntervalData<O>> intervals) {
		final Interval<O> newInterval = new Interval<O>();
		if (intervals.size() == 1) {
			final IntervalData<O> middle = intervals.get(0);
			newInterval.center = ((middle.start + middle.end) / 2);
			newInterval.add(middle);
		} else {
			final int half = intervals.size() / 2;
			final IntervalData<O> middle = intervals.get(half);
			newInterval.center = ((middle.start + middle.end) / 2);
			final List<IntervalData<O>> leftIntervals = new ArrayList<IntervalData<O>>();
			final List<IntervalData<O>> rightIntervals = new ArrayList<IntervalData<O>>();
			for (final IntervalData<O> interval : intervals) {
				if (interval.end < newInterval.center) {
					leftIntervals.add(interval);
				} else if (interval.start > newInterval.center) {
					rightIntervals.add(interval);
				} else {
					newInterval.add(interval);
				}
			}
			if (leftIntervals.size() > 0) {
				newInterval.left = createFromList(leftIntervals);
			}
			if (rightIntervals.size() > 0) {
				newInterval.right = createFromList(rightIntervals);
			}
		}
		return newInterval;
	}

	/**
	 * Stabbing query
	 * 
	 * @param index
	 *            to query for.
	 * @return data at index.
	 */
	public IntervalData<O> query(final long index) {
		return root.query(index);
	}

	/**
	 * Range query
	 * 
	 * @param start
	 *            of range to query for.
	 * @param end
	 *            of range to query for.
	 * @return data for range.
	 */
	public IntervalData<O> query(final long start, final long end) {
		return root.query(start, end);
	}

	public List<IntervalData<O>> queryIntersectedElements(final long start, final long end) {
		return root.queryIntersectedElements(start, end);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(IntervalTreePrinter.getString(this));
		return builder.toString();
	}

	protected static class IntervalTreePrinter {

		public static <O extends Object> String getString(final IntervalTree<O> tree) {
			if (tree.root == null) {
				return "Tree has no nodes.";
			}
			return getString(tree.root, "", true);
		}

		private static <O extends Object> String getString(final Interval<O> interval, final String prefix,
				final boolean isTail) {
			final StringBuilder builder = new StringBuilder();

			builder.append(prefix + (isTail ? "└── " : "├── ") + interval.toString() + "\n");
			final List<Interval<O>> children = new ArrayList<Interval<O>>();
			if (interval.left != null) {
				children.add(interval.left);
			}
			if (interval.right != null) {
				children.add(interval.right);
			}
			if (children.size() > 0) {
				for (int i = 0; i < children.size() - 1; i++) {
					builder.append(getString(children.get(i), prefix + (isTail ? "    " : "│   "), false));
				}
				if (children.size() > 0) {
					builder.append(getString(children.get(children.size() - 1), prefix + (isTail ? "    " : "│   "),
							true));
				}
			}

			return builder.toString();
		}
	}

	public static final class Interval<O> {

		private long center = Long.MIN_VALUE;
		private Interval<O> left = null;
		private Interval<O> right = null;
		private final List<IntervalData<O>> overlap = new ArrayList<IntervalData<O>>(); // startComparator

		private void add(final IntervalData<O> data) {
			overlap.add(data);
			Collections.sort(overlap, startComparator);
		}

		/**
		 * Stabbing query
		 * 
		 * @param index
		 *            to query for.
		 * @return data at index.
		 */
		public IntervalData<O> query(final long index) {
			IntervalData<O> results = null;
			if (index < center) {
				// overlap is sorted by start point
				for (final IntervalData<O> data : overlap) {
					if (data.start > index) {
						break;
					}

					final IntervalData<O> temp = data.query(index);
					if (results == null && temp != null) {
						results = temp;
					} else if (temp != null) {
						results.combined(temp);
					}
				}
			} else if (index >= center) {
				// overlapEnd is sorted by end point
				final List<IntervalData<O>> overlapEnd = new ArrayList<IntervalData<O>>();
				Collections.sort(overlapEnd, endComparator);
				overlapEnd.addAll(overlap);
				for (final IntervalData<O> data : overlapEnd) {
					if (data.end < index) {
						break;
					}

					final IntervalData<O> temp = data.query(index);
					if (results == null && temp != null) {
						results = temp;
					} else if (temp != null) {
						results.combined(temp);
					}
				}
			}
			if (index < center) {
				if (left != null) {
					final IntervalData<O> temp = left.query(index);
					if (results == null && temp != null) {
						results = temp;
					} else if (temp != null) {
						results.combined(temp);
					}
				}
			} else if (index >= center) {
				if (right != null) {
					final IntervalData<O> temp = right.query(index);
					if (results == null && temp != null) {
						results = temp;
					} else if (temp != null) {
						results.combined(temp);
					}
				}
			}
			return results;
		}

		/**
		 * Range query
		 * 
		 * @param start
		 *            of range to query for.
		 * @param end
		 *            of range to query for.
		 * @return data for range.
		 */
		public IntervalData<O> query(final long start, final long end) {
			IntervalData<O> results = null;
			for (final IntervalData<O> data : overlap) {
				if (data.start > end) {
					break;
				}
				final IntervalData<O> temp = data.query(start, end);
				if (results == null && temp != null) {
					results = temp;
				} else if (temp != null) {
					results.combined(temp);
				}
			}
			if (left != null && start < center) {
				final IntervalData<O> temp = left.query(start, end);
				if (temp != null && results == null) {
					results = temp;
				} else if (temp != null) {
					results.combined(temp);
				}
			}
			if (right != null && end >= center) {
				final IntervalData<O> temp = right.query(start, end);
				if (temp != null && results == null) {
					results = temp;
				} else if (temp != null) {
					results.combined(temp);
				}
			}
			return results;
		}

		public List<IntervalData<O>> queryIntersectedElements(final long start, final long end) {
			final List<IntervalData<O>> intersectedElements = new LinkedList<>();
			for (final IntervalData<O> data : overlap) {
				if (data.start >= end || data.end <= start) {
					continue;
				}
				final IntervalData<O> temp = data.query(start, end);
				if (temp != null) {
					intersectedElements.add(temp);
				}
			}
			if (left != null && start < center) {
				final List<IntervalData<O>> temp = left.queryIntersectedElements(start, end);
				if (!temp.isEmpty()) {
					intersectedElements.addAll(temp);
				}
			}
			if (right != null && end >= center) {
				final List<IntervalData<O>> temp = right.queryIntersectedElements(start, end);
				if (!temp.isEmpty()) {
					intersectedElements.addAll(temp);
				}
			}
			return intersectedElements;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append("Center=").append(center);
			builder.append(" Set=").append(overlap);
			return builder.toString();
		}
	}

	/**
	 * Data structure representing an interval.
	 */
	public static final class IntervalData<O> implements Comparable<IntervalData<O>> {

		private long start = Long.MIN_VALUE;
		private long end = Long.MAX_VALUE;
		private O object = null;

		/**
		 * Interval data using O as it's unique identifier
		 * 
		 * @param object
		 *            Object which defines the interval data
		 */
		public IntervalData(final long index, final O object) {
			this.start = index;
			this.end = index;
			this.object = object;
		}

		/**
		 * Interval data using O as it's unique identifier
		 * 
		 * @param object
		 *            Object which defines the interval data
		 */
		public IntervalData(final long start, final long end, final O object) {
			this.start = start;
			this.end = end;
			this.object = object;
		}

		public O getObject() {
			return object;
		}

		/**
		 * Clear the indices.
		 */
		public void clear() {
			this.start = Long.MIN_VALUE;
			this.end = Long.MAX_VALUE;
			this.object = null;
		}

		/**
		 * Combined this IntervalData with data.
		 * 
		 * @param data
		 *            to combined with.
		 * @return Data which represents the combination.
		 */
		public IntervalData<O> combined(final IntervalData<O> data) {
			if (data.start < this.start) {
				this.start = data.start;
			}
			if (data.end > this.end) {
				this.end = data.end;
			}
			this.object = data.object;
			return this;
		}

		/**
		 * Deep copy of data.
		 * 
		 * @return deep copy.
		 */
		public IntervalData<O> copy() {
			return new IntervalData<O>(start, end, object);
		}

		/**
		 * Query inside this data object.
		 * 
		 * @param start
		 *            of range to query for.
		 * @param end
		 *            of range to query for.
		 * @return Data queried for or NULL if it doesn't match the query.
		 */
		public IntervalData<O> query(final long index) {
			if (index < this.start || index > this.end) {
				// Ignore
			} else {
				return copy();
			}
			return null;
		}

		/**
		 * Query inside this data object.
		 * 
		 * @param start
		 *            of range to query for.
		 * @param end
		 *            of range to query for.
		 * @return Data queried for or NULL if it doesn't match the query.
		 */
		public IntervalData<O> query(final long start, final long end) {
			if (end < this.start || start > this.end) {
				// Ignore
			} else {
				return copy();
			}
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(final Object obj) {
			if (!(obj instanceof IntervalData)) {
				return false;
			}
			@SuppressWarnings("unchecked")
			final IntervalData<O> data = (IntervalData<O>) obj;
			if (this.start == data.start && this.end == data.end) {
				if (!this.object.equals(data.object)) {
					return false;
				}
				return true;
			}
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo(final IntervalData<O> d) {
			if (this.end < d.end) {
				return -1;
			}
			if (d.end < this.end) {
				return 1;
			}
			return 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append(start).append("->").append(end);
			builder.append(" object=").append(object);
			return builder.toString();
		}
	}
}
