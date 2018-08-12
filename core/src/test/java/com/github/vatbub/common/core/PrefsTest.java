package com.github.vatbub.common.core;

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


import org.junit.Test;

public class PrefsTest  extends CoreBaseTestClass{
	@Test
	public void readPrefOnCleanEnvironment(){
		Common.getInstance().setAppName(DEFAULT_APP_NAME);
		int randomNumber = (int) (Math.random()*1000000000);
		Prefs prefs = new Prefs(PrefsTest.class.getName() + randomNumber);
		String prefKey = "testPreference";
		String defaultValue = "theDefaultValue";
		
		// Should return the default value on a clean environment as no preferences have been set yet.
		String prefRead1 = prefs.getPreference(prefKey, defaultValue);
		assert prefRead1.equals(defaultValue);
	}
	
	@Test
	public void savePref(){
		Common.getInstance().setAppName(DEFAULT_APP_NAME);
		int randomNumber = (int) (Math.random()*1000000000);
		Prefs prefs = new Prefs(PrefsTest.class.getName() + randomNumber);
		String prefKey = "testPreference";
		String testValue = "theTestValue"; // Must be different than defaultValue
		String defaultValue = "theDefaultValue";
		
		prefs.setPreference(prefKey, testValue);
		
		// Should return testValue
		String prefRead1 = prefs.getPreference(prefKey, defaultValue);
		
		assert prefRead1.equals(testValue);
	}
	
	@Test
	public void readAfterSaveTest(){
		Common.getInstance().setAppName(DEFAULT_APP_NAME);
		int randomNumber = (int) (Math.random()*1000000000);
		Prefs prefs = new Prefs(PrefsTest.class.getName() + randomNumber);
		String prefKey = "testPreference";
		String testValue = "theTestValue"; // Must be different than defaultValue
		String defaultValue = "theDefaultValue";
		
		prefs.setPreference(prefKey, testValue);
		
		// Reread the preferences
		Prefs prefs2 = new Prefs(PrefsTest.class.getName() + randomNumber);
		
		// Should return testValue
		String prefRead1 = prefs2.getPreference(prefKey, defaultValue);
		
		assert prefRead1.equals(testValue);
	}
}
