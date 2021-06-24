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

import com.jcabi.manifests.Manifests
import java.io.File

object BuildInfo {
    val currentJarFile by lazy {
        File(javaClass.protectionDomain.codeSource.location.toURI())
    }

    val appVersion: String? by lazy {
        javaClass.`package`.implementationVersion
    }

    var buildNumberManifestEntry = "Custom-Implementation-Build"

    val buildNumber by lazy {
        if (Manifests.exists(buildNumberManifestEntry))
            Manifests.read(buildNumberManifestEntry)
        else null
    }

    val packaging by lazy {
        if (AndroidUtils.isAndroid)
            return@lazy "apk"
        currentJarFile.extension
    }
}
