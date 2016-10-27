package logging;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

import org.apache.commons.lang.exception.ExceptionUtils;

public class OneLineFormatter extends SimpleFormatter {

	public OneLineFormatter() {
		super();
	}
	
	public String format(LogRecord record) {
		String res =  "[" + record.getLevel() + "] " + record.getMessage() + "\r\n";
		
		if (record.getThrown()!=null){
			// An exception is associated with the record
			res = res + ExceptionUtils.getStackTrace(record.getThrown()) + "\r\n";
		}
		
		return res;
	}

}
