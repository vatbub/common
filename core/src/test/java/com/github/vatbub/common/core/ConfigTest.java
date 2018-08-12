package com.github.vatbub.common.core;

/*-
 * #%L
 * FOKProjects Common Core
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


import com.github.vatbub.common.core.logging.FOKLogger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.awaitility.Awaitility.await;

public class ConfigTest  extends CoreBaseTestClass{
    private final String cacheFileName = "commonConfigUnitTestCache" + new Random().nextInt() + ".cache";

    @BeforeClass
    public static void beforeClass() {
        Common.getInstance().setAppName(DEFAULT_APP_NAME);
    }

    @Test
    public void LoadRemoteConfigNoCacheTest() throws IOException {
        Config config = new Config(ConfigTest.class.getResource("RemoteTestConfig.properties"), ConfigTest.class.getResource("FallbackTestConfig.properties"), false, cacheFileName);
        Assert.assertTrue(config.contains("configSource"));
        Assert.assertEquals("remote", config.getValue("configSource"));
        Assert.assertTrue(config.getCurrentlyActiveSource().equals(Config.ConfigSource.ONLINE));
        Assert.assertFalse(config.isOfflineMode());
        config = new Config(ConfigTest.class.getResource("RemoteTestConfig.properties"), ConfigTest.class.getResource("FallbackTestConfig.properties"), false, cacheFileName, false, true);
        Assert.assertTrue(config.contains("configSource"));
        Assert.assertFalse(config.getCurrentlyActiveSource().equals(Config.ConfigSource.ONLINE));
        Assert.assertTrue(config.isOfflineMode());
        Assert.assertEquals("fallback", config.getValue("configSource"));
    }

    @Test
    public void LoadRemoteConfigWithCacheTest() throws IOException {
        Config config = new Config(ConfigTest.class.getResource("RemoteTestConfig.properties"), ConfigTest.class.getResource("FallbackTestConfig.properties"), cacheFileName);
        Assert.assertTrue(config.contains("configSource"));
        Assert.assertEquals("remote", config.getValue("configSource"));
        config = new Config(ConfigTest.class.getResource("RemoteTestConfig.properties"), ConfigTest.class.getResource("FallbackTestConfig.properties"), true, cacheFileName, false, true);
        Assert.assertTrue(config.contains("configSource"));
        Assert.assertEquals("remote", config.getValue("configSource"));
    }

    @Test
    public void LoadRemoteConfigNoCacheAsyncTest() throws IOException {
        final Config config = new Config(ConfigTest.class.getResource("RemoteTestConfig.properties"), ConfigTest.class.getResource("FallbackTestConfig.properties"), false, cacheFileName, true);
        await().until(() -> config.getCurrentlyActiveSource().equals(Config.ConfigSource.ONLINE));
        Assert.assertTrue(config.contains("configSource"));
        Assert.assertEquals("remote", config.getValue("configSource"));
        Assert.assertTrue(config.getCurrentlyActiveSource().equals(Config.ConfigSource.ONLINE));
        Assert.assertFalse(config.isOfflineMode());
        final Config config2 = new Config(ConfigTest.class.getResource("RemoteTestConfig.properties"), ConfigTest.class.getResource("FallbackTestConfig.properties"), false, cacheFileName, true, true);
        Assert.assertTrue(config2.contains("configSource"));
        Assert.assertFalse(config2.getCurrentlyActiveSource().equals(Config.ConfigSource.ONLINE));
        Assert.assertTrue(config2.isOfflineMode());
        Assert.assertEquals("fallback", config2.getValue("configSource"));
    }

    @Test
    public void LoadRemoteConfigWithCacheAsyncTest() throws IOException {
        final Config config = new Config(ConfigTest.class.getResource("RemoteTestConfig.properties"), ConfigTest.class.getResource("FallbackTestConfig.properties"), true, cacheFileName, true);
        await().until(() -> config.getCurrentlyActiveSource().equals(Config.ConfigSource.ONLINE));
        Assert.assertTrue(config.contains("configSource"));
        Assert.assertEquals("remote", config.getValue("configSource"));

        // Should read from the cache
        final Config config2 = new Config(ConfigTest.class.getResource("RemoteTestConfig.properties"), ConfigTest.class.getResource("FallbackTestConfig.properties"), true, cacheFileName, true);
        Assert.assertTrue(config2.contains("configSource"));
        Assert.assertEquals("remote", config2.getValue("configSource"));

        // Make sure that everything has settled before the test ends
        await().until(() -> config2.getCurrentlyActiveSource().equals(Config.ConfigSource.ONLINE));
    }

    @Test
    public void LoadConfigFromFile() throws IOException {
        Config config = new Config(ConfigTest.class.getResource("FileTestConfig.properties"));
        Assert.assertEquals("file", config.getValue("configSource"));
    }

    @Test
    public void mergedConfigTest() throws IOException {
        Config config = new Config(ConfigTest.class.getResource("RemoteMergedTestConfig.properties"), ConfigTest.class.getResource("FallbackMergedTestConfig.properties"), cacheFileName);
        Assert.assertTrue(config.contains("configSource"));
        Assert.assertEquals("mergedRemote", config.getValue("configSource"));
        Assert.assertTrue(config.contains("remoteParam"));
        Assert.assertEquals("yes", config.getValue("remoteParam"));
        Assert.assertTrue(config.contains("localParam"));
        Assert.assertEquals("yes", config.getValue("localParam"));
    }

    @Test
    public void mergedConfigAsyncTest() throws IOException {
        Config config = new Config(ConfigTest.class.getResource("RemoteMergedTestConfig.properties"), ConfigTest.class.getResource("FallbackMergedTestConfig.properties"), true, cacheFileName, true);
        await().until(() -> config.getCurrentlyActiveSource().equals(Config.ConfigSource.ONLINE));
        Assert.assertTrue(config.contains("configSource"));
        Assert.assertEquals("mergedRemote", config.getValue("configSource"));
        Assert.assertTrue(config.contains("remoteParam"));
        Assert.assertEquals("yes", config.getValue("remoteParam"));
        Assert.assertTrue(config.contains("localParam"));
        Assert.assertEquals("yes", config.getValue("localParam"));
    }

    @Test
    public void toStringTest() throws IOException {
        Config config = new Config(ConfigTest.class.getResource("RemoteMergedTestConfig.properties"), ConfigTest.class.getResource("FallbackMergedTestConfig.properties"), cacheFileName);
        Map<String, String> expectedValues = new HashMap<>();
        expectedValues.put("configSource", "mergedRemote");
        expectedValues.put("remoteParam", "yes");
        expectedValues.put("localParam", "yes");

        String actual = config.toString();
        for(Map.Entry<String, String> entry:expectedValues.entrySet()){
            String expectedString = entry.getKey() + "=" + entry.getValue();
            FOKLogger.info(getClass().getName(), "Testing: " + expectedString);
            Assert.assertTrue(actual.contains(expectedString));
        }
    }
}
