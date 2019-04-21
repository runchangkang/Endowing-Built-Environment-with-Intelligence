package plugin;

import model.Appliance;
import model.MQTTSender;
import model.MinuetSystem;
import org.eclipse.paho.client.mqttv3.MqttException;

public abstract class PluginTemplate {
    protected MQTTSender mqtt;


    protected MinuetSystem world;
    protected Appliance targetObject;
    protected int curStage = 0;
    protected String name;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    protected String userName;
    protected boolean isRunning = true;
    public PluginTemplate(Appliance targetObject) throws MqttException {
        this.mqtt = new MQTTSender();
        this.targetObject = targetObject;
        this.name = targetObject.getApplianceName();
        mqtt.sendMessage("connectedVoice","$please select target object");

    }
    public String getName(){return this.name;}
    public void kill() {
        isRunning = false;
    }
//    public void setTargetObject(Appliance targetObject){this.targetObject = targetObject;}
    public Appliance getTargetObject(){return this.targetObject;}

    public void setWorld(MinuetSystem world) {
        this.world = world;
    }
    abstract void getVoiceFeedback(String voice);
    public abstract Runnable getTheRunnable();
    protected enum Status{
        On,Off


    }
    public void start(){
        world.runPlugin(this);
    }

}
