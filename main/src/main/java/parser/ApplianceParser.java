package parser;

import model.*;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ApplianceParser {

    public void getAppliances(MinuetSystem world, File file){
        Yaml yaml = new Yaml(new Constructor(AppliancesBean.class));
        FileInputStream fi = null;
        AppliancesBean res = null;
        try{
            fi = new FileInputStream(file.getAbsoluteFile());
            res = (AppliancesBean) yaml.load(fi);
            for (ApplianceBean p: res.getAppliance()) {

                Appliance appliance = getRealAppliance(p);
                if(appliance != null){
                    world.addAppliance(appliance);
                }

            }


        }catch (FileNotFoundException e){
            e.printStackTrace();
        }


    }



    private Appliance getRealAppliance(ApplianceBean applianceBean){
        Appliance appliance = null;
        String type = applianceBean.getType();
        Vector3D loc = new Vector3D(applianceBean.getLocationX(),applianceBean.getLocationY(),applianceBean.getLocationZ());
        switch (type){
            case "Light":
                appliance = new Light(applianceBean.getName(),loc,applianceBean.getTopic());
                break;
            case "LightStrip":
                appliance = new LightStrip(applianceBean.getName(),loc,applianceBean.getTopic());
                break;
            case "SoundModule":
                appliance = new SoundModule(applianceBean.getName(),loc,applianceBean.getTopic());
                break;
            case "LightSoundStrip":
                appliance = new LightSoundStrip(applianceBean.getName(),loc,applianceBean.getTopic());
                break;
//            case "ColorLight":
//                appliance = new LightStrip(applianceBean.getName(),loc,applianceBean.getTopic());
//                break;
        }
        return appliance;
    }
}
