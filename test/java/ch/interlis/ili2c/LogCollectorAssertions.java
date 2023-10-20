package ch.interlis.ili2c;

import ch.ehi.basics.logging.LogEvent;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class LogCollectorAssertions {
    public static void assertContainsError(String text, int count, LogCollector logCollector) {
        assertEquals(String.format("Wrong number of error logs containing <%s> found.", text), count, countOccurrences(text, logCollector.getErrs()));
    }

    private static int countOccurrences(String text, List<LogEvent> logEvents) {
        int i = 0;
        for (LogEvent event : logEvents) {
            if (event.getEventMsg().contains(text)) {
                i++;
            }
        }
        return i;
    }
}
