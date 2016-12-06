package net.johnpage.kafka.formatter;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.json.simple.JSONObject;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JsonFormatter implements Formatter {
  private boolean includeMethodAndLineNumber = false;
  private Properties extraProperties;
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
    extraPropertiesMap = new HashMap();
    while(enumeration.hasMoreElements()){
      String name = (String)enumeration.nextElement();
      String value = extraProperties.getProperty(name);
      extraPropertiesMap.put(name,value);
    }
  }
}