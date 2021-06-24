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

import com.github.vatbub.common.core.logger
import org.apache.commons.lang.SystemUtils
import org.apache.commons.lang.exception.ExceptionUtils
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*
import javax.mail.*
import javax.mail.Message.RecipientType.TO
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object Internet {
    /**
     * Sends an event to the IFTTT Maker Channel. See
     * [https://maker.ifttt.com/use/](https://maker.ifttt.com/use/)
     * for more information.
     *
     * @param iftttMakerChannelApiKey Your Maker API Key. Get your one on
     * [https://ifttt.com/maker](https://ifttt.com/maker)
     * @param eventName               The name of the event to trigger.
     * @param details1                You can send up to three additional fields to the Maker
     * channel which you can use then as IFTTT ingredients. See
     * [https://maker.ifttt.com/use/](https://maker.ifttt.com/use/)
     * for more information.
     * @param details2                The second additional parameter.
     * @param details3                The third additional parameter.
     * @return The response text from IFTTT
     * wrong with the connection (e. g. not connected)
     */
    @JvmOverloads
    fun sendEventToIFTTTMakerChannel(
        iftttMakerChannelApiKey: String, eventName: String, details1: String = "",
        details2: String = "", details3: String = ""
    ): String {
        try {
            val url = URL("https://maker.ifttt.com/trigger/$eventName/with/key/$iftttMakerChannelApiKey")
            val postData =
                ("{ \"value1\" : \"$details1\", \"value2\" : \"$details2\", \"value3\" : \"$details3\" }")
            val postData2 = postData.toByteArray(StandardCharsets.UTF_8)
            val connection = (url.openConnection() as HttpURLConnection).apply {
                doOutput = true
                instanceFollowRedirects = false
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("charset", "utf-8")
            }
            DataOutputStream(connection.outputStream).use {
                it.write(postData2)
            }
            connection.connect()
            return BufferedReader(InputStreamReader(connection.inputStream, "UTF-8")).readText()
        } catch (e: MalformedURLException) {
            logger.error("An error occurred!", e)
            return ""
        }
    }

    /**
     * Sends a GET request to url and retrieves the server response
     *
     * @param url The url to call.
     * @return The server response
     */
    fun webread(url: URL): String {
        val result = StringBuilder()
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
        }
        return InputStreamReader(conn.inputStream).buffered().readText()
    }

    /**
     * Checks if the computer is connected to the common.internet by calling
     * `http://www.google.com`
     *
     * @return `true` if the computer is connected to the common.internet,
     * `false` otherwise.
     */
    val isConnected: Boolean
        get() = isConnected(URL("https://www.google.com"))

    /**
     * Checks if the computer is connected to the common.internet by calling
     * `urlToTest`
     *
     * @param urlToTest The url to be used to test the connection. It is recommended
     * to use a reliable server (like Google) for this to ensure that
     * the server is always online.
     * @return `true` if the computer is connected to the common.internet,
     * `false` otherwise.
     */
    fun isConnected(urlToTest: URL): Boolean {
        return try {
            webread(urlToTest)
            true
        } catch (e: IOException) {
            false
        }
    }

    /**
     * Sends a error message via gMail. This method requires a gMail account to send emails from. Get a new account [here](https://accounts.google.com/SignUp?continue=https%3A%2F%2Fwww.google.com%2F%3Fgfe_rd%3Dcr%26ei%3D30aJWLDMDrP08Af3oLrwDg%26gws_rd%3Dssl&hl=en)
     *
     * @param phase         The phase in which the error occurred. This can be any string that helps you to identify the part of the code where the exception happened.
     * @param requestBody   The body of the http request that caused the exception
     * @param e             The exception that occurred
     * @param gMailUsername The username of the gMail-account to use to send the mail from, including `{ @}gmail.com`
     * @param gMailPassword The password of the gMail-account
     */
    @JvmOverloads
    fun sendErrorMail(
        appName: String,
        phase: String,
        e: Throwable?,
        gMailUsername: String?,
        gMailPassword: String?,
        toAddress: String = "vatbub123+automatederrorreports@gmail.com",
        requestBody: String? = null
    ) {
        val props = Properties().also {
            it["mail.smtp.auth"] = "true"
            it["mail.smtp.starttls.enable"] = "true"
            it["mail.smtp.host"] = "smtp.gmail.com"
            it["mail.smtp.port"] = "587"
        }

        val session = Session.getInstance(props,
            object : Authenticator() {
                override fun getPasswordAuthentication() =
                    PasswordAuthentication(gMailUsername, gMailPassword)
            })
        val message: Message = MimeMessage(session).apply {
            setFrom(InternetAddress(gMailUsername))
            setRecipients(TO, InternetAddress.parse(toAddress))
            subject = "[$appName] An error occurred in your application"
            var messageText = "Exception occurred in phase: $phase"
            if (requestBody != null) {
                messageText = "$messageText\n\nRequest that caused the exception:\n$requestBody"
            }
            messageText = """
                    $messageText
                    
                    Stacktrace of the exception:
                    ${ExceptionUtils.getFullStackTrace(e)}
                    """.trimIndent()
            setText(messageText)
        }
        Transport.send(message)
        logger.info("Sent email with error message to $toAddress")
    }

    /**
     * Opens the specified url in the default browser.
     *
     * @param url The url to open
     */
    fun openInDefaultBrowser(url: URL) {
        val command = if (SystemUtils.IS_OS_WINDOWS) {
            "rundll32 url.dll,FileProtocolHandler $url"
        } else if (SystemUtils.IS_OS_MAC) {
            "open $url"
        } else {
            val browserPart = arrayOf(
                "epiphany", "firefox", "mozilla", "konqueror",
                "netscape", "opera", "links", "lynx"
            ).joinToString(separator = " || ") { "$it \"" }
            "sh -c $browserPart$url\" "
        }
        Runtime.getRuntime().exec(command)
    }
}
