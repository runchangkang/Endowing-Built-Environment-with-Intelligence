package parser;

import java.util.Map;

public class PluginBean {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    private String name;

    public Map<String, Map<String, String>> getFunctions() {
        return functions;
    }

    public void setFunctions(Map<String, Map<String, String>> functions) {
        this.functions = functions;
    }

    private Map<String,Map<String,String>> functions;

}
