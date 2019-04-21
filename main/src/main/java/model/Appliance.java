package model;

import javafx.util.Pair;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class Appliance {
    private String name;
    private Vector3D location;
    private String topic;
    protected Set<TokenNode> execuableWords = new HashSet<>();
    protected Set<String> supportedGestures = new HashSet<>();
    protected Set<String> rootSet = new HashSet<>();

    public Appliance(String name, Vector3D location,String topic) {
        this.name = name;
        this.topic = topic;
        this.location = location;

    }

    public String getApplianceName() {
        return name;
    }
    public void supportedGestures(String[] s) {
        this.supportedGestures.addAll(Arrays.asList(s));
    }
    void addExecuableWord(String[] s,String command) {
        TokenNode root = new TokenNode(s[0]);
        rootSet.add(root.getText());
        root.setCommand(command);
        if (s.length>=2) {
            TokenNode son = new TokenNode(s[1]);
            root.addSon(son,1);
            if(s.length >= 3){
                son.addSon(new TokenNode(s[2]),1);
            }

        }

        this.execuableWords.add(root);
    }
    void addExecuableWords(String[][]s,String command){
        for (String[] each:s) {
            addExecuableWord(each,command);
        }

    }
    public Vector3D getLocation() {
        return location;
    }

    public boolean checkBePointed(Pair<Vector3D, Vector3D> userLocAndPointVec) {
        Vector3D target = userLocAndPointVec.getKey();
        Vector3D pointingVec = userLocAndPointVec.getValue();


        Vector3D oc = location.subtract(target);
        double projectoc = oc.dotProduct(pointingVec);
        if (projectoc<=0)return false;
        double oc2 = oc.dotProduct(oc);
        double distant2 = oc2 - projectoc*projectoc;
        if (Math.toDegrees(Vector3D.angle(oc, pointingVec)) <= 20) {
            System.out.println("angle= "+ Math.toDegrees(Vector3D.angle(oc,pointingVec)));
            System.out.println("distant= "+ Math.sqrt(distant2));
        }
        return (Math.toDegrees(Vector3D.angle(oc,pointingVec))<=20.0);
    }
    public boolean canExeVoiceCommand(TokenNode userCommand) {
        for(TokenNode command : this.execuableWords){
            if(NLPHandler.isExecutable(command,userCommand))return true;
        }
        return false;
    }

    public boolean canExcuGestureCommand(String gesture){
        if(supportedGestures.contains(gesture)){
            return true;
        }
        return false;
    }
//    public abstract boolean canExcuVoiceWithGesture(ArraySet<Object> objects, String curGesture);




    public abstract String[] execuate(TokenNode curCommand);

    public abstract String[] execuate(String curGesture);

    public String getTopic() {
        return topic;
    }
}
