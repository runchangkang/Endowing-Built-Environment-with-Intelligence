package plugin;

import model.Appliance;
import model.TokenNode;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.paho.client.mqttv3.MqttException;



public class SpecialZonePlugin extends PluginTemplate {
    private String userName = "";
    private int maxX = 0;
    private int maxY = 0;
    private int minX = 0;

    public void setInRangeCommand(TokenNode inRangeCommand) {
        this.inRangeCommand = inRangeCommand;
    }

    public void setOutRangeCommand(TokenNode outRangeCommand) {
        this.outRangeCommand = outRangeCommand;
    }

    private TokenNode inRangeCommand ;
    private TokenNode outRangeCommand ;
    private boolean isTrigger = false;

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    private int minY = 0;
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public SpecialZonePlugin(Appliance targetObject) throws MqttException {
        super(targetObject);
    }

    @Override
    void getVoiceFeedback(String voice) {

    }

    @Override
    public Runnable getTheRunnable() {


        return new Runnable() {
            @Override
            public void run() {


                for(String userName :world.getUserMap().keySet()) {
                    Vector3D userLoc = new Vector3D(world.getUserMap().get(userName).getUserLoc().getX(), world.getUserMap().get(userName).getUserLoc().getY(), 0);
                    double userX = userLoc.getX();
                    double userY = userLoc.getY();
                    if(userX<maxX && userX>minX && userY<maxY &&userY>minY && !isTrigger ){
                        isTrigger = true;
                        try {
                            String[] retirmData = targetObject.execuate(inRangeCommand);
                            mqtt.sendMessage(retirmData[0],retirmData[1]);
                            Thread.sleep(300);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if(userX>maxX || userX<minX || userY>maxY ||userY<minY ){
                        if(isTrigger){
                            isTrigger = false;
                            try {
                                String[] retirmData = targetObject.execuate(outRangeCommand);
                                mqtt.sendMessage(retirmData[0],retirmData[1]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }






                }
            }
        };
    }
}
