package com.github.vatbub.common.internet;

/*-
 * #%L
 * FOKProjects Common
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


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.net.URL;

/**
 * Internet test class
 */
public class InternetTest {
    @Test
    public void httpCodeTest() {
        int invalidCode = 2345609;
        int[] codes = {100, 101, 102,
                200, 201, 202, 203, 204, 205, 206, 207, 208, 226, 300, 301, 302, 303, 304, 305, 307, 308,
                400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 420, 421, 422, 423, 424, 425, 426, 428, 429, 431, 451,
                500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511,
                invalidCode};

        for (int httpCode : codes) {
            String receivedCode = Internet.getReasonForHTTPCode(httpCode);
            assert httpCode == invalidCode || receivedCode != null;
            switch (httpCode) {
                case 100:
                    Assertions.assertEquals("Continue", receivedCode);
                    break;
                case 101:
                    Assertions.assertEquals("Switching Protocols", receivedCode);
                    break;
                case 102:
                    Assertions.assertEquals("Processing", receivedCode);
                    break;
                case 200:
                    Assertions.assertEquals("OK", receivedCode);
                    break;
                case 201:
                    Assertions.assertEquals("Created", receivedCode);
                    break;
                case 202:
                    Assertions.assertEquals("Accepted", receivedCode);
                    break;
                case 203:
                    Assertions.assertEquals("Non-Authoritative Information", receivedCode);
                    break;
                case 204:
                    Assertions.assertEquals("No Content", receivedCode);
                    break;
                case 205:
                    Assertions.assertEquals("Reset Content", receivedCode);
                    break;
                case 206:
                    Assertions.assertEquals("Partial Content", receivedCode);
                    break;
                case 207:
                    Assertions.assertEquals("Multi-Status", receivedCode);
                    break;
                case 208:
                    Assertions.assertEquals("Already Reported", receivedCode);
                    break;
                case 226:
                    Assertions.assertEquals("IM Used", receivedCode);
                    break;
                case 300:
                    Assertions.assertEquals("Multiple Choices", receivedCode);
                    break;
                case 301:
                    Assertions.assertEquals("Moved Permanently", receivedCode);
                    break;
                case 302:
                    Assertions.assertEquals("Moved Temporarily", receivedCode);
                    break;
                case 303:
                    Assertions.assertEquals("See Other", receivedCode);
                    break;
                case 304:
                    Assertions.assertEquals("Not Modified", receivedCode);
                    break;
                case 305:
                    Assertions.assertEquals("Use Proxy", receivedCode);
                    break;
                case 307:
                    Assertions.assertEquals("Temporary Redirect", receivedCode);
                    break;
                case 308:
                    Assertions.assertEquals("Permanent Redirect", receivedCode);
                    break;
                case 400:
                    Assertions.assertEquals("Bad Request", receivedCode);
                    break;
                case 401:
                    Assertions.assertEquals("Unauthorized", receivedCode);
                    break;
                case 402:
                    Assertions.assertEquals("Payment Required", receivedCode);
                    break;
                case 403:
                    Assertions.assertEquals("Forbidden", receivedCode);
                    break;
                case 404:
                    Assertions.assertEquals("Not Found", receivedCode);
                    break;
                case 405:
                    Assertions.assertEquals("Method Not Allowed", receivedCode);
                    break;
                case 406:
                    Assertions.assertEquals("Not Acceptable", receivedCode);
                    break;
                case 407:
                    Assertions.assertEquals("Proxy Authentication Required", receivedCode);
                    break;
                case 408:
                    Assertions.assertEquals("Request Time-out", receivedCode);
                    break;
                case 409:
                    Assertions.assertEquals("Conflict", receivedCode);
                    break;
                case 410:
                    Assertions.assertEquals("Gone", receivedCode);
                    break;
                case 411:
                    Assertions.assertEquals("Length required", receivedCode);
                    break;
                case 412:
                    Assertions.assertEquals("Precondition failed", receivedCode);
                    break;
                case 413:
                    Assertions.assertEquals("Request Entity Too Large", receivedCode);
                    break;
                case 414:
                    Assertions.assertEquals("Request-URL Too Long", receivedCode);
                    break;
                case 415:
                    Assertions.assertEquals("UnsupportedMedia Type", receivedCode);
                    break;
                case 416:
                    Assertions.assertEquals("Requested range not satisfiable", receivedCode);
                    break;
                case 417:
                    Assertions.assertEquals("Expectation failed", receivedCode);
                    break;
                case 418:
                    Assertions.assertEquals("I'm a teapot", receivedCode);
                    break;
                case 420:
                    Assertions.assertEquals("Policy Not Fulfilled", receivedCode);
                    break;
                case 421:
                    Assertions.assertEquals("Misdirected Request", receivedCode);
                    break;
                case 422:
                    Assertions.assertEquals("Unprocessable Entity", receivedCode);
                    break;
                case 423:
                    Assertions.assertEquals("Locked", receivedCode);
                    break;
                case 424:
                    Assertions.assertEquals("Failed Dependency", receivedCode);
                    break;
                case 425:
                    Assertions.assertEquals("Unordered Collection", receivedCode);
                    break;
                case 426:
                    Assertions.assertEquals("Upgrade Required", receivedCode);
                    break;
                case 428:
                    Assertions.assertEquals("Precondition Required", receivedCode);
                    break;
                case 429:
                    Assertions.assertEquals("Too Many Requests", receivedCode);
                    break;
                case 431:
                    Assertions.assertEquals("Request Header Fields Too Large", receivedCode);
                    break;
                case 451:
                    Assertions.assertEquals("Unavailable For Legal Reasons", receivedCode);
                    break;
                case 500:
                    Assertions.assertEquals("Internal Server Error", receivedCode);
                    break;
                case 501:
                    Assertions.assertEquals("Not Implemented", receivedCode);
                    break;
                case 502:
                    Assertions.assertEquals("Bad Gateway", receivedCode);
                    break;
                case 503:
                    Assertions.assertEquals("Service Unavailable", receivedCode);
                    break;
                case 504:
                    Assertions.assertEquals("Gateway Time-out", receivedCode);
                    break;
                case 505:
                    Assertions.assertEquals("HTTP Version not supported", receivedCode);
                    break;
                case 506:
                    Assertions.assertEquals("Variant Also Negotiates", receivedCode);
                    break;
                case 507:
                    Assertions.assertEquals("Insufficient Storage", receivedCode);
                    break;
                case 508:
                    Assertions.assertEquals("Loop Detected", receivedCode);
                    break;
                case 509:
                    Assertions.assertEquals("Bandwidth Limit Exceeded", receivedCode);
                    break;
                case 510:
                    Assertions.assertEquals("Not Extended", receivedCode);
                    break;
                case 511:
                    Assertions.assertEquals("Network Authentication Required", receivedCode);
                    break;
                default:
                    Assertions.assertNull(receivedCode);
                    break;
            }
        }
    }

    @Test
    public void webreadTest() throws IOException {
        String res = Internet.webread(new URL("https://www.google.com"));
        assert !res.equals("");
    }

    @Test
    public void iftttMakerChannelTestWithInvalidKey() {
        String apiKey = "invalidKey";
        String eventName = "vatbubUnitTest";
        String details1 = "details1";
        String details2 = "details2";
        String details3 = "details3";

        try {
            String response = Internet.sendEventToIFTTTMakerChannel(apiKey, eventName);
            System.out.println(response);
            Assertions.fail("IOException expected");
        } catch (IOException e) {
            Assertions.assertTrue(e.getMessage().contains("401"));
        }

        try {
            String response = Internet.sendEventToIFTTTMakerChannel(apiKey, eventName, details1);
            System.out.println(response);
            Assertions.fail("IOException expected");
        } catch (IOException e) {
            Assertions.assertTrue(e.getMessage().contains("401"));
        }

        try {
            String response = Internet.sendEventToIFTTTMakerChannel(apiKey, eventName, details1, details2);
            System.out.println(response);
            Assertions.fail("IOException expected");
        } catch (IOException e) {
            Assertions.assertTrue(e.getMessage().contains("401"));
        }

        try {
            String response = Internet.sendEventToIFTTTMakerChannel(apiKey, eventName, details1, details2, details3);
            System.out.println(response);
            Assertions.fail("IOException expected");
        } catch (IOException e) {
            Assertions.assertTrue(e.getMessage().contains("401"));
        }
    }

    @Test
    public void iftttMakerChannelTestWithValidKey() {
        String apiKey = System.getenv("CommonUnitTestsIFTTTMakerAPIKey");
        String eventName = "vatbubUnitTest";
        String details1 = "details1";
        String details2 = "details2";
        String details3 = "details3";

        try {
            String response = Internet.sendEventToIFTTTMakerChannel(apiKey, eventName);
            System.out.println(response);
            Assertions.assertTrue(response.contains("Congratulations"));
        } catch (IOException e) {
            Assertions.fail("IOException: " + e.getMessage());
        }

        try {
            String response = Internet.sendEventToIFTTTMakerChannel(apiKey, eventName, details1);
            System.out.println(response);
            Assertions.assertTrue(response.contains("Congratulations"));
        } catch (IOException e) {
            Assertions.fail("IOException: " + e.getMessage());
        }

        try {
            String response = Internet.sendEventToIFTTTMakerChannel(apiKey, eventName, details1, details2);
            System.out.println(response);
            Assertions.assertTrue(response.contains("Congratulations"));
        } catch (IOException e) {
            Assertions.fail("IOException: " + e.getMessage());
        }

        try {
            String response = Internet.sendEventToIFTTTMakerChannel(apiKey, eventName, details1, details2, details3);
            System.out.println(response);
            Assertions.assertTrue(response.contains("Congratulations"));
        } catch (IOException e) {
            Assertions.fail("IOException: " + e.getMessage());
        }
    }

    @Test
    public void isConnectedTest() {
        boolean isConnected;
        try {
            // Check if we are really connected
            Internet.webread(new URL("https://www.google.com"));
            isConnected = true;
        } catch (IOException e) {
            isConnected = false;
        }

        Assertions.assertEquals(isConnected, Internet.isConnected());
    }
}
