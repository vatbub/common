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


import java.net.URL;

/**
 * A class to communicate update information.
 * @see UpdateChecker
 * @author frede
 *
 */
public class UpdateInfo {
	/**
	 * Base URL of the maven repo where the artifact can be downloaded from.
	 */
	public URL mavenRepoBaseURL;
	
	/**
	 * The artifacts group id.
	 */
	public String mavenGroupID;
	
	/**
	 * The artifacts artifact id
	 */
	public String mavenArtifactID;
	
	/**
	 * The artifacts classifier or {@code ""} if the default artifact shall be used.
	 */
	public String mavenClassifier;
	
	/**
	 * Describes how the artifact is packed.
	 */
	public String packaging;
	
	/**
	 * {@code true} if a dialog should be presented to the user to download the update
	 */
	public boolean showAlert;
	
	/**
	 * The artifact version after the update
	 */
	public Version toVersion;
	
	/**
	 * The file size of the artifact in Megabytes. If the file size could not be determined, the value of this field is {@code -1}.
	 */
	public double fileSizeInMB;
}
