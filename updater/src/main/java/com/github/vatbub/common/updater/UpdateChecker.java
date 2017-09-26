package com.github.vatbub.common.updater;

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


import com.github.vatbub.common.core.Common;
import com.github.vatbub.common.core.Prefs;
import com.github.vatbub.common.core.logging.FOKLogger;
import javafx.application.Platform;
import org.apache.commons.lang.StringUtils;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * This class can be used to self-update applications that are deployed using a
 * maven repository.
 *
 * @author frede
 */
public class UpdateChecker {

    private static final String latestSeenVersionPrefKey = "updates.latestVersionOnWebsite";
    private static final Prefs updatePrefs = new Prefs(UpdateChecker.class.getName());
    private static boolean cancelDownloadAndLaunch;

    private static boolean cancelUpdateCompletion = false;
    private static String oldFile;
    private static final Thread deleteFileThread = new Thread() {
        @Override
        public void run() {
            FOKLogger.info(UpdateChecker.class.getName(), "Attempting to delete file " + oldFile + " ...");
            do {
                if (cancelUpdateCompletion) {
                    // cancel requested
                    FOKLogger.info(UpdateChecker.class.getName(), "Update completion cancelled. The file " + oldFile + " was not deleted.");
                    break;
                }
            } while (!new File(oldFile).delete());

            // If we arrive here, we successfully deleted the file
            FOKLogger.info(UpdateChecker.class.getName(), "Successfully deleted file " + oldFile);
        }
    };

    /**
     * Versions lower or equal to {@code ver} will be ignored when using
     * {@link #isUpdateAvailable(URL, String, String, String)}
     *
     * @param ver The version to ignore
     */
    public static void ignoreUpdate(Version ver) {
        FOKLogger.info(UpdateChecker.class.getName(), "User ignores all updates up to (and including) version " + ver.toString());
        updatePrefs.setPreference(latestSeenVersionPrefKey, ver.toString());
    }

    /**
     * Checks if a new release has been published on the website. This does not
     * compare the current app version to the release version on the website,
     * just checks if something happened on the website. This ensures that
     * updates that were ignored by the user do not show up again. Assumes that
     * the artifact has a jar-packaging.
     *
     * @param repoBaseURL     The base url of the maven repo to use
     * @param mavenGroupID    The maven groupId of the artifact to update
     * @param mavenArtifactID The maven artifactId of the artifact to update
     * @param mavenClassifier The maven classifier of the artifact to update
     * @return {@code true} if a new release is available and the user did not
     * ignore it.
     */
    @SuppressWarnings("unused")
    public static UpdateInfo isUpdateAvailable(URL repoBaseURL, String mavenGroupID, String mavenArtifactID,
                                               String mavenClassifier) {
        return isUpdateAvailable(repoBaseURL, mavenGroupID, mavenArtifactID, mavenClassifier, "jar");
    }

    /**
     * Checks if a new release has been published on the website. This does not
     * compare the current app version to the release version on the website,
     * just checks if something happened on the website. This ensures that
     * updates that were ignored by the user do not show up again.
     *
     * @param repoBaseURL     The base url of the maven repo to use
     * @param mavenGroupID    The maven groupId of the artifact to update
     * @param mavenArtifactID The maven artifactId of the artifact to update
     * @param mavenClassifier The maven classifier of the artifact to update
     * @param packaging       The packaging type of the artifact to update
     * @return {@code true} if a new release is available and the user did not
     * ignore it.
     * @see Common#getPackaging()
     */
    @SuppressWarnings("Duplicates")
    public static UpdateInfo isUpdateAvailable(URL repoBaseURL, String mavenGroupID, String mavenArtifactID,
                                               String mavenClassifier, @SuppressWarnings("SameParameterValue") String packaging) {
        String savedSetting = updatePrefs.getPreference(latestSeenVersionPrefKey, "");
        UpdateInfo res = null;
        try {
            FOKLogger.info(UpdateChecker.class.getName(), "Checking for updates...");
            res = getLatestUpdateInfo(repoBaseURL, mavenGroupID, mavenArtifactID, mavenClassifier, packaging);

            Version currentVersion;

            try {
                currentVersion = new Version(Common.getInstance().getAppVersion());
            } catch (IllegalArgumentException e) {
                FOKLogger.log(UpdateChecker.class.getName(), Level.SEVERE, "An error occurred", e);
                res.showAlert = false;
                return res;
            }
            Version savedVersion;
            try {
                savedVersion = new Version(savedSetting);
            } catch (IllegalArgumentException e) {
                // No update was ever ignored by the user so use the current
                // version as the savedVersion
                savedVersion = currentVersion;
            }

            if (res.toVersion.compareTo(savedVersion) > 0) {
                // new update found
                FOKLogger.info(UpdateChecker.class.getName(), "Update available!");
                FOKLogger.info(UpdateChecker.class.getName(), "Version after update: " + res.toVersion.toString());
                FOKLogger.info(UpdateChecker.class.getName(), "Filesize:             " + res.fileSizeInMB + "MB");
                res.showAlert = true;
            } else if (res.toVersion.compareTo(currentVersion) > 0) {
                // found update that is ignored
                FOKLogger.info(UpdateChecker.class.getName(), "Update available (Update was ignored by the user)!");
                FOKLogger.info(UpdateChecker.class.getName(), "Version after update: " + res.toVersion.toString());
                FOKLogger.info(UpdateChecker.class.getName(), "Filesize:             " + res.fileSizeInMB + "MB");
            } else {
                FOKLogger.info(UpdateChecker.class.getName(), "No update found.");
            }
        } catch (JDOMException | IOException e) {
            FOKLogger.log(UpdateChecker.class.getName(), Level.SEVERE, "An error occurred", e);
        }

        return res;
    }

    /**
     * Checks if a new release has been published on the website. This compares
     * the current appVersion to the version available on the website and thus
     * does not take into account if the user ignored that update. Assumes that
     * the artifact has a jar-packaging.
     *
     * @param repoBaseURL     The base url of the maven repo to use
     * @param mavenGroupID    The maven groupId of the artifact to update
     * @param mavenArtifactID The maven artifactId of the artifact to update
     * @param mavenClassifier The maven classifier of the artifact to update
     * @return {@code true} if a new release is available.
     */
    @SuppressWarnings("unused")
    public static UpdateInfo isUpdateAvailableCompareAppVersion(URL repoBaseURL, String mavenGroupID,
                                                                String mavenArtifactID, String mavenClassifier) {
        return isUpdateAvailableCompareAppVersion(repoBaseURL, mavenGroupID, mavenArtifactID, mavenClassifier, "jar");
    }

    /**
     * Checks if a new release has been published on the website. This compares
     * the current appVersion to the version available on the website and thus
     * does not take into account if the user ignored that update.
     *
     * @param repoBaseURL     The base url of the maven repo to use
     * @param mavenGroupID    The maven groupId of the artifact to update
     * @param mavenArtifactID The maven artifactId of the artifact to update
     * @param mavenClassifier The maven classifier of the artifact to update
     * @param packaging       The packaging type of the artifact to update
     * @return {@code true} if a new release is available.
     * @see Common#getPackaging()
     */
    @SuppressWarnings("Duplicates")
    public static UpdateInfo isUpdateAvailableCompareAppVersion(URL repoBaseURL, String mavenGroupID,
                                                                String mavenArtifactID, String mavenClassifier, @SuppressWarnings("SameParameterValue") String packaging) {
        UpdateInfo res = null;
        try {
            FOKLogger.info(UpdateChecker.class.getName(), "Checking for updates...");
            res = getLatestUpdateInfo(repoBaseURL, mavenGroupID, mavenArtifactID, mavenClassifier, packaging);

            Version currentVersion;

            try {
                currentVersion = new Version(Common.getInstance().getAppVersion());
            } catch (IllegalArgumentException e) {
                FOKLogger.log(UpdateChecker.class.getName(), Level.SEVERE, "An error occurred", e);
                res.showAlert = false;
                return res;
            }

            if (res.toVersion.compareTo(currentVersion) > 0) {
                // new update found
                updatePrefs.setPreference(latestSeenVersionPrefKey, res.toVersion.toString());
                FOKLogger.info(UpdateChecker.class.getName(), "Update available!");
                FOKLogger.info(UpdateChecker.class.getName(), "Version after update: " + res.toVersion.toString());
                FOKLogger.info(UpdateChecker.class.getName(), "Filesize:             " + res.fileSizeInMB + "MB");
                res.showAlert = true;
            } else {
                FOKLogger.info(UpdateChecker.class.getName(), "No update found.");
            }
        } catch (JDOMException | IOException e) {
            FOKLogger.log(UpdateChecker.class.getName(), Level.SEVERE, "An error occurred", e);
        }

        return res;
    }

    /**
     * Retrieves the {@link UpdateInfo} for the latest version available on the
     * specified maven repo for the specified artifact.
     *
     * @param repoBaseURL     The base url where the repo can be reached. For Maven Central,
     *                        this is <a href="http://repo1.maven.org/maven/">http://repo1.maven.org/maven/</a>
     * @param mavenGroupID    The groupID of the artifact to be looked up.
     * @param mavenArtifactID The artifactId of the artifact to be looked up.
     * @return The {@link UpdateInfo} of the latest version of the artifact
     * available at the specified repository.
     * @throws JDOMException If mavens {@code maven-metadata.xml} is not parseable (Which
     *                       will never be the case unless you don't modify it manually).
     * @throws IOException   In case mavens {@code maven-metadata.xml} cannot be retrieved
     *                       for any other reason.
     */
    @SuppressWarnings("unused")
    private static UpdateInfo getLatestUpdateInfo(URL repoBaseURL, String mavenGroupID, String mavenArtifactID)
            throws JDOMException, IOException {
        return getLatestUpdateInfo(repoBaseURL, mavenGroupID, mavenArtifactID, "");
    }

    /**
     * Retrieves the {@link UpdateInfo} for the latest version available on the
     * specified maven repo for the specified artifact. Assumes that the
     * artifact has a jar-packaging.
     *
     * @param repoBaseURL     The base url where the repo can be reached. For Maven Central,
     *                        this is <a href="http://repo1.maven.org/maven/">http://repo1.maven.org/maven/</a>
     * @param mavenGroupID    The groupID of the artifact to be looked up.
     * @param mavenArtifactID The artifactId of the artifact to be looked up.
     * @param mavenClassifier The classifier of the artifact to be looked up.
     * @return The {@link UpdateInfo} of the latest version of the artifact
     * available at the specified repository.
     * @throws JDOMException If mavens {@code maven-metadata.xml} is not parseable (Which
     *                       will never be the case unless you don't modify it manually).
     * @throws IOException   In case mavens {@code maven-metadata.xml} cannot be retrieved
     *                       for any other reason.
     */
    private static UpdateInfo getLatestUpdateInfo(URL repoBaseURL, String mavenGroupID, String mavenArtifactID,
                                                  @SuppressWarnings("SameParameterValue") String mavenClassifier) throws JDOMException, IOException {
        return getLatestUpdateInfo(repoBaseURL, mavenGroupID, mavenArtifactID, mavenClassifier, "jar");
    }

    /**
     * Retrieves the {@link UpdateInfo} for the latest version available on the
     * specified maven repo for the specified artifact.
     *
     * @param repoBaseURL     The base url where the repo can be reached. For Maven Central,
     *                        this is <a href="http://repo1.maven.org/maven/">http://repo1.maven.org/maven/</a>
     * @param mavenGroupID    The groupID of the artifact to be looked up.
     * @param mavenArtifactID The artifactId of the artifact to be looked up.
     * @param mavenClassifier The classifier of the artifact to be looked up.
     * @return The {@link UpdateInfo} of the latest version of the artifact
     * available at the specified repository.
     * @throws JDOMException If mavens {@code maven-metadata.xml} is not parseable (Which
     *                       will never be the case unless you don't modify it manually).
     * @throws IOException   In case mavens {@code maven-metadata.xml} cannot be retrieved
     *                       for any other reason.
     */
    private static UpdateInfo getLatestUpdateInfo(URL repoBaseURL, String mavenGroupID, String mavenArtifactID,
                                                  String mavenClassifier, String packaging) throws JDOMException, IOException {
        UpdateInfo res = new UpdateInfo();

        Document mavenMetadata = getMavenMetadata(repoBaseURL, mavenGroupID, mavenArtifactID);

        // get version number
        res.toVersion = new Version(
                mavenMetadata.getRootElement().getChild("versioning").getChild("latest").getValue());

        // get file size
        String url;
        if (!mavenClassifier.equals("")) {
            // classifier specified
            url = repoBaseURL.toString() + "/" + mavenGroupID.replace('.', '/') + "/" + mavenArtifactID + "/"
                    + res.toVersion + "/" + mavenArtifactID + "-" + res.toVersion + "-" + mavenClassifier + "."
                    + packaging;
        } else {
            url = repoBaseURL.toString() + "/" + mavenGroupID.replace('.', '/') + "/" + mavenArtifactID + "/"
                    + res.toVersion + "/" + mavenArtifactID + "-" + res.toVersion + "." + packaging;
        }

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("HEAD");
        res.fileSizeInMB = connection.getContentLength();

        if (res.fileSizeInMB != -1) {
            // File size is already known, convert it to mb
            res.fileSizeInMB = res.fileSizeInMB / 1024.0 / 1024.0;
        } else {
            // File size is not known
            try {
                // Try to get redirects
                URLConnection redirURLconn = new URL(connection.getHeaderField("Location")).openConnection();
                res.fileSizeInMB = redirURLconn.getContentLength();

                if (res.fileSizeInMB != -1) {
                    // File size is already known, convert it to mb
                    res.fileSizeInMB = res.fileSizeInMB / 1024.0 / 1024.0;
                } else {
                    res.fileSizeInMB = -1;
                }
            } catch (Exception e) {
                FOKLogger.log(UpdateChecker.class.getName(), Level.SEVERE, "An error occurred", e);
                res.fileSizeInMB = -1;
            }
        }

        res.mavenArtifactID = mavenArtifactID;
        res.mavenGroupID = mavenGroupID;
        res.mavenClassifier = mavenClassifier;
        res.mavenRepoBaseURL = repoBaseURL;
        res.packaging = packaging;

        return res;
    }

    // TODO: Fix javadoc

    /**
     * Get a DOM of mavens {@code maven-metadata.xml}-file of the specified
     * artifact.
     *
     * @param repoBaseURL     The base url where the repo can be reached. For Maven Central,
     *                        this is <a href="http://repo1.maven.org/maven/">http://repo1.maven.org/maven/</a>
     * @param mavenGroupID    The groupID of the artifact to be looked up.
     * @param mavenArtifactID The artifactId of the artifact to be looked up.
     * @return A JDOM {@link Document} representation of mavens
     * {@code maven-metadata.xml}
     * @throws JDOMException If mavens {@code maven-metadata.xml} is not parseable (Which
     *                       will never be the case unless you don't modify it manually).
     * @throws IOException   In case mavens {@code maven-metadata.xml} cannot be retrieved
     *                       for any other reason.
     */
    private static Document getMavenMetadata(URL repoBaseURL, String mavenGroupID, String mavenArtifactID)
            throws JDOMException, IOException {
        return new SAXBuilder().build(new URL(repoBaseURL.toString() + "/" + mavenGroupID.replace('.', '/')
                + "/" + mavenArtifactID + "/maven-metadata.xml"));
    }

    /**
     * Downloads the specified update as a jar-file and launches it. The jar
     * file will be saved at the same location as the currently executed file
     * but will not replace it (unless it has the same filename but this will
     * never happen). The old app file will be deleted once the updated one is
     * launched. <b>Please note</b> that the file can't delete itself on some
     * operating systems. Therefore, the deletion is done by the updated file.
     * To actually delete the file, you need to call
     * {@link #completeUpdate(String[])} in your applications main method.
     *
     * @param updateToInstall The {@link UpdateInfo}-object that contains the information
     *                        about the update to download
     * @param params          Additional commandline parameters to be submitted to the new application version.
     * @return {@code true} if the download finished successfully, {@code false}
     * if the download was cancelled using
     * {@link #cancelDownloadAndLaunch()}
     * @throws IllegalStateException if maven fails to download or copy the new artifact.
     * @throws IOException           If the updated artifact cannot be launched.
     * @see #completeUpdate(String[])
     */
    @SuppressWarnings("unused")
    public static boolean downloadAndInstallUpdate(UpdateInfo updateToInstall, String... params)
            throws IllegalStateException, IOException {
        return downloadAndInstallUpdate(updateToInstall, null, params);
    }

    /**
     * Downloads the specified update as a jar-file and launches it. The jar
     * file will be saved at the same location as the currently executed file
     * but will not replace it (unless it has the same filename but this will
     * never happen). The old app file will be deleted once the updated one is
     * launched. <b>Please note</b> that the file can't delete itself on some
     * operating systems. Therefore, the deletion is done by the updated file.
     * To actually delete the file, you need to call
     * {@link #completeUpdate(String[])} in your applications main method.
     *
     * @param updateToInstall The {@link UpdateInfo}-object that contains the information
     *                        about the update to download
     * @param gui             The reference to an {@link UpdateProgressDialog} that displays
     *                        the current update status.
     * @param params          Additional commandline parameters to be submitted to the new application version.
     * @return {@code true} if the download finished successfully, {@code false}
     * if the download was cancelled using
     * {@link #cancelDownloadAndLaunch()}
     * @throws IllegalStateException if maven fails to download or copy the new artifact.
     * @throws IOException           If the updated artifact cannot be launched.
     * @see #completeUpdate(String[])
     */
    public static boolean downloadAndInstallUpdate(UpdateInfo updateToInstall, UpdateProgressDialog gui, String... params)
            throws IllegalStateException, IOException {
        return downloadAndInstallUpdate(updateToInstall, gui, true, params);
    }

    /**
     * Downloads the specified update as a jar-file and launches it. The jar
     * file will be saved at the same location as the currently executed file
     * but will not replace it (unless it has the same filename but this will
     * never happen). The old app file will be deleted once the updated one is
     * launched. <b>Please note</b> that the file can't delete itself on some
     * operating systems. Therefore, the deletion is done by the updated file.
     * To actually delete the file, you need to call
     * {@link #completeUpdate(String[])} in your applications main method.
     *
     * @param updateToInstall          The {@link UpdateInfo}-object that contains the information
     *                                 about the update to download
     * @param gui                      The reference to an {@link UpdateProgressDialog} that displays
     *                                 the current update status.
     * @param launchUpdateAfterInstall If {@code true}, the downloaded file will be launched after
     *                                 the download succeeds.
     * @param params                   Additional commandline parameters to be submitted to the new application version.
     * @return {@code true} if the download finished successfully, {@code false}
     * if the download was cancelled using
     * {@link #cancelDownloadAndLaunch()}
     * @throws IllegalStateException if maven fails to download or copy the new artifact.
     * @throws IOException           If the updated artifact cannot be launched.
     * @see #completeUpdate(String[])
     */
    public static boolean downloadAndInstallUpdate(UpdateInfo updateToInstall, UpdateProgressDialog gui,
                                                   @SuppressWarnings("SameParameterValue") boolean launchUpdateAfterInstall, String... params) throws IllegalStateException, IOException {
        return downloadAndInstallUpdate(updateToInstall, gui, launchUpdateAfterInstall, true, params);
    }

    /**
     * Downloads the specified update as a jar-file and launches it. The jar
     * file will be saved at the same location as the currently executed file
     * but will not replace it (unless it has the same filename but this will
     * never happen)
     *
     * @param updateToInstall          The {@link UpdateInfo}-object that contains the information
     *                                 about the update to download
     * @param gui                      The reference to an {@link UpdateProgressDialog} that displays
     *                                 the current update status.
     * @param launchUpdateAfterInstall If {@code true}, the downloaded file will be launched after
     *                                 the download succeeds.
     * @param deleteOldVersion         If {@code true}, the old app version will be automatically
     *                                 deleted once the new version is downloaded. <b>Please note</b>
     *                                 that the file can't delete itself on some operating systems.
     *                                 Therefore, the deletion is done by the updated file. To
     *                                 actually delete the file, you need to call
     *                                 {@link #completeUpdate(String[])} in your applications main
     *                                 method.
     * @param params                   Additional commandline parameters to be submitted to the new application version.
     * @return {@code true} if the download finished successfully, {@code false}
     * if the download was cancelled using
     * {@link #cancelDownloadAndLaunch()}
     * @throws IllegalStateException if maven fails to download or copy the new artifact.
     * @throws IOException           If the updated artifact cannot be launched.
     * @see #completeUpdate(String[])
     */
    public static boolean downloadAndInstallUpdate(UpdateInfo updateToInstall, UpdateProgressDialog gui,
                                                   boolean launchUpdateAfterInstall, @SuppressWarnings("SameParameterValue") boolean deleteOldVersion, String... params) throws IllegalStateException, IOException {
        return downloadAndInstallUpdate(updateToInstall, gui, launchUpdateAfterInstall, deleteOldVersion, false, params);
    }

    /**
     * Downloads the specified update as a jar-file and launches it. The jar
     * file will be saved at the same location as the currently executed file
     * but will not replace it (unless it has the same filename but this will
     * never happen)
     *
     * @param updateToInstall          The {@link UpdateInfo}-object that contains the information
     *                                 about the update to download
     * @param gui                      The reference to an {@link UpdateProgressDialog} that displays
     *                                 the current update status.
     * @param launchUpdateAfterInstall If {@code true}, the downloaded file will be launched after
     *                                 the download succeeds.
     * @param deleteOldVersion         If {@code true}, the old app version will be automatically
     *                                 deleted once the new version is downloaded. <b>Please note</b>
     *                                 that the file can't delete itself on some operating systems.
     *                                 Therefore, the deletion is done by the updated file. To
     *                                 actually delete the file, you need to call
     *                                 {@link #completeUpdate(String[])} in your applications main
     *                                 method.
     * @param inheritIO                If st to {@code true}, the new version will inherit standard in and standard out.
     * @param params                   Additional commandline parameters to be submitted to the new application version.
     * @return {@code true} if the download finished successfully, {@code false}
     * if the download was cancelled using
     * {@link #cancelDownloadAndLaunch()}
     * @throws IllegalStateException if maven fails to download or copy the new artifact.
     * @throws IOException           If the updated artifact cannot be launched.
     * @see #completeUpdate(String[])
     */
    public static boolean downloadAndInstallUpdate(UpdateInfo updateToInstall, UpdateProgressDialog gui,
                                                   boolean launchUpdateAfterInstall, @SuppressWarnings("SameParameterValue") boolean deleteOldVersion, boolean inheritIO, String... params) throws IllegalStateException, IOException {

        // Reset cancel state
        cancelDownloadAndLaunch = false;

        if (gui != null) {
            gui.preparePhaseStarted();
        }

        // Perform Cancel if requested
        if (cancelDownloadAndLaunch) {
            //noinspection ConstantConditions
            if (gui != null) {
                gui.operationCanceled();
            }
            return false;
        }

        String destFolder;
        File currentSourceFolder = (new File(URLDecoder.decode(UpdateChecker.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8")));

        // get up the structure if we are currently on a file
        while (currentSourceFolder.isFile()) {
            currentSourceFolder = currentSourceFolder.getParentFile();
        }

        destFolder = currentSourceFolder.getAbsolutePath();

        String destFilename;

        // Construct file name of output file
        if (updateToInstall.mavenClassifier.equals("")) {
            // No classifier
            destFilename = updateToInstall.mavenArtifactID + "-" + updateToInstall.toVersion.toString() + "."
                    + updateToInstall.packaging;
        } else {
            destFilename = updateToInstall.mavenArtifactID + "-" + updateToInstall.toVersion.toString() + "-"
                    + updateToInstall.mavenClassifier + "." + updateToInstall.packaging;
        }

        URL artifactURL;

        // Construct the download url
        if (updateToInstall.mavenClassifier.equals("")) {
            artifactURL = new URL(updateToInstall.mavenRepoBaseURL.toString() + "/"
                    + updateToInstall.mavenGroupID.replace('.', '/') + "/" + updateToInstall.mavenArtifactID + "/"
                    + updateToInstall.toVersion.toString() + "/" + updateToInstall.mavenArtifactID + "-"
                    + updateToInstall.toVersion.toString() + "." + updateToInstall.packaging);
        } else {
            artifactURL = new URL(
                    updateToInstall.mavenRepoBaseURL.toString() + "/" + updateToInstall.mavenGroupID.replace('.', '/')
                            + "/" + updateToInstall.mavenArtifactID + "/" + updateToInstall.toVersion.toString() + "/"
                            + updateToInstall.mavenArtifactID + "-" + updateToInstall.toVersion.toString() + "-"
                            + updateToInstall.mavenClassifier + "." + updateToInstall.packaging);
        }

        // Perform Cancel if requested
        if (cancelDownloadAndLaunch) {
            if (gui != null) {
                gui.operationCanceled();
            }
            return false;
        }

        // Create empty file
        File outputFile = new File(destFolder + File.separator + destFilename);

        // Download
        if (gui != null) {
            gui.downloadStarted();
        }

        FOKLogger.info(UpdateChecker.class.getName(), "Downloading artifact from " + artifactURL.toString() + "...");
        FOKLogger.info(UpdateChecker.class.getName(), "Downloading to: " + outputFile.getAbsolutePath());
        // FileUtils.copyURLToFile(artifactURL, outputFile);

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) (artifactURL.openConnection());
            long completeFileSize = httpConnection.getContentLength();

            java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream(outputFile);
            java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;

                // calculate progress
                // final int currentProgress = (int)
                // ((((double)downloadedFileSize) / ((double)completeFileSize))
                // * 100000d);

                // update progress bar
                if (gui != null) {
                    gui.downloadProgressChanged(downloadedFileSize / 1024.0,
                            completeFileSize / 1024.0);
                }

                bout.write(data, 0, x);

                // Perform Cancel if requested
                if (cancelDownloadAndLaunch) {
                    bout.close();
                    in.close();
                    outputFile.delete();
                    if (gui != null) {
                        gui.operationCanceled();
                    }
                    return false;
                }
            }
            bout.close();
            in.close();
        } catch (IOException e) {
            FOKLogger.log(UpdateChecker.class.getName(), Level.SEVERE, "An error occurred", e);
        }

        // Perform Cancel if requested
        if (cancelDownloadAndLaunch) {
            if (gui != null) {
                gui.operationCanceled();
            }
            return false;
        }

        // Perform install steps (none at the moment)
        if (gui != null) {
            gui.installStarted();
        }

        // Perform Cancel if requested
        if (cancelDownloadAndLaunch) {
            if (gui != null) {
                gui.operationCanceled();
            }
            return false;
        }

        // launch the app
        if (gui != null) {
            gui.launchStarted();
        }

        if (launchUpdateAfterInstall) {
            ProcessBuilder pb;
            List<String> startupArgs = new ArrayList<>();

            if (updateToInstall.packaging.equals("jar")) {
                startupArgs.add("java");
                startupArgs.add("-jar");
            }

            startupArgs.add(destFolder + File.separator + destFilename);

            String decodedPath = Common.getInstance().getPathAndNameOfCurrentJar();

            if (deleteOldVersion) {
                FOKLogger.info(UpdateChecker.class.getName(), "The following file will be deleted once the update completes: " + decodedPath);
                startupArgs.add("deleteFile=" + decodedPath);
            }

            // add the version info of this file to the startup args
            startupArgs.add("oldVersion=" + Common.getInstance().getAppVersion());
            startupArgs.add("oldFile=" + decodedPath);

            startupArgs.addAll(Arrays.asList(params));

            FOKLogger
                    .info(UpdateChecker.class.getName(), "Launching new version using command: " + StringUtils.join(startupArgs.toArray(), " "));

            pb = new ProcessBuilder(startupArgs);// .inheritIO();
            pb.start();

			/*
             * // Wait for process to end try { process.waitFor(); } catch
			 * (InterruptedException e) { FOKLogger.log(Level.SEVERE,
			 * "An error occurred", e); }
			 */
            Platform.exit();
        }

        // Everything went smoothly
        return true;
    }

    /**
     * Completes the update after the updated version of the app is launched.
     * Especially deletes the old application file if commanded.<br>
     * <br>
     * <b>Note:</b> The old file is deleted in a separate thread. This way, the
     * file is deleted, as soon as it is unlocked by the os. If you wish to
     * cancel the delete thread (e. g. because the user exits the app), call
     * {@link #cancelUpdateCompletion()}
     *
     * @param startupArgs All arguments passed to the {@code main}-method of the app.
     */
    @SuppressWarnings("unused")
    public static void completeUpdate(String[] startupArgs) {
        completeUpdate(startupArgs, null);
    }

    /**
     * Completes the update after the updated version of the app is launched.
     * Especially deletes the old application file if commanded.<br>
     * <br>
     * <b>Note:</b> The old file is deleted in a separate thread. This way, the
     * file is deleted, as soon as it is unlocked by the os. If you wish to
     * cancel the delete thread (e. g. because the user exits the app), call
     * {@link #cancelUpdateCompletion()}
     *
     * @param startupArgs         All arguments passed to the {@code main}-method of the app.
     * @param executeOnFirstStart Executed if the app is started the first time after an update.
     */
    public static void completeUpdate(String[] startupArgs, @SuppressWarnings("SameParameterValue") CompleteUpdateRunnable executeOnFirstStart) {
        boolean updateToComplete = false;
        boolean deleteOldFile = false;
        File fileToDelete = null;
        Version oldVersion = null;

        // read all the args
        List<String> argsAsList = new ArrayList<>(Arrays.asList(startupArgs));
        for (String arg : startupArgs) {
            if (arg.toLowerCase().matches("oldfile=.*")) {
                updateToComplete = true;
                // get the info about the old file
                oldFile = arg.substring(arg.toLowerCase().indexOf('=') + 1);
                fileToDelete = new File(oldFile);
                argsAsList.remove(arg);
            } else if (arg.toLowerCase().matches("deletefile.*")) {
                deleteOldFile = true;
                updateToComplete = true;
                if (arg.contains("=")) {
                    // read the file to delete from this arg for backwards compatibility
                    oldFile = arg.substring(arg.toLowerCase().indexOf('=') + 1);
                    fileToDelete = new File(oldFile);
                }
                argsAsList.remove(arg);
            } else if (arg.toLowerCase().matches("oldversion=.*")) {
                // get the old version number
                oldVersion = new Version(arg.substring(arg.toLowerCase().indexOf('=') + 1));
                updateToComplete = true;
                argsAsList.remove(arg);
            }
        }
        //noinspection UnusedAssignment
        startupArgs = argsAsList.toArray(new String[0]);

        if (updateToComplete) {
            if (deleteOldFile) {
                deleteFileThread.setName("deleteFileThread");
                deleteFileThread.start();
            }

            // run firstRun runnable
            if (executeOnFirstStart != null) {
                executeOnFirstStart.run(oldVersion, fileToDelete);
            }
        }

    }

    /**
     * Cancels the update completion started using
     * {@link #completeUpdate(String[])}
     */
    @SuppressWarnings("unused")
    public static void cancelUpdateCompletion() {
        cancelUpdateCompletion = true;
    }

    @SuppressWarnings("unused")
    public static void cancelDownloadAndLaunch() {
        cancelDownloadAndLaunch(null);
    }

    public static void cancelDownloadAndLaunch(UpdateProgressDialog gui) {
        cancelDownloadAndLaunch = true;
        FOKLogger.info(UpdateChecker.class.getName(), "Requested to cancel the current operation, Cancel in progress...");

        if (gui != null) {
            gui.cancelRequested();
        }
    }

    public interface CompleteUpdateRunnable {
        /**
         * Called after the completion of an update
         *
         * @param oldVersion The version number that was previously installed. Might be {@code null} if the version number is unknown.
         * @param oldFile    The {@code File} where the old version was saved. Be careful, the file might be already deleted from the disc. Might be {@code null} if the old file is not known.
         */
        @SuppressWarnings({"unused"})
        void run(@Nullable Version oldVersion, @Nullable File oldFile);
    }

}
