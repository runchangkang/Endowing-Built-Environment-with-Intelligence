package parser;

import model.Appliance;
import model.MinuetSystem;
import model.NLPHandler;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import plugin.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;



public class PluginParser {

    public void getPlugins(MinuetSystem world, File file){
        Yaml yaml = new Yaml(new Constructor(PluginsBean.class));
        FileInputStream fi = null;
        PluginsBean res = null;
        try{
            fi = new FileInputStream(file.getAbsoluteFile());
            res = (PluginsBean) yaml.load(fi);
            for (PluginBean p: res.getPlugins()
                 ) {

                Appliance findAppliance =  world.getApplianceMap().get(p.getName());
                if(findAppliance == null){
                    System.err.println("Plugin parsing error, can't find appliance by name");
                    return;
                }



                for(String plugin : p.getFunctions().keySet()){

                    PluginTemplate newPlugin = getRealPlugin(findAppliance,plugin,p.getFunctions().get(plugin));

                    if(newPlugin != null)
                        newPlugin.setWorld(world);
                        newPlugin.start();
                }


            }


        }catch (FileNotFoundException e){
            e.printStackTrace();
        }


    }



    private PluginTemplate getRealPlugin(Appliance appliance, String type, Map<String,String> attributes){
        PluginTemplate pluginTemplate = null;
        switch (type){
            case "proximity":
                try {
                    pluginTemplate = new DistancePlugin(appliance);


                    ((DistancePlugin) pluginTemplate).setInRangeCommand(NLPHandler.parse(attributes.get("inRange")));
                    ((DistancePlugin) pluginTemplate).setOutRangeCommand(NLPHandler.parse(attributes.get("outRange")));
                    ((DistancePlugin) pluginTemplate).setDistant(Integer.parseInt(attributes.get("distance")));
                    break;
                } catch (Exception e) {
                    e.printStackTrace();

                }

            case "follow":
                try {
                    pluginTemplate = new FollowPlugin(appliance);

                    try {
                        ((FollowPlugin) pluginTemplate).setFollowingCommand(attributes.get("followingCommand"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            case "movement":
                try {
                    pluginTemplate = new MovementPlugin(appliance);
                    ((MovementPlugin) pluginTemplate).setMovingCommand(NLPHandler.parse(attributes.get("moving")));
                    ((MovementPlugin) pluginTemplate).setStableCommand(NLPHandler.parse(attributes.get("stable")));


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "specialZone":
                try {
                    pluginTemplate = new SpecialZonePlugin(appliance);
                    ((SpecialZonePlugin) pluginTemplate).setInRangeCommand(NLPHandler.parse(attributes.get("inRange")));
                    ((SpecialZonePlugin) pluginTemplate).setOutRangeCommand(NLPHandler.parse(attributes.get("outRange")));
                    ((SpecialZonePlugin) pluginTemplate).setMaxX(Integer.parseInt(attributes.get("maxX")));
                    ((SpecialZonePlugin) pluginTemplate).setMaxY(Integer.parseInt(attributes.get("maxY")));
                    ((SpecialZonePlugin) pluginTemplate).setMinX(Integer.parseInt(attributes.get("minX")));
                    ((SpecialZonePlugin) pluginTemplate).setMinY(Integer.parseInt(attributes.get("minY")));




                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


        }


        return pluginTemplate;
    }
}
