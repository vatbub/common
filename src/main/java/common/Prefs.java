package common;

/*
 * #%L
 * examTrainer
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


import logging.FOKLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

/**
 * A wrapper for {@code java.util.Properties} to save preferences. The preferences are saved in the applications AppData folder, make sure to call {@link Common#setAppName(String)} prior to using this class or else you will get a {@code NullPointerException}
 */
public class Prefs {
    private final Properties props = new Properties();
    private final File f;

    /**
     * Loads or creates the preference file for the specified class
     *
     * @param className The name of the class to load the properties file for
     */
    public Prefs(String className) {
        // Retrieve the user preference node for the package
        f = new File(Common.getAndCreateAppDataPath() + className + ".properties");
        reload();
    }

    public void reload() {
        try {
            if (f.exists()) {
                // Load the properties
                props.load(new FileReader(f));
            }

        } catch (IOException e) {
            FOKLogger.log(Prefs.class.getName(), Level.SEVERE, "An error occurred", e);
        }
    }

    /**
     * Sets the value of the specified preference in the properties file
     *
     * @param prefKey   The key of the preference to save
     * @param prefValue The value of the preference to save
     */
    public void setPreference(String prefKey, String prefValue) {
        props.setProperty(prefKey, prefValue);
        savePreferences();
    }

    /**
     * Returns the value of the specified preference.
     *
     * @param prefKey      The preference to look for
     * @param defaultValue The value to be returned if the key was not found in the properties file
     * @return The value of the specified preference or the {@code defaultValue} if the key was not found
     */
    public String getPreference(String prefKey, String defaultValue) {
        return props.getProperty(prefKey, defaultValue);
    }

    /**
     * Saves the properties file to the hard disk. No need to call this method explicitly as it is already called every time {@link #setPreference(String, String)} is called.
     */
    public void savePreferences() {
        try {
            FOKLogger.info(Prefs.class.getName(), "Saving preference file as: " + f.getAbsolutePath());
            f.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(f);
            props.store(out, "This is a preference file of the app " + Common.getAppName() + ". If you delete this file, the specified app will be (partly or entirely) reset to its factory settings.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
