package common;

import org.junit.Test;

public class StringCommonTest {

	@Test
	public void replaceLastTest(){
		String testStr = "BlablubBla";
		assert StringCommon.replaceLast(testStr, "Bla", "").equals("Blablub");
	}
}
