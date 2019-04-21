package controller;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class STTrunner {
    Thread audioThread;
    public STTrunner() {
        audioThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true){
                        try {
                            Process p = Runtime.getRuntime().exec("python /Users/runchangkang/Documents/Minuet/pySpeech/liveTest.py");
                            p.waitFor(30, TimeUnit.SECONDS);  // let the process run for 5 seconds
                            p.destroy();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }
    public void runnerStart(){audioThread.start();}
}
