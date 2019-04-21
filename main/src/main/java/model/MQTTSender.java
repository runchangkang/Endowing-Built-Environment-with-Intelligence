package model;

import util.Util;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTSender {
    private MqttClient client;

    public MQTTSender() throws MqttException {

        client = new MqttClient(Util.MQTT_SERVER_URI, MqttClient.generateClientId());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(Util.MQTT_USER_NAME);
        options.setPassword(Util.MQTT_PASSWORD.toCharArray());
        client.connect(options);
        System.out.println("MQTT sender successfully connected");

    }

    public void sendMessage(String topic,String message) throws MqttException{
        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    MqttMessage mqttMessage = new MqttMessage();
                    mqttMessage.setPayload(message.getBytes());
                    client.publish(topic, mqttMessage);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        sendThread.start();




    }



}
