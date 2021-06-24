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

import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class ConfigTest {
    private val appName = "CommonLibraryUnitTests"

    private val cacheFile = AppDataPath.Computer
        .forAppName(appName)
        .toPath()
        .resolve("commonConfigUnitTestCache${Random().nextInt()}.cache")
        .toFile()

    @Test
    fun loadRemoteConfigNoCacheTest() {
        var config = Config(
            appName,
            javaClass.getResource("RemoteTestConfig.properties")!!,
            javaClass.getResource("FallbackTestConfig.properties")!!,
            cacheFile = null
        )
        Assertions.assertTrue(config.contains("configSource"))
        Assertions.assertEquals("remote", config.get("configSource"))
        Assertions.assertEquals(config.currentlyActiveSource, Config.ConfigSource.ONLINE)

        config = Config(
            appName,
            javaClass.getResource("RemoteTestConfig.properties")!!,
            javaClass.getResource("FallbackTestConfig.properties")!!,
            cacheFile = null,
            forceOfflineMode = true
        )
        Assertions.assertTrue(config.contains("configSource"))
        Assertions.assertNotEquals(config.currentlyActiveSource, Config.ConfigSource.ONLINE)
        Assertions.assertEquals("fallback", config.get("configSource"))
    }

    @Test
    fun loadRemoteConfigWithCacheTest() {
        var config = Config(
            appName,
            javaClass.getResource("RemoteTestConfig.properties")!!,
            javaClass.getResource("FallbackTestConfig.properties")!!,
            cacheFile
        )
        Assertions.assertTrue(config.contains("configSource"))
        Assertions.assertEquals("remote", config.get("configSource"))
        config = Config(
            appName,
            javaClass.getResource("RemoteTestConfig.properties")!!,
            javaClass.getResource("FallbackTestConfig.properties")!!,
            cacheFile,
            forceOfflineMode = true
        )
        Assertions.assertTrue(config.contains("configSource"))
        Assertions.assertEquals("remote", config.get("configSource"))
    }

    @Test
    fun loadRemoteConfigNoCacheAsyncTest() {
        val config = Config(
            appName,
            javaClass.getResource("RemoteTestConfig.properties")!!,
            javaClass.getResource("FallbackTestConfig.properties")!!,
            cacheFile = null,
            readAsynchronously = true
        )
        Awaitility.await().until { config.currentlyActiveSource == Config.ConfigSource.ONLINE }
        Assertions.assertTrue(config.contains("configSource"))
        Assertions.assertEquals("remote", config.get("configSource"))
        Assertions.assertEquals(config.currentlyActiveSource, Config.ConfigSource.ONLINE)

        val config2 = Config(
            appName,
            javaClass.getResource("RemoteTestConfig.properties")!!,
            javaClass.getResource("FallbackTestConfig.properties")!!,
            cacheFile = null,
            readAsynchronously = true,
            forceOfflineMode = true
        )
        Assertions.assertTrue(config2.contains("configSource"))
        Assertions.assertNotEquals(config2.currentlyActiveSource, Config.ConfigSource.ONLINE)
        Assertions.assertEquals("fallback", config2.get("configSource"))
    }

    @Test
    fun loadRemoteConfigWithCacheAsyncTest() {
        val config = Config(
            appName,
            javaClass.getResource("RemoteTestConfig.properties")!!,
            javaClass.getResource("FallbackTestConfig.properties")!!,
            cacheFile,
            readAsynchronously = true
        )
        Awaitility.await().until { config.currentlyActiveSource == Config.ConfigSource.ONLINE }
        Assertions.assertTrue(config.contains("configSource"))
        Assertions.assertEquals("remote", config.get("configSource"))

        // Should read from the cache
        val config2 = Config(
            appName,
            javaClass.getResource("RemoteTestConfig.properties")!!,
            javaClass.getResource("FallbackTestConfig.properties")!!,
            cacheFile,
            readAsynchronously = true
        )
        Assertions.assertTrue(config2.contains("configSource"))
        Assertions.assertEquals("remote", config2.get("configSource"))

        // Make sure that everything has settled before the test ends
        Awaitility.await().until { config2.currentlyActiveSource == Config.ConfigSource.ONLINE }
    }


    @Test
    fun mergedConfigTest() {
        val config = Config(
            appName,
            javaClass.getResource("RemoteMergedTestConfig.properties")!!,
            javaClass.getResource("FallbackMergedTestConfig.properties")!!,
            cacheFile
        )
        Assertions.assertTrue(config.contains("configSource"))
        Assertions.assertEquals("mergedRemote", config.get("configSource"))
        Assertions.assertTrue(config.contains("remoteParam"))
        Assertions.assertEquals("yes", config.get("remoteParam"))
        Assertions.assertTrue(config.contains("localParam"))
        Assertions.assertEquals("yes", config.get("localParam"))
    }

    @Test
    fun mergedConfigAsyncTest() {
        val config = Config(
            appName,
            javaClass.getResource("RemoteMergedTestConfig.properties")!!,
            javaClass.getResource("FallbackMergedTestConfig.properties")!!,
            cacheFile,
            readAsynchronously = true
        )
        Awaitility.await().until { config.currentlyActiveSource == Config.ConfigSource.ONLINE }
        Assertions.assertTrue(config.contains("configSource"))
        Assertions.assertEquals("mergedRemote", config.get("configSource"))
        Assertions.assertTrue(config.contains("remoteParam"))
        Assertions.assertEquals("yes", config.get("remoteParam"))
        Assertions.assertTrue(config.contains("localParam"))
        Assertions.assertEquals("yes", config.get("localParam"))
    }

    @Test
    fun toStringTest() {
        val config = Config(
            appName,
            javaClass.getResource("RemoteMergedTestConfig.properties")!!,
            javaClass.getResource("FallbackMergedTestConfig.properties")!!,
            cacheFile
        )
        val expectedValues = mapOf(
            "configSource" to "mergedRemote",
            "remoteParam" to "yes",
            "localParam" to "yes"
        )
        val actual = config.toString()
        expectedValues.forEach { (key, value) ->
            val expectedString = "$key=$value"
            Assertions.assertTrue(actual.contains(expectedString))
        }
    }
}
