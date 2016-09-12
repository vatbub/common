package common;

import org.junit.Test;

public class PrefsTest {
	@Test
	public void readPrefOnCleanEnvironment(){
		Common.setAppName("fokprojectUnitTests");
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
		Common.setAppName("fokprojectUnitTests");
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
		Common.setAppName("fokprojectUnitTests");
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
