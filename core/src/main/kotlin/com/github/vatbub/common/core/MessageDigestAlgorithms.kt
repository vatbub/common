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

import java.security.MessageDigest

fun getMessageDigest(algorithm: MessageDigestAlgorithms): MessageDigest =
    MessageDigest.getInstance(algorithm.stringValue)

enum class MessageDigestAlgorithms(val stringValue: String) {
    MD2("MD2"), MD5("MD5"), SHA1("SHA-1"), SHA224("SHA-224"),
    SHA256("SHA-256"), SHA384("SHA-384"), SHA512_224("SHA-512/224"),
    SHA512_256("SHA-512/256"), SHA3_224("SHA3-224"), SHA3_256("SHA3-256"),
    SHA3_384("SHA3-384"), SHA3_512("SHA3-512")
}
