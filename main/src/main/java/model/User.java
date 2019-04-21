package model;

import entrance.XChartTest;
import javafx.util.Pair;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import util.KalmanFilter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class User {
    public enum Movement{
        STAY,WALKING,RUNNING
    }
    private boolean showVisualization = true;
    private String userName;
    private Vector3D userLoc;
    private Vector3D userPointVec;
    private double pitch = 0.0;
    private double roll = 0.0;
    private double yaw = 0.0;

    private boolean initialized;

    public Movement getCurMovementStatus() {
        return curMovementStatus;
    }

    private Movement curMovementStatus = Movement.STAY;
    private Queue<Vector3D> locationPoins = new LinkedList<>();

    private KalmanFilter kalmanFilter;
    public double getPitch() {
        return pitch;
    }
    public double getRoll() {
        return roll;
    }
    public double getYaw() {
        return yaw;
    }
    private XChartTest xChartTest;
    public User(String userName) {
        this.kalmanFilter = new KalmanFilter(3);
        this.initialized = false;
        this.userName = userName;
        this.userLoc = new Vector3D(0,0,0);
        this.userPointVec = new Vector3D(0,0,0);
        if(showVisualization)
            xChartTest = new XChartTest();


    }

    public String getUserName() {
        return userName;
    }
    public Vector3D getUserLoc() {
        return userLoc;
    }
    public Pair<Vector3D, Vector3D> getUserLocAndPointVec(){ return new Pair<>(userLoc,userPointVec); }
    public void updateLocWithKalmanFilter(Vector3D location, float accuracy){
        if(!initialized){
            kalmanFilter.SetState(location.getX(),location.getY(),accuracy,System.currentTimeMillis());
            initialized = true;

        }else{
            kalmanFilter.Process(location.getX(),location.getY(),accuracy,System.currentTimeMillis());
            Vector3D curLoc = new Vector3D(kalmanFilter.getX(),kalmanFilter.getY(),location.getZ());
            this.userLoc = curLoc;

            if(locationPoins.size()==25)locationPoins.poll();
            locationPoins.offer(curLoc);
            checkMovementStatus();
        }
        if (showVisualization)
            xChartTest.updateUserData(userLoc.getX(),userLoc.getY());
    }

    private void checkMovementStatus() {

        Iterator<Vector3D> i = locationPoins.iterator();

        Vector3D cur = i.next();
        if(!i.hasNext())return;
        Vector3D next = i.next();

        int accumulateDistance = 0;

        while(cur != null && next != null){

            accumulateDistance+=cur.distance(next);
            cur = next;
            if(!i.hasNext())next = null;
            else next = i.next();

        }
//        System.out.println("cur dif is : " + accumulateDistance/locationPoins.size() );
        if(accumulateDistance/locationPoins.size() < 60 )curMovementStatus = Movement.STAY;
        else curMovementStatus = Movement.WALKING;
//        System.out.println(curMovementStatus);
    }

    public void updateDirectionAndLocation(Pair<Vector3D, Vector3D> value) {
        userLoc = value.getKey();
        pitch = value.getValue().getX();
        roll = value.getValue().getY();
        yaw = value.getValue().getZ();
        double x = -Math.cos(Math.toRadians(pitch))*Math.sin(Math.toRadians(yaw));
        double y = -Math.cos(Math.toRadians(pitch))*Math.cos(Math.toRadians(yaw));
        double z = Math.sin(Math.toRadians(pitch));
        this.userPointVec = new Vector3D(x,y,z);

    }
}
