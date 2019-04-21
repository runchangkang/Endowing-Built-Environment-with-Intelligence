package entrance;

import org.eclipse.paho.client.mqttv3.*;

public class MQTTSounLightEffect implements MqttCallback {
    private MqttClient client;

    private MqttConnectOptions options;
    private Thread musicThread;
    private volatile boolean isRunning = true;
    public MQTTSounLightEffect() throws MqttException {

        client = new MqttClient("tcp://192.168.1.8:1883", "SoundLight");
        options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.setCallback(this);
        client.connect(options);
        System.out.println("SoundLight connected");
        client.subscribe("SoundLight");



    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String msg = new String(message.getPayload());
        if(msg.equals("start")){
            musicThread = new Thread(new Runnable() {
                @Override
                public void run() {


                    try {
                        Process p = Runtime.getRuntime().exec("python /Users/runchangkang/Desktop/testAudioLed/audio-reactive-led-strip/python/visualization.py");
//                            p.waitFor(30, TimeUnit.SECONDS);  // let the process run for 5 seconds
//                            p.destroy();
                        while(isRunning){
                            continue;
                        }
                        p.destroy();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }




                }
            });
            musicThread.start();
        }
        if (msg.equals("stop") && musicThread!= null && musicThread.isAlive()){
            isRunning = false;
            musicThread.interrupt();

            isRunning = true;

        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public static void main(String[] args) {
        try {
            MQTTSounLightEffect sounLightEffect = new MQTTSounLightEffect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
