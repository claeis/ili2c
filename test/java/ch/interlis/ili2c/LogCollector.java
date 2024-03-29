package ch.interlis.ili2c;

import java.util.ArrayList;

import ch.ehi.basics.logging.AbstractStdListener;
import ch.ehi.basics.logging.LogEvent;
import ch.interlis.iox.IoxLogEvent;


public class LogCollector implements  ch.ehi.basics.logging.LogListener,ch.interlis.iox.IoxLogging {
	private ArrayList<LogEvent> errors=new ArrayList<LogEvent>();

	public ArrayList<LogEvent> getErrs() {
		return errors;
	}

	public void logEvent(LogEvent event) {
	    final int eventKind = event.getEventKind();
        if(eventKind==LogEvent.ERROR) {
            errors.add(event);
	    }
	}
	public static String getMessage(LogEvent ev) {
	    java.util.List<String> msgs=AbstractStdListener.formatOutput(ev, false, false);
	    return msgs.get(0);
	}

    @Override
    public void addEvent(IoxLogEvent event) {
        logEvent((LogEvent)event);
    }
	
}
