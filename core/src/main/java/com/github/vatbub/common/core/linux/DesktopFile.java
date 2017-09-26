package com.github.vatbub.common.core.linux;

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

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;

/**
 * Can create GNOME/KDE-desktop files which register an application in the main
 * menu. See the official
 * <a href="https://developer.gnome.org/desktop-entry-spec/">docs</a> and
 * <a href=
 * "https://developer.gnome.org/integration-guide/stable/desktop-files.html.en">tutorials</a>.
 *
 * @author frede
 */
public class DesktopFile {

    public static final String specificationVersion = "1.0";

    // Required
    private DesktopFile.Type type;
    private String name;
    private URL url; // Only required for type==Link

    // optional
    private String genericName;
    private boolean noDisplay;
    private String comment;
    private String iconLocation;
    private boolean hidden;
    // Did not understand the docs of the following two so left them out
    // private List<String> onlyShowIn;
    // private List<String> notShowIn;
    private boolean dBusActivatable;
    private String tryExec;
    private String exec;
    private String path;
    private boolean terminal;
    private List<DesktopAction> actions;
    private List<String> mimeType;
    private List<String> categories;
    private List<String> implementsEntry;
    private List<String> keywords;
    private boolean startupNotify;
    private String startupWMClass;

    /**
     * Creates a new Link instance (but does not save it). <b>PLEASE NOTE</b>
     * that {@code type}, {@code name} and {@code url} must be specified using
     * {@link #setType(Type)}, {@link #setName(String)} and {@link #setUrl(URL)}
     * prior to saving this link.. Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     */
    @SuppressWarnings({"unused"})
    public DesktopFile() {
        this(null);
    }

    /**
     * Creates a new Link instance (but does not save it). <b>PLEASE NOTE</b>
     * that {@code name} and {@code url} must be specified using
     * {@link #setName(String)} and {@link #setUrl(URL)} prior to saving this
     * link.. Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type This specification defines 3 types of desktop entries:
     *             {@code Application} (type 1), {@code Link} (type 2) and
     *             {@code Directory} (type 3).
     */
    public DesktopFile(DesktopFile.Type type) {
        this(type, null);
    }

    /**
     * Creates a new Link instance (but does not save it). <b>PLEASE NOTE</b>
     * that {@code url} must be specified using {@link #setUrl(URL)} prior to
     * saving this link.. Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type This specification defines 3 types of desktop entries:
     *             {@code Application} (type 1), {@code Link} (type 2) and
     *             {@code Directory} (type 3).
     * @param name Specific name of the application, for example {@code Mozilla}.
     */
    public DesktopFile(DesktopFile.Type type, String name) {
        this(type, name, null);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type This specification defines 3 types of desktop entries:
     *             {@code Application} (type 1), {@code Link} (type 2) and
     *             {@code Directory} (type 3).
     * @param name Specific name of the application, for example {@code Mozilla}.
     * @param url  If entry is Link type, the URL to access.
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url) {
        this(type, name, url, null);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type        This specification defines 3 types of desktop entries:
     *                    {@code Application} (type 1), {@code Link} (type 2) and
     *                    {@code Directory} (type 3).
     * @param name        Specific name of the application, for example {@code Mozilla}.
     * @param url         If entry is Link type, the URL to access.
     * @param genericName Generic name of the application, for example
     *                    {@code Web Browser}.
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url, String genericName) {
        this(type, name, url, genericName, false);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type        This specification defines 3 types of desktop entries:
     *                    {@code Application} (type 1), {@code Link} (type 2) and
     *                    {@code Directory} (type 3).
     * @param name        Specific name of the application, for example {@code Mozilla}.
     * @param url         If entry is Link type, the URL to access.
     * @param genericName Generic name of the application, for example
     *                    {@code Web Browser}.
     * @param noDisplay   {@code noDisplay} means <i>"this application exists, but don't
     *                    display it in the menus"</i>. This can be useful to e.g.
     *                    associate this application with MIME types, so that it gets
     *                    launched from a file manager (or other apps), without having a
     *                    menu entry for it (there are tons of good reasons for this,
     *                    including e.g. the {@code netscape}
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url, String genericName, boolean noDisplay) {
        this(type, name, url, genericName, noDisplay, null);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type        This specification defines 3 types of desktop entries:
     *                    {@code Application} (type 1), {@code Link} (type 2) and
     *                    {@code Directory} (type 3).
     * @param name        Specific name of the application, for example {@code Mozilla}.
     * @param url         If entry is Link type, the URL to access.
     * @param genericName Generic name of the application, for example
     *                    {@code Web Browser}.
     * @param noDisplay   {@code noDisplay} means <i>"this application exists, but don't
     *                    display it in the menus"</i>. This can be useful to e.g.
     *                    associate this application with MIME types, so that it gets
     *                    launched from a file manager (or other apps), without having a
     *                    menu entry for it (there are tons of good reasons for this,
     *                    including e.g. the {@code netscape}
     * @param comment     Tooltip for the entry, for example <i>"View sites on the
     *                    Internet"</i>. The value should not be redundant with the
     *                    values of {@code name} and {@code genericName}.
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url, String genericName, boolean noDisplay,
                       String comment) {
        this(type, name, url, genericName, noDisplay, comment, null);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type         This specification defines 3 types of desktop entries:
     *                     {@code Application} (type 1), {@code Link} (type 2) and
     *                     {@code Directory} (type 3).
     * @param name         Specific name of the application, for example {@code Mozilla}.
     * @param url          If entry is Link type, the URL to access.
     * @param genericName  Generic name of the application, for example
     *                     {@code Web Browser}.
     * @param noDisplay    {@code noDisplay} means <i>"this application exists, but don't
     *                     display it in the menus"</i>. This can be useful to e.g.
     *                     associate this application with MIME types, so that it gets
     *                     launched from a file manager (or other apps), without having a
     *                     menu entry for it (there are tons of good reasons for this,
     *                     including e.g. the {@code netscape}
     * @param comment      Tooltip for the entry, for example <i>"View sites on the
     *                     Internet"</i>. The value should not be redundant with the
     *                     values of {@code name} and {@code genericName}.
     * @param iconLocation Icon to display in file manager, menus, etc. If the name is an
     *                     absolute path, the given file will be used. If the name is not
     *                     an absolute path, the algorithm described in the <a href=
     *                     "http://freedesktop.org/wiki/Standards/icon-theme-spec">Icon
     *                     Theme Specification</a> will be used to locate the icon.
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url, String genericName, boolean noDisplay,
                       String comment, String iconLocation) {
        this(type, name, url, genericName, noDisplay, comment, iconLocation, false);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type         This specification defines 3 types of desktop entries:
     *                     {@code Application} (type 1), {@code Link} (type 2) and
     *                     {@code Directory} (type 3).
     * @param name         Specific name of the application, for example {@code Mozilla}.
     * @param url          If entry is Link type, the URL to access.
     * @param genericName  Generic name of the application, for example
     *                     {@code Web Browser}.
     * @param noDisplay    {@code noDisplay} means <i>"this application exists, but don't
     *                     display it in the menus"</i>. This can be useful to e.g.
     *                     associate this application with MIME types, so that it gets
     *                     launched from a file manager (or other apps), without having a
     *                     menu entry for it (there are tons of good reasons for this,
     *                     including e.g. the {@code netscape}
     * @param comment      Tooltip for the entry, for example <i>"View sites on the
     *                     Internet"</i>. The value should not be redundant with the
     *                     values of {@code name} and {@code genericName}.
     * @param iconLocation Icon to display in file manager, menus, etc. If the name is an
     *                     absolute path, the given file will be used. If the name is not
     *                     an absolute path, the algorithm described in the <a href=
     *                     "http://freedesktop.org/wiki/Standards/icon-theme-spec">Icon
     *                     Theme Specification</a> will be used to locate the icon.
     * @param hidden       {@code hidden} should have been called {@code deleted}. It
     *                     means the user deleted (at his level) something that was
     *                     present (at an upper level, e.g. in the system dirs). It's
     *                     strictly equivalent to the {@code .desktop} file not existing
     *                     at all, as far as that user is concerned. This can also be
     *                     used to "uninstall" existing files (e.g. due to a renaming) -
     *                     by letting {@code make install} install a file with
     *                     {@code Hidden=true} in it.
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url, String genericName, boolean noDisplay,
                       String comment, String iconLocation, boolean hidden) {
        this(type, name, url, genericName, noDisplay, comment, iconLocation, hidden, false);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type            This specification defines 3 types of desktop entries:
     *                        {@code Application} (type 1), {@code Link} (type 2) and
     *                        {@code Directory} (type 3).
     * @param name            Specific name of the application, for example {@code Mozilla}.
     * @param url             If entry is Link type, the URL to access.
     * @param genericName     Generic name of the application, for example
     *                        {@code Web Browser}.
     * @param noDisplay       {@code noDisplay} means <i>"this application exists, but don't
     *                        display it in the menus"</i>. This can be useful to e.g.
     *                        associate this application with MIME types, so that it gets
     *                        launched from a file manager (or other apps), without having a
     *                        menu entry for it (there are tons of good reasons for this,
     *                        including e.g. the {@code netscape}
     * @param comment         Tooltip for the entry, for example <i>"View sites on the
     *                        Internet"</i>. The value should not be redundant with the
     *                        values of {@code name} and {@code genericName}.
     * @param iconLocation    Icon to display in file manager, menus, etc. If the name is an
     *                        absolute path, the given file will be used. If the name is not
     *                        an absolute path, the algorithm described in the <a href=
     *                        "http://freedesktop.org/wiki/Standards/icon-theme-spec">Icon
     *                        Theme Specification</a> will be used to locate the icon.
     * @param hidden          {@code hidden} should have been called {@code deleted}. It
     *                        means the user deleted (at his level) something that was
     *                        present (at an upper level, e.g. in the system dirs). It's
     *                        strictly equivalent to the {@code .desktop} file not existing
     *                        at all, as far as that user is concerned. This can also be
     *                        used to "uninstall" existing files (e.g. due to a renaming) -
     *                        by letting {@code make install} install a file with
     *                        {@code Hidden=true} in it.
     * @param dBusActivatable A boolean value specifying if D-Bus activation is supported
     *                        for this application. If this key is missing, the default
     *                        value is {@code false}. If the value is {@code true} then
     *                        implementations should ignore the Exec key and send a D-Bus
     *                        message to launch the application. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#dbus">D-Bus
     *                        Activation</a> for more information on how this works.
     *                        Applications should still include Exec= lines in their desktop
     *                        files for compatibility with implementations that do not
     *                        understand the DBusActivatable key.
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url, String genericName, boolean noDisplay,
                       String comment, String iconLocation, boolean hidden, boolean dBusActivatable) {
        this(type, name, url, genericName, noDisplay, comment, iconLocation, hidden, dBusActivatable, null);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type            This specification defines 3 types of desktop entries:
     *                        {@code Application} (type 1), {@code Link} (type 2) and
     *                        {@code Directory} (type 3).
     * @param name            Specific name of the application, for example {@code Mozilla}.
     * @param url             If entry is Link type, the URL to access.
     * @param genericName     Generic name of the application, for example
     *                        {@code Web Browser}.
     * @param noDisplay       {@code noDisplay} means <i>"this application exists, but don't
     *                        display it in the menus"</i>. This can be useful to e.g.
     *                        associate this application with MIME types, so that it gets
     *                        launched from a file manager (or other apps), without having a
     *                        menu entry for it (there are tons of good reasons for this,
     *                        including e.g. the {@code netscape}
     * @param comment         Tooltip for the entry, for example <i>"View sites on the
     *                        Internet"</i>. The value should not be redundant with the
     *                        values of {@code name} and {@code genericName}.
     * @param iconLocation    Icon to display in file manager, menus, etc. If the name is an
     *                        absolute path, the given file will be used. If the name is not
     *                        an absolute path, the algorithm described in the <a href=
     *                        "http://freedesktop.org/wiki/Standards/icon-theme-spec">Icon
     *                        Theme Specification</a> will be used to locate the icon.
     * @param hidden          {@code hidden} should have been called {@code deleted}. It
     *                        means the user deleted (at his level) something that was
     *                        present (at an upper level, e.g. in the system dirs). It's
     *                        strictly equivalent to the {@code .desktop} file not existing
     *                        at all, as far as that user is concerned. This can also be
     *                        used to "uninstall" existing files (e.g. due to a renaming) -
     *                        by letting {@code make install} install a file with
     *                        {@code Hidden=true} in it.
     * @param dBusActivatable A boolean value specifying if D-Bus activation is supported
     *                        for this application. If this key is missing, the default
     *                        value is {@code false}. If the value is {@code true} then
     *                        implementations should ignore the Exec key and send a D-Bus
     *                        message to launch the application. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#dbus">D-Bus
     *                        Activation</a> for more information on how this works.
     *                        Applications should still include Exec= lines in their desktop
     *                        files for compatibility with implementations that do not
     *                        understand the DBusActivatable key.
     * @param tryExec         Path to an executable file on disk used to determine if the
     *                        program is actually installed. If the path is not an absolute
     *                        path, the file is looked up in the {@code $PATH} environment
     *                        variable. If the file is not present or if it is not
     *                        executable, the entry may be ignored (not be used in menus,
     *                        for example).
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url, String genericName, boolean noDisplay,
                       String comment, String iconLocation, boolean hidden, boolean dBusActivatable, String tryExec) {
        this(type, name, url, genericName, noDisplay, comment, iconLocation, hidden, dBusActivatable, tryExec, null);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type            This specification defines 3 types of desktop entries:
     *                        {@code Application} (type 1), {@code Link} (type 2) and
     *                        {@code Directory} (type 3).
     * @param name            Specific name of the application, for example {@code Mozilla}.
     * @param url             If entry is Link type, the URL to access.
     * @param genericName     Generic name of the application, for example
     *                        {@code Web Browser}.
     * @param noDisplay       {@code noDisplay} means <i>"this application exists, but don't
     *                        display it in the menus"</i>. This can be useful to e.g.
     *                        associate this application with MIME types, so that it gets
     *                        launched from a file manager (or other apps), without having a
     *                        menu entry for it (there are tons of good reasons for this,
     *                        including e.g. the {@code netscape}
     * @param comment         Tooltip for the entry, for example <i>"View sites on the
     *                        Internet"</i>. The value should not be redundant with the
     *                        values of {@code name} and {@code genericName}.
     * @param iconLocation    Icon to display in file manager, menus, etc. If the name is an
     *                        absolute path, the given file will be used. If the name is not
     *                        an absolute path, the algorithm described in the <a href=
     *                        "http://freedesktop.org/wiki/Standards/icon-theme-spec">Icon
     *                        Theme Specification</a> will be used to locate the icon.
     * @param hidden          {@code hidden} should have been called {@code deleted}. It
     *                        means the user deleted (at his level) something that was
     *                        present (at an upper level, e.g. in the system dirs). It's
     *                        strictly equivalent to the {@code .desktop} file not existing
     *                        at all, as far as that user is concerned. This can also be
     *                        used to "uninstall" existing files (e.g. due to a renaming) -
     *                        by letting {@code make install} install a file with
     *                        {@code Hidden=true} in it.
     * @param dBusActivatable A boolean value specifying if D-Bus activation is supported
     *                        for this application. If this key is missing, the default
     *                        value is {@code false}. If the value is {@code true} then
     *                        implementations should ignore the Exec key and send a D-Bus
     *                        message to launch the application. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#dbus">D-Bus
     *                        Activation</a> for more information on how this works.
     *                        Applications should still include Exec= lines in their desktop
     *                        files for compatibility with implementations that do not
     *                        understand the DBusActivatable key.
     * @param tryExec         Path to an executable file on disk used to determine if the
     *                        program is actually installed. If the path is not an absolute
     *                        path, the file is looked up in the {@code $PATH} environment
     *                        variable. If the file is not present or if it is not
     *                        executable, the entry may be ignored (not be used in menus,
     *                        for example).
     * @param exec            Program to execute, possibly with arguments. See the <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#exec-variables">Exec
     *                        key</a> for details on how this key works. The {@code Exec}
     *                        key is required if {@code DBusActivatable} is not set to
     *                        {@code true}. Even if {@code DBusActivatable} is {@code true},
     *                        {@code Exec} should be specified for compatibility with
     *                        implementations that do not understand
     *                        {@code DBusActivatable}.
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url, String genericName, boolean noDisplay,
                       String comment, String iconLocation, boolean hidden, boolean dBusActivatable, String tryExec, String exec) {
        this(type, name, url, genericName, noDisplay, comment, iconLocation, hidden, dBusActivatable, tryExec, exec,
                null);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type            This specification defines 3 types of desktop entries:
     *                        {@code Application} (type 1), {@code Link} (type 2) and
     *                        {@code Directory} (type 3).
     * @param name            Specific name of the application, for example {@code Mozilla}.
     * @param url             If entry is Link type, the URL to access.
     * @param genericName     Generic name of the application, for example
     *                        {@code Web Browser}.
     * @param noDisplay       {@code noDisplay} means <i>"this application exists, but don't
     *                        display it in the menus"</i>. This can be useful to e.g.
     *                        associate this application with MIME types, so that it gets
     *                        launched from a file manager (or other apps), without having a
     *                        menu entry for it (there are tons of good reasons for this,
     *                        including e.g. the {@code netscape}
     * @param comment         Tooltip for the entry, for example <i>"View sites on the
     *                        Internet"</i>. The value should not be redundant with the
     *                        values of {@code name} and {@code genericName}.
     * @param iconLocation    Icon to display in file manager, menus, etc. If the name is an
     *                        absolute path, the given file will be used. If the name is not
     *                        an absolute path, the algorithm described in the <a href=
     *                        "http://freedesktop.org/wiki/Standards/icon-theme-spec">Icon
     *                        Theme Specification</a> will be used to locate the icon.
     * @param hidden          {@code hidden} should have been called {@code deleted}. It
     *                        means the user deleted (at his level) something that was
     *                        present (at an upper level, e.g. in the system dirs). It's
     *                        strictly equivalent to the {@code .desktop} file not existing
     *                        at all, as far as that user is concerned. This can also be
     *                        used to "uninstall" existing files (e.g. due to a renaming) -
     *                        by letting {@code make install} install a file with
     *                        {@code Hidden=true} in it.
     * @param dBusActivatable A boolean value specifying if D-Bus activation is supported
     *                        for this application. If this key is missing, the default
     *                        value is {@code false}. If the value is {@code true} then
     *                        implementations should ignore the Exec key and send a D-Bus
     *                        message to launch the application. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#dbus">D-Bus
     *                        Activation</a> for more information on how this works.
     *                        Applications should still include Exec= lines in their desktop
     *                        files for compatibility with implementations that do not
     *                        understand the DBusActivatable key.
     * @param tryExec         Path to an executable file on disk used to determine if the
     *                        program is actually installed. If the path is not an absolute
     *                        path, the file is looked up in the {@code $PATH} environment
     *                        variable. If the file is not present or if it is not
     *                        executable, the entry may be ignored (not be used in menus,
     *                        for example).
     * @param exec            Program to execute, possibly with arguments. See the <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#exec-variables">Exec
     *                        key</a> for details on how this key works. The {@code Exec}
     *                        key is required if {@code DBusActivatable} is not set to
     *                        {@code true}. Even if {@code DBusActivatable} is {@code true},
     *                        {@code Exec} should be specified for compatibility with
     *                        implementations that do not understand
     *                        {@code DBusActivatable}.
     * @param path            If entry is of type {@code Application}, the working directory
     *                        to run the program in.
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url, String genericName, boolean noDisplay,
                       String comment, String iconLocation, boolean hidden, boolean dBusActivatable, String tryExec, String exec,
                       String path) {
        this(type, name, url, genericName, noDisplay, comment, iconLocation, hidden, dBusActivatable, tryExec, exec,
                path, false);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type            This specification defines 3 types of desktop entries:
     *                        {@code Application} (type 1), {@code Link} (type 2) and
     *                        {@code Directory} (type 3).
     * @param name            Specific name of the application, for example {@code Mozilla}.
     * @param url             If entry is Link type, the URL to access.
     * @param genericName     Generic name of the application, for example
     *                        {@code Web Browser}.
     * @param noDisplay       {@code noDisplay} means <i>"this application exists, but don't
     *                        display it in the menus"</i>. This can be useful to e.g.
     *                        associate this application with MIME types, so that it gets
     *                        launched from a file manager (or other apps), without having a
     *                        menu entry for it (there are tons of good reasons for this,
     *                        including e.g. the {@code netscape}
     * @param comment         Tooltip for the entry, for example <i>"View sites on the
     *                        Internet"</i>. The value should not be redundant with the
     *                        values of {@code name} and {@code genericName}.
     * @param iconLocation    Icon to display in file manager, menus, etc. If the name is an
     *                        absolute path, the given file will be used. If the name is not
     *                        an absolute path, the algorithm described in the <a href=
     *                        "http://freedesktop.org/wiki/Standards/icon-theme-spec">Icon
     *                        Theme Specification</a> will be used to locate the icon.
     * @param hidden          {@code hidden} should have been called {@code deleted}. It
     *                        means the user deleted (at his level) something that was
     *                        present (at an upper level, e.g. in the system dirs). It's
     *                        strictly equivalent to the {@code .desktop} file not existing
     *                        at all, as far as that user is concerned. This can also be
     *                        used to "uninstall" existing files (e.g. due to a renaming) -
     *                        by letting {@code make install} install a file with
     *                        {@code Hidden=true} in it.
     * @param dBusActivatable A boolean value specifying if D-Bus activation is supported
     *                        for this application. If this key is missing, the default
     *                        value is {@code false}. If the value is {@code true} then
     *                        implementations should ignore the Exec key and send a D-Bus
     *                        message to launch the application. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#dbus">D-Bus
     *                        Activation</a> for more information on how this works.
     *                        Applications should still include Exec= lines in their desktop
     *                        files for compatibility with implementations that do not
     *                        understand the DBusActivatable key.
     * @param tryExec         Path to an executable file on disk used to determine if the
     *                        program is actually installed. If the path is not an absolute
     *                        path, the file is looked up in the {@code $PATH} environment
     *                        variable. If the file is not present or if it is not
     *                        executable, the entry may be ignored (not be used in menus,
     *                        for example).
     * @param exec            Program to execute, possibly with arguments. See the <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#exec-variables">Exec
     *                        key</a> for details on how this key works. The {@code Exec}
     *                        key is required if {@code DBusActivatable} is not set to
     *                        {@code true}. Even if {@code DBusActivatable} is {@code true},
     *                        {@code Exec} should be specified for compatibility with
     *                        implementations that do not understand
     *                        {@code DBusActivatable}.
     * @param path            If entry is of type {@code Application}, the working directory
     *                        to run the program in.
     * @param terminal        Whether the program runs in a terminal window.
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url, String genericName, boolean noDisplay,
                       String comment, String iconLocation, boolean hidden, boolean dBusActivatable, String tryExec, String exec,
                       String path, boolean terminal) {
        this(type, name, url, genericName, noDisplay, comment, iconLocation, hidden, dBusActivatable, tryExec, exec,
                path, terminal, null);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type            This specification defines 3 types of desktop entries:
     *                        {@code Application} (type 1), {@code Link} (type 2) and
     *                        {@code Directory} (type 3).
     * @param name            Specific name of the application, for example {@code Mozilla}.
     * @param url             If entry is Link type, the URL to access.
     * @param genericName     Generic name of the application, for example
     *                        {@code Web Browser}.
     * @param noDisplay       {@code noDisplay} means <i>"this application exists, but don't
     *                        display it in the menus"</i>. This can be useful to e.g.
     *                        associate this application with MIME types, so that it gets
     *                        launched from a file manager (or other apps), without having a
     *                        menu entry for it (there are tons of good reasons for this,
     *                        including e.g. the {@code netscape}
     * @param comment         Tooltip for the entry, for example <i>"View sites on the
     *                        Internet"</i>. The value should not be redundant with the
     *                        values of {@code name} and {@code genericName}.
     * @param iconLocation    Icon to display in file manager, menus, etc. If the name is an
     *                        absolute path, the given file will be used. If the name is not
     *                        an absolute path, the algorithm described in the <a href=
     *                        "http://freedesktop.org/wiki/Standards/icon-theme-spec">Icon
     *                        Theme Specification</a> will be used to locate the icon.
     * @param hidden          {@code hidden} should have been called {@code deleted}. It
     *                        means the user deleted (at his level) something that was
     *                        present (at an upper level, e.g. in the system dirs). It's
     *                        strictly equivalent to the {@code .desktop} file not existing
     *                        at all, as far as that user is concerned. This can also be
     *                        used to "uninstall" existing files (e.g. due to a renaming) -
     *                        by letting {@code make install} install a file with
     *                        {@code Hidden=true} in it.
     * @param dBusActivatable A boolean value specifying if D-Bus activation is supported
     *                        for this application. If this key is missing, the default
     *                        value is {@code false}. If the value is {@code true} then
     *                        implementations should ignore the Exec key and send a D-Bus
     *                        message to launch the application. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#dbus">D-Bus
     *                        Activation</a> for more information on how this works.
     *                        Applications should still include Exec= lines in their desktop
     *                        files for compatibility with implementations that do not
     *                        understand the DBusActivatable key.
     * @param tryExec         Path to an executable file on disk used to determine if the
     *                        program is actually installed. If the path is not an absolute
     *                        path, the file is looked up in the {@code $PATH} environment
     *                        variable. If the file is not present or if it is not
     *                        executable, the entry may be ignored (not be used in menus,
     *                        for example).
     * @param exec            Program to execute, possibly with arguments. See the <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#exec-variables">Exec
     *                        key</a> for details on how this key works. The {@code Exec}
     *                        key is required if {@code DBusActivatable} is not set to
     *                        {@code true}. Even if {@code DBusActivatable} is {@code true},
     *                        {@code Exec} should be specified for compatibility with
     *                        implementations that do not understand
     *                        {@code DBusActivatable}.
     * @param path            If entry is of type {@code Application}, the working directory
     *                        to run the program in.
     * @param terminal        Whether the program runs in a terminal window.
     * @param actions         Identifiers for application actions. This can be used to tell
     *                        the application to make a specific action, different from the
     *                        default behavior. The <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#extra-actions">Application
     *                        actions</a> section describes how actions work.
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url, String genericName, boolean noDisplay,
                       String comment, String iconLocation, boolean hidden, boolean dBusActivatable, String tryExec, String exec,
                       String path, boolean terminal, List<DesktopAction> actions) {
        this(type, name, url, genericName, noDisplay, comment, iconLocation, hidden, dBusActivatable, tryExec, exec,
                path, terminal, actions, null);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type            This specification defines 3 types of desktop entries:
     *                        {@code Application} (type 1), {@code Link} (type 2) and
     *                        {@code Directory} (type 3).
     * @param name            Specific name of the application, for example {@code Mozilla}.
     * @param url             If entry is Link type, the URL to access.
     * @param genericName     Generic name of the application, for example
     *                        {@code Web Browser}.
     * @param noDisplay       {@code noDisplay} means <i>"this application exists, but don't
     *                        display it in the menus"</i>. This can be useful to e.g.
     *                        associate this application with MIME types, so that it gets
     *                        launched from a file manager (or other apps), without having a
     *                        menu entry for it (there are tons of good reasons for this,
     *                        including e.g. the {@code netscape}
     * @param comment         Tooltip for the entry, for example <i>"View sites on the
     *                        Internet"</i>. The value should not be redundant with the
     *                        values of {@code name} and {@code genericName}.
     * @param iconLocation    Icon to display in file manager, menus, etc. If the name is an
     *                        absolute path, the given file will be used. If the name is not
     *                        an absolute path, the algorithm described in the <a href=
     *                        "http://freedesktop.org/wiki/Standards/icon-theme-spec">Icon
     *                        Theme Specification</a> will be used to locate the icon.
     * @param hidden          {@code hidden} should have been called {@code deleted}. It
     *                        means the user deleted (at his level) something that was
     *                        present (at an upper level, e.g. in the system dirs). It's
     *                        strictly equivalent to the {@code .desktop} file not existing
     *                        at all, as far as that user is concerned. This can also be
     *                        used to "uninstall" existing files (e.g. due to a renaming) -
     *                        by letting {@code make install} install a file with
     *                        {@code Hidden=true} in it.
     * @param dBusActivatable A boolean value specifying if D-Bus activation is supported
     *                        for this application. If this key is missing, the default
     *                        value is {@code false}. If the value is {@code true} then
     *                        implementations should ignore the Exec key and send a D-Bus
     *                        message to launch the application. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#dbus">D-Bus
     *                        Activation</a> for more information on how this works.
     *                        Applications should still include Exec= lines in their desktop
     *                        files for compatibility with implementations that do not
     *                        understand the DBusActivatable key.
     * @param tryExec         Path to an executable file on disk used to determine if the
     *                        program is actually installed. If the path is not an absolute
     *                        path, the file is looked up in the {@code $PATH} environment
     *                        variable. If the file is not present or if it is not
     *                        executable, the entry may be ignored (not be used in menus,
     *                        for example).
     * @param exec            Program to execute, possibly with arguments. See the <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#exec-variables">Exec
     *                        key</a> for details on how this key works. The {@code Exec}
     *                        key is required if {@code DBusActivatable} is not set to
     *                        {@code true}. Even if {@code DBusActivatable} is {@code true},
     *                        {@code Exec} should be specified for compatibility with
     *                        implementations that do not understand
     *                        {@code DBusActivatable}.
     * @param path            If entry is of type {@code Application}, the working directory
     *                        to run the program in.
     * @param terminal        Whether the program runs in a terminal window.
     * @param actions         Identifiers for application actions. This can be used to tell
     *                        the application to make a specific action, different from the
     *                        default behavior. The <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#extra-actions">Application
     *                        actions</a> section describes how actions work.
     * @param mimeType        The MIME type(s) supported by this application.
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url, String genericName, boolean noDisplay,
                       String comment, String iconLocation, boolean hidden, boolean dBusActivatable, String tryExec, String exec,
                       String path, boolean terminal, List<DesktopAction> actions, List<String> mimeType) {
        this(type, name, url, genericName, noDisplay, comment, iconLocation, hidden, dBusActivatable, tryExec, exec,
                path, terminal, actions, mimeType, null);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type            This specification defines 3 types of desktop entries:
     *                        {@code Application} (type 1), {@code Link} (type 2) and
     *                        {@code Directory} (type 3).
     * @param name            Specific name of the application, for example {@code Mozilla}.
     * @param url             If entry is Link type, the URL to access.
     * @param genericName     Generic name of the application, for example
     *                        {@code Web Browser}.
     * @param noDisplay       {@code noDisplay} means <i>"this application exists, but don't
     *                        display it in the menus"</i>. This can be useful to e.g.
     *                        associate this application with MIME types, so that it gets
     *                        launched from a file manager (or other apps), without having a
     *                        menu entry for it (there are tons of good reasons for this,
     *                        including e.g. the {@code netscape}
     * @param comment         Tooltip for the entry, for example <i>"View sites on the
     *                        Internet"</i>. The value should not be redundant with the
     *                        values of {@code name} and {@code genericName}.
     * @param iconLocation    Icon to display in file manager, menus, etc. If the name is an
     *                        absolute path, the given file will be used. If the name is not
     *                        an absolute path, the algorithm described in the <a href=
     *                        "http://freedesktop.org/wiki/Standards/icon-theme-spec">Icon
     *                        Theme Specification</a> will be used to locate the icon.
     * @param hidden          {@code hidden} should have been called {@code deleted}. It
     *                        means the user deleted (at his level) something that was
     *                        present (at an upper level, e.g. in the system dirs). It's
     *                        strictly equivalent to the {@code .desktop} file not existing
     *                        at all, as far as that user is concerned. This can also be
     *                        used to "uninstall" existing files (e.g. due to a renaming) -
     *                        by letting {@code make install} install a file with
     *                        {@code Hidden=true} in it.
     * @param dBusActivatable A boolean value specifying if D-Bus activation is supported
     *                        for this application. If this key is missing, the default
     *                        value is {@code false}. If the value is {@code true} then
     *                        implementations should ignore the Exec key and send a D-Bus
     *                        message to launch the application. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#dbus">D-Bus
     *                        Activation</a> for more information on how this works.
     *                        Applications should still include Exec= lines in their desktop
     *                        files for compatibility with implementations that do not
     *                        understand the DBusActivatable key.
     * @param tryExec         Path to an executable file on disk used to determine if the
     *                        program is actually installed. If the path is not an absolute
     *                        path, the file is looked up in the {@code $PATH} environment
     *                        variable. If the file is not present or if it is not
     *                        executable, the entry may be ignored (not be used in menus,
     *                        for example).
     * @param exec            Program to execute, possibly with arguments. See the <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#exec-variables">Exec
     *                        key</a> for details on how this key works. The {@code Exec}
     *                        key is required if {@code DBusActivatable} is not set to
     *                        {@code true}. Even if {@code DBusActivatable} is {@code true},
     *                        {@code Exec} should be specified for compatibility with
     *                        implementations that do not understand
     *                        {@code DBusActivatable}.
     * @param path            If entry is of type {@code Application}, the working directory
     *                        to run the program in.
     * @param terminal        Whether the program runs in a terminal window.
     * @param actions         Identifiers for application actions. This can be used to tell
     *                        the application to make a specific action, different from the
     *                        default behavior. The <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#extra-actions">Application
     *                        actions</a> section describes how actions work.
     * @param mimeType        The MIME type(s) supported by this application.
     * @param categories      Categories in which the entry should be shown in a menu (for
     *                        possible values see the <a href=
     *                        "http://www.freedesktop.org/Standards/menu-spec">Desktop Menu
     *                        Specification</a>).
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url, String genericName, boolean noDisplay,
                       String comment, String iconLocation, boolean hidden, boolean dBusActivatable, String tryExec, String exec,
                       String path, boolean terminal, List<DesktopAction> actions, List<String> mimeType,
                       List<String> categories) {
        this(type, name, url, genericName, noDisplay, comment, iconLocation, hidden, dBusActivatable, tryExec, exec,
                path, terminal, actions, mimeType, categories, null);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type            This specification defines 3 types of desktop entries:
     *                        {@code Application} (type 1), {@code Link} (type 2) and
     *                        {@code Directory} (type 3).
     * @param name            Specific name of the application, for example {@code Mozilla}.
     * @param url             If entry is Link type, the URL to access.
     * @param genericName     Generic name of the application, for example
     *                        {@code Web Browser}.
     * @param noDisplay       {@code noDisplay} means <i>"this application exists, but don't
     *                        display it in the menus"</i>. This can be useful to e.g.
     *                        associate this application with MIME types, so that it gets
     *                        launched from a file manager (or other apps), without having a
     *                        menu entry for it (there are tons of good reasons for this,
     *                        including e.g. the {@code netscape}
     * @param comment         Tooltip for the entry, for example <i>"View sites on the
     *                        Internet"</i>. The value should not be redundant with the
     *                        values of {@code name} and {@code genericName}.
     * @param iconLocation    Icon to display in file manager, menus, etc. If the name is an
     *                        absolute path, the given file will be used. If the name is not
     *                        an absolute path, the algorithm described in the <a href=
     *                        "http://freedesktop.org/wiki/Standards/icon-theme-spec">Icon
     *                        Theme Specification</a> will be used to locate the icon.
     * @param hidden          {@code hidden} should have been called {@code deleted}. It
     *                        means the user deleted (at his level) something that was
     *                        present (at an upper level, e.g. in the system dirs). It's
     *                        strictly equivalent to the {@code .desktop} file not existing
     *                        at all, as far as that user is concerned. This can also be
     *                        used to "uninstall" existing files (e.g. due to a renaming) -
     *                        by letting {@code make install} install a file with
     *                        {@code Hidden=true} in it.
     * @param dBusActivatable A boolean value specifying if D-Bus activation is supported
     *                        for this application. If this key is missing, the default
     *                        value is {@code false}. If the value is {@code true} then
     *                        implementations should ignore the Exec key and send a D-Bus
     *                        message to launch the application. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#dbus">D-Bus
     *                        Activation</a> for more information on how this works.
     *                        Applications should still include Exec= lines in their desktop
     *                        files for compatibility with implementations that do not
     *                        understand the DBusActivatable key.
     * @param tryExec         Path to an executable file on disk used to determine if the
     *                        program is actually installed. If the path is not an absolute
     *                        path, the file is looked up in the {@code $PATH} environment
     *                        variable. If the file is not present or if it is not
     *                        executable, the entry may be ignored (not be used in menus,
     *                        for example).
     * @param exec            Program to execute, possibly with arguments. See the <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#exec-variables">Exec
     *                        key</a> for details on how this key works. The {@code Exec}
     *                        key is required if {@code DBusActivatable} is not set to
     *                        {@code true}. Even if {@code DBusActivatable} is {@code true},
     *                        {@code Exec} should be specified for compatibility with
     *                        implementations that do not understand
     *                        {@code DBusActivatable}.
     * @param path            If entry is of type {@code Application}, the working directory
     *                        to run the program in.
     * @param terminal        Whether the program runs in a terminal window.
     * @param actions         Identifiers for application actions. This can be used to tell
     *                        the application to make a specific action, different from the
     *                        default behavior. The <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#extra-actions">Application
     *                        actions</a> section describes how actions work.
     * @param mimeType        The MIME type(s) supported by this application.
     * @param categories      Categories in which the entry should be shown in a menu (for
     *                        possible values see the <a href=
     *                        "http://www.freedesktop.org/Standards/menu-spec">Desktop Menu
     *                        Specification</a>).
     * @param implementsEntry A list of interfaces that this application implements. By
     *                        default, a desktop file implements no interfaces. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#interfaces">Interfaces</a>
     *                        for more information on how this works.<br>
     *                        <br>
     *                        <b>NOTE:</b> This is entry named {@code Implementation} in the
     *                        actual {@code *.desktop}-file but {@code implements} is
     *                        already a reserved java keyword which is why we had to rename
     *                        it.
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url, String genericName, boolean noDisplay,
                       String comment, String iconLocation, boolean hidden, boolean dBusActivatable, String tryExec, String exec,
                       String path, boolean terminal, List<DesktopAction> actions, List<String> mimeType, List<String> categories,
                       List<String> implementsEntry) {
        this(type, name, url, genericName, noDisplay, comment, iconLocation, hidden, dBusActivatable, tryExec, exec,
                path, terminal, actions, mimeType, categories, implementsEntry, null);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type            This specification defines 3 types of desktop entries:
     *                        {@code Application} (type 1), {@code Link} (type 2) and
     *                        {@code Directory} (type 3).
     * @param name            Specific name of the application, for example {@code Mozilla}.
     * @param url             If entry is Link type, the URL to access.
     * @param genericName     Generic name of the application, for example
     *                        {@code Web Browser}.
     * @param noDisplay       {@code noDisplay} means <i>"this application exists, but don't
     *                        display it in the menus"</i>. This can be useful to e.g.
     *                        associate this application with MIME types, so that it gets
     *                        launched from a file manager (or other apps), without having a
     *                        menu entry for it (there are tons of good reasons for this,
     *                        including e.g. the {@code netscape}
     * @param comment         Tooltip for the entry, for example <i>"View sites on the
     *                        Internet"</i>. The value should not be redundant with the
     *                        values of {@code name} and {@code genericName}.
     * @param iconLocation    Icon to display in file manager, menus, etc. If the name is an
     *                        absolute path, the given file will be used. If the name is not
     *                        an absolute path, the algorithm described in the <a href=
     *                        "http://freedesktop.org/wiki/Standards/icon-theme-spec">Icon
     *                        Theme Specification</a> will be used to locate the icon.
     * @param hidden          {@code hidden} should have been called {@code deleted}. It
     *                        means the user deleted (at his level) something that was
     *                        present (at an upper level, e.g. in the system dirs). It's
     *                        strictly equivalent to the {@code .desktop} file not existing
     *                        at all, as far as that user is concerned. This can also be
     *                        used to "uninstall" existing files (e.g. due to a renaming) -
     *                        by letting {@code make install} install a file with
     *                        {@code Hidden=true} in it.
     * @param dBusActivatable A boolean value specifying if D-Bus activation is supported
     *                        for this application. If this key is missing, the default
     *                        value is {@code false}. If the value is {@code true} then
     *                        implementations should ignore the Exec key and send a D-Bus
     *                        message to launch the application. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#dbus">D-Bus
     *                        Activation</a> for more information on how this works.
     *                        Applications should still include Exec= lines in their desktop
     *                        files for compatibility with implementations that do not
     *                        understand the DBusActivatable key.
     * @param tryExec         Path to an executable file on disk used to determine if the
     *                        program is actually installed. If the path is not an absolute
     *                        path, the file is looked up in the {@code $PATH} environment
     *                        variable. If the file is not present or if it is not
     *                        executable, the entry may be ignored (not be used in menus,
     *                        for example).
     * @param exec            Program to execute, possibly with arguments. See the <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#exec-variables">Exec
     *                        key</a> for details on how this key works. The {@code Exec}
     *                        key is required if {@code DBusActivatable} is not set to
     *                        {@code true}. Even if {@code DBusActivatable} is {@code true},
     *                        {@code Exec} should be specified for compatibility with
     *                        implementations that do not understand
     *                        {@code DBusActivatable}.
     * @param path            If entry is of type {@code Application}, the working directory
     *                        to run the program in.
     * @param terminal        Whether the program runs in a terminal window.
     * @param actions         Identifiers for application actions. This can be used to tell
     *                        the application to make a specific action, different from the
     *                        default behavior. The <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#extra-actions">Application
     *                        actions</a> section describes how actions work.
     * @param mimeType        The MIME type(s) supported by this application.
     * @param categories      Categories in which the entry should be shown in a menu (for
     *                        possible values see the <a href=
     *                        "http://www.freedesktop.org/Standards/menu-spec">Desktop Menu
     *                        Specification</a>).
     * @param implementsEntry A list of interfaces that this application implements. By
     *                        default, a desktop file implements no interfaces. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#interfaces">Interfaces</a>
     *                        for more information on how this works.<br>
     *                        <br>
     *                        <b>NOTE:</b> This is entry named {@code Implementation} in the
     *                        actual {@code *.desktop}-file but {@code implements} is
     *                        already a reserved java keyword which is why we had to rename
     *                        it.
     * @param keywords        A list of strings which may be used in addition to other
     *                        metadata to describe this entry. This can be useful e.g. to
     *                        facilitate searching through entries. The values are not meant
     *                        for display, and should not be redundant with the values of
     *                        {@code Name} or {@code GenericName}.
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url, String genericName, boolean noDisplay,
                       String comment, String iconLocation, boolean hidden, boolean dBusActivatable, String tryExec, String exec,
                       String path, boolean terminal, List<DesktopAction> actions, List<String> mimeType, List<String> categories,
                       List<String> implementsEntry, List<String> keywords) {
        this(type, name, url, genericName, noDisplay, comment, iconLocation, hidden, dBusActivatable, tryExec, exec,
                path, terminal, actions, mimeType, categories, implementsEntry, keywords, false);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type            This specification defines 3 types of desktop entries:
     *                        {@code Application} (type 1), {@code Link} (type 2) and
     *                        {@code Directory} (type 3).
     * @param name            Specific name of the application, for example {@code Mozilla}.
     * @param url             If entry is Link type, the URL to access.
     * @param genericName     Generic name of the application, for example
     *                        {@code Web Browser}.
     * @param noDisplay       {@code noDisplay} means <i>"this application exists, but don't
     *                        display it in the menus"</i>. This can be useful to e.g.
     *                        associate this application with MIME types, so that it gets
     *                        launched from a file manager (or other apps), without having a
     *                        menu entry for it (there are tons of good reasons for this,
     *                        including e.g. the {@code netscape}
     * @param comment         Tooltip for the entry, for example <i>"View sites on the
     *                        Internet"</i>. The value should not be redundant with the
     *                        values of {@code name} and {@code genericName}.
     * @param iconLocation    Icon to display in file manager, menus, etc. If the name is an
     *                        absolute path, the given file will be used. If the name is not
     *                        an absolute path, the algorithm described in the <a href=
     *                        "http://freedesktop.org/wiki/Standards/icon-theme-spec">Icon
     *                        Theme Specification</a> will be used to locate the icon.
     * @param hidden          {@code hidden} should have been called {@code deleted}. It
     *                        means the user deleted (at his level) something that was
     *                        present (at an upper level, e.g. in the system dirs). It's
     *                        strictly equivalent to the {@code .desktop} file not existing
     *                        at all, as far as that user is concerned. This can also be
     *                        used to "uninstall" existing files (e.g. due to a renaming) -
     *                        by letting {@code make install} install a file with
     *                        {@code Hidden=true} in it.
     * @param dBusActivatable A boolean value specifying if D-Bus activation is supported
     *                        for this application. If this key is missing, the default
     *                        value is {@code false}. If the value is {@code true} then
     *                        implementations should ignore the Exec key and send a D-Bus
     *                        message to launch the application. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#dbus">D-Bus
     *                        Activation</a> for more information on how this works.
     *                        Applications should still include Exec= lines in their desktop
     *                        files for compatibility with implementations that do not
     *                        understand the DBusActivatable key.
     * @param tryExec         Path to an executable file on disk used to determine if the
     *                        program is actually installed. If the path is not an absolute
     *                        path, the file is looked up in the {@code $PATH} environment
     *                        variable. If the file is not present or if it is not
     *                        executable, the entry may be ignored (not be used in menus,
     *                        for example).
     * @param exec            Program to execute, possibly with arguments. See the <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#exec-variables">Exec
     *                        key</a> for details on how this key works. The {@code Exec}
     *                        key is required if {@code DBusActivatable} is not set to
     *                        {@code true}. Even if {@code DBusActivatable} is {@code true},
     *                        {@code Exec} should be specified for compatibility with
     *                        implementations that do not understand
     *                        {@code DBusActivatable}.
     * @param path            If entry is of type {@code Application}, the working directory
     *                        to run the program in.
     * @param terminal        Whether the program runs in a terminal window.
     * @param actions         Identifiers for application actions. This can be used to tell
     *                        the application to make a specific action, different from the
     *                        default behavior. The <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#extra-actions">Application
     *                        actions</a> section describes how actions work.
     * @param mimeType        The MIME type(s) supported by this application.
     * @param categories      Categories in which the entry should be shown in a menu (for
     *                        possible values see the <a href=
     *                        "http://www.freedesktop.org/Standards/menu-spec">Desktop Menu
     *                        Specification</a>).
     * @param implementsEntry A list of interfaces that this application implements. By
     *                        default, a desktop file implements no interfaces. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#interfaces">Interfaces</a>
     *                        for more information on how this works.<br>
     *                        <br>
     *                        <b>NOTE:</b> This is entry named {@code Implementation} in the
     *                        actual {@code *.desktop}-file but {@code implements} is
     *                        already a reserved java keyword which is why we had to rename
     *                        it.
     * @param keywords        A list of strings which may be used in addition to other
     *                        metadata to describe this entry. This can be useful e.g. to
     *                        facilitate searching through entries. The values are not meant
     *                        for display, and should not be redundant with the values of
     *                        {@code Name} or {@code GenericName}.
     * @param startupNotify   If {@code true}, it is <b><i>KNOWN</i></b> that the
     *                        application will send a "remove" message when started with the
     *                        DESKTOP_STARTUP_ID environment variable set. If {@code false},
     *                        it is KNOWN that the application does not work with startup
     *                        notification at all (does not shown any window, breaks even
     *                        when using StartupWMClass, etc.). If absent, a reasonable
     *                        handling is up to implementations (assuming false, using
     *                        StartupWMClass, etc.). (See the <a href=
     *                        "http://www.freedesktop.org/Standards/startup-notification-spec">Startup
     *                        Notification Protocol Specification</a> for more details).
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url, String genericName, boolean noDisplay,
                       String comment, String iconLocation, boolean hidden, boolean dBusActivatable, String tryExec, String exec,
                       String path, boolean terminal, List<DesktopAction> actions, List<String> mimeType, List<String> categories,
                       List<String> implementsEntry, List<String> keywords, boolean startupNotify) {
        this(type, name, url, genericName, noDisplay, comment, iconLocation, hidden, dBusActivatable, tryExec, exec,
                path, terminal, actions, mimeType, categories, implementsEntry, keywords, startupNotify, null);
    }

    /**
     * Creates a new Link instance (but does not save it). Docs taken from
     * <a href="https://developer.gnome.org/desktop-entry-spec/">here</a>
     *
     * @param type            This specification defines 3 types of desktop entries:
     *                        {@code Application} (type 1), {@code Link} (type 2) and
     *                        {@code Directory} (type 3).
     * @param name            Specific name of the application, for example {@code Mozilla}.
     * @param url             If entry is Link type, the URL to access.
     * @param genericName     Generic name of the application, for example
     *                        {@code Web Browser}.
     * @param noDisplay       {@code noDisplay} means <i>"this application exists, but don't
     *                        display it in the menus"</i>. This can be useful to e.g.
     *                        associate this application with MIME types, so that it gets
     *                        launched from a file manager (or other apps), without having a
     *                        menu entry for it (there are tons of good reasons for this,
     *                        including e.g. the {@code netscape}
     * @param comment         Tooltip for the entry, for example <i>"View sites on the
     *                        Internet"</i>. The value should not be redundant with the
     *                        values of {@code name} and {@code genericName}.
     * @param iconLocation    Icon to display in file manager, menus, etc. If the name is an
     *                        absolute path, the given file will be used. If the name is not
     *                        an absolute path, the algorithm described in the <a href=
     *                        "http://freedesktop.org/wiki/Standards/icon-theme-spec">Icon
     *                        Theme Specification</a> will be used to locate the icon.
     * @param hidden          {@code hidden} should have been called {@code deleted}. It
     *                        means the user deleted (at his level) something that was
     *                        present (at an upper level, e.g. in the system dirs). It's
     *                        strictly equivalent to the {@code .desktop} file not existing
     *                        at all, as far as that user is concerned. This can also be
     *                        used to "uninstall" existing files (e.g. due to a renaming) -
     *                        by letting {@code make install} install a file with
     *                        {@code Hidden=true} in it.
     * @param dBusActivatable A boolean value specifying if D-Bus activation is supported
     *                        for this application. If this key is missing, the default
     *                        value is {@code false}. If the value is {@code true} then
     *                        implementations should ignore the Exec key and send a D-Bus
     *                        message to launch the application. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#dbus">D-Bus
     *                        Activation</a> for more information on how this works.
     *                        Applications should still include Exec= lines in their desktop
     *                        files for compatibility with implementations that do not
     *                        understand the DBusActivatable key.
     * @param tryExec         Path to an executable file on disk used to determine if the
     *                        program is actually installed. If the path is not an absolute
     *                        path, the file is looked up in the {@code $PATH} environment
     *                        variable. If the file is not present or if it is not
     *                        executable, the entry may be ignored (not be used in menus,
     *                        for example).
     * @param exec            Program to execute, possibly with arguments. See the <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#exec-variables">Exec
     *                        key</a> for details on how this key works. The {@code Exec}
     *                        key is required if {@code DBusActivatable} is not set to
     *                        {@code true}. Even if {@code DBusActivatable} is {@code true},
     *                        {@code Exec} should be specified for compatibility with
     *                        implementations that do not understand
     *                        {@code DBusActivatable}.
     * @param path            If entry is of type {@code Application}, the working directory
     *                        to run the program in.
     * @param terminal        Whether the program runs in a terminal window.
     * @param actions         Identifiers for application actions. This can be used to tell
     *                        the application to make a specific action, different from the
     *                        default behavior. The <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#extra-actions">Application
     *                        actions</a> section describes how actions work.
     * @param mimeType        The MIME type(s) supported by this application.
     * @param categories      Categories in which the entry should be shown in a menu (for
     *                        possible values see the <a href=
     *                        "http://www.freedesktop.org/Standards/menu-spec">Desktop Menu
     *                        Specification</a>).
     * @param implementsEntry A list of interfaces that this application implements. By
     *                        default, a desktop file implements no interfaces. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#interfaces">Interfaces</a>
     *                        for more information on how this works.<br>
     *                        <br>
     *                        <b>NOTE:</b> This is entry named {@code Implementation} in the
     *                        actual {@code *.desktop}-file but {@code implements} is
     *                        already a reserved java keyword which is why we had to rename
     *                        it.
     * @param keywords        A list of strings which may be used in addition to other
     *                        metadata to describe this entry. This can be useful e.g. to
     *                        facilitate searching through entries. The values are not meant
     *                        for display, and should not be redundant with the values of
     *                        {@code Name} or {@code GenericName}.
     * @param startupNotify   If {@code true}, it is <b><i>KNOWN</i></b> that the
     *                        application will send a "remove" message when started with the
     *                        DESKTOP_STARTUP_ID environment variable set. If {@code false},
     *                        it is KNOWN that the application does not work with startup
     *                        notification at all (does not shown any window, breaks even
     *                        when using StartupWMClass, etc.). If absent, a reasonable
     *                        handling is up to implementations (assuming false, using
     *                        StartupWMClass, etc.). (See the <a href=
     *                        "http://www.freedesktop.org/Standards/startup-notification-spec">Startup
     *                        Notification Protocol Specification</a> for more details).
     * @param startupWMClass  If specified, it is known that the application will map at
     *                        least one window with the given string as its WM class or WM
     *                        name hint (see the <a href=
     *                        "http://www.freedesktop.org/Standards/startup-notification-spec">Startup
     *                        Notification Protocol Specification</a> for more details).
     */
    public DesktopFile(DesktopFile.Type type, String name, URL url, String genericName, boolean noDisplay,
                       String comment, String iconLocation, boolean hidden, boolean dBusActivatable, String tryExec, String exec,
                       String path, boolean terminal, List<DesktopAction> actions, List<String> mimeType, List<String> categories,
                       List<String> implementsEntry, List<String> keywords, boolean startupNotify, String startupWMClass) {
        setType(type);
        setName(name);
        setGenericName(genericName);
        setNoDisplay(noDisplay);
        setComment(comment);
        setIconLocation(iconLocation);
        setHidden(hidden);
        setdBusActivatable(dBusActivatable);
        setTryExec(tryExec);
        setExec(exec);
        setPath(path);
        setTerminal(terminal);
        setActions(actions);
        setMimeType(mimeType);
        setCategories(categories);
        setImplementsEntry(implementsEntry);
        setKeywords(keywords);
        setStartupNotify(startupNotify);
        setStartupWMClass(startupWMClass);
        setUrl(url);
    }

    /**
     * @return the type
     */
    public DesktopFile.Type getType() {
        return type;
    }

    /**
     * @param type This specification defines 3 types of desktop entries:
     *             {@code Application} (type 1), {@code Link} (type 2) and
     *             {@code Directory} (type 3).
     */
    public void setType(DesktopFile.Type type) {
        this.type = type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Specific name of the application, for example {@code Mozilla}.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the genericName
     */
    public String getGenericName() {
        return genericName;
    }

    /**
     * @param genericName Generic name of the application, for example
     *                    {@code Web Browser}.
     */
    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    /**
     * @return the noDisplay
     */
    public boolean isNoDisplay() {
        return noDisplay;
    }

    /**
     * @param noDisplay {@code noDisplay} means <i>"this application exists, but don't
     *                  display it in the menus"</i>. This can be useful to e.g.
     *                  associate this application with MIME types, so that it gets
     *                  launched from a file manager (or other apps), without having a
     *                  menu entry for it (there are tons of good reasons for this,
     *                  including e.g. the {@code netscape}
     */
    public void setNoDisplay(boolean noDisplay) {
        this.noDisplay = noDisplay;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment Tooltip for the entry, for example <i>"View sites on the
     *                Internet"</i>. The value should not be redundant with the
     *                values of {@code name} and {@code genericName}.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the iconLocation
     */
    public String getIconLocation() {
        return iconLocation;
    }

    /**
     * @param iconLocation Icon to display in file manager, menus, etc. If the name is an
     *                     absolute path, the given file will be used. If the name is not
     *                     an absolute path, the algorithm described in the <a href=
     *                     "http://freedesktop.org/wiki/Standards/icon-theme-spec">Icon
     *                     Theme Specification</a> will be used to locate the icon.
     */
    public void setIconLocation(String iconLocation) {
        this.iconLocation = iconLocation;
    }

    /**
     * @return the hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * @param hidden {@code hidden} should have been called {@code deleted}. It
     *               means the user deleted (at his level) something that was
     *               present (at an upper level, e.g. in the system dirs). It's
     *               strictly equivalent to the {@code .desktop} file not existing
     *               at all, as far as that user is concerned. This can also be
     *               used to "uninstall" existing files (e.g. due to a renaming) -
     *               by letting {@code make install} install a file with
     *               {@code Hidden=true} in it.
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * @return the dBusActivatable
     */
    public boolean isdBusActivatable() {
        return dBusActivatable;
    }

    /**
     * @param dBusActivatable A boolean value specifying if D-Bus activation is supported
     *                        for this application. If this key is missing, the default
     *                        value is {@code false}. If the value is {@code true} then
     *                        implementations should ignore the Exec key and send a D-Bus
     *                        message to launch the application. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#dbus">D-Bus
     *                        Activation</a> for more information on how this works.
     *                        Applications should still include Exec= lines in their desktop
     *                        files for compatibility with implementations that do not
     *                        understand the DBusActivatable key.
     */
    public void setdBusActivatable(boolean dBusActivatable) {
        this.dBusActivatable = dBusActivatable;
    }

    /**
     * @return the tryExec
     */
    public String getTryExec() {
        return tryExec;
    }

    /**
     * @param tryExec Path to an executable file on disk used to determine if the
     *                program is actually installed. If the path is not an absolute
     *                path, the file is looked up in the {@code $PATH} environment
     *                variable. If the file is not present or if it is not
     *                executable, the entry may be ignored (not be used in menus,
     *                for example).
     */
    public void setTryExec(String tryExec) {
        this.tryExec = tryExec;
    }

    /**
     * @return the exec
     */
    public String getExec() {
        return exec;
    }

    /**
     * @param exec Program to execute, possibly with arguments. See the <a href=
     *             "https://developer.gnome.org/desktop-entry-spec/#exec-variables">Exec
     *             key</a> for details on how this key works. The {@code Exec}
     *             key is required if {@code DBusActivatable} is not set to
     *             {@code true}. Even if {@code DBusActivatable} is {@code true},
     *             {@code Exec} should be specified for compatibility with
     *             implementations that do not understand
     *             {@code DBusActivatable}.
     */
    public void setExec(String exec) {
        this.exec = exec;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path If entry is of type {@code Application}, the working directory
     *             to run the program in.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the terminal
     */
    public boolean isTerminal() {
        return terminal;
    }

    /**
     * @param terminal Whether the program runs in a terminal window.
     */
    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    /**
     * @return the actions
     */
    public List<DesktopAction> getActions() {
        return actions;
    }

    /**
     * @param actions Identifiers for application actions. This can be used to tell
     *                the application to make a specific action, different from the
     *                default behavior. The <a href=
     *                "https://developer.gnome.org/desktop-entry-spec/#extra-actions">Application
     *                actions</a> section describes how actions work.
     */
    public void setActions(List<DesktopAction> actions) {
        this.actions = actions;
    }

    /**
     * @return The MIME type(s) supported by this application.
     */
    public List<String> getMimeType() {
        return mimeType;
    }

    /**
     * @param mimeType The MIME type(s) supported by this application.
     */
    public void setMimeType(List<String> mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * @return the categories
     */
    public List<String> getCategories() {
        return categories;
    }

    /**
     * @param categories Categories in which the entry should be shown in a menu (for
     *                   possible values see the <a href=
     *                   "http://www.freedesktop.org/Standards/menu-spec">Desktop Menu
     *                   Specification</a>).
     */
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    /**
     * @return the implementsEntry
     */
    public List<String> getImplementsEntry() {
        return implementsEntry;
    }

    /**
     * @param implementsEntry A list of interfaces that this application implements. By
     *                        default, a desktop file implements no interfaces. See <a href=
     *                        "https://developer.gnome.org/desktop-entry-spec/#interfaces">Interfaces</a>
     *                        for more information on how this works.<br>
     *                        <br>
     *                        <b>NOTE:</b> This is entry named {@code Implementation} in the
     *                        actual {@code *.desktop}-file but {@code implements} is
     *                        already a reserved java keyword which is why we had to rename
     *                        it.
     */
    public void setImplementsEntry(List<String> implementsEntry) {
        this.implementsEntry = implementsEntry;
    }

    /**
     * @return the keywords
     */
    public List<String> getKeywords() {
        return keywords;
    }

    /**
     * @param keywords A list of strings which may be used in addition to other
     *                 metadata to describe this entry. This can be useful e.g. to
     *                 facilitate searching through entries. The values are not meant
     *                 for display, and should not be redundant with the values of
     *                 {@code Name} or {@code GenericName}.
     */
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * @return the startupNotify
     */
    public boolean isStartupNotify() {
        return startupNotify;
    }

    /**
     * @param startupNotify If {@code true}, it is <b><i>KNOWN</i></b> that the
     *                      application will send a "remove" message when started with the
     *                      DESKTOP_STARTUP_ID environment variable set. If {@code false},
     *                      it is KNOWN that the application does not work with startup
     *                      notification at all (does not shown any window, breaks even
     *                      when using StartupWMClass, etc.). If absent, a reasonable
     *                      handling is up to implementations (assuming false, using
     *                      StartupWMClass, etc.). (See the <a href=
     *                      "http://www.freedesktop.org/Standards/startup-notification-spec">Startup
     *                      Notification Protocol Specification</a> for more details).
     */
    public void setStartupNotify(boolean startupNotify) {
        this.startupNotify = startupNotify;
    }

    /**
     * @return the startupWMClass
     */
    public String getStartupWMClass() {
        return startupWMClass;
    }

    /**
     * @param startupWMClass If specified, it is known that the application will map at
     *                       least one window with the given string as its WM class or WM
     *                       name hint (see the <a href=
     *                       "http://www.freedesktop.org/Standards/startup-notification-spec">Startup
     *                       Notification Protocol Specification</a> for more details).
     */
    public void setStartupWMClass(String startupWMClass) {
        this.startupWMClass = startupWMClass;
    }

    /**
     * @return If entry is Link type, the URL to access.
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @param url If entry is Link type, the URL to access.
     */
    public void setUrl(URL url) {
        if (this.getType() != Type.Link) {
            throw new IllegalStateException("This link is not of type Link (type is " + type.toString() + ")");
        }
        this.url = url;
    }

    /**
     * Checks if this link is valid to be saved
     *
     * @return {@code true} if this link is valid, {@code false} if not.
     */
    public boolean isValid() {
        if (name == null) {
            return false;
        }

        if (type == null || !type.isValid()) {
            return false;
        } else if (type == Type.Link) {
            if (url == null) {
                return false;
            }
        }

        // Check actions
        if (this.getActions() != null) {
            for (DesktopAction a : this.getActions()) {
                if (!a.isValid()) {
                    return false;
                }
            }
        }

        // Everything ok
        return true;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("[Desktop Entry]\n");

        if (this.getType() != null && this.getType().isValid()) {
            res.append("Type=").append(this.getType().toString()).append("\n");
        }

        res.append("Version=" + DesktopFile.specificationVersion + "\n");

        if (this.getName() != null) {
            res.append("Name=").append(this.getName()).append("\n");
        }

        if (this.getGenericName() != null) {
            res.append("GenericName=").append(this.getGenericName()).append("\n");
        }

        res.append("Name=").append(this.isNoDisplay()).append("\n");

        if (this.getComment() != null) {
            res.append("Comment=").append(this.getComment()).append("\n");
        }

        if (this.getIconLocation() != null) {
            res.append("Icon=").append(this.getIconLocation()).append("\n");
        }

        res.append("Hidden=").append(this.isHidden()).append("\n");

        res.append("DBusActivatable=").append(this.isdBusActivatable()).append("\n");

        if (this.getTryExec() != null) {
            res.append("TryExec=").append(this.getTryExec()).append("\n");
        }

        if (this.getExec() != null) {
            res.append("Exec=").append(this.getExec()).append("\n");
        }

        if (this.getPath() != null) {
            res.append("Path=").append(this.getPath()).append("\n");
        }

        res.append("Terminal=").append(this.isTerminal()).append("\n");

        if (this.getMimeType() != null) {
            StringBuilder t = new StringBuilder();
            for (String a : this.getMimeType()) {
                t.append(a).append(";");
            }

            res.append("MimeType=").append(t);
        }

        if (this.getCategories() != null) {
            StringBuilder t = new StringBuilder();
            for (String a : this.getCategories()) {
                t.append(a).append(";");
            }

            res.append("Categories=").append(t);
        }

        if (this.getImplementsEntry() != null) {
            StringBuilder t = new StringBuilder();
            for (String a : this.getImplementsEntry()) {
                t.append(a).append(";");
            }

            res.append("Implements=").append(t);
        }

        if (this.getKeywords() != null) {
            StringBuilder t = new StringBuilder();
            for (String a : this.getKeywords()) {
                t.append(a).append(";");
            }

            res.append("Keywords=").append(t);
        }

        if (this.getActions() != null) {
            StringBuilder t = new StringBuilder();
            for (DesktopAction a : this.getActions()) {
                t.append(a.getInternalName()).append(";");
            }

            res.append("Actions=").append(t);
        }

        res.append("StartupNotify=").append(this.isStartupNotify()).append("\n");

        if (this.getStartupWMClass() != null) {
            res.append("StartupWMClass=").append(this.getStartupWMClass()).append("\n");
        }

        if (this.getUrl() != null) {
            res.append("URL=").append(this.getUrl().toString()).append("\n");
        }

        if (this.getActions() != null) {
            // Add the actual actions
            for (DesktopAction a : this.getActions()) {
                res.append("\n\n");
                res.append(a.toString());
            }
        }

        return res.toString();
    }

    @SuppressWarnings("unused")
    public void saveForUser(String fileName) {
        this.save("~/.local/share/applications", fileName);
    }

    @SuppressWarnings("unused")
    public void saveSystemWide(String fileName) {
        this.save("/usr/share/applications", fileName);
    }

    private void save(String path, String fileName) {
        if (!this.isValid()) {
            throw new IllegalStateException("Link is not valid");
        }

        // Remove every file extension and replace it with .desktop
        fileName = fileName.substring(0, fileName.toLowerCase().lastIndexOf('.')) + ".desktop";
        System.out.println(fileName);

        // print the file
        try (PrintWriter out = new PrintWriter(path + File.separator + fileName)) {
            out.println(this.toString());
        } catch (Exception e) {
            FOKLogger.log(DesktopFile.class.getName(), Level.SEVERE, "Could not save the desktop file", e);
        }
    }

    public enum Type {
        Application, Link, Directory;

        public boolean isValid() {
            for (Type i : Type.values()) {
                if (i == this) {
                    return true;
                }
            }

            return false;
        }
    }

}
