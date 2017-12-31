package com.github.vatbub.common.core;

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


import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ArrayListWithSortableKeyTest {

    /**
     * Tests if all constructors work without exceptions
     */
    @SuppressWarnings("UnusedAssignment")
    @Test
    public void constructorTest() {
        // Constructor with no arguments
        ArrayListWithSortableKey<String> list = new ArrayListWithSortableKey<>();

        // Constructor wit elements
        String[] elements = new String[]{"1", "2", "3"};
        list = new ArrayListWithSortableKey<>(Arrays.asList(elements));

        for (String el : elements) {
            assert list.contains(el);
        }

        // Constructor with initial capacity
        list = new ArrayListWithSortableKey<>(10);
    }

    private void assertList(ArrayList<ArrayListWithSortableKey<String>> list, String[][] elements, int firstSortKey){
        for (String[] row : elements) {
            ArrayListWithSortableKey<String> rowList = new ArrayListWithSortableKey<>(Arrays.asList(row));
            rowList.setSortKey(firstSortKey);
            assert rowList.getSortKey() == firstSortKey;
            list.add(rowList);
        }
    }

    @Test
    public void sortArrayListWithSortableKeyTest() {
        // Generate a new list
        ArrayList<ArrayListWithSortableKey<String>> list = new ArrayList<>();
        int firstSortKey = 0;
        int secondSortKey = 1;

        String[][] elements = new String[][]{{"abc", "jkl", "mno"}, {"def", "ghi", "pqr"}};

        assertList(list, elements, firstSortKey);

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
        ArrayList<ArrayListWithSortableKey<String>> list = new ArrayList<>();
        int firstSortKey = 0;
        int secondSortKey = 1;

        String[][] elements = new String[][]{{"abc", "jkl", "mno"}, {"def", "ghi", "pqr"}};

        assertList(list, elements, firstSortKey);

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
