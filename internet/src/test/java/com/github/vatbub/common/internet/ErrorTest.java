package com.github.vatbub.common.internet;

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
