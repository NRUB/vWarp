package vwarp.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author NRUB
 */
public class Blocks {

    private static List<String> safe;
    private static List<String> dangerous;

    public static void load(JavaPlugin plugin) throws IOException, FileNotFoundException, InvalidConfigurationException {
        YamlConfiguration configBlocks = new YamlConfiguration();
        InputStream loadBlocks;
        try {
            loadBlocks = new FileInputStream(new File("plugins/vWarp/blocks.yml"));
        } catch (FileNotFoundException FNFex) {
            try (InputStream in = plugin.getResource("blocks.yml"); OutputStream out = new FileOutputStream(new File("plugins/vWarp", "blocks.yml"))) {
                byte[] buf = new byte[2048];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            loadBlocks = new FileInputStream(new File("plugins/vWarp/blocks.yml"));
        }
        configBlocks.load("plugins/vWarp/blocks.yml");
        safe = configBlocks.getStringList("safe");
        dangerous = configBlocks.getStringList("dangerous");
    }

    public static List<String> getSafeBlockList() {
        return safe;
    }

    public static List<String> getDangerousBlockList() {
        return dangerous;
    }
}
