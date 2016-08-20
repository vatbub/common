package common;

import java.util.ArrayList;
import java.util.Collection;

public class VersionList extends ArrayList<Version> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2012909928456027745L;

	public VersionList() {
		super();
	}

	public VersionList(int initialCapacity) {
		super(initialCapacity);
	}

	public VersionList(Collection<? extends Version> c) {
		super(c);
	}
	
	/**
	 * Removes all snapshots from this list
	 */
	public void removeSnapshots(){
		VersionList versToRemove = new VersionList();
		
		// collect Versions to be removed
		for (Version ver:this){
			if (ver.isSnapshot()){
				versToRemove.add(ver);
			}
		}
		
		// remove them
		this.removeAll(versToRemove);
	}

	/**
	 * Checks if this list contains a snapshot version.
	 * 
	 * @return {@code true} if this list contains a snapshot, {@code false}
	 *         otherwise
	 * @see Version#isSnapshot()
	 */
	public boolean containsSnapshot() {

		for (Version ver : this) {
			if (ver.isSnapshot()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if this list contains a release version.
	 * 
	 * @return {@code true} if this list contains a release, {@code false}
	 *         otherwise
	 * @see Version#isSnapshot()
	 */
	public boolean containsRelease() {

		for (Version ver : this) {
			if (!ver.isSnapshot()) {
				return true;
			}
		}

		return false;
	}
	
	@Override
	public VersionList clone(){
		VersionList res = new VersionList();
		for (Version ver:this){
			res.add(ver.clone());
		}
		
		return res;
	}

}
