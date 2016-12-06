package net.johnpage.kafka.formatter;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import java.util.Enumeration;
import java.util.Properties;

public class JsonFormatter implements Formatter {
  private static final String QUOTE = "\"";
  private static final String COLON = ":";
  private static final String COMMA = ",";

  private boolean expectJson = false;
  private boolean includeMethodAndLineNumber = false;
  private Properties extraProperties;
  private String extraPropertiesString;
  private Map extraPropertiesMap = null;

  public String format(LoggingEvent event) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("level", event.getLevel().toString());
    jsonObject.put("logger", event.getLoggerName());
    jsonObject.put("timestamp", event.timeStamp);
    jsonObject.put("message", event.getMessage());
    if (includeMethodAndLineNumber) {
      LocationInfo locationInfo = event.getLocationInformation();
      if (locationInfo != null) {
        jsonObject.put("method", locationInfo.getMethodName());
        jsonObject.put("lineNumber", locationInfo.getLineNumber() + "");
      }
    }
    if (this.extraPropertiesMap != null) {
      jsonObject.putAll(extraPropertiesMap);
    }
    return jsonObject.toJSONString();
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

  public Properties getExtraProperties() {
    return extraProperties;
  }

  public void setExtraProperties(Properties extraProperties) {
    this.extraProperties = extraProperties;
    Enumeration enumeration = extraProperties.propertyNames();
    StringBuilder extraPropertiesStringBuilder = new StringBuilder();
    boolean isFirst = true;
    while (enumeration.hasMoreElements()) {
      if(!isFirst){
        extraPropertiesStringBuilder.append(COMMA);
      }
      String key = (String) enumeration.nextElement();
      fieldName(key,extraPropertiesStringBuilder);
      quote(extraProperties.getProperty(key),extraPropertiesStringBuilder);
      isFirst = false;
    }
    extraPropertiesString = extraPropertiesStringBuilder.toString();
  }

  public void setExtraProperties(String thatExtraProperties) {
    final Properties properties = new Properties();
    try {
      properties.load(new ByteArrayInputStream(thatExtraProperties.getBytes()));
      Enumeration<?> enumeration = properties.propertyNames();
      extraPropertiesMap = new HashMap();
      while(enumeration.hasMoreElements()){
        String name = (String)enumeration.nextElement();
        String value = properties.getProperty(name);
        extraPropertiesMap.put(name,value);
      }
    } catch (IOException e) {
      System.out.println("There was a problem reading the extra properties configuration: "+e.getMessage());
      e.printStackTrace();
    }
  }
}