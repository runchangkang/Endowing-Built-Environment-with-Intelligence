package plugin;

import model.Appliance;
import model.TokenNode;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.paho.client.mqttv3.MqttException;

public class DistancePlugin extends PluginTemplate {

    private Status curStatus;
    private int distant;
    private TokenNode inRangeCommand ;
    private TokenNode outRangeCommand ;

    public DistancePlugin(Appliance targetObject) throws MqttException {
        super(targetObject);
        this.name+=String.format("&%s","proximity");
        this.curStatus = Status.Off;
    }


    public void setDistant(int distant) { this.distant = distant; }

    public void setInRangeCommand(TokenNode inRangeCommand) {
        this.inRangeCommand = inRangeCommand;
    }

    public void setOutRangeCommand(TokenNode outRangeCommand) {
        this.outRangeCommand = outRangeCommand;
    }

    @Override
    void getVoiceFeedback(String voice) { }

    @Override
    public Runnable getTheRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    for(String userName :world.getUserMap().keySet()){
                        Vector3D userLoc = new Vector3D(world.getUserMap().get(userName).getUserLoc().getX(),world.getUserMap().get(userName).getUserLoc().getY(),0);
                        Vector3D objLoc = new Vector3D(targetObject.getLocation().getX(),targetObject.getLocation().getY(),0);


                        double distance = userLoc.distance(objLoc);


                        synchronized (this) {
                            if (isRunning) {

//                                System.out.println(distance);
                                if(distance>=distant*1000 && curStatus.equals(Status.On)){

                                    String[] retirmData = targetObject.execuate(outRangeCommand);
                                    try {
                                        mqtt.sendMessage(retirmData[0], retirmData[1]);
                                        curStatus = Status.Off;

                                    } catch (MqttException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if(distance<distant*1000 && curStatus.equals(Status.Off)) {

                                    String[] retirmData = targetObject.execuate(inRangeCommand);
                                    try {
                                        mqtt.sendMessage(retirmData[0], retirmData[1]);
                                        curStatus = Status.On;

                                    } catch (MqttException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
