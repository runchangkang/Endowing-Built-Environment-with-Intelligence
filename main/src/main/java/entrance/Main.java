package entrance;

import controller.SystemController;
import model.MinuetSystem;
import model.User;
import parser.ApplianceParser;
import parser.PluginParser;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        MinuetSystem system = new MinuetSystem();
        system.addUser(new User("Richard"));
//        system.addUser(new User("Tim"));

        File applianceFile =new File("main/src/resources/appliance.yml");
        ApplianceParser applianceParser = new ApplianceParser();
        applianceParser.getAppliances(system,applianceFile);
        System.out.println("System has appliance in amount of :" + system.getApplianceMap().entrySet().size());

        SystemController systemController = new SystemController(system);
        File file = new File("main/src/resources/rules.yml");
        PluginParser pluginParser = new PluginParser();
        pluginParser.getPlugins(system,file);





    }
}
