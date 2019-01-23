package org.aksw.rex.util;

/**
 * Generic class to represent pairs. Can be any used to represent any pairs of
 * related data, such as <b>&lt;url, content&gt;</b> or <b>&lt;Subject XPath,
 * Object XPath&gt;</b>, etc.
 * 
 * @author r.usbeck
 *
 * @param <L> Left element of the pair.
 * @param <R> Right element of the pair.
 */
public class Pair<L, R> {

	private final L left;
	private final R right;

	public Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}

	public L getLeft() {
		return left;
	}

	public R getRight() {
		return right;
	}

	@Override
	public int hashCode() {
		return left.hashCode() ^ right.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Pair))
			return false;
		Pair pairo = (Pair) o;
		return this.left.equals(pairo.getLeft()) && this.right.equals(pairo.getRight());
	}

	@Override
	public String toString() {
		return "<" + left + ", " + right + ">";
	}

}