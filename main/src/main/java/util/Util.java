package util;

import javafx.util.Pair;
import model.IMUData;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Util {
    public final static double STAND_Z = 1400.0;
    public final static double SIT_Z = 1000.0;
    public final static double L = 350.0;
    public final static int TIME_OUT = 15;
    public final static String MQTT_USER_NAME  = "admin";
    public final static String MQTT_PASSWORD  = "19930903";
    public final static String MQTT_SERVER_URI  = "tcp://192.168.1.8:1883";


    public static Pair<String,Vector3D> fromStringsToLocation(String[] splitedString){

        double yaw = (Double.parseDouble(splitedString[3]) - 30);
        yaw = (yaw >= 0) ? yaw : 360 + yaw;
        double pitch = Double.parseDouble(splitedString[4]);
        double x = Double.parseDouble(splitedString[0]) + L * Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw));
        double y = Double.parseDouble(splitedString[1]) + L * Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
        double proxyZ = Double.parseDouble(splitedString[2]) - Math.sin(Math.toRadians(pitch)) * L;
        double z = (Math.abs(proxyZ - STAND_Z) >= Math.abs(proxyZ - SIT_Z)) ? SIT_Z : STAND_Z;

        Vector3D loc = new Vector3D(x, y, z);
        String userName = splitedString[6];

        return new Pair<>(userName,loc);
    }
    public static Pair<String,Pair<Float,Vector3D>> fromStringsToLocationWithAccuracy(String[] splitedString){

        double yaw = (Double.parseDouble(splitedString[3]) - 30);
        yaw = (yaw >= 0) ? yaw : 360 + yaw;
        double pitch = Double.parseDouble(splitedString[4]);
        double x = Double.parseDouble(splitedString[0]) + L * Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw));
        double y = Double.parseDouble(splitedString[1]) + L * Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
        double proxyZ = Double.parseDouble(splitedString[2]) - Math.sin(Math.toRadians(pitch)) * L;
        double z = (Math.abs(proxyZ - STAND_Z) >= Math.abs(proxyZ - SIT_Z)) ? SIT_Z : STAND_Z;

        Vector3D loc = new Vector3D(x, y, z);
        String userName = splitedString[6];
        float accuracy = (float) (Double.parseDouble(splitedString[7])/100.0);

        Pair<Float,Vector3D> l = new Pair<>(accuracy,loc);
        return new Pair<>(userName,l);
    }

    public static Pair<String,Pair<Vector3D,Vector3D>> fromStringsToPointing(String[] splitedString){
        double yaw = (Double.parseDouble(splitedString[3]) - 30);
        yaw = (yaw >= 0) ? yaw : 360 + yaw;
        double pitch = Double.parseDouble(splitedString[4]);
        double roll = Double.parseDouble(splitedString[5]);
        double x = Double.parseDouble(splitedString[0]) + L * Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw));
        double y = Double.parseDouble(splitedString[1]) + L * Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
        double proxyZ = Double.parseDouble(splitedString[2]) - Math.sin(Math.toRadians(pitch)) * L;
        double z = (Math.abs(proxyZ - STAND_Z) >= Math.abs(proxyZ - SIT_Z)) ? SIT_Z : STAND_Z;

        Vector3D loc = new Vector3D(x, y, z);
        Vector3D dir = new Vector3D(pitch,roll,yaw);
        String userName = splitedString[6];
        Pair<Vector3D,Vector3D> pl = new Pair<>(loc,dir);
        return new Pair<String, Pair<Vector3D,Vector3D>>(userName,pl);
    }






    public static Pair<String,IMUData> fromStringsToIMU(String[] splitedString){
        IMUData curStruct = new IMUData(Double.parseDouble(splitedString[0]),
                Double.parseDouble(splitedString[1]),
                Double.parseDouble(splitedString[2]),
                Double.parseDouble(splitedString[3]),
                Double.parseDouble(splitedString[4]),
                Double.parseDouble(splitedString[5]));
        String curUserName = splitedString[6];
        return new Pair<String, IMUData>(curUserName,curStruct);
    }

}
