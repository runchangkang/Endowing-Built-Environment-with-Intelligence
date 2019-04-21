package controller;

import javafx.util.Pair;
import model.IMUData;
import model.MQTTSender;
import model.MinuetSystem;
import model.NLPHandler;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.paho.client.mqttv3.*;
import util.Util;

import java.util.Arrays;

public class SystemController implements MqttCallback {
    private MqttClient client;
    private STTrunner sttrunner;
    private MQTTSender mqttSender;
    private MinuetSystem minuetSystem;
    private GestureRecognizer gestureRecognizer;

    public SystemController(MinuetSystem system) throws Exception {
        this.gestureRecognizer = new GestureRecognizer();
        this.minuetSystem = system;
        client = new MqttClient(Util.MQTT_SERVER_URI, MqttClient.generateClientId());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(Util.MQTT_USER_NAME);
        options.setPassword(Util.MQTT_PASSWORD.toCharArray());
        client.setCallback(this);
        client.connect(options);

        //locData is the location and direction data of user's pointing
        //userLoc is for general the user location updates
        //data is for IMU data
        String[] strings = {"locData", "speechResult", "data", "userLoc"};
        client.subscribe(strings);

        sttrunner = new STTrunner();
        sttrunner.runnerStart();
        mqttSender = new MQTTSender();
    }

    @Override
    public void connectionLost(Throwable cause) {}

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String newData = new String(message.getPayload());
        newData = newData.trim();
        String[] splitedString = newData.split("\\s+");


        switch (topic){
            case "locData":
                pointingResultHandler(splitedString);
                break;
            case "speechResult":
                speechResultHandler(splitedString);
                break;
            case "data":
                imuDataHandler(splitedString);
                break;
            case "userLoc":
                userLocationHandler(splitedString);
                break;

        }

    }

    private void userLocationHandler(String[] splitedString) {
        Pair<String,Pair<Float,Vector3D>> userInfo = Util.fromStringsToLocationWithAccuracy(splitedString);

        minuetSystem.updateUserLoc(userInfo.getKey(),userInfo.getValue().getValue(),userInfo.getValue().getKey());
    }

    private void imuDataHandler(String[] splitedString) throws MqttException {
        Pair<String, IMUData> imuData = Util.fromStringsToIMU(splitedString);
        if(minuetSystem.checkIfAbleCheckGesture() ){
            String gesture = gestureRecognizer.calculateGesture(imuData.getKey(),imuData.getValue());
            if(!gesture.equals("")){

                minuetSystem.getCurFrame().setCurGesture(gesture);
            }
        }
        gestureRecognizer.calculateTriggerGesture(imuData.getKey(),imuData.getValue());

    }
    /*
    TODO:
     */
    private void pointingResultHandler(String[] splitedString) throws MqttException {
        Pair<String,Pair<Vector3D,Vector3D>> pairPair = Util.fromStringsToPointing(splitedString);
        minuetSystem.getPointingResult(pairPair.getKey(),pairPair.getValue());
    }

    private void speechResultHandler(String[] splitedString) throws Exception {
        System.out.println("Speech got: "+ Arrays.toString(splitedString));
        if(minuetSystem.getCurFrame()!=null){
            minuetSystem.getCurFrame().setCurCommand(NLPHandler.parse(Arrays.toString(splitedString)));
        }



    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
