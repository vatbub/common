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
import android.content.Context.TELEPHONY_SERVICE
import android.provider.Settings
import android.telephony.TelephonyManager
import org.apache.commons.text.similarity.JaccardSimilarity
import oshi.SystemInfo
import oshi.hardware.HWDiskStore
import oshi.hardware.UsbDevice
import java.security.MessageDigest

sealed class DeviceInfo(protected val messageDigest: MessageDigest) {
    abstract val uniqueDeviceIdentifier: ByteArray

    class Computer(messageDigest: MessageDigest = getMessageDigest(MessageDigestAlgorithms.SHA3_512)) :
        DeviceInfo(messageDigest) {
        override val uniqueDeviceIdentifier: ByteArray by lazy {
            val systemInfo = SystemInfo()
            val operatingSystem = systemInfo.operatingSystem
            val hardwareAbstractionLayer = systemInfo.hardware
            val centralProcessor = hardwareAbstractionLayer.processor
            val computerSystem = hardwareAbstractionLayer.computerSystem

            messageDigest.update(operatingSystem.family.toByteArray())
            messageDigest.update(operatingSystem.versionInfo.version.toByteArray())

            val usbDevices = hardwareAbstractionLayer.getUsbDevices(false)
            hardwareAbstractionLayer.diskStores
                .filterNot { it.serial == null }
                .filterNot { it.serial == "unknown" }
                .filterNot { isRemovableDrive(it, usbDevices) }
                .forEach { messageDigest.update(it.serial.toByteArray()) }


            messageDigest.update(centralProcessor.logicalProcessorCount.toString().toByteArray())


            if (computerSystem.serialNumber != null
                && computerSystem.serialNumber != ""
                && !computerSystem.serialNumber.equals("unknown", ignoreCase = true)
            ) {
                messageDigest.update(computerSystem.serialNumber.toByteArray())
            }

            messageDigest.digest()
        }

        private fun isRemovableDrive(
            store: HWDiskStore,
            usbDevices: List<UsbDevice>,
            jaccardSimilarityThreshold: Double = 0.7
        ): Boolean = usbDevices.any { device ->
            store.model.equals(device.name, ignoreCase = true)
                    || store.model.contains(device.name)
                    || device.name.contains(store.model)
                    || JaccardSimilarity().apply(store.model, device.name) > jaccardSimilarityThreshold
        }
    }

    class Android(
        messageDigest: MessageDigest = getMessageDigest(MessageDigestAlgorithms.SHA3_512),
        private val context: Context
    ) : DeviceInfo(messageDigest) {
        override val uniqueDeviceIdentifier: ByteArray by lazy {
            messageDigest.update(Settings.Secure.ANDROID_ID.toByteArray())
            val telephonyManager = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            messageDigest.update(telephonyManager.deviceId.toByteArray())
            messageDigest.digest()
        }
    }
}
