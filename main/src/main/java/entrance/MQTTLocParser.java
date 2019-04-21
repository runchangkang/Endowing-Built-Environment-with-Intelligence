package entrance;

import model.MQTTSender;
import org.eclipse.paho.client.mqttv3.*;

public class MQTTLocParser implements MqttCallback {

    private MqttClient client;

    private MqttConnectOptions options;
    private MQTTSender sender = new MQTTSender();
    private int counter = 0;
    private int preY = 0;


    public MQTTLocParser() throws MqttException{

        client = new MqttClient("tcp://192.168.1.8:1883", "locParser");
        options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.setCallback(this);
        client.connect(options);
        System.out.println("locParser connected");
        client.subscribe("userLoc");


    }






    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        counter++;
        if(counter == 4){
            String msg = new String(message.getPayload());
            String[] strings = msg.split("\\s+");
//            int y = Integer.parseInt(strings[1]);
            int y = Integer.parseInt(strings[0]);
//            y = x+y;
            y = y < 0? 0:y;
            y = (y/1000)%6;
//            y = (y/100)%35;
            if(y != preY){
                System.out.println("sending msg to music: "+y);
                sender.sendMessage("music",""+y);
                preY = y;
            }
//            y = (y/1000)%6+1;




        }
        if(counter>4)counter=0;




    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public static void main(String[] args) {
        try {
            MQTTLocParser m = new MQTTLocParser();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
