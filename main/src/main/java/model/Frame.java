package model;

import util.Util;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static model.ExecutableType.gestureOnly;
import static model.ExecutableType.voiceOnly;

public class Frame {
    private ObjectBox objectBox;
    private String curGesture;
    private TokenNode curCommand;
    private String userName;

    public Vector3D getSecLoc() { return secLoc; }

    private Vector3D secLoc;
    private boolean isDead;
    private ExecutableType executableType;
    private boolean isExecuable = false;
    private ScheduledExecutorService scheduledExecutorService;
    private MinuetSystem minuetSystem;

    public Frame(ObjectBox objectBox, MinuetSystem system,String userName) {
        this.minuetSystem = system;
        this.objectBox = objectBox;
        this.userName = userName;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(5);
        runTimeOut(Util.TIME_OUT);
    }
    public void setSecLoc(Vector3D secLoc){this.secLoc = secLoc;}
    public void kill() {
        this.isDead = true;
    }

    public void setCurCommand(TokenNode command) throws MqttException {
        if (objectBox.getCurObject().canExeVoiceCommand(command)) {
            this.curCommand = command;
            checkExcuable();
        }
    }
    public void setCurGesture(String curGesture) throws MqttException {
        if (objectBox.getCurObject().canExcuGestureCommand(curGesture)) {
            this.curGesture = curGesture;
            checkExcuable();
        }
    }
    public void execuate(MQTTSender mqtt) {
        String[] retirmData = new String[2];
        switch (executableType) {
            case voiceOnly:
                retirmData = objectBox.getCurObject().execuate(curCommand);
                break;
            case voiceAndGesture:
                //discard
//                retirmData = objectBox.getCurObject().execuate(new ArraySet<>(), curGesture);
                break;
            case gestureOnly:
                retirmData = objectBox.getCurObject().execuate(curGesture);
                break;
        }
        if (retirmData.length == 2) {
            try {
//                these are for roomba and projector cases
//                if (Roomba.class.isInstance(objectBox.getCurObject()) && retirmData[1].equals("g") && secLoc != null) {
//
//                    retirmData[1] = retirmData[1] + " " + (int) secLoc.getX() + " " + (int) secLoc.getY();
//
//                }
//                if (Projector.class.isInstance(objectBox.getCurObject()) && retirmData[1].equals("show")) {
//
//                    retirmData[1] = retirmData[1] + " " + userName;
//
//                }
                mqtt.sendMessage(retirmData[0], retirmData[1]);
            } catch (MqttException e) {
                e.printStackTrace();
            }

        }
    }

    public TokenNode getCurCommand() {
        return curCommand;
    }

    public String getCurGesture() {
        return curGesture;
    }


    private void runTimeOut(int second) {

        final Runnable cancellation = new Runnable() {
            @Override
            public void run() {
                synchronized (this) {

                    if (!isDead && !isExecuable) {
                        System.out.println("time out ");
                        try {
                            minuetSystem.killCurFrame();
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        scheduledExecutorService.schedule(cancellation, second, TimeUnit.SECONDS);
    }
    private void checkExcuable() throws MqttException {
        if (objectBox.getCurObject().canExeVoiceCommand(curCommand)) {
            isExecuable = true;
            executableType = voiceOnly;
            //discard
//        } else if (objectBox.getCurObject().canExcuVoiceWithGesture(new ArraySet<>(), curGesture)) {
//            isExecuable = true;
//            executableType = voiceAndGesture;
        } else if (objectBox.getCurObject().canExcuGestureCommand(curGesture)) {
            isExecuable = true;
            executableType = gestureOnly;
        }
        if (isExecuable) {
            minuetSystem.execuFrame(executableType);
        }
    }


    public String getUserName() {
        return userName;
    }


}
