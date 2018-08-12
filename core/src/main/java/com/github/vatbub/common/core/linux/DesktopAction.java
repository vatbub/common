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


public class DesktopAction {

    // Required
    private String internalName;
    private String name;

    // optional
    private String iconLocation;
    private String exec;

    /**
     *
     * @param internalName
     *            The name used internally in the {@link DesktopFile}.
     * @param name
     *            Label that will be shown to the user. Since actions are always
     *            shown in the context of a specific application (that is, as a
     *            submenu of a launcher), this only needs to be unambiguous
     *            within one application and should not include the application
     *            name.
     * @param iconLocation
     *            Icon to be shown together with the action. If the name is an
     *            absolute path, the given file will be used. If the name is not
     *            an absolute path, the algorithm described in the <a href=
     *            "http://freedesktop.org/wiki/Standards/icon-theme-spec">Icon
     *            Theme Specification</a> will be used to locate the icon.
     *            Implementations may choose to ignore it.
     * @param exec
     *            Program to execute for this action, possibly with arguments.
     *            See the <a href=
     *            "https://developer.gnome.org/desktop-entry-spec/#exec-variables">Exec
     *            key</a> for details on how this key works. The {@code Exec}
     *            key is required if {@code DBusActivatable} is not set to true
     *            in the main desktop entry group. Even if
     *            {@code DBusActivatable} is {@code true}, {@code Exec} should
     *            be specified for compatibility with implementations that do
     *            not understand {@code DBusActivatable}.
     */
    public DesktopAction(String internalName, String name, String iconLocation, String exec) {
        setInternalName(internalName);
        setName(name);
        setIconLocation(iconLocation);
        setExec(exec);
    }

    /**
     * @return the internalName
     */
    public String getInternalName() {
        return internalName;
    }

    /**
     * @param internalName
     *            the internalName to set
     */
    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the iconLocation
     */
    public String getIconLocation() {
        return iconLocation;
    }

    /**
     * @param iconLocation
     *            the iconLocation to set
     */
    public void setIconLocation(String iconLocation) {
        this.iconLocation = iconLocation;
    }

    /**
     * @return the exec
     */
    public String getExec() {
        return exec;
    }

    /**
     * @param exec
     *            the exec to set
     */
    public void setExec(String exec) {
        this.exec = exec;
    }

    public boolean isValid() {
        return this.getInternalName() != null && this.getName() != null;
    }

    @Override
    public String toString() {
        String res = "[Desktop Action " + this.getInternalName() + "]\n";

        if (this.getName() != null) {
            res = res + "Name=" + this.getName() + "\n";
        }

        if (this.getIconLocation() != null) {
            res = res + "Icon=" + this.getIconLocation() + "\n";
        }

        if (this.getExec() != null) {
            res = res + "Exec=" + this.getExec() + "\n";
        }

        return res;
    }

}
