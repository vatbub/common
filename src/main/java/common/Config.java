package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.Properties;

import logging.FOKLogger;

public class Config {

	private Properties props = new Properties();
	private boolean remoteConfigEnabled;
	private static FOKLogger log = new FOKLogger(Config.class.getName());

	/**
	 * Creates a new {@code Config}-Instance from the specified
	 * {@code *.properties}-file
	 * 
	 * @param configFile
	 *            The {@code *.properties}-file to import.
	 * @throws FileNotFoundException
	 *             If the specified file does not exist.
	 * @throws IOException
	 *             If the specified file cannot be read.
	 */
	public Config(File configFile) throws FileNotFoundException, IOException {
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
	 * @param remoteConfig
	 *            The {@code URL} of the remote config to be read.
	 * @param fallbackConfig
	 *            The config file to be read in case the {@code remoteConfig}
	 *            cannot be downloaded and no cached version is available.
	 *            downloaded for offline use.
	 * @param cacheFileName
	 *            The file name of the offline cache.
	 * @throws FileNotFoundException
	 *             If the specified fallbackConfig does not exist.
	 * @throws IOException
	 *             If the specified fallbackConfig cannot be read.
	 */
	public Config(URL remoteConfig, File fallbackConfig, String cacheFileName) throws IOException {
		this(remoteConfig, fallbackConfig, true, cacheFileName);
	}

	/**
	 * Creates a new {@code Config}-instance and reads the config from the
	 * remote url asynchronsously. See
	 * {@link #Config(URL, File, boolean, boolean)} to see how it works. If this
	 * fails for any reason and a cached config is available, the cached config
	 * is used instead. If the remote config can't be read and no cached version
	 * is available, the fallbackConfig is used.
	 * 
	 * @param remoteConfig
	 *            The {@code URL} of the remote config to be read.
	 * @param fallbackConfig
	 *            The config file to be read in case the {@code remoteConfig}
	 *            cannot be downloaded and no cached version is available.
	 * @param cacheRemoteConfig
	 *            If {@code true}, the remote config will be cached once
	 *            downloaded for offline use.
	 * @param cacheFileName
	 *            The file name of the offline cache. Only taken into account if
	 *            {@code cacheRemoteConfig==true}
	 * @throws FileNotFoundException
	 *             If the specified fallbackConfig does not exist.
	 * @throws IOException
	 *             If the specified fallbackConfig cannot be read.
	 * @see #Config(URL, File, boolean, boolean)
	 */
	public Config(URL remoteConfig, File fallbackConfig, boolean cacheRemoteConfig, String cacheFileName)
			throws FileNotFoundException, IOException {

	}

	/**
	 * Creates a new {@code Config}-instance and reads the config from the
	 * remote url. If this fails for any reason and a cached config is
	 * available, the cached config is used instead. If the remote config can't
	 * be read and no cached version is available, the fallbackConfig is used.
	 * 
	 * @param remoteConfig
	 *            The {@code URL} of the remote config to be read.
	 * @param fallbackConfig
	 *            The config file to be read in case the {@code remoteConfig}
	 *            cannot be downloaded and no cached version is available.
	 * @param cacheRemoteConfig
	 *            If {@code true}, the remote config will be cached once
	 *            downloaded for offline use.
	 * @param readAsynchronously
	 *            If {@code true}, the remote config will be read
	 *            asynchronously. This happens as follows: First, the
	 *            fallbackConfig or the cached config (if one is available) is
	 *            read. Then, the remote config is loaded in a new Thread. This
	 *            ensures that your Config is instantly accessible while it
	 *            seamlessly updates in the background. You do not need to
	 *            refresh the config once the remote config is loaded, it will
	 *            do that by itself. If the remote config fails to load, a
	 *            message will appear in the log and you will use the cached
	 *            config/fallbackConfig.
	 * @throws FileNotFoundException
	 *             If the specified fallbackConfig does not exist.
	 * @throws IOException
	 *             If the specified fallbackConfig cannot be read.
	 */
	public Config(URL remoteConfig, File fallbackConfig, boolean cacheRemoteConfig, String cacheFileName,
			boolean readAsynchronously) throws FileNotFoundException, IOException {
		if (readAsynchronously) {
			this.readRemoteConfigAsynchronous(remoteConfig, fallbackConfig, cacheRemoteConfig, cacheFileName);
		} else {
			this.readRemoteConfig(remoteConfig, fallbackConfig, cacheRemoteConfig, cacheFileName);
		}
	}

	/**
	 * Imports the specified {@code *.properties}-file
	 * 
	 * @param file
	 *            The {@code *.properties}-file to import.
	 * @throws FileNotFoundException
	 *             If the specified file does not exist.
	 * @throws IOException
	 *             If the specified file cannot be read.
	 */
	private void readConfigFromFile(File file) throws FileNotFoundException, IOException {
		log.getLogger().info("Reading config from local file...");
		props.load(new FileReader(file));
	}

	/**
	 * Reads the config from the remote url. If this fails for any reason and a
	 * cached config is available, the cached config is used instead. If the
	 * remote config can't be read and no cached version is available, the
	 * fallbackConfig is used.
	 * 
	 * @param remoteConfig
	 *            The {@code URL} of the remote config to be read.
	 * @param fallbackConfig
	 *            The config file to be read in case the {@code remoteConfig}
	 *            cannot be downloaded and no cached version is available.
	 * @param cacheRemoteConfig
	 *            If {@code true}, the remote config will be cached once
	 *            downloaded for offline use.
	 * @throws FileNotFoundException
	 *             If the specified fallbackConfig does not exist.
	 * @throws IOException
	 *             If the specified fallbackConfig cannot be read.
	 */
	private void readRemoteConfig(URL remoteConfig, File fallbackConfig, boolean cacheRemoteConfig,
			String cacheFileName) throws FileNotFoundException, IOException {
		try {
			getRemoteConfig(remoteConfig, cacheRemoteConfig, cacheFileName);
		} catch (IOException e) {
			// Something went wrong
			// Check if offline cache exists
			checkForOfflineCacheOrLoadFallback(fallbackConfig, cacheFileName);
		}
	}

	private void getRemoteConfig(URL remoteConfig, boolean cacheRemoteConfig, String cacheFileName) throws IOException {
		log.getLogger().info("Trying to read remote config...");
		props.load(remoteConfig.openStream());
		remoteConfigEnabled = true;
		log.getLogger().info("Import of remote config successful.");

		if (cacheRemoteConfig) {
			// Update the offline cache
			log.getLogger().info("Caching remote config for offline use...");
			File f = new File(Common.getAndCreateAppDataPath() + cacheFileName);
			f.getParentFile().mkdirs();
			FileOutputStream out = new FileOutputStream(f);
			props.store(out, "Config of app " + Common.getAppName());
			out.close();
		}
	}

	private void checkForOfflineCacheOrLoadFallback(File fallbackConfig, String cacheFileName)
			throws FileNotFoundException, IOException {
		File f = new File(Common.getAndCreateAppDataPath() + cacheFileName);
		if (f.exists()) {
			// Offline cache exists
			log.getLogger().info("Reading cached config...");
			this.readConfigFromFile(f);
		} else {
			log.getLogger().info("Reading fallbackConfig...");
			this.readConfigFromFile(fallbackConfig);
			remoteConfigEnabled = false;
		}
	}

	private void readRemoteConfigAsynchronous(URL remoteConfig, File fallbackConfig, boolean cacheRemoteConfig,
			String cacheFileName) throws FileNotFoundException, IOException {
		// Read offline cache or fallback first
		checkForOfflineCacheOrLoadFallback(fallbackConfig, cacheFileName);

		// Now load the remote config in a new Thread
		Thread loadConfigThread = new Thread() {
			@Override
			public void run() {
				try {
					getRemoteConfig(remoteConfig, cacheRemoteConfig, cacheFileName);
				} catch (IOException e) {
					log.getLogger().log(Level.SEVERE, "An error occurred", e);
				}
			}
		};

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
	 * Checks if the specified key is defined in this Cconfig file.
	 * 
	 * @param key
	 *            The key of the property to be checked.
	 * @return {@code true} if a property with the specified key is found,
	 *         {@code false} otherwise.
	 */
	public boolean contains(String key) {
		return props.getProperty(key) == null;
	}

	/**
	 * Returns the config value for the specified key or {@code null} if the key
	 * was not found.
	 * 
	 * @param key
	 *            The key of the config parameter.
	 * @return the config value for the specified key or {@code null} if the key
	 *         was not found.
	 */
	public String getValue(String key) {
		return props.getProperty(key);
	}

	@Override
	public String toString() {
		String res = "";

		for (Entry<Object, Object> e : props.entrySet()) {
			res = res + e.getKey().toString() + "=" + e.getValue() + "\n";
		}

		return res;
	}
}
