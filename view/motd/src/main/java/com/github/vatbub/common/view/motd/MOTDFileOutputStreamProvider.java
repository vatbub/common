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


import com.github.vatbub.common.core.Common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Implement this interface to replace the behaviour that the {@link MOTD} class uses to save read messages.
 */
public interface MOTDFileOutputStreamProvider {
    /**
     * Creates a {@code FileOutputStream} that points to a file with the specified fileName where the MOTD class may save a serialized MOTD in.
     * The implementing class is in charge of finding a suitable folder to save the file in.
     * Suitable folders are:
     * <ul>
     *     <li>Folders where the application has write access for (good idea: Use {@link Common#getAppDataPath()})</li>
     *     <li>Folders whose contents are likely to persist for a long time (i. e. NOT the Windows temp folder)</li>
     * </ul>
     * The application must have write access to the file.
     * @param fileName The name of the file to create a {@code FileOutputStream} for. The supplied file name must not be changed or otherwise the {@link MOTD} class will not work properly.
     * @return A {@code FileOutputStream} that points to a file with the specified file name.
     * @throws FileNotFoundException Exception is declared by the FileOutputStream constructor and will be handled by the MOTD class.
     * @see #getMOTDFolder()
     */
    FileOutputStream createFileOutputStream(String fileName) throws FileNotFoundException;

    /**
     * The subfolder where all files created with {@link #createFileOutputStream(String)} are saved in.
     * @return The subfolder where all files created with {@link #createFileOutputStream(String)} are saved in.
     */
    File getMOTDFolder();
}
