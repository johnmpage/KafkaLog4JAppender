package net.johnpage.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerFactory{
  private static Producer producer;
  private static Properties properties;
  public static void setProperties(Properties properties) {
    ProducerFactory.properties = properties;
  }
  public static Producer getInstance() {
    if(producer==null){
      producer = new KafkaProducer(ProducerFactory.properties);
    }
    return producer;
  }
  protected static void setInstance(Producer thisProducer) {
    producer = thisProducer;
  }
}