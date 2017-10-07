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
