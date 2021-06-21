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
import android.telephony.TelephonyManager
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DeviceInfoTest {
    @Test
    fun computer_sha3512DeviceIdentifierTest() {
        val identifier = DeviceInfo.Computer().uniqueDeviceIdentifier
        Assertions.assertNotEquals("", identifier)
    }

    @Test
    fun android_sha3512DeviceIdentifierTest() {
        mockTelephonyManager()
        val identifier = DeviceInfo.Android(context = context).uniqueDeviceIdentifier
        Assertions.assertNotEquals("", identifier)
    }

    private val context = mockk<Context>()
    private fun mockTelephonyManager() {
        val telephonyManager = mockk<TelephonyManager>()
        every {
            context.getSystemService(Context.TELEPHONY_SERVICE)
        } returns telephonyManager
        every { telephonyManager.deviceId } returns "990000862471854"
    }
}
