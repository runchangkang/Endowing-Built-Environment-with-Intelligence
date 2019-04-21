package model;

import com.google.cloud.language.v1.Token;

import java.util.HashMap;
import java.util.Map;

public class TokenNode {

    private Map<Integer,TokenNode> sons = new HashMap<>();
    private String text;
    private String command;
    public TokenNode(Token itself) {
        text = itself.getLemma();
    }
    public TokenNode(String text){
        this.text = text;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void addSon(TokenNode son, int index){
        sons.put(index,son);
    }
    public String getText(){return text;}

    public Map<Integer, TokenNode> getSons() {
        return sons;
    }
    public boolean hasSons(){return sons.size()>0;}
    public boolean hasThisSons(String text){
        for(TokenNode son : sons.values()){
            if (son.getText().equals(text))return true;
        }
        return false;
    }
    public TokenNode getThisSon(String text){
        for(TokenNode son: sons.values()){
            if(son.getText().equals(text))return son;
        }
        return null;
    }
}
