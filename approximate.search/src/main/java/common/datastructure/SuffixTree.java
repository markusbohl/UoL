package common.datastructure;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * An adapted version of Justin Wetherell's implementation of a suffix tree based on the Ukkonnen's algorithm. The
 * original code can be found on <a href=
 * "https://github.com/phishman3579/java-algorithms-implementation/blob/master/src/com/jwetherell/algorithms/data_structures/SuffixTree.java"
 * >https://github.com/phishman3579/java-algorithms-implementation</a>
 * <p>
 * The changes include the implementation of an <code>indicesOf(C)</code> method which returns all indices at which a
 * given {@link CharSequence} can be found in the sequence the suffix tree has been created for.
 */
public class SuffixTree<C extends CharSequence> {

	private static final char DEFAULT_END_SEQ_CHAR = '$';

	private String string = null;
	private char[] characters = null;

	private final Map<Integer, Link> linksMap = new HashMap<Integer, Link>();
	private final Map<Integer, Edge<C>> edgeMap = new TreeMap<Integer, Edge<C>>();

	private int currentNode = 0;
	private int firstCharIndex = 0;
	private int lastCharIndex = -1;

	private char END_SEQ_CHAR = DEFAULT_END_SEQ_CHAR;

	/**
	 * Create suffix tree with sequence and default end sequence.
	 * 
	 * @param seq
	 *            to create a suffix tree with.
	 */
	public SuffixTree(final C seq) {
		this(seq, DEFAULT_END_SEQ_CHAR);
	}

	/**
	 * Create suffix tree with sequence and end sequence parameter.
	 * 
	 * @param seq
	 *            to create a suffix tree with.
	 * @param endSeq
	 *            which defines the end of a sequence.
	 */
	public SuffixTree(final C seq, final char endSeq) {
		END_SEQ_CHAR = endSeq;
		final StringBuilder builder = new StringBuilder(seq);
		if (builder.indexOf(String.valueOf(seq)) >= 0) {
			builder.append(END_SEQ_CHAR);
		}
		string = builder.toString();
		final int length = string.length();
		characters = new char[length];
		for (int i = 0; i < length; i++) {
			characters[i] = string.charAt(i);
		}

		for (int i = 0; i < length; i++) {
			addPrefix(i);
		}
	}

	/**
	 * Does the sub-sequence exist in the suffix tree.
	 * 
	 * @param sub
	 *            sub-sequence to locate in the tree.
	 * @return True if the sub-sequence exist in the tree.
	 */
	public boolean doesSubStringExist(final C sub) {
		final char[] chars = toCharArray(sub);
		final int[] indices = searchEdges(chars);
		final int start = indices[0];
		final int end = indices[1];
		final int length = end - start;
		if (length == (chars.length - 1)) {
			return true;
		}
		return false;
	}

	/**
	 * Get all the suffixes in the tree.
	 * 
	 * @return set of suffixes in the tree.
	 */
	public Set<String> getSuffixes() {
		final Set<String> set = getSuffixes(0);
		return set;
	}

	/**
	 * Get all suffixes at starting node.
	 * 
	 * @param start
	 *            node.
	 * @return set of suffixes in the tree at start node.
	 */
	private Set<String> getSuffixes(final int start) {
		final Set<String> set = new TreeSet<String>();
		for (final int key : edgeMap.keySet()) {
			final Edge<C> e = edgeMap.get(key);
			if (e == null) {
				continue;
			}
			if (e.startNode != start) {
				continue;
			}

			String s = (string.substring(e.firstCharIndex, e.lastCharIndex + 1));
			final Link n = linksMap.get(e.endNode);
			if (n == null) {
				final int index = s.indexOf(END_SEQ_CHAR);
				if (index >= 0) {
					s = s.substring(0, index);
				}
				set.add(s);
			} else {
				final Set<String> set2 = getSuffixes(e.endNode);
				for (String s2 : set2) {
					final int index = s2.indexOf(END_SEQ_CHAR);
					if (index >= 0) {
						s2 = s2.substring(0, index);
					}
					set.add(s + s2);
				}
			}
		}
		return set;
	}

	/**
	 * Get all edges in the table
	 * 
	 * @return debug string.
	 */
	public String getEdgesTable() {
		final StringBuilder builder = new StringBuilder();
		if (edgeMap.size() > 0) {
			final int lastCharIndex = characters.length;
			builder.append("Edge\tStart\tEnd\tSuf\tfirst\tlast\tString\n");
			for (final int key : edgeMap.keySet()) {
				final Edge<C> e = edgeMap.get(key);
				final Link link = linksMap.get(e.endNode);
				final int suffix = (link != null) ? link.suffixNode : -1;
				builder.append("\t" + e.startNode + "\t" + e.endNode + "\t" + suffix + "\t" + e.firstCharIndex + "\t"
						+ e.lastCharIndex + "\t");
				final int begin = e.firstCharIndex;
				final int end = (lastCharIndex < e.lastCharIndex) ? lastCharIndex : e.lastCharIndex;
				builder.append(string.substring(begin, end + 1));
				builder.append("\n");
			}
			builder.append("Link\tStart\tEnd\n");
			for (final int key : linksMap.keySet()) {
				final Link link = linksMap.get(key);
				builder.append("\t" + link.node + "\t" + link.suffixNode + "\n");
			}
		}
		return builder.toString();
	}

	/**
	 * Add prefix at index.
	 * 
	 * @param index
	 *            to add prefix at.
	 */
	private void addPrefix(final int index) {
		int parentNodeIndex = 0;
		int lastParentIndex = -1;

		while (true) {
			Edge<C> edge = null;
			parentNodeIndex = currentNode;
			if (isExplicit()) {
				edge = Edge.find(this, currentNode, characters[index]);
				if (edge != null) {
					// Edge already exists
					break;
				}
			} else {
				// Implicit node, a little more complicated
				edge = Edge.find(this, currentNode, characters[firstCharIndex]);
				final int span = lastCharIndex - firstCharIndex;
				if (characters[edge.firstCharIndex + span + 1] == characters[index]) {
					// If the edge is the last char, don't split
					break;
				}
				parentNodeIndex = edge.split(currentNode, firstCharIndex, lastCharIndex);
			}
			edge = new Edge<C>(this, index, characters.length - 1, parentNodeIndex);
			if (lastParentIndex > 0) {
				// Last parent is not root, create a link.
				linksMap.get(lastParentIndex).suffixNode = parentNodeIndex;
			}
			lastParentIndex = parentNodeIndex;
			if (currentNode == 0) {
				firstCharIndex++;
			} else {
				// Current node is not root, follow link
				currentNode = linksMap.get(currentNode).suffixNode;
			}
			if (!isExplicit()) {
				canonize();
			}
		}
		if (lastParentIndex > 0) {
			// Last parent is not root, create a link.
			linksMap.get(lastParentIndex).suffixNode = parentNodeIndex;
		}
		lastParentIndex = parentNodeIndex;
		lastCharIndex++; // Now the endpoint is the next active point
		if (!isExplicit()) {
			canonize();
		}
	};

	/**
	 * Is the tree explicit
	 * 
	 * @return True if explicit.
	 */
	private boolean isExplicit() {
		return firstCharIndex > lastCharIndex;
	}

	/**
	 * Canonize the tree.
	 */
	private void canonize() {
		Edge<C> edge = Edge.find(this, currentNode, characters[firstCharIndex]);
		int edgeSpan = edge.lastCharIndex - edge.firstCharIndex;
		while (edgeSpan <= (lastCharIndex - firstCharIndex)) {
			firstCharIndex = firstCharIndex + edgeSpan + 1;
			currentNode = edge.endNode;
			if (firstCharIndex <= lastCharIndex) {
				edge = Edge.find(this, edge.endNode, characters[firstCharIndex]);
				edgeSpan = edge.lastCharIndex - edge.firstCharIndex;
			}
		}
	}

	/**
	 * Returns a two element int array who's 0th index is the start index and 1th is the end index.
	 */
	private int[] searchEdges(final char[] query) {
		int startNode = 0;
		int queryPosition = 0;
		int startIndex = -1;
		int endIndex = -1;
		boolean stop = false;

		while (!stop && queryPosition < query.length) {
			final Edge<C> edge = Edge.find(this, startNode, query[queryPosition]);
			if (edge == null) {
				stop = true;
				break;
			}
			if (startNode == 0) {
				startIndex = edge.firstCharIndex;
			}
			for (int i = edge.firstCharIndex; i <= edge.lastCharIndex; i++) {
				if (queryPosition >= query.length) {
					stop = true;
					startIndex = endIndex - queryPosition;
					break;
				} else if (query[queryPosition] == characters[i]) {
					queryPosition++;
					endIndex = i + 1;
				} else {
					stop = true;
					startIndex = endIndex - queryPosition;
					break;
				}
			}
			if (!stop) { // proceed with next node
				startNode = edge.endNode;
				if (startNode == -1) {
					stop = true;
				}
			}
		}
		return (new int[] { startIndex, endIndex });
	}

	public List<Integer> indicesOf(final C sub) {
		final char[] chars = toCharArray(sub);
		final String queryString = new String(chars);
		final Edge<C> edge = Edge.find(this, 0, chars[0]);
		if (edge == null) {
			return Collections.emptyList();
		}
		final String edgeLabel = labelOf(edge);
		return indicesOf(queryString, edge, edgeLabel);
	}

	private char[] toCharArray(final C sub) {
		final char[] chars = new char[sub.length()];
		for (int i = 0; i < sub.length(); i++) {
			chars[i] = sub.charAt(i);
		}
		return chars;
	}

	private String labelOf(final Edge<C> edge) {
		return edge.tree.string.substring(edge.firstCharIndex, edge.lastCharIndex + 1);
	}

	private List<Integer> indicesOf(final String queryString, final Edge<C> edge, final String mergedEdgeLabel) {
		final List<Integer> indices = new LinkedList<>();

		if (mergedEdgeLabel.length() < queryString.length()) {
			if (queryString.startsWith(mergedEdgeLabel)) {
				final Edge<C> next = Edge.find(edge.tree, edge.endNode, queryString.charAt(mergedEdgeLabel.length()));
				if (next != null) {
					indices.addAll(indicesOf(queryString, next, mergedEdgeLabel + labelOf(next)));
				}
			}
		} else {
			if (mergedEdgeLabel.startsWith(queryString)) {
				if (edge.lastCharIndex == edge.tree.lastCharIndex) {
					indices.add(edge.lastCharIndex - mergedEdgeLabel.length() + 1);
				} else {
					for (final int key : edgeMap.keySet()) {
						final Edge<C> e = edgeMap.get(key);
						if (edge.endNode == e.startNode) {
							if (e.lastCharIndex == e.tree.lastCharIndex) {
								indices.add(e.lastCharIndex - (mergedEdgeLabel + labelOf(e)).length() + 1);
							} else {
								indices.addAll(indicesOf(queryString, e, mergedEdgeLabel + labelOf(e)));
							}
						}
					}
				}
			}
		}

		return indices;
	}

	public Match findLongestCommonSubString(final C sub) {
		final char[] chars = toCharArray(sub);
		final int[] startAndEndOfEdge = searchEdges(chars);
		final int startIndex = startAndEndOfEdge[0];
		final int endIndex = startAndEndOfEdge[1];
		final int length = endIndex - startIndex;

		if (startIndex < 0) {
			return new Match(startIndex, 0);
		}

		return new Match(startIndex, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("String = ").append(this.string).append("\n");
		builder.append("End of word character = ").append(END_SEQ_CHAR).append("\n");
		builder.append(TreePrinter.getString(this));
		return builder.toString();
	}

	private static class Link implements Comparable<Link> {

		private int node = 0;
		private int suffixNode = -1;

		public Link(final int node) {
			this.node = node;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append("node=").append(node).append("\n");
			builder.append("suffixNode=").append(suffixNode).append("\n");
			return builder.toString();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo(final Link link) {
			if (link == null) {
				return -1;
			}

			if (node < link.node) {
				return -1;
			}
			if (node > link.node) {
				return 1;
			}

			if (suffixNode < link.suffixNode) {
				return -1;
			}
			if (suffixNode > link.suffixNode) {
				return 1;
			}

			return 0;
		}
	};

	private static class Edge<C extends CharSequence> implements Comparable<Edge<C>> {

		private static final int KEY_MOD = 54768941; // Should be a prime that is
												// roughly 10% larger than the
												// String
		private static int count = 1;

		private SuffixTree<C> tree = null;

		private int startNode = -1;
		private int endNode = 0;
		private int firstCharIndex = 0;
		private int lastCharIndex = 0;

		private Edge(final SuffixTree<C> tree, final int first, final int last, final int parent) {
			this.tree = tree;
			firstCharIndex = first;
			lastCharIndex = last;
			startNode = parent;
			endNode = count++;
			insert(this);
		}

		private int getKey() {
			return key(startNode, tree.characters[firstCharIndex]);
		}

		private static int key(final int node, final char c) {
			return ((node << 8) + c) % KEY_MOD;
		}

		private void insert(final Edge<C> edge) {
			tree.edgeMap.put(edge.getKey(), edge);
		}

		private void remove(final Edge<C> edge) {
			int i = edge.getKey();
			Edge<C> e = tree.edgeMap.remove(i);
			while (true) {
				e.startNode = -1;
				final int j = i;
				while (true) {
					i = ++i % KEY_MOD;
					e = tree.edgeMap.get(i);
					if (e == null) {
						return;
					}
					final int r = key(e.startNode, tree.characters[e.firstCharIndex]);
					if (i >= r && r > j) {
						continue;
					}
					if (r > j && j > i) {
						continue;
					}
					if (j > i && i >= r) {
						continue;
					}
					break;
				}
				tree.edgeMap.put(j, e);
			}
		}

		private static <C extends CharSequence> Edge<C> find(final SuffixTree<C> tree, final int node, final char c) {
			final int key = key(node, c);
			return tree.edgeMap.get(key);
		}

		private int split(final int originNode, final int firstCharIndex, final int lastCharIndex) {
			remove(this);
			final Edge<C> newEdge = new Edge<C>(tree, this.firstCharIndex, this.firstCharIndex + lastCharIndex
					- firstCharIndex, originNode);
			Link link = tree.linksMap.get(newEdge.endNode);
			if (link == null) {
				link = new Link(newEdge.endNode);
				tree.linksMap.put(newEdge.endNode, link);
			}
			tree.linksMap.get(newEdge.endNode).suffixNode = originNode;
			this.firstCharIndex += lastCharIndex - firstCharIndex + 1;
			this.startNode = newEdge.endNode;
			insert(this);
			return newEdge.endNode;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			return getKey();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(final Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj instanceof Edge) {
				return false;
			}

			@SuppressWarnings("unchecked")
			final Edge<C> e = (Edge<C>) obj;
			if (startNode == e.startNode && tree.characters[firstCharIndex] == tree.characters[e.firstCharIndex]) {
				return true;
			}

			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append("startNode=").append(startNode).append("\n");
			builder.append("endNode=").append(endNode).append("\n");
			builder.append("firstCharIndex=").append(firstCharIndex).append("\n");
			builder.append("lastCharIndex=").append(lastCharIndex).append("\n");
			final String s = tree.string.substring(firstCharIndex, lastCharIndex + 1);
			builder.append("string=").append(s).append("\n");
			return builder.toString();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo(final Edge<C> edge) {
			if (edge == null) {
				return -1;
			}

			if (startNode < edge.startNode) {
				return -1;
			}
			if (startNode > edge.startNode) {
				return 1;
			}

			if (endNode < edge.endNode) {
				return -1;
			}
			if (endNode > edge.endNode) {
				return 1;
			}

			if (firstCharIndex < edge.firstCharIndex) {
				return -1;
			}
			if (firstCharIndex > edge.firstCharIndex) {
				return 1;
			}

			if (lastCharIndex < edge.lastCharIndex) {
				return -1;
			}
			if (lastCharIndex > edge.lastCharIndex) {
				return 1;
			}

			return 0;
		}
	}

	public static final class Match {

		private final int index;
		private final int length;

		public Match(final int index, final int length) {
			this.index = index;
			this.length = length;
		}

		public int getIndex() {
			return index;
		}

		public int getLength() {
			return length;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + length;
			result = prime * result + index;
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Match other = (Match) obj;
			if (length != other.length) {
				return false;
			}
			if (index != other.index) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return String.format("Match [position=%s, length=%s]", index, length);
		}
	}

	protected static class TreePrinter {

		public static <C extends CharSequence> void printNode(final SuffixTree<C> tree) {
			System.out.println(getString(tree, null, "", true));
		}

		public static <C extends CharSequence> String getString(final SuffixTree<C> tree) {
			return getString(tree, null, "", true);
		}

		private static <C extends CharSequence> String getString(final SuffixTree<C> tree, final Edge<C> e,
				final String prefix, final boolean isTail) {
			final StringBuilder builder = new StringBuilder();
			int value = 0;
			if (e != null) {
				value = e.endNode;
				String string = tree.string.substring(e.firstCharIndex, e.lastCharIndex + 1);
				final int index = string.indexOf(tree.END_SEQ_CHAR);
				if (index >= 0) {
					string = string.substring(0, index + 1);
				}
				builder.append(prefix + (isTail ? "└── " : "├── ") + "(" + value + ") " + string + "\n");
			} else {
				builder.append(prefix + (isTail ? "└── " : "├── ") + "(" + 0 + ")" + "\n");
			}

			if (tree.edgeMap.size() > 0) {
				final List<Edge<C>> children = new LinkedList<Edge<C>>();
				for (final Edge<C> edge : tree.edgeMap.values()) {
					if (edge != null && (edge.startNode == value)) {
						children.add(edge);
					}
				}
				if (children.size() > 0) {
					for (int i = 0; i < children.size() - 1; i++) {
						final Edge<C> edge = children.get(i);
						builder.append(getString(tree, edge, prefix + (isTail ? "    " : "│   "), false));
					}
					if (children.size() >= 1) {
						final Edge<C> edge = children.get(children.size() - 1);
						builder.append(getString(tree, edge, prefix + (isTail ? "    " : "│   "), true));
					}
				}
			}
			return builder.toString();
		}
	}
}
