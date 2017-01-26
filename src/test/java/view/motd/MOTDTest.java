package view.motd;

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


import com.rometools.rome.io.FeedException;
import common.Common;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MOTDTest {

	private static final String appName = "fokprojectUnitTests";
    private static URL testFeedURL;

	@Before
	public void prepare() throws MalformedURLException {
		Common.setAppName(appName);
		testFeedURL = new URL("https://fokprojects.mo-mar.de/message-of-the-day/feed/");
	}

	@Test
	public void getMOTDTest() throws IllegalArgumentException, FeedException, IOException, ClassNotFoundException {
        MOTD motd = MOTD.getLatestMOTD(testFeedURL);

        assert !motd.isMarkedAsRead();
        assert motd.getEntry() != null;
		assert motd.getFeedTitle() != null;
		assert motd.getImage() != null;

		// mark as read
		motd.markAsRead();
        assert motd.isMarkedAsRead();
    }

	@After
	public void cleanUp() {
		for (File f : MOTD.getSerializedMOTFiles()) {
			f.delete();
		}
	}
}
