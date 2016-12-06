package net.johnpage.kafka.formatter;

import org.apache.log4j.spi.LoggingEvent;

import java.util.Properties;

public interface Formatter {
    String format(LoggingEvent event);
    void setExtraProperties(Properties extraProperties);
}
