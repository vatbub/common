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

class StringCommonTest {
    @Test
    fun replaceLastTest() {
        val testStr = "BlablubBla"
        Assertions.assertEquals("Blablub", testStr.replaceLast("Bla", ""))
    }

    @Test
    fun formatMessageTest() {
        val testMessage = listOf(
            "line 1",
            "is very interesting",
            "each line should have",
            "at least one space",
            "before and after it"
        )
        val formattedMessage: List<String> = testMessage.formatAsPrintableMessage()
        Assertions.assertEquals(testMessage.size + 2, formattedMessage.size)
        val testRegex = "#*".toRegex()
        Assertions.assertTrue(formattedMessage[0].matches(testRegex))
        Assertions.assertTrue(formattedMessage[formattedMessage.size - 1].matches(testRegex))
        formattedMessage
            .drop(1)
            .dropLast(1)
            .forEachIndexed { index, line ->
                Assertions.assertTrue(line.matches("# ${testMessage[index]} *#".toRegex()))
            }
    }
}
