package entrance;

import org.eclipse.paho.client.mqttv3.*;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MQTTMusicAppliance implements MqttCallback {

    private MqttClient client;

    private MqttConnectOptions options;
//    private ExecutorService executorService;


    private volatile Queue<String> q = new ConcurrentLinkedDeque<>();
    private String base = "main/src/resources/wav/";
    public MQTTMusicAppliance() throws MqttException{
//        executorService = Executors.newCachedThreadPool();

        client = new MqttClient("tcp://192.168.1.8:1883", "Music");
        options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.setCallback(this);
        client.connect(options);
        System.out.println("MusicPlayer connected");
        client.subscribe("music");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                play();
            }
        });
        thread.start();
    }

    private void play(){
        while (true){
            if(q.size()!=0){
                String s = q.poll();
                System.out.println("play method :"+s);

                try {

                    InputStream in = new FileInputStream(s);
                    AudioStream audioStream = new AudioStream(in);
                    AudioPlayer.player.start(audioStream);
//                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }




    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {


        String msg = new String(message.getPayload());
        String fileName = base+msg+".wav";
        System.out.println(fileName);
        q.add(fileName);


    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public static void main(String[] args) {
        try {
            MQTTMusicAppliance m = new MQTTMusicAppliance();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
