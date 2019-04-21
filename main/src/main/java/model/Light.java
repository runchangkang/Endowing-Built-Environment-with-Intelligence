package model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Light extends Appliance {

    @Override
    public  String[] execuate(TokenNode userCommand){
        String[] topicNMes = new String[2];
        for(TokenNode node : execuableWords){
            if(NLPHandler.isExecutable(node,userCommand)){
                topicNMes[0]=getTopic();
                topicNMes[1] = node.getCommand();

                return topicNMes;
            }
        }

        return topicNMes;
    }




    @Override
    public String[] execuate(String gesture) {
        String[] topicNMes = new String[2];
        if(gesture.equals("upSwap")){

            topicNMes[0] = getTopic();
            topicNMes[1]="ON";

        }
        if(gesture.equals("downSwap")){

            topicNMes[0] = getTopic();
            topicNMes[1]="OFF";

        }
        System.out.println("trying to execute with command "+ topicNMes[1]);
        return topicNMes;
    }

    public Light( String name, Vector3D location,String topic) {
        super(name, location, topic);


        String[][] turnOn={{"turn","on"},{"on"},{"light"}};
        String[][] turnOff={{"turn","off"},{"off"},{"dim"}};




        String[]gestures={"upSwap","downSwap"};
        for(int i=0;i<turnOn.length;i++){
            addExecuableWord(turnOn[i],"ON");
        }
        for(int j=0;j<turnOff.length;j++){
            addExecuableWord(turnOff[j],"OFF");
    }

        supportedGestures(gestures);
    }




}
