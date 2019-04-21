package model;

import javafx.util.Pair;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.paho.client.mqttv3.MqttException;
import plugin.PluginTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MinuetSystem {



    private Map<String,User> userMap = new HashMap<>();



    private Map<String,Appliance> applianceMap = new HashMap<>();
    private MQTTSender sender;
    private Frame curFrame;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(20);
    private Map<String,ScheduledFuture> stringScheduledFutureMap = new HashMap<>();


    public MinuetSystem() throws MqttException {
        sender = new MQTTSender();
    }

    public void runPlugin(PluginTemplate pluginTemplate){
        ScheduledFuture theFuture = scheduledExecutorService.scheduleWithFixedDelay(pluginTemplate.getTheRunnable(),0,1,TimeUnit.SECONDS);
        stringScheduledFutureMap.put(pluginTemplate.getName(),theFuture);
    }


    public void removePlugin(String pluginName){
        this.stringScheduledFutureMap.get(pluginName).cancel(true);
        stringScheduledFutureMap.remove(pluginName);

    }

//    public void addPlugin(PluginTemplate pluginTemplate){this.stringScheduledFutureMap.put(pluginTemplate.getName(),pluginTemplate)}
    public void addUser(User user){userMap.put(user.getUserName(),user);}
    public void addAppliance(Appliance appliance){applianceMap.put(appliance.getApplianceName(),appliance);}
    public void updateUserLoc(String userName, Vector3D location,float accuracy){userMap.get(userName).updateLocWithKalmanFilter(location,accuracy);}
    public Map<String, User> getUserMap() { return userMap; }
    public Map<String, Appliance> getApplianceMap() { return applianceMap; }
    public void getPointingResult(String key, Pair<Vector3D, Vector3D> value) throws MqttException {
        userMap.get(key).updateDirectionAndLocation(value);
        if(curFrame == null)
            checkIfHitAnyAppliance(key);
        else{
            performPointingWhilHasFrame(key);
        }
    }

    private void performPointingWhilHasFrame(String userName){
        synchronized (this) {
            double pitch = userMap.get(userName).getPitch();

            double yaw = userMap.get(userName).getYaw();
            double tmpz = userMap.get(userName).getUserLoc().getZ();
            double tmpX = Math.abs(tmpz / Math.tan(Math.toRadians(pitch))) * -Math.sin(Math.toRadians(yaw)) + userMap.get(userName).getUserLoc().getX();
            double tmpY = Math.abs(tmpz / Math.tan(Math.toRadians(pitch))) * -Math.cos(Math.toRadians(yaw)) + userMap.get(userName).getUserLoc().getY();
            curFrame.setSecLoc(new Vector3D(tmpX, tmpY, 0));
            System.out.println("update sec loc to " + tmpX + " " + tmpY);
        }
    }

    private void checkIfHitAnyAppliance(String userName) throws MqttException {
        ObjectBox box = new ObjectBox();
        for(String applianceName : applianceMap.keySet()){
            Appliance curAppliance = applianceMap.get(applianceName);
            if(curAppliance.checkBePointed(userMap.get(userName).getUserLocAndPointVec())){
                box.addToBox(curAppliance);
            }
        }
        if(!box.isEmpty()){
            synchronized (this){
                sender.sendMessage("trigger/" + userName,"1");
                curFrame = new CommandFrame(box, this, userName);
                if(box.isOne())sender.sendMessage("connectedVoice","*" + box.getCurObject().getApplianceName());
                else sender.sendMessage("connectedVoice","$multiple devices");

            }
        }
    }

    public void killCurFrame() throws MqttException {
        synchronized (this){
            if(curFrame != null){
                String name = curFrame.getUserName();
                sender.sendMessage("trigger/" + name, "0");
                curFrame.kill();
                sender.sendMessage("connectedVoice","$timeout");
                curFrame = null;

            }


        }
    }

    public Frame getCurFrame() {
        return curFrame;
    }

    public boolean checkIfAbleCheckGesture() {
        if(curFrame != null) return true;
        return false;
    }

    public void execuFrame(ExecutableType executableType) throws MqttException {
        synchronized (this){
            String name = curFrame.getUserName();
            this.curFrame.execuate(this.sender);
            sender.sendMessage("trigger/" + name, "0");
            curFrame.kill();
            curFrame = null;
            sender.sendMessage("connectedVoice", "$command executed");
        }
    }


}
