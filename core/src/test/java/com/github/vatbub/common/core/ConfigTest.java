package com.github.vatbub.common.core;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

public class ConfigTest {
    private final String cacheFileName = "commonConfigUnitTestCache" + new Random().nextInt() + ".cache";

    @BeforeClass
    public static void beforeClass() {
        Common.setAppName("fokprojectUnitTests");
    }

    @Test
    public void LoadRemoteConfigNoCacheTest() throws IOException, URISyntaxException {
        Config config = new Config(ConfigTest.class.getResource("RemoteTestConfig.properties"), new File(ConfigTest.class.getResource("FallbackTestConfig.properties").toURI()), false, cacheFileName);
        Assert.assertEquals("remote", config.getValue("configSource"));
        config = new Config(ConfigTest.class.getResource("RemoteTestConfig.properties"), new File(ConfigTest.class.getResource("FallbackTestConfig.properties").toURI()), false, cacheFileName, false, true);
        Assert.assertEquals("fallback", config.getValue("configSource"));
    }

    @Test
    public void LoadRemoteConfigWithCacheTest() throws IOException, URISyntaxException {
        Config config = new Config(ConfigTest.class.getResource("RemoteTestConfig.properties"), new File(ConfigTest.class.getResource("FallbackTestConfig.properties").toURI()), cacheFileName);
        Assert.assertEquals("remote", config.getValue("configSource"));
        config = new Config(ConfigTest.class.getResource("RemoteTestConfig.properties"), new File(ConfigTest.class.getResource("FallbackTestConfig.properties").toURI()), true, cacheFileName, false, true);
        Assert.assertEquals("remote", config.getValue("configSource"));
    }

    @Test
    public void LoadConfigFromFile() throws IOException, URISyntaxException {
        Config config = new Config(new File(ConfigTest.class.getResource("FileTestConfig.properties").toURI()));
        Assert.assertEquals("file", config.getValue("configSource"));
    }
}
