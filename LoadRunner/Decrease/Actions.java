/*
 * LoadRunner Java script. (Build: _build_number_)
 * 
 * Script Description: 
 *                     
 */

import lrapi.lr;
import lrapi.web;
import org.apache.log4j.*;

import java.util.*;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.header.*;
import org.apache.kafka.common.utils.*;

import java.nio.*;


public class Actions
{

	private Producer<String, String> producer = null;
	private String topicName = "operations";
	private ProducerRecord record = null;
	
	public int init() throws Throwable {
		Logger.getRootLogger().setLevel(Level.OFF);
		BasicConfigurator.configure();
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("security.protocol", "SSL");
		props.put("ssl.trustore.location", "cert_trustore.jks");
		props.put("ssl.trustore.type", "JKS");
		props.put("ssl.trustore.password", "Ab123456");
		props.put("ssl.keystore.location", "cert_keystore.jks");
		props.put("ssl.keystore.type", "JKS");
		props.put("ssl.keystore.password", "Ab123456");
		props.put("ssl.key.password", "Ab123456");
		
		producer = new KafkaProducer<String, String>(props);
		
		lr.read_file("test.json", "kafka-msg", 0);
		
		return 0;
	}//end of init


	public int action() throws Throwable {
		lr.start_transaction("DECREASE");
		
		String cardNumber = lr.eval_string("cardNumber");
		String officeNumber = lr.eval_string("officeNumber");
		inе amount = amount;
		
		//int id_partitions = lr.eval_int("{id_partition}");  // в переменных указать рандомные номера портиций
		int id_partitions = 0; // будет отправлять только в портицию 0
		
		List<RecordHeader> headers = new ArrayList<>();
	//	headers.add(new RecordHeader("slxid", slxid));
	//	headers.add(new RecordHeader("TraceId", "jefhweuistcfbs"));
		                      
		String value = lr.eval_string(lr.eval_string("{kafka_msg}"));
		
		//String value = "{\"mdmcode\":\"22334455\"}"; //если json писать прямо в скрипте а не брать из файла
		
		record = new ProducerRecord(topicName, id_partitions, keyKafka, value, headers);
		
		producer.send(record);
		
		lr.end_transaction("DECREASE", lr.AUTO);
		
		return 0;
	}//end of action


	public int end() throws Throwable {
		producer.close();
		
		return 0;
	}//end of end
	
	public class RecordHeader implements Header {
	
		private ByteBuffer keyBuffer;
		private String keyHeader;
		private ByteBuffer valueBuffer;
		private byte[] value;
		
		public RecordHeader(String keyHeader, String value) {
			Objects.requireNonNull(keyHeader, "Null header keys are not permitter");
			this.keyHeader = keyHeader;
			this.value = value.getBytes();
		}
		
		public RecordHeader(ByteBuffer keyBuffer, ByteBuffer valueBuffer) {
			this.keyBuffer = Objects.requireNonNull(keyBuffer, "Null header keys are not permitter");
			this.valueBuffer = valueBuffer;
		}
		
		public String key() {
			if (keyHeader == null) {
				keyHeader = Utils.utf8(keyBuffer, keyBuffer.remaining());
				keyBuffer = null;
			}
			return keyHeader;
		}
		
		public byte[] value() {
			if (value == null && valueBuffer != null) {
				value = Utils.toArray(valueBuffer);
				valueBuffer = null;
			}
			return value;
		}		
		
		public String toString() {
			return "RecorderHeader(keyHeader = " + key() + ", value = " + Arrays.toString(value()) + ")";
		}
		
		
	}
	
	
}
