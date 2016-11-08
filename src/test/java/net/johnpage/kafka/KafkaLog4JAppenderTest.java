package net.johnpage.kafka;

import net.johnpage.kafka.mock.MockKafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class KafkaLog4JAppenderTest {
  @Test
  public void testAppend () throws ParseException {
    Logger logger = Logger.getLogger(KafkaLog4JAppenderTest.class);
    logger.setLevel(Level.WARN);
    KafkaLog4JAppender appender = new KafkaLog4JAppender();
    String properties ="property=value";
    appender.setKafkaProducerProperties(properties);
    appender.setTopic("a-topic");
    MockKafkaProducer mockKafkaProducer = new MockKafkaProducer(new Properties());
    ProducerFactory.setInstance(mockKafkaProducer);
    appender.activateOptions();
    logger.addAppender(appender);
    logger.warn("First message.");
    logger.warn("Second message.");
    logger.warn("Third message.");
    logger.debug("Fourth message shouldn't be printed.");
    assertEquals(3,mockKafkaProducer.recordList.size());
    ProducerRecord producerRecord = mockKafkaProducer.recordList.get(0);
    System.out.println(producerRecord.value().toString());
    JSONObject jsonObject  = (JSONObject)new JSONParser().parse(producerRecord.value().toString());
    assertEquals("First message.",jsonObject.get("message"));
  }
}
