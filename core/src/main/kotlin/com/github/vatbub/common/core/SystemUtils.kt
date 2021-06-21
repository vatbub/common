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

import org.apache.commons.lang3.SystemUtils.*
import java.util.*
import java.util.concurrent.TimeUnit

object SystemUtils {
    fun shutComputerDown(time: Int): Boolean {
        val shutdownCommand: String
        val timerCommand = if (time == 0) "now" else time.toString()
        shutdownCommand =
            if (IS_OS_AIX) "shutdown -Fh $timerCommand"
            else if (IS_OS_FREE_BSD
                || IS_OS_LINUX
                || IS_OS_MAC
                || IS_OS_MAC_OSX
                || IS_OS_NET_BSD
                || IS_OS_OPEN_BSD
                || IS_OS_UNIX
            ) {
                "shutdown -h $timerCommand"
            } else if (IS_OS_HP_UX) "shutdown -hy $timerCommand"
            else if (IS_OS_IRIX) "shutdown -y -g $timerCommand"
            else if (IS_OS_SOLARIS || IS_OS_SUN_OS) "shutdown -y -i5 -g$timerCommand"
            else if (IS_OS_WINDOWS) "shutdown.exe -s -t $time"
            else return false

        Runtime.getRuntime().exec(shutdownCommand)
        return true
    }

    private var timer: Timer? = null

    fun startAutoShutdownTimer(timerDuration: Long, timerDurationUnit: TimeUnit) {
        timer?.cancel()
        this.logger.info("Computer will shut down in $timerDuration $timerDurationUnit")

        val executionTime = Calendar.getInstance()
        executionTime.add(Calendar.MILLISECOND, TimeUnit.MILLISECONDS.convert(timerDuration, timerDurationUnit).toInt())

        timer = Timer("AutoShutdownDaemon").apply {
            schedule(object : TimerTask() {
                override fun run() {
                    try {
                        this.logger.error("Shutting computer down...")
                        val res = shutComputerDown(0)
                        if (!res) this.logger.error("Unable to shut computer down: shutComputerDown returned false")
                    } catch (e: Exception) {
                        this.logger.error("Unable to shut computer down$e")
                    }
                }
            }, executionTime.time)
        }
    }

    fun cancelAutoShutdownTimer() {
        timer?.cancel()
    }
}
