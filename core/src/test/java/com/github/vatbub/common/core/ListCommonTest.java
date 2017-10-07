package com.github.vatbub.common.core;

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
}
