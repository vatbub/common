package common.internet;

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


import org.junit.Assert;
import org.junit.Test;

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
            if (httpCode != invalidCode) {
                assert receivedCode != null;
            }
            switch (httpCode) {
                case 100:
                    Assert.assertEquals("Continue", receivedCode);
                    break;
                case 101:
                    Assert.assertEquals("Switching Protocols", receivedCode);
                    break;
                case 102:
                    Assert.assertEquals("Processing", receivedCode);
                    break;
                case 200:
                    Assert.assertEquals("OK", receivedCode);
                    break;
                case 201:
                    Assert.assertEquals("Created", receivedCode);
                    break;
                case 202:
                    Assert.assertEquals("Accepted", receivedCode);
                    break;
                case 203:
                    Assert.assertEquals("Non-Authoritative Information", receivedCode);
                    break;
                case 204:
                    Assert.assertEquals("No Content", receivedCode);
                    break;
                case 205:
                    Assert.assertEquals("Reset Content", receivedCode);
                    break;
                case 206:
                    Assert.assertEquals("Partial Content", receivedCode);
                    break;
                case 207:
                    Assert.assertEquals("Multi-Status", receivedCode);
                    break;
                case 208:
                    Assert.assertEquals("Already Reported", receivedCode);
                    break;
                case 226:
                    Assert.assertEquals("IM Used", receivedCode);
                    break;
                case 300:
                    Assert.assertEquals("Multiple Choices", receivedCode);
                    break;
                case 301:
                    Assert.assertEquals("Moved Permanently", receivedCode);
                    break;
                case 302:
                    Assert.assertEquals("Moved Temporarily", receivedCode);
                    break;
                case 303:
                    Assert.assertEquals("See Other", receivedCode);
                    break;
                case 304:
                    Assert.assertEquals("Not Modified", receivedCode);
                    break;
                case 305:
                    Assert.assertEquals("Use Proxy", receivedCode);
                    break;
                case 307:
                    Assert.assertEquals("Temporary Redirect", receivedCode);
                    break;
                case 308:
                    Assert.assertEquals("Permanent Redirect", receivedCode);
                    break;
                case 400:
                    Assert.assertEquals("Bad Request", receivedCode);
                    break;
                case 401:
                    Assert.assertEquals("Unauthorized", receivedCode);
                    break;
                case 402:
                    Assert.assertEquals("Payment Required", receivedCode);
                    break;
                case 403:
                    Assert.assertEquals("Forbidden", receivedCode);
                    break;
                case 404:
                    Assert.assertEquals("Not Found", receivedCode);
                    break;
                case 405:
                    Assert.assertEquals("Method Not Allowed", receivedCode);
                    break;
                case 406:
                    Assert.assertEquals("Not Acceptable", receivedCode);
                    break;
                case 407:
                    Assert.assertEquals("Proxy Authentication Required", receivedCode);
                    break;
                case 408:
                    Assert.assertEquals("Request Time-out", receivedCode);
                    break;
                case 409:
                    Assert.assertEquals("Conflict", receivedCode);
                    break;
                case 410:
                    Assert.assertEquals("Gone", receivedCode);
                    break;
                case 411:
                    Assert.assertEquals("Length required", receivedCode);
                    break;
                case 412:
                    Assert.assertEquals("Precondition failed", receivedCode);
                    break;
                case 413:
                    Assert.assertEquals("Request Entity Too Large", receivedCode);
                    break;
                case 414:
                    Assert.assertEquals("Request-URL Too Long", receivedCode);
                    break;
                case 415:
                    Assert.assertEquals("UnsupportedMedia Type", receivedCode);
                    break;
                case 416:
                    Assert.assertEquals("Requested range not satisfiable", receivedCode);
                    break;
                case 417:
                    Assert.assertEquals("Expectation failed", receivedCode);
                    break;
                case 418:
                    Assert.assertEquals("I'm a teapot", receivedCode);
                    break;
                case 420:
                    Assert.assertEquals("Policy Not Fulfilled", receivedCode);
                    break;
                case 421:
                    Assert.assertEquals("Misdirected Request", receivedCode);
                    break;
                case 422:
                    Assert.assertEquals("Unprocessable Entity", receivedCode);
                    break;
                case 423:
                    Assert.assertEquals("Locked", receivedCode);
                    break;
                case 424:
                    Assert.assertEquals("Failed Dependency", receivedCode);
                    break;
                case 425:
                    Assert.assertEquals("Unordered Collection", receivedCode);
                    break;
                case 426:
                    Assert.assertEquals("Upgrade Required", receivedCode);
                    break;
                case 428:
                    Assert.assertEquals("Precondition Required", receivedCode);
                    break;
                case 429:
                    Assert.assertEquals("Too Many Requests", receivedCode);
                    break;
                case 431:
                    Assert.assertEquals("Request Header Fields Too Large", receivedCode);
                    break;
                case 451:
                    Assert.assertEquals("Unavailable For Legal Reasons", receivedCode);
                    break;
                case 500:
                    Assert.assertEquals("Internal Server Error", receivedCode);
                    break;
                case 501:
                    Assert.assertEquals("Not Implemented", receivedCode);
                    break;
                case 502:
                    Assert.assertEquals("Bad Gateway", receivedCode);
                    break;
                case 503:
                    Assert.assertEquals("Service Unavailable", receivedCode);
                    break;
                case 504:
                    Assert.assertEquals("Gateway Time-out", receivedCode);
                    break;
                case 505:
                    Assert.assertEquals("HTTP Version not supported", receivedCode);
                    break;
                case 506:
                    Assert.assertEquals("Variant Also Negotiates", receivedCode);
                    break;
                case 507:
                    Assert.assertEquals("Insufficient Storage", receivedCode);
                    break;
                case 508:
                    Assert.assertEquals("Loop Detected", receivedCode);
                    break;
                case 509:
                    Assert.assertEquals("Bandwidth Limit Exceeded", receivedCode);
                    break;
                case 510:
                    Assert.assertEquals("Not Extended", receivedCode);
                    break;
                case 511:
                    Assert.assertEquals("Network Authentication Required", receivedCode);
                    break;
                default:
                    Assert.assertEquals(null, receivedCode);
                    break;
            }
        }
    }

    @Test
    public void webreadTest() throws IOException {
        String res = Internet.webread(new URL("https://www.google.com"));
        assert !res.equals("");
    }
}
