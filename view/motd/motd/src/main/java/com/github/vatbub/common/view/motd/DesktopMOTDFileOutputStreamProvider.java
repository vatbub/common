package com.github.vatbub.common.view.motd;

/*-
 * #%L
 * FOKProjects Common View Message of the day
 * %%
 * Copyright (C) 2016 - 2018 Frederik Kammel
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


import com.github.vatbub.common.view.motd.core.MOTDFileOutputStreamProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class DesktopMOTDFileOutputStreamProvider implements MOTDFileOutputStreamProvider {
    /**
     * The name of the folder which is used to save serialized messages of the
     * day.
     */
    private String latestMOTDSerializedFilePath = "motd";

    @Override
    public FileOutputStream createFileOutputStream(String fileName) throws FileNotFoundException {
        return new FileOutputStream(getMOTDFolder().toPath().resolve(fileName).toFile());
    }

    @Override
    public File getMOTDFolder() {
        return new File(Common.getInstance().getAndCreateAppDataPath() + getLatestMOTDSerializedFilePath());
    }

    public String getLatestMOTDSerializedFilePath() {
        return latestMOTDSerializedFilePath;
    }

    public void setLatestMOTDSerializedFilePath(String latestMOTDSerializedFilePath) {
        this.latestMOTDSerializedFilePath = latestMOTDSerializedFilePath;
    }
}
