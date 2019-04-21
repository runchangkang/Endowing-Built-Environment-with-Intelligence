package model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class LightSoundStrip extends Appliance  {


    public LightSoundStrip(String name, Vector3D location, String topic) {
        super(name, location, topic);


        String[][] start={{"start"}};
        String[][] stop={{"stop"}};







        for(int i=0;i<start.length;i++){
            addExecuableWord(start[i],"start");
        }
        for(int j=0;j<stop.length;j++){
            addExecuableWord(stop[j],"stop");
        }




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
        return new String[0];

    }
}
