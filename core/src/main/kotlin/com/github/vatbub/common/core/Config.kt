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

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Files
import java.util.*

/**
 * Creates a new `Config`-instance and reads the config from the
 * remote url. If this fails for any reason and a cached config is
 * available, the cached config is used instead. If the remote config can't
 * be read and no cached version is available, the fallbackConfig is used.
 *
 * @param remoteConfig       The `URL` of the remote config to be read.
 * @param fallbackConfig     The config file to be read in case the `remoteConfig`
 * cannot be downloaded and no cached version is available.
 * @param cacheRemoteConfig  If `true`, the remote config will be cached once
 * downloaded for offline use.
 * @param cacheFileName      The file name of the offline cache. Only taken into account if
 * `cacheRemoteConfig==true`
 * @param readAsynchronously If `true`, the remote config will be read
 * asynchronously. This happens as follows: First, the
 * fallbackConfig or the cached config (if one is available) is
 * read. Then, the remote config is loaded in a new Thread. This
 * ensures that your Config is instantly accessible while it
 * seamlessly updates in the background. You do not need to
 * refresh the config once the remote config is loaded, it will
 * do that by itself. If the remote config fails to load, a
 * message will appear in the log and you will use the cached
 * config/fallbackConfig.
 * @param forceOfflineMode        If `true`, offline mode will be simulated
 */
class Config @JvmOverloads constructor(
    private val appName: String,
    private val remoteConfig: URL,
    private val fallbackConfig: URL,
    private val cacheFile: File?,
    readAsynchronously: Boolean = false,
    private val forceOfflineMode: Boolean = false
) {

    private val onlineProperties = Properties()
    private val offlineProperties = Properties()


    @Volatile
    var currentlyActiveSource: ConfigSource? = null
        private set

    init {
        if (readAsynchronously) readRemoteConfigAsynchronous()
        else readRemoteConfig()
    }

    private fun readRemoteConfig() {
        loadOfflineCacheOrFallback()
        if (forceOfflineMode) return
        downloadRemoteConfig()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun readRemoteConfigAsynchronous() {
        loadOfflineCacheOrFallback()

        if (forceOfflineMode) return

        GlobalScope.launch {
            downloadRemoteConfig()
        }
    }

    private fun downloadRemoteConfig() {
        this.logger.info("Trying to read remote config...")
        remoteConfig.openStream().use { inputStream ->
            onlineProperties.load(inputStream)
        }
        this.currentlyActiveSource = ConfigSource.ONLINE
        this.logger.info("Import of remote config successful.")

        if (cacheFile == null) return

        this.logger.info("Caching remote config for offline use...")
        Files.createDirectories(cacheFile.parentFile.toPath())
        FileOutputStream(cacheFile).use { fileOutputStream ->
            onlineProperties.store(fileOutputStream, "Config of app $appName")
        }
    }

    private fun loadOfflineCacheOrFallback() {
        if (cacheFile?.exists() == true) {
            this.logger.info("Reading cached config...")
            readConfigFromFile(cacheFile.toURI().toURL())
            this.currentlyActiveSource = ConfigSource.CACHE
        } else {
            this.logger.info("Reading fallbackConfig...")
            readConfigFromFile(fallbackConfig)
            this.currentlyActiveSource = ConfigSource.OFFLINE
        }
    }

    private fun readConfigFromFile(file: URL) {
        this.logger.info("Reading config from local file...")
        file.openStream().use { inputStream ->
            offlineProperties.load(inputStream)
        }
    }

    /**
     * Checks if the specified key is defined in this Config file.
     *
     * @param key The key of the property to be checked.
     * @return `true` if a property with the specified key is found,
     * `false` otherwise.
     */
    operator fun contains(key: String): Boolean =
        onlineProperties.getProperty(key) != null
                || offlineProperties.getProperty(key) != null

    /**
     * Returns the config value for the specified key or `null` if the key
     * was not found. If the key is defined in both, the remote and the fallback config,
     * the value from the remote config will be returned. If the value is *only* defined
     * in the fallback config but *not* in the remote config, the local fallback value will
     * be returned. (I. e. the remote and fallback config are merged and the remote config is given priority).
     *
     * @param key The key of the config parameter.
     * @return the config value for the specified key or `null` if the key
     * was not found.
     */
    fun get(key: String): String =
        if (onlineProperties.containsKey(key)) onlineProperties.getProperty(key)
        else offlineProperties.getProperty(key)

    @Suppress("SimplifiableCallChain")
    override fun toString(): String {
        val onlineData = onlineProperties.map { (key, value) ->
            "$key=$value"
        }.joinToString(separator = "\n")
        val offlineData = offlineProperties.map { (key, value) ->
            "$key=$value"
        }.joinToString(separator = "\n")
        return "$onlineData\n${offlineData}"
    }

    enum class ConfigSource {
        ONLINE, CACHE, OFFLINE
    }
}
