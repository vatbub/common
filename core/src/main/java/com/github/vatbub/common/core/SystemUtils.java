package com.github.vatbub.common.core;

/*-
 * #%L
 * FOKProjects Common Core
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


import com.github.vatbub.common.core.logging.FOKLogger;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class SystemUtils {
    private static SystemUtils instance;
    private Timer timer;

    private SystemUtils() {
    }

    public static SystemUtils getInstance() {
        if (instance == null)
            instance = new SystemUtils();
        return instance;
    }

    public boolean shutComputerDown(int time) throws IOException {
        String shutdownCommand, t = time == 0 ? "now" : String.valueOf(time);

        if (org.apache.commons.lang3.SystemUtils.IS_OS_AIX)
            shutdownCommand = "shutdown -Fh " + t;
        else if (org.apache.commons.lang3.SystemUtils.IS_OS_FREE_BSD || org.apache.commons.lang3.SystemUtils.IS_OS_LINUX || org.apache.commons.lang3.SystemUtils.IS_OS_MAC || org.apache.commons.lang3.SystemUtils.IS_OS_MAC_OSX || org.apache.commons.lang3.SystemUtils.IS_OS_NET_BSD || org.apache.commons.lang3.SystemUtils.IS_OS_OPEN_BSD || org.apache.commons.lang3.SystemUtils.IS_OS_UNIX)
            shutdownCommand = "shutdown -h " + t;
        else if (org.apache.commons.lang3.SystemUtils.IS_OS_HP_UX)
            shutdownCommand = "shutdown -hy " + t;
        else if (org.apache.commons.lang3.SystemUtils.IS_OS_IRIX)
            shutdownCommand = "shutdown -y -g " + t;
        else if (org.apache.commons.lang3.SystemUtils.IS_OS_SOLARIS || org.apache.commons.lang3.SystemUtils.IS_OS_SUN_OS)
            shutdownCommand = "shutdown -y -i5 -g" + t;
        else if (org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS)
            shutdownCommand = "shutdown.exe -s -t " + time;
        else
            return false;

        Runtime.getRuntime().exec(shutdownCommand);
        return true;
    }

    public void startAutoShutdownTimer(long timerDuration, TimeUnit timerDurationUnit) {
        if (timer != null)
            timer.cancel();
        FOKLogger.info(getClass().getName(), "Computer will shut down in " + timerDuration + " " + timerDurationUnit.toString());
        timer = new Timer("AutoShutdownDaemon");
        Calendar executionTime = Calendar.getInstance();
        executionTime.add(Calendar.MILLISECOND, (int) TimeUnit.MILLISECONDS.convert(timerDuration, timerDurationUnit));
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    FOKLogger.log(getClass().getName(), Level.SEVERE, "Shutting computer down...");
                    boolean res = shutComputerDown(0);
                    if (!res)
                        FOKLogger.log(getClass().getName(), Level.SEVERE, "Unable to shut computer down: shutComputerDown returned false");
                } catch (Exception e) {
                    FOKLogger.log(getClass().getName(), Level.SEVERE, "Unable to shut computer down" + e);
                }
            }
        }, executionTime.getTime());
    }

    public void cancelAutoShutdownTimer() {
        if (timer != null)
            timer.cancel();
    }
}
