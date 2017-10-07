package com.github.vatbub.common.core;

import java.util.Arrays;
import java.util.List;

public class ListCommon {
    public static boolean listContainsArray(List<byte[]> list, byte[] array) {
        for (byte[] element : list) {
            if (!Arrays.equals(element, array)) {
                return false;
            }
        }
        return true;
    }

    public static boolean listContainsArray(List<short[]> list, short[] array) {
        for (short[] element : list) {
            if (!Arrays.equals(element, array)) {
                return false;
            }
        }
        return true;
    }

    public static boolean listContainsArray(List<int[]> list, int[] array) {
        for (int[] element : list) {
            if (!Arrays.equals(element, array)) {
                return false;
            }
        }
        return true;
    }

    public static boolean listContainsArray(List<long[]> list, long[] array) {
        for (long[] element : list) {
            if (!Arrays.equals(element, array)) {
                return false;
            }
        }
        return true;
    }

    public static boolean listContainsArray(List<float[]> list, float[] array) {
        for (float[] element : list) {
            if (!Arrays.equals(element, array)) {
                return false;
            }
        }
        return true;
    }

    public static boolean listContainsArray(List<double[]> list, double[] array) {
        for (double[] element : list) {
            if (!Arrays.equals(element, array)) {
                return false;
            }
        }
        return true;
    }

    public static boolean listContainsArray(List<boolean[]> list, boolean[] array) {
        for (boolean[] element : list) {
            if (!Arrays.equals(element, array)) {
                return false;
            }
        }
        return true;
    }

    public static boolean listContainsArray(List<char[]> list, char[] array) {
        for (char[] element : list) {
            if (!Arrays.equals(element, array)) {
                return false;
            }
        }
        return true;
    }
}
