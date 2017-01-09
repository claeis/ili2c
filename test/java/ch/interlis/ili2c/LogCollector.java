package ch.interlis.ili2c;

import java.util.ArrayList;
import ch.ehi.basics.logging.LogEvent;


public class LogCollector implements  ch.ehi.basics.logging.LogListener {
	private ArrayList<LogEvent> events=new ArrayList<LogEvent>();

	public ArrayList<LogEvent> getErrs() {
		return events;
	}

	public void logEvent(LogEvent event) {
		events.add(event);
	}
	
}
