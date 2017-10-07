package com.github.vatbub.common.core;

/*-
 * #%L
 * FOKProjects Common Core
 * %%
 * Copyright (C) 2016 - 2017 Frederik Kammel
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


import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListCommonTest {
    @Test
    public void positiveByteTest() {
        byte[] content = {0, 1, 2, 3, 4};
        byte[] check = {0, 1, 2, 3, 4};
        List<byte[]> list = new ArrayList<>();
        list.add(content);
        Assert.assertTrue(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void positiveShortTest() {
        short[] content = {0, 1, 2, 3, 4};
        short[] check = {0, 1, 2, 3, 4};
        List<short[]> list = new ArrayList<>();
        list.add(content);
        Assert.assertTrue(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void positiveIntTest() {
        int[] content = {0, 1, 2, 3, 4};
        int[] check = {0, 1, 2, 3, 4};
        List<int[]> list = new ArrayList<>();
        list.add(content);
        Assert.assertTrue(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void positiveLongTest() {
        long[] content = {0, 1, 2, 3, 4};
        long[] check = {0, 1, 2, 3, 4};
        List<long[]> list = new ArrayList<>();
        list.add(content);
        Assert.assertTrue(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void positiveFloatTest() {
        float[] content = {0, 1, 2, 3, 4};
        float[] check = {0, 1, 2, 3, 4};
        List<float[]> list = new ArrayList<>();
        list.add(content);
        Assert.assertTrue(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void positiveDoubleTest() {
        double[] content = {0, 1, 2, 3, 4};
        double[] check = {0, 1, 2, 3, 4};
        List<double[]> list = new ArrayList<>();
        list.add(content);
        Assert.assertTrue(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void positiveBooleanTest() {
        boolean[] content = {true, true, false, false};
        boolean[] check = {true, true, false, false};
        List<boolean[]> list = new ArrayList<>();
        list.add(content);
        Assert.assertTrue(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void positiveCharTest() {
        char[] content = {0, 1, 2, 3, 4};
        char[] check = {0, 1, 2, 3, 4};
        List<char[]> list = new ArrayList<>();
        list.add(content);
        Assert.assertTrue(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void negativeByteTest() {
        byte[] content = {0, 1, 2, 3, 4};
        byte[] check = {0, 1, 2, 3, 5};
        List<byte[]> list = new ArrayList<>();
        list.add(content);
        Assert.assertFalse(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void negativeShortTest() {
        short[] content = {0, 1, 2, 3, 4};
        short[] check = {0, 1, 2, 3, 5};
        List<short[]> list = new ArrayList<>();
        list.add(content);
        Assert.assertFalse(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void negativeIntTest() {
        int[] content = {0, 1, 2, 3, 4};
        int[] check = {0, 1, 2, 3, 5};
        List<int[]> list = new ArrayList<>();
        list.add(content);
        Assert.assertFalse(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void negativeLongTest() {
        long[] content = {0, 1, 2, 3, 4};
        long[] check = {0, 1, 2, 3, 5};
        List<long[]> list = new ArrayList<>();
        list.add(content);
        Assert.assertFalse(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void negativeFloatTest() {
        float[] content = {0, 1, 2, 3, 4};
        float[] check = {0, 1, 2, 3, 5};
        List<float[]> list = new ArrayList<>();
        list.add(content);
        Assert.assertFalse(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void negativeDoubleTest() {
        double[] content = {0, 1, 2, 3, 4};
        double[] check = {0, 1, 2, 3, 5};
        List<double[]> list = new ArrayList<>();
        list.add(content);
        Assert.assertFalse(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void negativeBooleanTest() {
        boolean[] content = {true, true, false, false};
        boolean[] check = {true, true, false, true};
        List<boolean[]> list = new ArrayList<>();
        list.add(content);
        Assert.assertFalse(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void negativeCharTest() {
        char[] content = {0, 1, 2, 3, 4};
        char[] check = {0, 1, 2, 3, 5};
        List<char[]> list = new ArrayList<>();
        list.add(content);
        Assert.assertFalse(ListCommon.listContainsArray(list, check));
    }
}
