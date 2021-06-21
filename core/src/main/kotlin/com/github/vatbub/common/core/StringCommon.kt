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

import java.util.*
import kotlin.math.roundToInt

fun String.replaceLast(regex: String, replacement: String): String =
    this.replaceFirst("(?s)(.*)$regex".toRegex(), "$1$replacement")

fun List<String>.formatAsPrintableMessage(): List<String> {
    val maxLength = this.maxOfOrNull { it.length } ?: 0
    val separator = "#".repeat(maxLength + 4)

    val res = mutableListOf(separator)
    this.mapTo(res) { line ->
        "# $line${getRequiredSpaces(separator.length, line.length)} #"
    }
    res.add(separator)
    return res
}

private fun getRequiredSpaces(separatorLength: Int, messageLength: Int) =
    " ".repeat(separatorLength - messageLength - 4)

fun String.countOccurrencesOf(searchString: String): Int {
    return (this.length - this.replace(searchString, "").length) / searchString.length
}

fun convertFileSizeToReadableString(kilobytes: Double): String {
    val bundle = ResourceBundle.getBundle("com.github.vatbub.common.core.filesizeUnitStrings")
    return when {
        kilobytes < 1024 -> "${(kilobytes * 100.0).roundToInt() / 100.0} ${bundle["kilobyte"]}"
        kilobytes / 1024 < 1024 -> ("${(kilobytes * 100.0 / 1024).roundToInt() / 100.0} ${bundle["megabyte"]}")
        kilobytes / 1024 / 1024 < 1024 -> ("${(kilobytes * 100.0 / 1024 / 1024).roundToInt() / 100.0} ${bundle["gigabyte"]}")
        else -> ("${(kilobytes * 100.0 / 1024 / 1024 / 1024).roundToInt() / 100.0} ${bundle["terabyte"]}")
    }
}
