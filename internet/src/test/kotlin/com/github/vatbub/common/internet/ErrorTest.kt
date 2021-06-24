/*-
 * #%L
 * FOKProjects Common Internet
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
package com.github.vatbub.common.internet

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ErrorTest {
    @Test
    fun noStackTraceTest() {
        val errorText = "Exception in thread \"main\" java.lang.NullPointerException"
        val error = Error(errorText)
        Assertions.assertEquals(errorText, error.error)
        Assertions.assertEquals("", error.stacktrace)
    }

    @Test
    fun stackTraceTest() {
        val errorText = "Exception in thread \"main\" java.lang.NullPointerException"

        val stacktraceText = """ at java.io.Writer.write(Writer.java:157)
 at java.io.PrintStream.write(PrintStream.java:462)
 at java.io.PrintStream.print(PrintStream.java:584)
 at java.io.PrintStream.println(PrintStream.java:700)
 at com.gmail.br45entei.main.Main.main(Main.java:21)"""
        val error = Error(errorText, stacktraceText)
        Assertions.assertEquals(errorText, error.error)
        Assertions.assertEquals(stacktraceText, error.stacktrace)
    }
}
