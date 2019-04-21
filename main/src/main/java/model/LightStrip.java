package model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class LightStrip extends Appliance  {

    public Vector3D getHead() {
        return head;
    }

    public void setHead(Vector3D head) {
        this.head = head;
    }

    public Vector3D getTail() {
        return tail;
    }

    public void setTail(Vector3D tail) {
        this.tail = tail;
    }

    private Vector3D head;
    private Vector3D tail;

    public LightStrip(String name, Vector3D location, String topic) {
        super(name, location, topic);

        String[][] cycle={{"cycle"}};
        String[][] show={{"show"}};
        String[][] disappear={{"disappear"}};

        String[][] color={{"color"}};





        String[]gestures={"leftSwap","rightSwap","circleCW","circleCCW"};
        for(int i=0;i<cycle.length;i++){
            addExecuableWord(cycle[i],"2");
        }
        for(int j=0;j<show.length;j++){
            addExecuableWord(show[j],"0");
        }

        for(int j=0;j<disappear.length;j++){
            addExecuableWord(disappear[j],"1");
        }
        for(int j=0;j<color.length;j++){
            addExecuableWord(color[j],"5");
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
        if(curGesture.equals("leftSwap")){

            topicNMes[0] = getTopic();
            topicNMes[1]="1";

        }
        if(curGesture.equals("rightSwap")){

            topicNMes[0] = getTopic();
            topicNMes[1]="0";

        }
        if(curGesture.equals("circleCW")){

            topicNMes[0] = getTopic();
            topicNMes[1]="2";

        }
        if(curGesture.equals("circleCCW")){

            topicNMes[0] = getTopic();
            topicNMes[1]="5";

        }

        return topicNMes;

    }
}
