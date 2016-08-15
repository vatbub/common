package logging;

/*
 * #%L
 * examTrainer
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


import java.util.logging.Level;
import java.util.logging.Logger;

import common.Common;

/**
 * Quick manual test for the FOKLogger class
 * @author Frederik Kammel
 *
 */
public class FOKLoggerQuickTest {

	public static void main(String[] args) {
		Common.setAppName("FOKLoggerQuickTest");
		FOKLogger testLogger = new FOKLogger(FOKLoggerQuickTest.class.getName());
		Logger testLog = testLogger.getLogger();
		testLog.log(Level.INFO, "Some info message");
		testLog.log(Level.WARNING, "a warning");

		try {
			((Object) null).toString();
		} catch (Exception e) {
			testLog.log(Level.SEVERE, "An error occured!", e);
		}
	}

}
