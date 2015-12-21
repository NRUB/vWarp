package vwarp.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import vwarp.VWarp;
import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import static vwarp.VWarp.log;

/**
 *
 * @author NRUB
 */
public class DataBaseRW {

    public static void readFile() throws UnsupportedEncodingException, IOException, StringIndexOutOfBoundsException { //import Warp from .csv file
        VWarp.clearWarpList();
        BufferedReader vWarpDataBase;
        try {
            vWarpDataBase = new BufferedReader(new InputStreamReader(new FileInputStream("plugins/vWarp/vWarp.csv"), "UTF-8"));
            String line;
            do {
                line = vWarpDataBase.readLine();

                if (line != null) {
                    String[] temp = line.split(";");

                    if (temp.length == 8) { //vwarp must have 8 args(nickname, number of vwarp, posX, posY, posZ, Yaw, Pitch, World name)
                        try {
                            String name = temp[0];
                            int nr = parseInt(temp[1]);
                            double x = parseDouble(temp[2]);
                            double y = parseDouble(temp[3]);
                            double z = parseDouble(temp[4]);
                            float yaw = parseFloat(temp[5]);
                            float pitch = parseFloat(temp[6]);
                            String world = temp[7];

                            VWarp.addWarp(new Warp(name, nr, x, y, z, yaw, pitch, world));
                            continue;
                        } catch (NumberFormatException NFex) {
                        }
                    }
                    log.log(Level.WARNING, "[{0}] {1}", new Object[]{log.getName(), line.concat(" is corrupted!")});
                }
            } while (vWarpDataBase.ready());
            vWarpDataBase.close();
            log.log(Level.INFO, "[{0}" + "] " + "vWarp database is loaded!", log.getName());
        } catch (FileNotFoundException FNFex) {
            new File("plugins/vWarp", "vWarp.csv").createNewFile();
            log.log(Level.INFO, "[{0}" + "] " + "File with database of vWarps was not found!", log.getName());
        }
    }

    public static void writeFile() throws UnsupportedEncodingException, FileNotFoundException, IOException { //export Warp list to file
        int iterator = 0;
        new File("plugins/vWarp", "temp.tmp").createNewFile();
        try (BufferedWriter nextRecord = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("plugins/vWarp/temp.tmp", true), "UTF-8"))) {
            while (iterator < VWarp.getWarpList().size()) {
                nextRecord.write((VWarp.getWarpList().get((int) iterator)).toString());
                iterator++;
            }
        }
        new File("plugins/vWarp/vWarp.csv").delete();
        new File("plugins/vWarp/temp.tmp").renameTo(new File("plugins/vWarp/vWarp.csv"));
    }

    public static void makeBackup() throws IOException { //load new version of Warp list and save backup
        String date = "";
        Date today = new Date(System.currentTimeMillis());
        date += today.getYear() + 1900 + "-";
        date += today.getMonth() + 1;
        date += "-" + today.getDate() + " ";
        date += today.getHours() + "_";
        if (today.getMinutes() < 10) {
            date += "0";
        }
        date += today.getMinutes() + "_";
        if (today.getSeconds() < 10) {
            date += "0";
        }
        date += today.getSeconds();
        int iterator = 0;

        new File("plugins/vWarp", ("vWarpBackup " + date + ".back")).createNewFile();
        try (BufferedWriter nextRecord = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(("plugins/vWarp/vWarpBackup " + date + ".back"), true), "UTF-8"))) {
            while (iterator < VWarp.getWarpList().size()) {
                nextRecord.write((VWarp.getWarpList().get((int) iterator)).toString());
                iterator++;
            }
        }
    }

    public static void getBackupsList(CommandSender sender) {
        File[] files = new File("plugins/vWarp/").listFiles();
        ArrayList<String> backups = new ArrayList<>();
        for (File f : files) {
            if (f.getName().endsWith(".back")) {
                backups.add(f.getName());
            }
        }
        if (!backups.isEmpty()) {
            for (String back : backups) {
                sender.sendMessage(ChatColor.GREEN + back);
            }
        }
        else {
            sender.sendMessage(ChatColor.RED + Lang.getMessage("NB"));
        }
    }

    public static void loadBackup(CommandSender sender, String[] DT) throws IOException {
        if (new File("plugins/vWarp/vWarpBackup " + DT[0] + " " + DT[1] + ".back").exists()) {
            makeBackup();
            new File("plugins/vWarp/vWarp.csv").delete();
            new File("plugins/vWarp/vWarpBackup " + DT[0] + " " + DT[1] + ".back").renameTo(new File("plugins/vWarp/vWarp.csv"));
            sender.sendMessage(Lang.getMessage("SBL") + DT[0] + " " + DT[1]);
            readFile();
            writeFile();
        }
        else {
            sender.sendMessage(ChatColor.RED + Lang.getMessage("SBNF"));
        }
    }
}
