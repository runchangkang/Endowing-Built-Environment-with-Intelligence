package controller;

import model.IMUData;
import model.MQTTSender;
import org.eclipse.paho.client.mqttv3.MqttException;
import util.ClassifierUtil;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.RandomForest;
import weka.core.SerializationHelper;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestureRecognizer {
    private Map<String, List<IMUData>> gestureDict = new HashMap<>();
    private Map<String, List<IMUData>> triggerDict = new HashMap<>();
    private AbstractClassifier model;
    private AbstractClassifier triggerModel;
    private MQTTSender mqttSender;
    public GestureRecognizer() throws Exception {
        this.mqttSender = new MQTTSender();
        model = (IBk) SerializationHelper.read(new FileInputStream("main/src/weka/KNNgestures.model"));
        triggerModel = (RandomForest) SerializationHelper.read(new FileInputStream("main/src/weka/RTpointing.model"));
    }
    public void calculateTriggerGesture(String curUserName,IMUData data) throws MqttException {
        if (!triggerDict.containsKey(curUserName)) {
            triggerDict.put(curUserName, new ArrayList<IMUData>(15));
        }
        if (triggerDict.get(curUserName).size() == 15) triggerDict.get(curUserName).remove(0);

        triggerDict.get(curUserName).add(data);
        if (triggerDict.get(curUserName).size() == 15)
            checkTriggerGesture(triggerDict.get(curUserName), curUserName);
    }
    public String calculateGesture(String curUserName,IMUData data){
        if (!gestureDict.containsKey(curUserName)) {
            gestureDict.put(curUserName, new ArrayList<IMUData>(15));
        }
        if (gestureDict.get(curUserName).size() == 15){ gestureDict.get(curUserName).remove(0);}
        gestureDict.get(curUserName).add(data);
        if (gestureDict.get(curUserName).size() == 15) {return checkGesture(gestureDict.get(curUserName), curUserName);}
        return "";
    }


    public void clearAllGestureData(String userName){
        if (triggerDict.containsKey(userName))
            triggerDict.get(userName).clear();
        if (gestureDict.containsKey(userName))
            gestureDict.get(userName).clear();
    }

    private String checkGesture(List<IMUData> curIMUDatas, String userName) {
        String gesture = ClassifierUtil.Classify(model, curIMUDatas, 0);

        if (!gesture.equals("noInteraction") && !gesture.equals("")) {
            System.out.println("Gesture get: " + gesture);


            gestureDict.get(userName).clear();
            return gesture;
        }
        return "";
    }

    private void checkTriggerGesture(List<IMUData> curTriggerIMUDatas, String userName) throws MqttException {

        String gesture = ClassifierUtil.Classify(triggerModel, curTriggerIMUDatas, 1);

        if (gesture.equals("pointing")) {
            this.mqttSender.sendMessage("point/"+userName,"1");
            triggerDict.get(userName).clear();
        }
    }
}
