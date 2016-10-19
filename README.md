# Kafka Log4J Appender

## A log4j Appender that streams log events to a Kafka topic. 

Configuration:
```xml
<appender name="KafkaStream" class="net.johnpage.kafka.KafkaLog4JAppender">
		<param name="Topic" value="lp-en-tomcat-dev" />
		<param name="KafkaProducerProperties" value="
		  bootstrap.servers=a.domain.com:9092\n
		  value.serializer=org.apache.kafka.common.serialization.StringSerializer\n
		  key.serializer=org.apache.kafka.common.serialization.StringSerializer\n
		  security.protocol=SSL\n
		  ssl.truststore.location=a.kafka.client.truststore.jks\n
		  ssl.truststore.password=apassword\n
		 " />
</appender>
```
This is a Log4J Appender integrated with a Kafka Producer. It posts events as they are added to a remote server from your file. Kafka 0.70 has a Log4j appender, but newer versions have omitted one. This Appender is intended to be used with the latest version of Kafka 0.10.

### Building
```
mvn clean install
```

### Kafka Producer Properties
A typical set of Kafka Producer properties file might be:
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

### Log4J Version
This has not been tested with Log4J 2. 

### Kafka Version
Tested with Kafka 0.10. Should be backwards compatible with 0.90 and 0.82. All 3 versions rely on:
```java
new KafkaProducer(Properties properties) 
```
To use a different version of Kafka, include the desired version on the classpath. Version-appropriate properties will need to be used.
