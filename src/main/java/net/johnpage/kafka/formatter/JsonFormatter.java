package net.johnpage.kafka.formatter;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

public class JsonFormatter implements Formatter {
  private static final String QUOTE = "\"";
  private static final String COLON = ":";
  private static final String COMMA = ",";

  private boolean expectJson = false;
  private boolean includeMethodAndLineNumber = false;
  private String serverId = null;

  public String format(LoggingEvent event) {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    fieldName("level", sb);
    quote(event.getLevel().toString(), sb);
    sb.append(COMMA);
    fieldName("logger", sb);
    quote(event.getLoggerName(), sb);
    sb.append(COMMA);
    fieldName("timestamp", sb);
    sb.append(event.timeStamp);
    sb.append(COMMA);
    fieldName("message", sb);
    if (this.expectJson) {
      sb.append(event.getMessage());
    } else {
      quote(event.getMessage().toString(), sb);
    }
    if(includeMethodAndLineNumber) {
      sb.append(COMMA);
      // Caller Data
      LocationInfo locationInfo = event.getLocationInformation();
      if (locationInfo != null) {
        fieldName("method", sb);
        quote(locationInfo.getMethodName(), sb);
        sb.append(COMMA);
        fieldName("lineNumber", sb);
        quote(locationInfo.getLineNumber() + "", sb);
      }
    }
    if(serverId!=null){
      sb.append(COMMA);
      fieldName("serverId", sb);
      quote(serverId,sb);
    }
    sb.append("}");
    return sb.toString();
  }

  private static void fieldName(String name, StringBuilder sb) {
    quote(name, sb);
    sb.append(COLON);
  }

  private static void quote(String value, StringBuilder sb) {
    sb.append(QUOTE);
    sb.append(value);
    sb.append(QUOTE);
  }

  public boolean getExpectJson() {
    return expectJson;
  }

  public void setExpectJson(boolean expectJson) {
    this.expectJson = expectJson;
  }

  public boolean getIncludeMethodAndLineNumber() {
    return includeMethodAndLineNumber;
  }

  public void setIncludeMethodAndLineNumber(boolean includeMethodAndLineNumber) {
    this.includeMethodAndLineNumber = includeMethodAndLineNumber;
  }

  public String getServerId() {
    return serverId;
  }

  public void setServerId(String serverId) {
    this.serverId = serverId;
  }
}