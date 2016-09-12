package common;

import java.util.ArrayList;
import java.util.Collection;

public class ArrayListWithSortableKey<E extends Comparable<E>> extends ArrayList<E>
		implements Comparable<ArrayListWithSortableKey<E>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6086310283583070059L;
	private int sortKey;
	
	public ArrayListWithSortableKey(){
		super();
	}

	public ArrayListWithSortableKey(Collection<? extends E> c) {
		super(c);
	}
	
	public ArrayListWithSortableKey(int initialCapacity){
		super(initialCapacity);
	}

	/**
	 * @return the sortKey
	 */
	public int getSortKey() {
		return sortKey;
	}

	/**
	 * @param sortKey
	 *            the sortKey to set
	 */
	public void setSortKey(int sortKey) {
		this.sortKey = sortKey;
	}

	/**
	 * Sets the {@code sortKey} for {@code this} and {@code that} and then calls
	 * {@code this.compareTo(that)}
	 * 
	 * @param sortKey
	 *            The column to be used as the sortKey
	 * @param that
	 *            The {@code ArrayListWithSortableKey} to be compared
	 * @return Same as {@link #compareTo(ArrayListWithSortableKey)} after
	 *         applying the sortKey to {@code this} and {@code that}
	 */
	public int compareTo(int sortKey, ArrayListWithSortableKey<E> that) {
		this.setSortKey(sortKey);
		that.setSortKey(sortKey);
		return this.compareTo(that);
	}

	@Override
	public int compareTo(ArrayListWithSortableKey<E> that) {
		return this.get(this.getSortKey()).compareTo(that.get(that.getSortKey()));
	}
}
