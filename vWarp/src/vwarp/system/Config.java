package vwarp.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import static vwarp.VWarp.log;

/**
 *
 * @author NRUB
 */
public class Config {

    private static Map<String, Integer> configPack;
    private static List<String> groups;
    private static int delay=3;
    private static boolean oneWorld=false;
    private static List<String> nameOfWorld;

    public static void load(JavaPlugin plugin) throws IOException, FileNotFoundException, InvalidConfigurationException {
        Yaml configMap=new Yaml();
        YamlConfiguration config=new YamlConfiguration();
        InputStream loadConfig;
        try {
            loadConfig=new FileInputStream(new File("plugins/vWarp/config.yml"));
        }
        catch (FileNotFoundException FNFex) {
            InputStream in=plugin.getResource("config.yml");
            OutputStream out=new FileOutputStream(new File("plugins/vWarp", "config.yml"));
            byte[] buf=new byte[2048];
            int len;
            while ((len=in.read(buf))>0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            loadConfig=new FileInputStream(new File("plugins/vWarp/config.yml"));
        }
        configPack=(Map<String, Integer>) configMap.load(loadConfig);
        config.load("plugins/vWarp/config.yml");
        groups=config.getStringList("groups");
        try {
            delay=Integer.parseInt(config.getString("delay"));
        }
        catch (NumberFormatException NFex) {
            log.warning("["+log.getName()+"] "+"Time of delay is wrong. Uses the default.");
        }
        oneWorld=config.getBoolean("only_one_world");
        if (oneWorld) {
            nameOfWorld=config.getStringList("name");
        }
    }

    private static int getNumber(String group) {
        if (configPack.containsKey(group)) {
            return configPack.get(group);
        }
        return 0;
    }

    private static String getGroup(Player player) {
        int higherNumber=0;
        String theBestGroup="User";
        PermissionUser user=PermissionsEx.getUser(player);
        for (String key:configPack.keySet()) {
            if (user.inGroup(key)&&higherNumber<configPack.get(key)) {
                theBestGroup=key;
            }
        }
        return theBestGroup;
    }

    public static int getMaxNumber(Player player) {
        return getNumber(getGroup(player));
    }

    public static boolean isOnRightWorld(Player player) {
        if (oneWorld) {
            return nameOfWorld.contains(player.getWorld().getName());
        }
        return true;
    }

    public static int waitTime(Player player) {
        boolean mustWait=true;
        PermissionUser user=PermissionsEx.getUser(player);
        String[] perms=user.getGroupsNames();
        Iterator<String> OP=groups.iterator();
        while (OP.hasNext()&&mustWait) {
            String thisGroup=OP.next();
            for (int i=0; i<perms.length; i++) {
                if (thisGroup.equals(perms[i])) {
                    mustWait=false;
                    i=perms.length;
                }
            }
        }
        if (mustWait) {
            return delay;
        }
        else {
            return 0;
        }
    }
}
