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


/**
 * This interface is used to communicate with a gui while downloading and
 * installing an update. Unfortunately, Maven does not provide any progess (in
 * percent) during the download.
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
	 * Invoked when the download progress changes
	 * 
	 * @param kilobytesDownloaded
	 *            Specifies how many kilobytes of the file have already been
	 *            downloaded.
	 * @param totalFileSizeInKB
	 *            Specifies the total file size to download in kilobytes.
	 */
	public void downloadProgressChanged(double kilobytesDownloaded, double totalFileSizeInKB);

	/**
	 * Invoked once the artifact is copied to its destination.
	 */
	public void installStarted();

	/**
	 * Invoked once the updated artifact is launched.
	 */
	public void launchStarted();

	/**
	 * Invoked when the user requested to cancel the operation an the request
	 * was received.
	 */
	public void cancelRequested();

	/**
	 * Invoked when the user requested to cancel the current operation on the
	 * operation was cancelled.
	 */
	public void operationCanceled();
	
	/**
	 * Invoked when any error happens
	 * @param message The message to display
	 */
	public void showErrorMessage(String message);
}
