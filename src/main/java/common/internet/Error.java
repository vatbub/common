package common.internet;

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
 * A class to represent error messages that can be sent over http in json
 */
@SuppressWarnings("unused")
public class Error {
    String error;
    String stacktrace;

    public Error(String error) {
        this(error, "");
    }

    public Error(String error, String stacktrace) {
        this.error = error;
        this.stacktrace = stacktrace;
    }
}
