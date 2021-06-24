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

import com.github.vatbub.common.internet.Internet.sendEventToIFTTTMakerChannel
import com.github.vatbub.common.internet.Internet.webread
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.IOException
import java.net.URL

class InternetTest {
    @Test
    fun webreadTest() {
        val res = webread(URL("https://www.google.com"))
        assert(res != "")
    }

    @Test
    fun iftttMakerChannelTestWithInvalidKey() {
        val apiKey = "invalidKey"
        val eventName = "vatbubUnitTest"
        val details1 = "details1"
        val details2 = "details2"
        val details3 = "details3"
        try {
            val response = sendEventToIFTTTMakerChannel(apiKey, eventName, details1, details2, details3)
            Assertions.fail<Any>("IOException expected")
        } catch (e: IOException) {
            Assertions.assertTrue(e.message!!.contains("401"))
        }
    }

    // TODO Call mock api
    @Disabled
    @Test
    fun iftttMakerChannelTestWithValidKey() {
        val apiKey = System.getenv("CommonUnitTestsIFTTTMakerAPIKey")
        val eventName = "vatbubUnitTest"
        val details1 = "details1"
        val details2 = "details2"
        val details3 = "details3"

        val response = sendEventToIFTTTMakerChannel(apiKey, eventName, details1, details2, details3)
        Assertions.assertTrue(response.contains("Congratulations"))
    }

    // Check if we are really connected
    @Test
    fun isConnectedTest(): Unit {
        val isConnected: Boolean = try {
            // Check if we are really connected
            webread(URL("https://www.google.com"))
            true
        } catch (e: IOException) {
            false
        }
        Assertions.assertEquals(isConnected, Internet.isConnected)
    }
}
