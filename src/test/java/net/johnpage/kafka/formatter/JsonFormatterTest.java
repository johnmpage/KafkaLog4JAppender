package net.johnpage.kafka.formatter;

import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class JsonFormatterTest {
  @Test
  public void format() throws Exception {
    LoggingEvent event = new LoggingEvent("Category Class",Category.getInstance("Category"),Priority.INFO,"Message",new Exception("Test Exception"));
    JsonFormatter formatter = new JsonFormatter();
    Properties properties = new Properties();
    properties.setProperty("propertyOne","valueOne");
    properties.setProperty("propertyTwo","valueTwo");
    formatter.setExtraProperties(properties);
    String formattedEvent = formatter.format(event);
    System.out.println("JsonFormatterTest: "+formattedEvent);
    assertEquals("{\"level\":\"INFO\",\"logger\":\"Category\",\"propertyOne\":\"valueOne\",\"message\":\"Message\",\"propertyTwo\":\"valueTwo\",\"timestamp\":"+event.timeStamp+"}",formattedEvent);
  }
  public class MockEvent extends LoggingEvent {
    public MockEvent(String fqnOfCategoryClass, Category logger, Priority priority, Object message, Throwable throwable) {
      super(fqnOfCategoryClass, logger, priority, message, throwable);
    }
  }
}