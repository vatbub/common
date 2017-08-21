package com.github.vatbub.common.view.core;

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


@SuppressWarnings("unused")
public interface ProgressDialog {

    /**
     * Invoked when the first operation starts
     */
    void operationsStarted();

    /**
     * Invoked when progress changes
     *
     * @param operationsDone
     *            Specifies how many operations have been completed
     * @param totalOperationsToDo
     *            Specifies the total number of operations to do.
     */
    void progressChanged(double operationsDone, double totalOperationsToDo);

    /**
     * Invoked when all operations are finished.
     */
    void operationsFinished();
}