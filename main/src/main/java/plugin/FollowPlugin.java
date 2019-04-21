package plugin;

import model.Appliance;
import model.User;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.paho.client.mqttv3.MqttException;

public class FollowPlugin extends PluginTemplate {
    private String followingCommand;
//    private LightStrip lightStrip;
    public FollowPlugin(Appliance targetObject) throws MqttException {
        super(targetObject);
        this.name+=String.format("&%s","follow");

//        if(targetObject instanceof LightStrip){
//            lightStrip = (LightStrip) targetObject;
//        }
//        else{
//            System.err.println("can't apply follow plugin here");
//        }
    }
    @Override
    void getVoiceFeedback(String voice) {}
    @Override
    public Runnable getTheRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                for(String userName :world.getUserMap().keySet()) {
                    Vector3D userLoc = new Vector3D(world.getUserMap().get(userName).getUserLoc().getX(), world.getUserMap().get(userName).getUserLoc().getY(), 0);
                    if(world.getUserMap().get(userName).getCurMovementStatus() == User.Movement.WALKING){
                        String realcommand = followingCommand;
                        if(followingCommand.equals("followingX")){
                            realcommand = getFollowingXCommand(userLoc);
                        }
                        else if(followingCommand.equals("followingY")){
                            realcommand = getFollowingYCommand(userLoc);
                        }
                        try {
                            mqtt.sendMessage(targetObject.getTopic(),realcommand);
                            Thread.sleep(300);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }
            }


        };
    }
    private String getFollowingXCommand(Vector3D userLoc) {
        int X = (int)userLoc.getX();
        X = ((X < 0? 0:X)/1000)%6;
        return ""+X;
    }
    private String getFollowingYCommand(Vector3D userLoc) {
        int y = (int)userLoc.getY();
        y = ((y < 0? 0:y)/1000)%6;
        return ""+y;
    }
    public String getFollowingCommand() {
        return followingCommand;
    }

    public void setFollowingCommand(String followingCommand) {
        this.followingCommand = followingCommand;
    }
}
