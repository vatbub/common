package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class ArrayListWithSortableKeyTest {

	@Test
	/**
	 * Tests if all constructors work without exceptions
	 */
	public void constructorTest() {
		// Constructor with no arguments
		ArrayListWithSortableKey<String> list = new ArrayListWithSortableKey<String>();

		// Constructor wit elements
		String[] elements = new String[] { "1", "2", "3" };
		list = new ArrayListWithSortableKey<String>(Arrays.asList(elements));

		for (String el : elements) {
			assert list.contains(el);
		}

		// Constructor with initial capacity
		list = new ArrayListWithSortableKey<String>(10);
	}

	@Test
	public void sortArrayListWithSortableKeyTest() {
		// Generate a new list
		ArrayList<ArrayListWithSortableKey<String>> list = new ArrayList<ArrayListWithSortableKey<String>>();
		int firstSortKey = 0;
		int secondSortKey = 1;

		String[][] elements = new String[][] { { "abc", "jkl", "mno" }, { "def", "ghi", "pqr" } };

		for (String[] row : elements) {
			ArrayListWithSortableKey<String> rowList = new ArrayListWithSortableKey<String>(Arrays.asList(row));
			rowList.setSortKey(firstSortKey);
			assert rowList.getSortKey() == firstSortKey;
			list.add(rowList);
		}

		Collections.sort(list);

		// Should not change the order
		for (int r = 0; r < list.size(); r++) {
			for (int el = 0; el < list.get(r).size(); el++) {
				assert list.get(r).get(el).equals(elements[r][el]);
			}
		}

		// Apply the second sort key
		for (ArrayListWithSortableKey<String> row : list) {
			row.setSortKey(secondSortKey);
			assert row.getSortKey() == secondSortKey;
		}

		Collections.sort(list);

		// Should invert the row order
		for (int r = 0; r < list.size(); r++) {
			for (int el = 0; el < list.get(r).size(); el++) {
				assert list.get(r).get(el).equals(elements[list.size() - r - 1][el]);
			}
		}

	}

	@Test
	public void compareToTest() {
		ArrayList<ArrayListWithSortableKey<String>> list = new ArrayList<ArrayListWithSortableKey<String>>();
		int firstSortKey = 0;
		int secondSortKey = 1;

		String[][] elements = new String[][] { { "abc", "jkl", "mno" }, { "def", "ghi", "pqr" } };

		for (String[] row : elements) {
			ArrayListWithSortableKey<String> rowList = new ArrayListWithSortableKey<String>(Arrays.asList(row));
			rowList.setSortKey(firstSortKey);
			assert rowList.getSortKey() == firstSortKey;
			list.add(rowList);
		}

		// Compare first using the default compareTo and then using the custom
		// compareTo
		{
			int defaultCompareResult = list.get(0).compareTo(list.get(1));
			int customCompareResult = list.get(0).compareTo(firstSortKey, list.get(1));

			assert defaultCompareResult == customCompareResult;
		}

		// Change the sort key while comparing
		// Compare first using the default compareTo and then using the custom
		// compareTo
		{
			int defaultCompareResult = list.get(0).compareTo(list.get(1));
			int customCompareResult = list.get(0).compareTo(secondSortKey, list.get(1));

			// Should not be equal
			assert defaultCompareResult != customCompareResult;
		}

		// Compare first using the default compareTo and then using the custom
		// compareTo
		{
			int defaultCompareResult = list.get(0).compareTo(list.get(1));
			int customCompareResult = list.get(0).compareTo(secondSortKey, list.get(1));

			// Should be equal
			assert defaultCompareResult == customCompareResult;
		}
	}
}
