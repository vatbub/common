package com.github.vatbub.common.view.motd;

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


import com.github.vatbub.common.core.Common;
import com.github.vatbub.common.core.logging.FOKLogger;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndImage;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class that represents a message of the day
 *
 * @author frede
 * @since 0.0.15
 */
@SuppressWarnings("ConstantConditions")
public class MOTD extends PlatformIndependentMOTD{
    static {
        PlatformIndependentMOTD.setMotdFileOutputStreamProvider(new DesktopMOTDFileOutputStreamProvider());
    }

    public MOTD(com.rometools.rome.feed.synd.SyndImage image, String feedTitle, com.rometools.rome.feed.synd.SyndEntry entry) {
        super(image, feedTitle, entry);
    }

    public static MOTD getLatestMOTD(URL feedUrl) throws IllegalArgumentException, FeedException, IOException {
        FOKLogger.info(PlatformIndependentMOTD.class.getName(), "Retrieving latest MOTD from url " + feedUrl.toString());
        PlatformIndependentMOTD result = PlatformIndependentMOTD.getLatestMOTD(feedUrl);
        // We need to copy the object as we cannot cast :/
        return new MOTD(result.getImage(), result.getFeedTitle(), result.getEntry());
    }
}
