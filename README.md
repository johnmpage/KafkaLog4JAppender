# Kafka Log4J Appender

## A log4j Appender that streams log events to a Kafka topic. 

### Configuration

```xml
<appender name="KafkaStream" class="net.johnpage.kafka.KafkaLog4JAppender">
  <param name="Topic" value="a-topic" />
  <param name="KafkaProducerPropertiesFilePath" value="/kafka-producer.properties" />
</appender>
```
This is a Log4J Appender integrated with a Kafka Producer. It posts logging events as they occur to a remote Kafka topic. 

Kafka 0.70 has a Log4j appender, but newer versions omit one. This Appender is intended to be used with the latest version of Kafka 0.10.

### Building
```
mvn clean install
```

### Usage

1. Make the following jars available on the classpath:
 * kafka-log4j-appender-1.0.jar
 * kafka-clients-0.10.0.0.jar
 * json-simple-1.1.1.jar
2. Provide a Kafka Producer properties file at the location configured in the lo4j configuration file.

### Kafka Producer Properties

A typical Kafka Producer properties file read as follows:
```properties
bootstrap.servers=a.domain.com:9092
value.serializer=org.apache.kafka.common.serialization.StringSerializer
key.serializer=org.apache.kafka.common.serialization.StringSerializer
security.protocol=SSL
ssl.truststore.location=a.kafka.client.truststore.jks
ssl.truststore.password=apassword
```
A complete reference to the producer properties is [here](https://kafka.apache.org/documentation.html#producerconfigs).

### Built using:
* [Log4J 1.2.8](https://commons.apache.org/proper/commons-io/)
* [Apache Kafka Producer 0.10](https://kafka.apache.org/)

### Log4J Version 1.2.8
This has not been tested with Log4J 2. 

### Kafka Version
Tested with Kafka 0.10. Should be backwards compatible with 0.90 and 0.82. These 3 versions rely on the following initialization of the Producer:
```java
new KafkaProducer(Properties properties) 
```
Version-appropriate properties will need to be used.
