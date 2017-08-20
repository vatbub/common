package com.github.vatbub.common.core;

/*-
 * #%L
 * FOKProjects Common
 * %%
 * Copyright (C) 2016 Frederik Kammel
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

import java.io.*;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;

@SuppressWarnings("unused")
public class Config {

    private final Properties props = new Properties();
    private boolean remoteConfigEnabled;
    private boolean offlineMode;

    /**
     * Creates a new {@code Config}-Instance from the specified
     * {@code *.properties}-file
     *
     * @param configFile The {@code *.properties}-file to import.
     * @throws IOException If the specified file does not exist or cannot be read.
     */
    public Config(File configFile) throws IOException {
        this.readConfigFromFile(configFile);
    }

    /**
     * Creates a new {@code Config}-instance and reads the config from the
     * remote url. If this fails for any reason and a cached config is
     * available, the cached config is used instead. If the remote config can't
     * be read and no cached version is available, the fallbackConfig is
     * used.<br>
     * <br>
     * This constructor caches the remote config for offline use.
     *
     * @param remoteConfig   The {@code URL} of the remote config to be read.
     * @param fallbackConfig The config file to be read in case the {@code remoteConfig}
     *                       cannot be downloaded and no cached version is available.
     *                       downloaded for offline use.
     * @param cacheFileName  The file name of the offline cache.
     * @throws FileNotFoundException If the specified fallbackConfig does not exist.
     * @throws IOException           If the specified fallbackConfig cannot be read.
     */
    public Config(URL remoteConfig, File fallbackConfig, String cacheFileName) throws IOException {
        this(remoteConfig, fallbackConfig, true, cacheFileName);
    }

    /**
     * Creates a new {@code Config}-instance and reads the config from the
     * remote url asynchronously. See the constructor below to see how it
     * works. If this fails for any reason and a cached config is available, the
     * cached config is used instead. If the remote config can't be read and no
     * cached version is available, the fallbackConfig is used.
     *
     * @param remoteConfig      The {@code URL} of the remote config to be read.
     * @param fallbackConfig    The config file to be read in case the {@code remoteConfig}
     *                          cannot be downloaded and no cached version is available.
     * @param cacheRemoteConfig If {@code true}, the remote config will be cached once
     *                          downloaded for offline use.
     * @param cacheFileName     The file name of the offline cache. Only taken into account if
     *                          {@code cacheRemoteConfig==true}
     * @throws IOException If the specified fallbackConfig does not exist or cannot be read.
     */
    @SuppressWarnings("RedundantThrows")
    public Config(URL remoteConfig, File fallbackConfig, boolean cacheRemoteConfig, String cacheFileName)
            throws IOException {
        this(remoteConfig, fallbackConfig, cacheRemoteConfig, cacheFileName, false);
    }

    /**
     * Creates a new {@code Config}-instance and reads the config from the
     * remote url. If this fails for any reason and a cached config is
     * available, the cached config is used instead. If the remote config can't
     * be read and no cached version is available, the fallbackConfig is used.
     *
     * @param remoteConfig       The {@code URL} of the remote config to be read.
     * @param fallbackConfig     The config file to be read in case the {@code remoteConfig}
     *                           cannot be downloaded and no cached version is available.
     * @param cacheRemoteConfig  If {@code true}, the remote config will be cached once
     *                           downloaded for offline use.
     * @param cacheFileName      The file name of the offline cache. Only taken into account if
     *                           {@code cacheRemoteConfig==true}
     * @param readAsynchronously If {@code true}, the remote config will be read
     *                           asynchronously. This happens as follows: First, the
     *                           fallbackConfig or the cached config (if one is available) is
     *                           read. Then, the remote config is loaded in a new Thread. This
     *                           ensures that your Config is instantly accessible while it
     *                           seamlessly updates in the background. You do not need to
     *                           refresh the config once the remote config is loaded, it will
     *                           do that by itself. If the remote config fails to load, a
     *                           message will appear in the log and you will use the cached
     *                           config/fallbackConfig.
     * @throws IOException If the specified fallbackConfig does not exist or cannot be read.
     */
    public Config(URL remoteConfig, File fallbackConfig, boolean cacheRemoteConfig, String cacheFileName,
                  boolean readAsynchronously) throws IOException {
        this(remoteConfig, fallbackConfig, cacheRemoteConfig, cacheFileName, readAsynchronously, false);
    }

    /**
     * Creates a new {@code Config}-instance and reads the config from the
     * remote url. If this fails for any reason and a cached config is
     * available, the cached config is used instead. If the remote config can't
     * be read and no cached version is available, the fallbackConfig is used.
     *
     * @param remoteConfig       The {@code URL} of the remote config to be read.
     * @param fallbackConfig     The config file to be read in case the {@code remoteConfig}
     *                           cannot be downloaded and no cached version is available.
     * @param cacheRemoteConfig  If {@code true}, the remote config will be cached once
     *                           downloaded for offline use.
     * @param cacheFileName      The file name of the offline cache. Only taken into account if
     *                           {@code cacheRemoteConfig==true}
     * @param readAsynchronously If {@code true}, the remote config will be read
     *                           asynchronously. This happens as follows: First, the
     *                           fallbackConfig or the cached config (if one is available) is
     *                           read. Then, the remote config is loaded in a new Thread. This
     *                           ensures that your Config is instantly accessible while it
     *                           seamlessly updates in the background. You do not need to
     *                           refresh the config once the remote config is loaded, it will
     *                           do that by itself. If the remote config fails to load, a
     *                           message will appear in the log and you will use the cached
     *                           config/fallbackConfig.
     * @param offlineMode        If {@code true}, offline mode will be simulated
     * @throws IOException If the specified fallbackConfig does not exist or cannot be read.
     */
    public Config(URL remoteConfig, File fallbackConfig, boolean cacheRemoteConfig, String cacheFileName,
                  boolean readAsynchronously, boolean offlineMode) throws IOException {
        setOfflineMode(offlineMode);
        if (readAsynchronously) {
            this.readRemoteConfigAsynchronous(remoteConfig, fallbackConfig, cacheRemoteConfig, cacheFileName);
        } else {
            this.readRemoteConfig(remoteConfig, fallbackConfig, cacheRemoteConfig, cacheFileName);
        }
    }

    /**
     * Imports the specified {@code *.properties}-file
     *
     * @param file The {@code *.properties}-file to import.
     * @throws IOException If the specified file does not exist or cannot be read.
     */
    private void readConfigFromFile(File file) throws IOException {
        FOKLogger.info(Config.class.getName(), "Reading config from local file...");
        props.load(new FileReader(file));
    }

    /**
     * Reads the config from the remote url. If this fails for any reason and a
     * cached config is available, the cached config is used instead. If the
     * remote config can't be read and no cached version is available, the
     * fallbackConfig is used.
     *
     * @param remoteConfig      The {@code URL} of the remote config to be read.
     * @param fallbackConfig    The config file to be read in case the {@code remoteConfig}
     *                          cannot be downloaded and no cached version is available.
     * @param cacheRemoteConfig If {@code true}, the remote config will be cached once
     *                          downloaded for offline use.
     * @throws IOException If the specified fallbackConfig does not exist or cannot be read.
     */
    private void readRemoteConfig(URL remoteConfig, File fallbackConfig, boolean cacheRemoteConfig,
                                  String cacheFileName) throws IOException {
        try {
            if (isOfflineMode()) {
                throw new IOException("Offline mode enabled");
            }
            getRemoteConfig(remoteConfig, cacheRemoteConfig, cacheFileName);
        } catch (IOException e) {
            // Something went wrong
            // Check if offline cache exists
            checkForOfflineCacheOrLoadFallback(fallbackConfig, cacheFileName);
        }
    }

    private void getRemoteConfig(URL remoteConfig, boolean cacheRemoteConfig, String cacheFileName) throws IOException {
        FOKLogger.info(Config.class.getName(), "Trying to read remote config...");
        props.load(remoteConfig.openStream());
        remoteConfigEnabled = true;
        FOKLogger.info(Config.class.getName(), "Import of remote config successful.");

        if (cacheRemoteConfig) {
            // Update the offline cache
            FOKLogger.info(Config.class.getName(), "Caching remote config for offline use...");
            File f = new File(Common.getAndCreateAppDataPath() + cacheFileName);
            f.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(f);
            props.store(out, "Config of app " + Common.getAppName());
            out.close();
        }
    }

    private void checkForOfflineCacheOrLoadFallback(File fallbackConfig, String cacheFileName)
            throws IOException {
        File f = new File(Common.getAndCreateAppDataPath() + cacheFileName);
        if (f.exists()) {
            // Offline cache exists
            FOKLogger.info(Config.class.getName(), "Reading cached config...");
            this.readConfigFromFile(f);
        } else {
            FOKLogger.info(Config.class.getName(), "Reading fallbackConfig...");
            this.readConfigFromFile(fallbackConfig);
            remoteConfigEnabled = false;
        }
    }

    private void readRemoteConfigAsynchronous(URL remoteConfig, File fallbackConfig, boolean cacheRemoteConfig,
                                              String cacheFileName) throws IOException {
        // Read offline cache or fallback first
        checkForOfflineCacheOrLoadFallback(fallbackConfig, cacheFileName);

        // Now load the remote config in a new Thread
        Thread loadConfigThread = new Thread(() -> {
            try {
                getRemoteConfig(remoteConfig, cacheRemoteConfig, cacheFileName);
            } catch (IOException e) {
                FOKLogger.log(Config.class.getName(), Level.SEVERE, "An error occurred", e);
            }
        });

        loadConfigThread.setName("loadConfigThread");
        loadConfigThread.start();
    }

    /**
     * @return the remoteConfigEnabled
     */
    public boolean isRemoteConfigEnabled() {
        return remoteConfigEnabled;
    }

    /**
     * Checks if the specified key is defined in this Config file.
     *
     * @param key The key of the property to be checked.
     * @return {@code true} if a property with the specified key is found,
     * {@code false} otherwise.
     */
    public boolean contains(String key) {
        return props.getProperty(key) == null;
    }

    /**
     * Returns the config value for the specified key or {@code null} if the key
     * was not found.
     *
     * @param key The key of the config parameter.
     * @return the config value for the specified key or {@code null} if the key
     * was not found.
     */
    public String getValue(String key) {
        return props.getProperty(key);
    }

    public boolean isOfflineMode() {
        return offlineMode;
    }

    public void setOfflineMode(boolean offlineMode) {
        this.offlineMode = offlineMode;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        for (Entry<Object, Object> e : props.entrySet()) {
            res.append(e.getKey().toString()).append("=").append(e.getValue()).append("\n");
        }

        return res.toString();
    }
}
