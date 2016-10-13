package common;

import java.util.Arrays;

import org.junit.Test;

public class VersionListTest {

	@Test
	public void instanciateTest(){
		VersionList vList1 = new VersionList();
		assert vList1!=null;
		
		vList1 = new VersionList(10);
		assert vList1!=null;
		
		Version[] elements = {new Version("0.0.1"), new Version("0.0.2"), new Version("0.0.3")};
		vList1 = new VersionList(Arrays.asList(elements));
		
		for (Version ver:elements){
			assert vList1.contains(ver);
		}
		
	}
	
	@Test
	public void removeSnapshotsTest(){
		Version[] elements = {new Version("0.0.1"), new Version("0.0.2"), new Version("0.0.3"), new Version("0.0.3-SNAPSHOT")};
		VersionList vList = new VersionList(Arrays.asList(elements));
		
		assert vList.containsSnapshot() == true;
		vList.removeSnapshots();
		assert vList.containsSnapshot() == false;
		
		for (Version ver:elements){
			assert vList.contains(ver)!=ver.isSnapshot();
		}
	}
	
	@Test
	public void containsReleaseTest(){
		Version[] elements1 = {new Version("0.0.1"), new Version("0.0.2"), new Version("0.0.3"), new Version("0.0.3-SNAPSHOT")};
		VersionList vList = new VersionList(Arrays.asList(elements1));
		
		assert vList.containsRelease() == true;
		
		Version[] elements2 = {new Version("0.0.1-SNAPSHOT"), new Version("0.0.2-SNAPSHOT"), new Version("0.0.3-SNAPSHOT")};
		vList = new VersionList(Arrays.asList(elements2));
		assert vList.containsRelease()==false;
	}
	
	@Test
	public void cloneTest(){
		Version[] elements = {new Version("0.0.1"), new Version("0.0.2"), new Version("0.0.3"), new Version("0.0.3-SNAPSHOT")};
		VersionList vList1 = new VersionList(Arrays.asList(elements));
		
		for (Version ver:elements){
			assert vList1.contains(ver);
		}
		
		VersionList vList2 = vList1.clone();
		
		for (Version ver:elements){
			assert vList2.contains(ver);
		}
	}

}
