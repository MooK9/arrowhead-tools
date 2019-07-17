package com.incquerylabs.floodtest.mqtt;

import java.net.ServerSocket;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.incquerylabs.floodtest.Constants;
import com.incquerylabs.floodtest.Processor;

public class MqttProcessor extends Thread implements Processor, MqttCallback {

	MqttClient mqc = null;
	MqttMessage emptyMessage = new MqttMessage();
	ServerSocket serverSocket = null;
	int n;
	volatile boolean a = false;
	volatile boolean b = false;
	final String sensorATopic;
	final String sensorBTopic;
	String name;

	public MqttProcessor(int i) {
		n = i;
		name = "MqttProc" + n;
		sensorATopic = Constants.MQTT_SENSOR_BACK_TOPIC_BASENAME + "A" + n;
		sensorBTopic = Constants.MQTT_SENSOR_BACK_TOPIC_BASENAME + "B" + n;
		try {
			mqc = new MqttClient("tcp://" + Constants.SERVER_IP + ":" + Constants.MQTT_SERVER_PORT, name,
					new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			mqc.setCallback(this);
			mqc.connect(options);
			mqc.subscribe(Constants.MQTT_PROCESSOR_FORWARD_TOPIC_NAME);
			mqc.subscribe(sensorATopic);
			mqc.subscribe(sensorBTopic);
		} catch (MqttException e) {
			System.out.println("Excepton in MQTT creation in " + name );
		}
	}
	
	@Override
	public void run() {
		System.out.println(name + " ready");
	}

	@Override
	public void kill() {
		if (mqc != null) {
			try {
				mqc.disconnect();
				mqc.close();
			} catch (MqttException e) {
				System.out.println("Problem on closing MQTT connection in " + name);
			}
			mqc = null;
		}
		this.interrupt();
	}

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost because: " + cause);
		System.exit(1);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		mqc.publish(Constants.RECEIVED_TOPIC_NAME, emptyMessage);
		if (topic.equals(Constants.MQTT_PROCESSOR_FORWARD_TOPIC_NAME)) {
			System.out.println(name + " received request from Consumer.");
			MqttMessage ma = new MqttMessage(sensorATopic.getBytes());
			MqttMessage mb = new MqttMessage(sensorBTopic.getBytes());
			mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
			mqc.publish(Constants.MQTT_SENSOR_A_TOPIC_NAME, ma);
			mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
			mqc.publish(Constants.MQTT_SENSOR_B_TOPIC_NAME, mb);
			a = false;
			b = false;
		} else if (topic.equals(sensorATopic)) {
			System.out.println(name + " received response from Sensor A.");
			if (b == true) {
				if(a != true) {
					mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
					mqc.publish(Constants.MQTT_PROCESSOR_BACKWARD_TOPIC_NAME, emptyMessage);
					a = true;
				}
			} else {
				a = true;
			}
		} else if (topic.equals(sensorBTopic)) {
			System.out.println(name + " received response from Sensor B.");
			if (a == true) {
				if(b != true) {
					mqc.publish(Constants.SENT_TOPIC_NAME, emptyMessage);
					mqc.publish(Constants.MQTT_PROCESSOR_BACKWARD_TOPIC_NAME, emptyMessage);
					b = true;
				}
			} else {
				b = true;
			}
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}
}
