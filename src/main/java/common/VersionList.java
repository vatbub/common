package common;

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
