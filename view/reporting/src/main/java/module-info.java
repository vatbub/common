/*-
 * #%L
 * FOKProjects Common Reporting view
 * %%
 * Copyright (C) 2016 - 2020 Frederik Kammel
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
module common.view.reporting {
    requires java.logging;
    requires common.core;
    requires com.google.gson;
    requires common.internet;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;
    requires software.amazon.awssdk.auth;
    requires software.amazon.awssdk.services.s3;
    requires software.amazon.awssdk.core;
    requires javafx.swing;
}
