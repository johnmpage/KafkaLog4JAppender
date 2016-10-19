package net.johnpage.kafka.formatter;

import org.apache.log4j.spi.LoggingEvent;

public interface Formatter {
    String format(LoggingEvent event);
}
