package com.github.vatbub.common.core;

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


import com.github.vatbub.common.core.logging.FOKLogger;

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
    private final File file;

    /**
     * Loads or creates the preference file for the specified class
     *
     * @param className The name of the class to load the properties file for
     */
    public Prefs(String className) {
        // Retrieve the user preference node for the package
        file = new File(Common.getInstance().getAndCreateAppDataPath() + className + ".properties");
        reload();
    }

    /**
     * Reloads this preference file from the hard disk
     */
    public void reload() {
        try {
            if (file.exists()) {
                try(FileReader fileReader = new FileReader(file)) {
                    props.load(fileReader);
                }
            }

        } catch (IOException e) {
            FOKLogger.log(Prefs.class.getName(), Level.SEVERE, FOKLogger.DEFAULT_ERROR_TEXT, e);
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
        FOKLogger.info(Prefs.class.getName(), "Saving preference file as: " + file.getAbsolutePath());
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs())
            throw new IllegalStateException("Unable to create the folder to save the Prefs in");

        try (FileOutputStream out = new FileOutputStream(file)){
            props.store(out, "This is a preference file of the app " + Common.getInstance().getAppName() + ". If you delete this file, the specified app will be (partly or entirely) reset to its factory settings.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
