package entrance;

import javafx.util.Pair;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.paho.client.mqttv3.*;
import util.KalmanFilter;
import util.Util;

public class KalmanFilterTest implements MqttCallback {
    private KalmanFilter kalmanFilter;
    boolean initialized = false;
    private MqttClient client;
    public KalmanFilterTest() throws Exception{
        kalmanFilter = new KalmanFilter(3);
        client = new MqttClient(Util.MQTT_SERVER_URI, MqttClient.generateClientId());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(Util.MQTT_USER_NAME);
        options.setPassword(Util.MQTT_PASSWORD.toCharArray());
        client.setCallback(this);
        client.connect(options);

        //locData is the location and direction data of user's pointing
        //userLoc is for general the user location updates
        //data is for IMU data
        String[] strings = { "userLoc"};
        client.subscribe(strings);
        System.out.println("test started");
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
//        System.out.println("new message arrived");
        String newData = new String(message.getPayload());
        newData = newData.trim();
        String[] splitedString = newData.split("\\s+");

        Pair<String,Pair<Float,Vector3D>> result =  Util.fromStringsToLocationWithAccuracy(splitedString);
        String userName = result.getKey();
        Pair<Float,Vector3D> l = result.getValue();
        double x = l.getValue().getX();
        double y = l.getValue().getY();
        float accuracy = l.getKey();
//        System.out.println();
        if(!initialized){
            kalmanFilter.SetState(x,y,accuracy,System.currentTimeMillis());
            initialized = true;
        }


        else{
            kalmanFilter.Process(x,y,accuracy,System.currentTimeMillis());
        }


    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public static void main(String[] args) {
        try {
            KalmanFilterTest k= new KalmanFilterTest();
            while (true){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
