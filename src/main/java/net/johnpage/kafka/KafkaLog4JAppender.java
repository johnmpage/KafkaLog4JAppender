package net.johnpage.kafka;

import net.johnpage.kafka.formatter.Formatter;
import net.johnpage.kafka.formatter.JsonFormatter;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KafkaLog4JAppender extends AppenderSkeleton {
  private boolean logToSystemOut = false;
  private String kafkaProducerProperties;
  private String kafkaProducerPropertiesFilePath;
  private String topic;
  private Producer producer;
  private Formatter formatter = new JsonFormatter();
  private boolean isInitialized = false;
  public void activateOptions() {
    System.out.println("KafkaLog4JAppender: kafkaProducerProperties configuration = "+kafkaProducerProperties);
    Properties properties = null;
    try {
      if(kafkaProducerPropertiesFilePath!=null && kafkaProducerPropertiesFilePath.length()>0){
        properties = getPropertiesFromFile();
      }else if(kafkaProducerProperties!=null && kafkaProducerProperties.length()>0){
        properties = new Properties();
        properties.load(new ByteArrayInputStream(kafkaProducerProperties.getBytes()));
      }else{
        throw new NullPointerException("Either the kafkaProducerPropertiesFilePath or kafkaProducerProperties property must be set in the Log4j configuration file.");
      }
      System.out.println("KafkaLog4JAppender: Kafka Producer Properties = "+properties);
      if(properties.getProperty("ssl.truststore.location")!=null){
        File keystoreFile = new File(properties.getProperty("ssl.truststore.location"));
        if( !keystoreFile.exists() ){
          System.out.println("KafkaLog4JAppender: Keystore does not exist at the configured path: "+ keystoreFile.getAbsolutePath());
          throw new RuntimeException("KafkaLog4JAppender: Keystore does not exist at the configured path: "+ keystoreFile.getAbsolutePath());
        }
      }
      ProducerFactory.setProperties(properties);
      producer = ProducerFactory.getInstance();
    } catch (Exception exception) {
      System.out.println("KafkaLog4JAppender: Exception initializing Producer. "+ exception +" : "+ exception.getMessage());
      exception.printStackTrace();
      throw new RuntimeException("KafkaLog4JAppender: Exception initializing Producer.",exception);
    }
    System.out.println("KafkaLog4JAppender: Producer initialized: "+ producer);
    if (topic == null) {
      System.out.println("KafkaLog4JAppender requires a topic. Add this to the appender configuration.");
    } else {
      System.out.println("KafkaLog4JAppender will publish messages to the '" + topic + "' topic.");
      isInitialized = true;
    }
  }
  @Override
  protected void append(LoggingEvent loggingEvent) {
    if(!isInitialized){
      System.out.println("KafkaLog4JAppender: Appender not initialized.");
      return;
    }
    String string = this.formatter.format(loggingEvent);
    if( logToSystemOut ) {
      System.out.println("KafkaLog4JAppender: string = " + string);
    }
    try {
      ProducerRecord<String, String> producerRecord = new ProducerRecord(topic, string);
      producer.send(producerRecord);
    }catch(Exception e){
      System.out.println("KafkaAppender: Exception sending message: '" + e + " : "+ e.getMessage() + "'.");
      e.printStackTrace();
    }
  }
  private Properties getPropertiesFromFile() throws IOException {
    Properties properties = new Properties();
    InputStream in;
    in = getClass().getResourceAsStream(kafkaProducerPropertiesFilePath);
    properties.load(in);
    in.close();
    return properties;
  }
  public void close() {
    producer.close();
  }
  public Formatter getFormatter() {
    return formatter;
  }
  public void setFormatter(Formatter formatter) {
    this.formatter = formatter;
  }
  public String getTopic() {
    return topic;
  }
  public void setTopic(String topic) {
    this.topic = topic;
  }
  public String getLogToSystemOut() {
    return logToSystemOut + "";
  }
  public void setLogToSystemOut(String logToSystemOutString) {
    if ("true".equalsIgnoreCase(logToSystemOutString)) {
      this.logToSystemOut = true;
    }
  }
  public boolean requiresLayout() {
    return false;
  }
  public String getKafkaProducerProperties() {
    return kafkaProducerProperties;
  }
  public void setKafkaProducerProperties(String kafkaProducerProperties) {
    this.kafkaProducerProperties = kafkaProducerProperties;
  }
  public String getKafkaProducerPropertiesFilePath() {
    return kafkaProducerPropertiesFilePath;
  }
  public void setKafkaProducerPropertiesFilePath(String kafkaProducerPropertiesFilePath) {
    this.kafkaProducerPropertiesFilePath = kafkaProducerPropertiesFilePath;
  }
}
