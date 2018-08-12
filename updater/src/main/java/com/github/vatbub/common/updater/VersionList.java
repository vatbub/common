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


import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class VersionList extends ArrayList<Version> {
    /**
     *
     */
    private static final long serialVersionUID = -2012909928456027745L;
    private boolean ensureNoDuplicates;

    public VersionList() {
        super();
    }


    public VersionList(int initialCapacity) {
        super(initialCapacity);
    }


    public VersionList(Collection<? extends Version> c) {
        super(c);
    }

    /**
     * Removes all snapshots from this list
     */

    public void removeSnapshots() {
        VersionList versionToRemove = new VersionList();

        // collect Versions to be removed
        for (Version ver : this) {
            if (ver.isSnapshot()) {
                versionToRemove.add(ver);
            }
        }

        // remove them
        this.removeAll(versionToRemove);
    }

    /**
     * Checks if this list contains a snapshot version.
     *
     * @return {@code true} if this list contains a snapshot, {@code false}
     * otherwise
     * @see Version#isSnapshot()
     */

    public boolean containsSnapshot() {

        for (Version ver : this) {
            if (ver.isSnapshot()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if this list contains a release version.
     *
     * @return {@code true} if this list contains a release, {@code false}
     * otherwise
     * @see Version#isSnapshot()
     */

    public boolean containsRelease() {

        for (Version ver : this) {
            if (!ver.isSnapshot()) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public VersionList clone() {
        VersionList res = new VersionList();
        for (Version ver : this) {
            res.add(ver.clone());
        }

        return res;
    }

    public boolean isEnsureNoDuplicates() {
        return ensureNoDuplicates;
    }

    public VersionList setEnsureNoDuplicates(boolean ensureNoDuplicates) {
        this.ensureNoDuplicates = ensureNoDuplicates;
        return this;
    }

    @Override
    public boolean add(Version e) {
        //noinspection SimplifiableIfStatement
        if (this.contains(e) && isEnsureNoDuplicates()) {
            return false;
        } else {
            return super.add(e);
        }
    }

    @Override
    public boolean addAll(Collection<? extends Version> collection) {
        if (isEnsureNoDuplicates()) {
            Collection<Version> copy = new LinkedList<>(collection);
            copy.removeAll(this);
            return super.addAll(copy);
        } else {
            return super.addAll(collection);
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends Version> collection) {
        if (isEnsureNoDuplicates()) {
            Collection<Version> copy = new LinkedList<>(collection);
            copy.removeAll(this);
            return super.addAll(index, copy);
        } else {
            return super.addAll(index, collection);
        }
    }

    @Override
    public void add(int index, Version element) {
        if (!this.contains(element) || !isEnsureNoDuplicates()) {
            super.add(index, element);
        }
    }
}
