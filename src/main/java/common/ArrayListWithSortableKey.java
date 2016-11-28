package common;

/*-
 * #%L
 * FOKProjects Common
 * %%
 * Copyright (C) 2016 Frederik Kammel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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
