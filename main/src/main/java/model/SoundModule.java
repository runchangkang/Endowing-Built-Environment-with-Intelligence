package model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class SoundModule  extends Appliance  {
    public SoundModule(String name, Vector3D location, String topic) {
        super(name, location, topic);

        String[][] play={{"play"}};





        String[]gestures={"upSwap"};
        for(int i=0;i<play.length;i++){
            addExecuableWord(play[i],"0");
        }


        supportedGestures(gestures);
    }

    @Override
    public String[] execuate(TokenNode curCommand) {
        String[] topicNMes = new String[2];
        for(TokenNode node : execuableWords){
            if(NLPHandler.isExecutable(node,curCommand)){
                topicNMes[0]=getTopic();

                topicNMes[1] = node.getCommand();
                return topicNMes;
            }
        }

        return topicNMes;
    }

    @Override
    public String[] execuate(String curGesture) {
        String[] topicNMes = new String[2];
        if(curGesture.equals("upSwap")){

            topicNMes[0] = getTopic();
            topicNMes[1]="-1";

        }

        return topicNMes;
    }
}
