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


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class VersionListTest {

    @SuppressWarnings("UnusedAssignment")
    @Test
    public void instantiateTest() {
        @SuppressWarnings("UnusedAssignment") VersionList vList1 = new VersionList();

        vList1 = new VersionList(10);

        Version[] elements = {new Version("0.0.1"), new Version("0.0.2"), new Version("0.0.3")};
        vList1 = new VersionList(Arrays.asList(elements));

        for (Version ver : elements) {
            assert vList1.contains(ver);
        }

    }

    @Test
    public void removeSnapshotsTest() {
        Version[] elements = {new Version("0.0.1"), new Version("0.0.2"), new Version("0.0.3"), new Version("0.0.3-SNAPSHOT")};
        VersionList vList = new VersionList(Arrays.asList(elements));

        assert vList.containsSnapshot();
        vList.removeSnapshots();
        assert !vList.containsSnapshot();

        for (Version ver : elements) {
            assert vList.contains(ver) != ver.isSnapshot();
        }
    }

    @Test
    public void containsReleaseTest() {
        Version[] elements1 = {new Version("0.0.1"), new Version("0.0.2"), new Version("0.0.3"), new Version("0.0.3-SNAPSHOT")};
        VersionList vList = new VersionList(Arrays.asList(elements1));

        assert vList.containsRelease();

        Version[] elements2 = {new Version("0.0.1-SNAPSHOT"), new Version("0.0.2-SNAPSHOT"), new Version("0.0.3-SNAPSHOT")};
        vList = new VersionList(Arrays.asList(elements2));
        assert !vList.containsRelease();
    }

    @Test
    public void cloneTest() {
        Version[] elements = {new Version("0.0.1"), new Version("0.0.2"), new Version("0.0.3"), new Version("0.0.3-SNAPSHOT")};
        VersionList vList1 = new VersionList(Arrays.asList(elements));

        for (Version ver : elements) {
            assert vList1.contains(ver);
        }

        VersionList vList2 = vList1.clone();

        for (Version ver : elements) {
            assert vList2.contains(ver);
        }
    }

    @Test
    public void noDuplicatesAddSingleVersionTest() {
        Version version1 = new Version("0.0.1");
        VersionList versionList = new VersionList().setEnsureNoDuplicates(true);
        versionList.add(version1);
        Assertions.assertEquals(1, versionList.size());

        versionList.add(version1.clone());
        Assertions.assertEquals(1, versionList.size());
    }

    @Test
    public void noDuplicatesAddSingleVersionAtIndexTest() {
        Version version1 = new Version("0.0.1");
        VersionList versionList = new VersionList().setEnsureNoDuplicates(true);
        versionList.add(0, version1);
        Assertions.assertEquals(1, versionList.size());

        versionList.add(1, version1.clone());
        Assertions.assertEquals(1, versionList.size());
    }

    @Test
    public void noDuplicatesAddAllTest() {
        Version[] elements = {new Version("0.0.1"), new Version("0.0.2"), new Version("0.0.3"), new Version("0.0.3-SNAPSHOT")};
        VersionList versionList = new VersionList(Arrays.asList(elements)).setEnsureNoDuplicates(true);
        Assertions.assertEquals(elements.length, versionList.size());

        versionList.addAll(Arrays.asList(elements));
        Assertions.assertEquals(elements.length, versionList.size());
    }

    @Test
    public void noDuplicatesAddAllAtIndexTest() {
        Version[] elements = {new Version("0.0.1"), new Version("0.0.2"), new Version("0.0.3"), new Version("0.0.3-SNAPSHOT")};
        VersionList versionList = new VersionList(Arrays.asList(elements)).setEnsureNoDuplicates(true);
        Assertions.assertEquals(elements.length, versionList.size());

        versionList.addAll(Math.round(elements.length / 2.0f), Arrays.asList(elements));
        Assertions.assertEquals(elements.length, versionList.size());
    }

    @Test
    public void addAllTest() {
        Version[] elements = {new Version("0.0.1"), new Version("0.0.2"), new Version("0.0.3"), new Version("0.0.3-SNAPSHOT")};
        VersionList versionList = new VersionList(Arrays.asList(elements));
        Assertions.assertEquals(elements.length, versionList.size());

        versionList.addAll(Arrays.asList(elements));
        Assertions.assertEquals(2 * elements.length, versionList.size());
    }

    @Test
    public void addAllAtIndexTest() {
        Version[] elements = {new Version("0.0.1"), new Version("0.0.2"), new Version("0.0.3"), new Version("0.0.3-SNAPSHOT")};
        VersionList versionList = new VersionList(Arrays.asList(elements));
        Assertions.assertEquals(elements.length, versionList.size());

        versionList.addAll(Math.round(elements.length / 2.0f), Arrays.asList(elements));
        Assertions.assertEquals(2 * elements.length, versionList.size());
    }
}
