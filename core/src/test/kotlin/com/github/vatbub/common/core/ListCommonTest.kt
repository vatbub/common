/*-
 * #%L
 * FOKProjects Common Core
 * %%
 * Copyright (C) 2016 - 2021 Frederik Kammel
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
package com.github.vatbub.common.core

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ListCommonTest {
    @Test
    fun positiveByteTest() {
        val content = byteArrayOf(0, 1, 2, 3, 4)
        val content2 = byteArrayOf(5, 6, 7, 8, 9)
        val content3 = byteArrayOf(10, 11, 12, 13)
        val list: List<ByteArray> = listOf(content, content2, content3)
        val check = byteArrayOf(5, 6, 7, 8, 9)
        Assertions.assertTrue(list.containsArray(check))
    }

    @Test
    fun positiveShortTest() {
        val content = shortArrayOf(0, 1, 2, 3, 4)
        val content2 = shortArrayOf(5, 6, 7, 8, 9)
        val content3 = shortArrayOf(10, 11, 12, 13)
        val list: List<ShortArray> = listOf(content, content2, content3)
        val check = shortArrayOf(5, 6, 7, 8, 9)
        Assertions.assertTrue(list.containsArray(check))
    }

    @Test
    fun positiveIntTest() {
        val content = intArrayOf(0, 1, 2, 3, 4)
        val content2 = intArrayOf(5, 6, 7, 8, 9)
        val content3 = intArrayOf(10, 11, 12, 13)
        val list: List<IntArray> = listOf(content, content2, content3)
        val check = intArrayOf(5, 6, 7, 8, 9)
        Assertions.assertTrue(list.containsArray(check))
    }

    @Test
    fun positiveLongTest() {
        val content = longArrayOf(0, 1, 2, 3, 4)
        val content2 = longArrayOf(5, 6, 7, 8, 9)
        val content3 = longArrayOf(10, 11, 12, 13)
        val list: List<LongArray> = listOf(content, content2, content3)
        val check = longArrayOf(5, 6, 7, 8, 9)
        Assertions.assertTrue(list.containsArray(check))
    }

    @Test
    fun positiveFloatTest() {
        val content = floatArrayOf(0f, 1f, 2f, 3f, 4f)
        val content2 = floatArrayOf(5f, 6f, 7f, 8f, 9f)
        val content3 = floatArrayOf(10f, 11f, 12f, 13f)
        val list: List<FloatArray> = listOf(content, content2, content3)
        val check = floatArrayOf(5f, 6f, 7f, 8f, 9f)
        Assertions.assertTrue(list.containsArray(check))
    }

    @Test
    fun positiveDoubleTest() {
        val content = doubleArrayOf(0.0, 1.0, 2.0, 3.0, 4.0)
        val content2 = doubleArrayOf(5.0, 6.0, 7.0, 8.0, 9.0)
        val content3 = doubleArrayOf(10.0, 11.0, 12.0, 13.0)
        val list: List<DoubleArray> = listOf(content, content2, content3)
        val check = doubleArrayOf(5.0, 6.0, 7.0, 8.0, 9.0)
        Assertions.assertTrue(list.containsArray(check))
    }

    @Test
    fun positiveBooleanTest() {
        val content = booleanArrayOf(true, false, true, false)
        val content2 = booleanArrayOf(false, true, false, true)
        val content3 = booleanArrayOf(true, true, false, false)
        val list: List<BooleanArray> = listOf(content, content2, content3)
        val check = booleanArrayOf(false, true, false, true)
        Assertions.assertTrue(list.containsArray(check))
    }

    @Test
    fun positiveCharTest() {
        val content = charArrayOf(0.toChar(), 1.toChar(), 2.toChar(), 3.toChar(), 4.toChar())
        val content2 = charArrayOf(5.toChar(), 6.toChar(), 7.toChar(), 8.toChar(), 9.toChar())
        val content3 = charArrayOf(10.toChar(), 11.toChar(), 12.toChar(), 13.toChar())
        val list: List<CharArray> = listOf(content, content2, content3)
        val check = charArrayOf(5.toChar(), 6.toChar(), 7.toChar(), 8.toChar(), 9.toChar())
        Assertions.assertTrue(list.containsArray(check))
    }

    @Test
    fun positiveObjectTest() {
        val content = arrayOf("Hi", "Hello", "I", "am", "a")
        val content2 = arrayOf("stupid", "sample", "text", "that", "is")
        val content3 = arrayOf("divided", "into", "individual", "words")
        val list = listOf(content, content2, content3)
        val check = arrayOf("stupid", "sample", "text", "that", "is")
        Assertions.assertTrue(list.containsArray(check))
    }

    @Test
    fun negativeByteTest() {
        val content = byteArrayOf(0, 1, 2, 3, 4)
        val content2 = byteArrayOf(5, 6, 7, 8, 9)
        val content3 = byteArrayOf(10, 11, 12, 13)
        val list: List<ByteArray> = listOf(content, content2, content3)
        val check = byteArrayOf(1, 5, 6, 3, 5, 10)
        Assertions.assertFalse(list.containsArray(check))
    }

    @Test
    fun negativeShortTest() {
        val content = shortArrayOf(0, 1, 2, 3, 4)
        val content2 = shortArrayOf(5, 6, 7, 8, 9)
        val content3 = shortArrayOf(10, 11, 12, 13)
        val list: List<ShortArray> = listOf(content, content2, content3)
        val check = shortArrayOf(1, 5, 6, 3, 5, 10)
        Assertions.assertFalse(list.containsArray(check))
    }

    @Test
    fun negativeIntTest() {
        val content = intArrayOf(0, 1, 2, 3, 4)
        val content2 = intArrayOf(5, 6, 7, 8, 9)
        val content3 = intArrayOf(10, 11, 12, 13)
        val list: List<IntArray> = listOf(content, content2, content3)
        val check = intArrayOf(1, 5, 6, 3, 5, 10)
        Assertions.assertFalse(list.containsArray(check))
    }

    @Test
    fun negativeLongTest() {
        val content = longArrayOf(0, 1, 2, 3, 4)
        val content2 = longArrayOf(5, 6, 7, 8, 9)
        val content3 = longArrayOf(10, 11, 12, 13)
        val list: List<LongArray> = listOf(content, content2, content3)
        val check = longArrayOf(1, 5, 6, 3, 5, 10)
        Assertions.assertFalse(list.containsArray(check))
    }

    @Test
    fun negativeFloatTest() {
        val content = floatArrayOf(0f, 1f, 2f, 3f, 4f)
        val content2 = floatArrayOf(5f, 6f, 7f, 8f, 9f)
        val content3 = floatArrayOf(10f, 11f, 12f, 13f)
        val list: List<FloatArray> = listOf(content, content2, content3)
        val check = floatArrayOf(1f, 5f, 6f, 3f, 5f, 10f)
        Assertions.assertFalse(list.containsArray(check))
    }

    @Test
    fun negativeDoubleTest() {
        val content = doubleArrayOf(0.0, 1.0, 2.0, 3.0, 4.0)
        val content2 = doubleArrayOf(5.0, 6.0, 7.0, 8.0, 9.0)
        val content3 = doubleArrayOf(10.0, 11.0, 12.0, 13.0)
        val list: List<DoubleArray> = listOf(content, content2, content3)
        val check = doubleArrayOf(1.0, 5.0, 6.0, 3.0, 5.0, 10.0)
        Assertions.assertFalse(list.containsArray(check))
    }

    @Test
    fun negativeBooleanTest() {
        val content = booleanArrayOf(true, false, true, false)
        val content2 = booleanArrayOf(false, true, false, true)
        val content3 = booleanArrayOf(true, true, false, false)
        val list: List<BooleanArray> = listOf(content, content2, content3)
        val check = booleanArrayOf(true, true, true, true)
        Assertions.assertFalse(list.containsArray(check))
    }

    @Test
    fun negativeCharTest() {
        val content = charArrayOf(0.toChar(), 1.toChar(), 2.toChar(), 3.toChar(), 4.toChar())
        val content2 = charArrayOf(5.toChar(), 6.toChar(), 7.toChar(), 8.toChar(), 9.toChar())
        val content3 = charArrayOf(10.toChar(), 11.toChar(), 12.toChar(), 13.toChar())
        val list: List<CharArray> = listOf(content, content2, content3)
        val check = charArrayOf(1.toChar(), 5.toChar(), 6.toChar(), 3.toChar(), 5.toChar(), 10.toChar())
        Assertions.assertFalse(list.containsArray(check))
    }

    @Test
    fun negativeObjectTest() {
        val content = arrayOf("Hi", "Hello", "I", "am", "a")
        val content2 = arrayOf("stupid", "sample", "text", "that", "is")
        val content3 = arrayOf("divided", "into", "individual", "words")
        val list = listOf(content, content2, content3)
        val check = arrayOf("Hello", "sample", "a", "words", "stupid", "is")
        Assertions.assertFalse(list.containsArray(check))
    }
}
