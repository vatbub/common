package com.github.vatbub.common.internet;

/*-
 * #%L
 * FOKProjects Common Internet
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

public class ErrorTest {
    @Test
    public void instantiationTest() {
        String errorText = "Exception in thread \"main\" java.lang.NullPointerException";
        String stacktraceText = " at java.io.Writer.write(Writer.java:157)" +
                "\n at java.io.PrintStream.write(PrintStream.java:462)" +
                "\n at java.io.PrintStream.print(PrintStream.java:584)" +
                "\n at java.io.PrintStream.println(PrintStream.java:700)" +
                "\n at com.gmail.br45entei.main.Main.main(Main.java:21)";
        Error error = new Error(errorText);
        Assert.assertEquals(errorText, error.error);
        Assert.assertEquals("", error.stacktrace);

        error = new Error(errorText, stacktraceText);
        Assert.assertEquals(errorText, error.error);
        Assert.assertEquals(stacktraceText, error.stacktrace);
    }
}
