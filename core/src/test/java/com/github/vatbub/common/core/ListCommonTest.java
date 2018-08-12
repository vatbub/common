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
import java.util.Arrays;
import java.util.List;

public class ListCommonTest  extends CoreBaseTestClass{
    @Test
    public void positiveByteTest() {
        byte[] content = {0, 1, 2, 3, 4};
        byte[] content2 = {5, 6, 7, 8, 9};
        byte[] content3 = {10, 11, 12, 13};

        List<byte[]> list = new ArrayList<>(Arrays.asList(content, content2, content3));

        byte[] check = {5, 6, 7, 8, 9};
        Assert.assertTrue(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void positiveShortTest() {
        short[] content = {0, 1, 2, 3, 4};
        short[] content2 = {5, 6, 7, 8, 9};
        short[] content3 = {10, 11, 12, 13};

        List<short[]> list = new ArrayList<>(Arrays.asList(content, content2, content3));

        short[] check = {5, 6, 7, 8, 9};

        Assert.assertTrue(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void positiveIntTest() {
        int[] content = {0, 1, 2, 3, 4};
        int[] content2 = {5, 6, 7, 8, 9};
        int[] content3 = {10, 11, 12, 13};

        List<int[]> list = new ArrayList<>(Arrays.asList(content, content2, content3));

        int[] check = {5, 6, 7, 8, 9};

        Assert.assertTrue(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void positiveLongTest() {
        long[] content = {0, 1, 2, 3, 4};
        long[] content2 = {5, 6, 7, 8, 9};
        long[] content3 = {10, 11, 12, 13};

        List<long[]> list = new ArrayList<>(Arrays.asList(content, content2, content3));

        long[] check = {5, 6, 7, 8, 9};

        Assert.assertTrue(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void positiveFloatTest() {
        float[] content = {0, 1, 2, 3, 4};
        float[] content2 = {5, 6, 7, 8, 9};
        float[] content3 = {10, 11, 12, 13};

        List<float[]> list = new ArrayList<>(Arrays.asList(content, content2, content3));

        float[] check = {5, 6, 7, 8, 9};

        Assert.assertTrue(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void positiveDoubleTest() {
        double[] content = {0, 1, 2, 3, 4};
        double[] content2 = {5, 6, 7, 8, 9};
        double[] content3 = {10, 11, 12, 13};

        List<double[]> list = new ArrayList<>(Arrays.asList(content, content2, content3));

        double[] check = {5, 6, 7, 8, 9};

        Assert.assertTrue(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void positiveBooleanTest() {
        boolean[] content = {true, false, true, false};
        boolean[] content2 = {false, true, false, true};
        boolean[] content3 = {true, true, false, false};

        List<boolean[]> list = new ArrayList<>(Arrays.asList(content, content2, content3));

        boolean[] check = {false, true, false, true};

        Assert.assertTrue(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void positiveCharTest() {
        char[] content = {0, 1, 2, 3, 4};
        char[] content2 = {5, 6, 7, 8, 9};
        char[] content3 = {10, 11, 12, 13};

        List<char[]> list = new ArrayList<>(Arrays.asList(content, content2, content3));

        char[] check = {5, 6, 7, 8, 9};

        Assert.assertTrue(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void negativeByteTest() {
        byte[] content = {0, 1, 2, 3, 4};
        byte[] content2 = {5, 6, 7, 8, 9};
        byte[] content3 = {10, 11, 12, 13};

        List<byte[]> list = new ArrayList<>(Arrays.asList(content, content2, content3));

        byte[] check = {1, 5, 6, 3, 5, 10};

        Assert.assertFalse(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void negativeShortTest() {
        short[] content = {0, 1, 2, 3, 4};
        short[] content2 = {5, 6, 7, 8, 9};
        short[] content3 = {10, 11, 12, 13};

        List<short[]> list = new ArrayList<>(Arrays.asList(content, content2, content3));

        short[] check = {1, 5, 6, 3, 5, 10};

        Assert.assertFalse(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void negativeIntTest() {
        int[] content = {0, 1, 2, 3, 4};
        int[] content2 = {5, 6, 7, 8, 9};
        int[] content3 = {10, 11, 12, 13};

        List<int[]> list = new ArrayList<>(Arrays.asList(content, content2, content3));

        int[] check = {1, 5, 6, 3, 5, 10};

        Assert.assertFalse(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void negativeLongTest() {
        long[] content = {0, 1, 2, 3, 4};
        long[] content2 = {5, 6, 7, 8, 9};
        long[] content3 = {10, 11, 12, 13};

        List<long[]> list = new ArrayList<>(Arrays.asList(content, content2, content3));

        long[] check = {1, 5, 6, 3, 5, 10};

        Assert.assertFalse(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void negativeFloatTest() {
        float[] content = {0, 1, 2, 3, 4};
        float[] content2 = {5, 6, 7, 8, 9};
        float[] content3 = {10, 11, 12, 13};

        List<float[]> list = new ArrayList<>(Arrays.asList(content, content2, content3));

        float[] check = {1, 5, 6, 3, 5, 10};

        Assert.assertFalse(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void negativeDoubleTest() {
        double[] content = {0, 1, 2, 3, 4};
        double[] content2 = {5, 6, 7, 8, 9};
        double[] content3 = {10, 11, 12, 13};

        List<double[]> list = new ArrayList<>(Arrays.asList(content, content2, content3));

        double[] check = {1, 5, 6, 3, 5, 10};

        Assert.assertFalse(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void negativeBooleanTest() {
        boolean[] content = {true, false, true, false};
        boolean[] content2 = {false, true, false, true};
        boolean[] content3 = {true, true, false, false};

        List<boolean[]> list = new ArrayList<>(Arrays.asList(content, content2, content3));

        boolean[] check = {true, true, true, true};

        Assert.assertFalse(ListCommon.listContainsArray(list, check));
    }

    @Test
    public void negativeCharTest() {
        char[] content = {0, 1, 2, 3, 4};
        char[] content2 = {5, 6, 7, 8, 9};
        char[] content3 = {10, 11, 12, 13};

        List<char[]> list = new ArrayList<>(Arrays.asList(content, content2, content3));

        char[] check = {1, 5, 6, 3, 5, 10};

        Assert.assertFalse(ListCommon.listContainsArray(list, check));
    }
}
