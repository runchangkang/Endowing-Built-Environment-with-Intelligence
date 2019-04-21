package plugin;

import model.Appliance;
import model.TokenNode;
import model.User;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MovementPlugin extends PluginTemplate {


    public void setMovingCommand(TokenNode movingCommand) {
        this.movingCommand = movingCommand;
    }

    public void setStableCommand(TokenNode stableCommand) {
        this.stableCommand = stableCommand;
    }

    private TokenNode movingCommand ;
    private TokenNode stableCommand ;
    private User.Movement curState ;




    @Override
    void getVoiceFeedback(String voice) {

    }

    @Override
    public Runnable getTheRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                User user = world.getUserMap().get(userName);
                if(user != null){
                    User.Movement curMovement = user.getCurMovementStatus();
                    if(curMovement != curState){
                        if(curMovement == User.Movement.STAY){
                            String[] retirmData = targetObject.execuate(stableCommand);
                            try {
                                mqtt.sendMessage(retirmData[0], retirmData[1]);
                                curState = User.Movement.STAY;

                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }else if(curMovement == User.Movement.WALKING){
                            String[] retirmData = targetObject.execuate(movingCommand);
                            try {
                                mqtt.sendMessage(retirmData[0], retirmData[1]);
                                curState = User.Movement.WALKING;

                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    public MovementPlugin(Appliance targetObject) throws MqttException {
        super(targetObject);

        this.name+=String.format("&%s","moving");
        this.curState = User.Movement.STAY;
    }
}
