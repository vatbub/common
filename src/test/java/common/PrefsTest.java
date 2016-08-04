package common;

import org.junit.Test;

public class PrefsTest {
	@Test
	public void readPrefOnCleanEnvironment(){
		int randomNumber = (int) Math.random();
		Prefs prefs = new Prefs(PrefsTest.class.getName() + randomNumber);
		String prefKey = "testPreference";
		String defaultValue = "theDefaultValue";
		
		// Should return the default value on a clean environment as no preferences have been set yet.
		String prefRead1 = prefs.getPreference(prefKey, defaultValue);
		
		assert prefRead1.equals(defaultValue);
	}
	
	@Test
	public void savePref(){
		int randomNumber = (int) Math.random();
		Prefs prefs = new Prefs(PrefsTest.class.getName() + randomNumber);
		String prefKey = "testPreference";
		String testValue = "theTestValue"; // Must be different than defaultValue
		String defaultValue = "theDefaultValue";
		
		prefs.setPreference(prefKey, testValue);
		
		// Should return testValue
		String prefRead1 = prefs.getPreference(prefKey, defaultValue);
		
		assert prefRead1.equals(testValue);
	}
}
