package common;

/**
 * This interface is used to communicate with a gui while downloading and
 * installing an update. Unfortunately, Maven does not provide any progess (in percent) during the download.
 * 
 * @author frede
 *
 */
public interface UpdateProgressDialog {
	/**
	 * Invoked when the preparation phase started.
	 */
	public void preparePhaseStarted();

	/**
	 * Invoked once the download is started.
	 */
	public void downloadStarted();

	/**
	 * Invoked once the artifact is copied to its destination.
	 */
	public void installStarted();
	
	/**
	 * Invoked once the updated artifact is launched.
	 */
	public void launchStarted();
}