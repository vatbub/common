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


fun List<ByteArray>.containsArray(array: ByteArray) = this.any { it.contentEquals(array) }

fun List<ShortArray>.containsArray(array: ShortArray) = this.any { it.contentEquals(array) }

fun List<IntArray>.containsArray(array: IntArray) = this.any { it.contentEquals(array) }

fun List<LongArray>.containsArray(array: LongArray) = this.any { it.contentEquals(array) }

fun List<FloatArray>.containsArray(array: FloatArray) = this.any { it.contentEquals(array) }

fun List<DoubleArray>.containsArray(array: DoubleArray) = this.any { it.contentEquals(array) }

fun List<BooleanArray>.containsArray(array: BooleanArray) = this.any { it.contentEquals(array) }

fun List<CharArray>.containsArray(array: CharArray) = this.any { it.contentEquals(array) }

fun <T> List<Array<T>>.containsArray(array: Array<T>) = this.any { it.contentEquals(array) }
