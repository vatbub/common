/*-
 * #%L
 * FOKProjects Common Core
 * %%
 * Copyright (C) 2016 - 2021 Frederik Kammel
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
package com.github.vatbub.common.core

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import org.apache.commons.lang.SystemUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class AppDataPathTest {
    @Test
    fun computer_appDataPathTest() {
        val expectedPath = when {
            SystemUtils.IS_OS_WINDOWS -> System.getenv("AppData")
            SystemUtils.IS_OS_MAC -> "${System.getProperty("user.home")}/Library/Application Support"
            else -> "${System.getProperty("user.home")}/.local/share"
        }
        val appDataPath = AppDataPath.Computer.appDataPath
        Assertions.assertEquals(File(expectedPath).absolutePath, appDataPath.absolutePath)
    }

    @Test
    fun computer_forAppNameTest() {
        val appName = "helloTestApp"
        val appDataPathForApp = AppDataPath.Computer.forAppName(appName)
        Assertions.assertEquals(appName, appDataPathForApp.absolutePath.split(File.separator).last())
    }

    @Test
    fun android_appDataPathTest() {
        val expectedFile = File("")
        val context = mockk<Context>().also {
            every { it.filesDir } returns expectedFile
        }
        Assertions.assertSame(expectedFile, AppDataPath.Android(context).appDataPath)
    }

    @Test
    fun android_forAppNameTest() {
        val appName = "helloTestApp"
        val context = mockk<Context>().also {
            every { it.filesDir } returns File("")
        }
        val appDataPathForApp = AppDataPath.Computer.forAppName(appName)
        Assertions.assertEquals(appName, appDataPathForApp.absolutePath.split(File.separator).last())
    }
}
