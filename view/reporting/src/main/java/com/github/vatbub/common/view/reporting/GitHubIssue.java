package com.github.vatbub.common.view.reporting;

/*-
 * #%L
 * FOKProjects Common
 * %%
 * Copyright (C) 2016 - 2017 Frederik Kammel
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
 * Representation of an issue on GitHub
 */
@SuppressWarnings("unused")
public class GitHubIssue {
    private String reporterName;
    private String reporterEmail;
    private String title;
    private String body;
    private String logLocation;
    private String toRepo_Owner;
    private String toRepo_RepoName;
    private Throwable throwable;
    private String screenshotLocation;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLogLocation() {
        return logLocation;
    }

    public void setLogLocation(String logLocation) {
        this.logLocation = logLocation;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getToRepo_Owner() {
        return toRepo_Owner;
    }

    public void setToRepo_Owner(String toRepo_Owner) {
        this.toRepo_Owner = toRepo_Owner;
    }

    public String getToRepo_RepoName() {
        return toRepo_RepoName;
    }

    public void setToRepo_RepoName(String toRepo_RepoName) {
        this.toRepo_RepoName = toRepo_RepoName;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getScreenshotLocation() {
        return screenshotLocation;
    }

    public void setScreenshotLocation(String screenshotLocation) {
        this.screenshotLocation = screenshotLocation;
    }
}
