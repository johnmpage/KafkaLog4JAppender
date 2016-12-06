# Kafka Log4J Appender

## A log4j Appender that streams log events to a Kafka topic. 

### Configuration

```xml
<appender name="KafkaStream" class="net.johnpage.kafka.KafkaLog4JAppender">
		<param name="Topic" value="a-topic" />
		<param name="KafkaProducerPropertiesFilePath" value="/kafka-producer.properties" />
</appender>
```
This is a Log4J Appender integrated with a Kafka Producer. It posts events as they are added to a remote server from your file. 
Kafka 0.70 has a Log4j appender, but newer versions omit an appender. This Appender is intended to be used with the latest version of Kafka 0.10.


### Building
```
mvn clean install
```

### Usage

1. Place the *kafka-log4j-appender-1.0.jar* in the application *"lib"* directory.
2. Download and place the *kafka-clients-0.10.0.0.jar* and the *json-simple-1.1.1.jar* into the application *"lib"* directory.
3. Provide a Kafka Producer properties file at the location configured in the lo4j configuration file.

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

### Optional Extra Message Properties

With load-balanced applications it can be useful to tag log messages with a server identifier. 
By setting an *ExtraPropertiesFilePath* parameter any number of extra properties will be added to each message sent to the Kafka topic.
```xml
<appender name="KafkaStream" class="net.johnpage.kafka.KafkaLog4JAppender">
		<param name="Topic" value="a-topic" />
		<param name="KafkaProducerPropertiesFilePath" value="/kafka-producer.properties" />
		<param name="ExtraPropertiesFilePath" value="/extra.properties" />
</appender>
```

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
To use a different version of Kafka, include the desired version on the classpath. Version-appropriate properties will need to be used.
