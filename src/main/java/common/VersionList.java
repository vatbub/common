package common;

import java.util.ArrayList;
import java.util.Collection;

public class VersionList extends ArrayList<Version> {

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

}
