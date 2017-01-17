package logging;

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


import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class OneLineFormatter extends SimpleFormatter {

    public OneLineFormatter() {
        super();
    }

    public String format(LogRecord record) {
        String res = "[" + record.getLevel() + "] " + record.getMessage() + "\r\n";

        if (record.getThrown() != null) {
            // An exception is associated with the record
            res = res + ExceptionUtils.getStackTrace(record.getThrown()) + "\r\n";
        }

        return res;
    }

}
