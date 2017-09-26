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


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

public class ConfigTest {
    private final String cacheFileName = "commonConfigUnitTestCache" + new Random().nextInt() + ".cache";

    @BeforeClass
    public static void beforeClass() {
        Common.getInstance().setAppName("fokprojectUnitTests");
    }

    @Test
    public void LoadRemoteConfigNoCacheTest() throws IOException {
        Config config = new Config(ConfigTest.class.getResource("RemoteTestConfig.properties"), ConfigTest.class.getResource("FallbackTestConfig.properties"), false, cacheFileName);
        Assert.assertTrue(config.contains("configSource"));
        Assert.assertEquals("remote", config.getValue("configSource"));
        Assert.assertTrue(config.isRemoteConfigEnabled());
        Assert.assertFalse(config.isOfflineMode());
        config = new Config(ConfigTest.class.getResource("RemoteTestConfig.properties"), ConfigTest.class.getResource("FallbackTestConfig.properties"), false, cacheFileName, false, true);
        Assert.assertTrue(config.contains("configSource"));
        Assert.assertFalse(config.isRemoteConfigEnabled());
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
    public void LoadConfigFromFile() throws IOException {
        Config config = new Config(ConfigTest.class.getResource("FileTestConfig.properties"));
        Assert.assertEquals("file", config.getValue("configSource"));
    }
}
