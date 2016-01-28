package vwarp;

import vwarp.system.Warp;
import vwarp.system.Warps;
import vwarp.system.Lang;
import vwarp.system.DataBaseRW;
import vwarp.system.Config;
import vwarp.system.CheckSafety;
import vwarp.system.Blocks;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.bukkit.Bukkit.getWorld;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import vwarp.listeners.JoinListener;
import vwarp.utils.Metrics;
import vwarp.utils.Updater;
import vwarp.utils.Updater.UpdateType;

/**
 *
 * @author NRUB
 */
public final class VWarp extends JavaPlugin {

    public static final Logger log = Logger.getLogger("vWarp");
    private static final List<Warp> warpList = new ArrayList<>();
    private boolean wasAddedOrRemoved = true;
    private final List<Warps> linkedListVWarps = new LinkedList<>();
    private static Updater updater;

    @Override
    public void onEnable() {
        PluginManager listeners = getServer().getPluginManager();
        listeners.registerEvents(new JoinListener(), this);

        if (!(new File("plugins", "vWarp").isDirectory())) {
            new File("plugins", "vWarp").mkdir();
        }
        try {
            DataBaseRW.readFile();
            Lang.load(this);
            Config.load(this);
            Blocks.load(this);
        } catch (IOException | InvalidConfigurationException IOex) {
        }
        List<Warp> corrupted = new LinkedList<>();
        for (Warp v : warpList) {
            try {
                CheckSafety.validate(v);

            } catch (NullPointerException NPex) {
                corrupted.add(v);
            }
        }
        if (!corrupted.isEmpty()) {
            log.log(Level.WARNING, "[{0}] \u001b[31;1m{1}\u001b[0m", new Object[]{log.getName(), Lang.getMessage("DLE")});
            for (Warp w : corrupted) {
                StringBuilder line = new StringBuilder(50);
                line.append(w.getName());
                line.append(";");
                line.append(w.getNumber());
                line.append(";");
                line.append(w.getX());
                line.append(";");
                line.append(w.getY());
                line.append(";");
                line.append(w.getZ());
                line.append(";");
                line.append(w.getYaw());
                line.append(";");
                line.append(w.getPitch());
                line.append(";");
                line.append(w.getWorld());
                line.append(";");
                log.log(Level.WARNING, "[\u001b[31;1m{0}\u001b[0m] {1}", new Object[]{log.getName(), line.toString()});
            }
        }
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException IOex) {
            log.log(Level.WARNING, "[{0}" + "] " + "Metrics failed!", log.getName());
        }
        updater = new Updater(this, 96502, this.getFile(), UpdateType.NO_DOWNLOAD, false);
        if (updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE) {
            log.log(Level.WARNING, "[vWarp] {0}{1}{2}http://dev.bukkit.org/bukkit-plugins/vwarp/{3}", new Object[]{Lang.getMessage("UAL"), updater.getLatestName(), Lang.getMessage("UAM"), Lang.getMessage("UAR")});
        }

        log.log(Level.INFO, "[{0}" + "] " + "vWarp started", log.getName());
    }

    @Override
    public void onDisable() {
        try {
            DataBaseRW.writeFile();
        } catch (FileNotFoundException FNFex) {
        } catch (IOException IOex) {
        }

        log.log(Level.INFO, "[{0}" + "] " + "vWarp stopped", log.getName());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vwarp")) { //use vwarp (only in game)
            return vwarp(sender, args);
        }
        else if (cmd.getName().equalsIgnoreCase("setvwarp")) { //set vwarp (only in game)
            return setwarp(sender, args);
        }
        else if (cmd.getName().equalsIgnoreCase("delvwarp")) { //delete vwarp (only in game)
            return delwarp(sender, args);
        }
        else if (cmd.getName().equalsIgnoreCase("asetvwarp")) { //set vwarp by the administrator (game and command prompt)
            return asetwarp(sender, args);
        }
        else if (cmd.getName().equalsIgnoreCase("adelvwarp")) { //delete vwarp by the administrator (game and command prompt)
            return adelwarp(sender, args);
        }
        else if (cmd.getName().equalsIgnoreCase("vwarps")) { //shows list of all vwarps
            return vwarps(sender, args);
        }
        else if (cmd.getName().equalsIgnoreCase("vreload")) { //save database, close plugin, run it again and load database
            return vreload(sender);
        }
        else if (cmd.getName().equalsIgnoreCase("vrepair")) { //close plugin (without saving database), run it again and load database 
            return vrepair(sender);
        }
        else if (cmd.getName().equalsIgnoreCase("vlist")) { //show list of all database backups
            return vlist(sender);
        }
        else if (cmd.getName().equalsIgnoreCase("vbackuprestore")) { //restore selected backup
            return vbackuprestore(sender, args);
        }
        else if (cmd.getName().equalsIgnoreCase("vupdate")) { //update plugin
            return vupdate(sender);
        }
        return false;
    }

    public static List<Warp> getWarpList() {
        return warpList;
    }

    public static void addWarp(Warp newWarp) {
        warpList.add(newWarp);
    }

    public static void clearWarpList() {
        warpList.clear();
    }

    public static Updater getUpdater() {
        return updater;
    }

    private boolean vwarp(final CommandSender sender, String[] args) {
        if (sender instanceof Player) { //use by the player
            if (args.length == 0) {
                return false;
            }
            if (args.length == 1) { //use default (0) vwarp
                final int j;
                for (int i = 0; i < warpList.size(); i++) {
                    if (warpList.get(i).getName().equalsIgnoreCase(args[0]) && (warpList.get(i).getNumber()) == 0) {
                        String w = warpList.get(i).getWorld();
                        double x = warpList.get(i).getX();
                        double y = warpList.get(i).getY();
                        double z = warpList.get(i).getZ();
                        float yaw = warpList.get(i).getYaw();
                        float pitch = warpList.get(i).getPitch();

                        final Location loc = new Location(getWorld(w), x, y, z, yaw, pitch);

                        final int[] A = new int[3];
                        A[0] = (int) ((Player) sender).getLocation().getX();
                        A[1] = (int) ((Player) sender).getLocation().getY();
                        A[2] = (int) ((Player) sender).getLocation().getZ();

                        j = i;

                        String safety = CheckSafety.checkSafety(warpList.get(i));
                        if (!safety.equals("")) {
                            sender.sendMessage(Lang.getMessage(safety));
                        }

                        if (!safety.equals("SVID")) {
                            if (Config.waitTime((Player) sender) != 0) {
                                sender.sendMessage(Lang.getMessage("WOTTvwarp"));
                                int counter = 0;
                                int maxTicks = Config.waitTime((Player) sender) * 20;
                                dustTeleportTask(loc, counter, maxTicks - 30);
                                dustTeleportTask(((Player) sender).getLocation(), counter, maxTicks);
                            }

                            getServer().getScheduler().scheduleSyncDelayedTask(this, () -> { //delayed teleport task
                                int[] B = new int[3];
                                B[0] = (int) ((Player) sender).getLocation().getX();
                                B[1] = (int) ((Player) sender).getLocation().getY();
                                B[2] = (int) ((Player) sender).getLocation().getZ();

                                if (A[0] == B[0] && A[1] == B[1] && A[2] == B[2]) {
                                    ((Player) sender).teleport(loc);
                                    sender.sendMessage(Lang.getMessage("TTvwarp") + warpList.get(j).getName() + "!");
                                }
                                else {
                                    sender.sendMessage(Lang.getMessage("ATRTvwarp"));
                                }
                            }, 20 * Config.waitTime((Player) sender));
                        }
                        return true;
                    }
                }
                sender.sendMessage(Lang.getMessage("NE"));
                return true;
            }
            if (args.length == 2) { //use extra vwarp
                final int nr;
                try {
                    nr = parseInt(args[1]);
                } catch (NumberFormatException NFex) { //second argument is not a number
                    return false;
                }
                final int j;
                for (int i = 0; i < warpList.size(); i++) {
                    if (warpList.get(i).getName().equalsIgnoreCase(args[0]) && (warpList.get(i).getNumber()) == nr) {
                        String w = warpList.get(i).getWorld();
                        double x = warpList.get(i).getX();
                        double y = warpList.get(i).getY();
                        double z = warpList.get(i).getZ();
                        float yaw = warpList.get(i).getYaw();
                        float pitch = warpList.get(i).getPitch();

                        final Location loc = new Location(getWorld(w), x, y, z, yaw, pitch);

                        final int[] A = new int[3];
                        A[0] = (int) ((Player) sender).getLocation().getX();
                        A[1] = (int) ((Player) sender).getLocation().getY();
                        A[2] = (int) ((Player) sender).getLocation().getZ();

                        j = i;

                        String safety = CheckSafety.checkSafety(warpList.get(i));
                        if (!safety.equals("")) {
                            sender.sendMessage(Lang.getMessage(safety));
                        }

                        if (!safety.equals("SVID")) {
                            if (Config.waitTime((Player) sender) != 0) {
                                sender.sendMessage(Lang.getMessage("WOTTvwarp"));
                                int counter = 0;
                                int maxTicks = Config.waitTime((Player) sender) * 20;
                                dustTeleportTask(loc, counter, maxTicks - 30);
                                dustTeleportTask(((Player) sender).getLocation(), counter, maxTicks);
                            }

                            getServer().getScheduler().scheduleSyncDelayedTask(this, () -> { //delayed teleport task
                                int[] B = new int[3];
                                B[0] = (int) ((Player) sender).getLocation().getX();
                                B[1] = (int) ((Player) sender).getLocation().getY();
                                B[2] = (int) ((Player) sender).getLocation().getZ();

                                if (A[0] == B[0] && A[1] == B[1] && A[2] == B[2]) {
                                    ((Player) sender).teleport(loc);
                                    sender.sendMessage((nr == 0) ? (Lang.getMessage("TTvwarp") + warpList.get(j).getName() + "!") : (Lang.getMessage("TTvwarp") + warpList.get(j).getName() + " - " + nr + "!"));
                                }
                                else {
                                    sender.sendMessage(Lang.getMessage("ATRTvwarp"));
                                }
                            }, 20 * Config.waitTime((Player) sender));
                        }
                        return true;
                    }
                }
                sender.sendMessage(Lang.getMessage("NE"));
                return true;
            }
        }
        else { //use in command prompt
            sender.sendMessage(Lang.getMessage("NICP"));
            return true;
        }
        return false;
    }

    private boolean setwarp(CommandSender sender, String[] args) {
        if (sender instanceof Player) { //use by the player
            if (args.length == 0 && canSetVWarpNumberException((Player) sender, 0) && canSetVWarpWorldException((Player) sender)) { //set default vwarp
                Warp temp = new Warp(sender.getName(), 0, ((Player) sender).getLocation().getX(), ((Player) sender).getLocation().getY(), ((Player) sender).getLocation().getZ(), ((Player) sender).getLocation().getYaw(), ((Player) sender).getLocation().getPitch(), ((Player) sender).getWorld().getName());
                for (int i = 0; i < warpList.size(); i++) {
                    if (warpList.get(i).getName().equals(temp.getName()) && (warpList.get(i).getNumber()) == (temp.getNumber())) {
                        warpList.remove(i);
                        i = warpList.size();
                    }
                }
                warpList.add(temp);
                sender.sendMessage(Lang.getMessage("CDvwarp"));
                if (!CheckSafety.insecure(temp)) {
                    dustStartTask(temp.getLocation(), 0, 30);
                }
                wasAddedOrRemoved = true;
                return true;
            }
            if (args.length == 1) { //set extra vwarp
                int nr;
                try {
                    nr = parseInt(args[0]);
                    nr = Math.abs(nr);
                } catch (NumberFormatException NFex) { //first argument is not a number
                    return false;
                }
                if (canSetVWarpNumberException((Player) sender, nr) && canSetVWarpWorldException((Player) sender)) {
                    Warp temp = new Warp(sender.getName(), nr, ((Player) sender).getLocation().getX(), ((Player) sender).getLocation().getY(), ((Player) sender).getLocation().getZ(), ((Player) sender).getLocation().getYaw(), ((Player) sender).getLocation().getPitch(), ((Player) sender).getWorld().getName());
                    for (int i = 0; i < warpList.size(); i++) {
                        if (warpList.get(i).getName().equals(temp.getName()) && (warpList.get(i).getNumber()) == (temp.getNumber())) {
                            warpList.remove(i);
                            i = warpList.size();
                        }
                    }
                    warpList.add(temp);
                    sender.sendMessage((nr == 0) ? (Lang.getMessage("CDvwarp")) : (Lang.getMessage("CEvwarp") + nr + "."));
                    if (!CheckSafety.insecure(temp)) {
                        dustStartTask(temp.getLocation(), 0, 30);
                    }
                    wasAddedOrRemoved = true;
                    return true;
                }
                else if (canSetVWarpWorldException((Player) sender)) { //typing too large number of vwarp
                    sender.sendMessage(Lang.getMessage("TBNOvwarp"));
                    return true;
                }
            }
            if (!canSetVWarpWorldException((Player) sender)) {
                sender.sendMessage(Lang.getMessage("BW"));
                return true;
            }
            else { //typing too many arguments
                return false;
            }
        }
        else { //use in command prompt
            sender.sendMessage(Lang.getMessage("NICP"));
            return true;
        }
    }

    private boolean delwarp(CommandSender sender, String[] args) {
        if (sender instanceof Player) { //use by the player             
            if (args.length == 0) { //delete default vwarp
                for (int i = 0; i < warpList.size(); i++) {
                    if (warpList.get(i).getName().equals(sender.getName()) && (warpList.get(i).getNumber()) == 0) {
                        warpList.remove(i);
                        i = warpList.size();
                    }
                }
                sender.sendMessage(Lang.getMessage("DDvwarp"));
                wasAddedOrRemoved = true;
                return true;
            }
            if (args.length == 1) { //delete extra vwarp
                int nr;
                try {
                    nr = parseInt(args[0]);
                    nr = Math.abs(nr);

                    for (int i = 0; i < warpList.size(); i++) {
                        if (warpList.get(i).getName().equals(sender.getName()) && (warpList.get(i).getNumber()) == nr) {
                            warpList.remove(i);
                            i = warpList.size();
                        }
                    }
                    sender.sendMessage((nr == 0) ? (Lang.getMessage("DDvwarp")) : (Lang.getMessage("DEvwarpF") + nr + Lang.getMessage("DEvwarpE")));
                    wasAddedOrRemoved = true;
                    return true;
                } catch (NumberFormatException NFex) { //first argument is not a number
                    return false;
                }
            }
            else { //typing too many arguments
                return false;
            }
        }
        else { //use in command prompt
            sender.sendMessage(Lang.getMessage("NICP"));
            return true;
        }
    }

    private boolean asetwarp(CommandSender sender, String[] args) {
        if (sender instanceof Player) { //use by the administrator
            if (args.length == 0) { //empty user name
                return false;
            }
            if (args.length == 1) { //set defaul vwarp for user by the administrator
                Warp temp = new Warp(args[0], 0, ((Player) sender).getLocation().getX(), ((Player) sender).getLocation().getY(), ((Player) sender).getLocation().getZ(), ((Player) sender).getLocation().getYaw(), ((Player) sender).getLocation().getPitch(), ((Player) sender).getWorld().getName());
                for (int i = 0; i < warpList.size(); i++) {
                    if (warpList.get(i).getName().equals(temp.getName()) && (warpList.get(i).getNumber()) == 0) {
                        warpList.remove(i);
                        i = warpList.size();
                    }
                }
                warpList.add(temp);
                sender.sendMessage(Lang.getMessage("ACDvwarp") + args[0]);
                if (!CheckSafety.insecure(temp)) {
                    dustStartTask(temp.getLocation(), 0, 30);
                }
                wasAddedOrRemoved = true;
                return true;
            }
            if (args.length == 2) { //set extra vwarp for user by the administrator
                int nr;
                try {
                    nr = parseInt(args[1]);
                    nr = Math.abs(nr);

                    Warp temp = new Warp(args[0], nr, ((Player) sender).getLocation().getX(), ((Player) sender).getLocation().getY(), ((Player) sender).getLocation().getZ(), ((Player) sender).getLocation().getYaw(), ((Player) sender).getLocation().getPitch(), ((Player) sender).getWorld().getName());
                    for (int i = 0; i < warpList.size(); i++) {
                        if (warpList.get(i).getName().equals(temp.getName()) & (warpList.get(i).getNumber()) == nr) {
                            warpList.remove(i);
                            i = warpList.size();
                        }
                    }
                    warpList.add(temp);
                    sender.sendMessage((nr == 0) ? (Lang.getMessage("ACDvwarp") + args[0]) : (Lang.getMessage("ACEvwarpF") + nr + Lang.getMessage("ACEvwarpE") + args[0]));
                    if (!CheckSafety.insecure(temp)) {
                        dustStartTask(temp.getLocation(), 0, 30);
                    }
                    wasAddedOrRemoved = true;
                    return true;
                } catch (NumberFormatException NFex) { //second argument is not a number
                    return false;
                }
            }
            else { //typing too many arguments
                return false;
            }
        }
        else { //use in command prompt
            sender.sendMessage(Lang.getMessage("NICP"));
            return true;
        }
    }

    private boolean adelwarp(CommandSender sender, String[] args) {
        if (sender instanceof Player) { //use by the administrator
            if (args.length == 0) { //empty user name
                return false;
            }
            if (args.length == 1) { //delete deafult vwarp by the administrator
                for (int i = 0; i < warpList.size(); i++) {
                    if (warpList.get(i).getName().equals(args[0]) && (warpList.get(i).getNumber()) == 0) {
                        warpList.remove(i);
                        i = warpList.size();
                    }
                }
                sender.sendMessage(Lang.getMessage("ADDvwarp") + args[0]);
                wasAddedOrRemoved = true;
                return true;
            }
            if (args.length == 2) { //delete extra vwarp by the administrator
                int nr;
                try {
                    nr = parseInt(args[1]);
                    nr = Math.abs(nr);

                    for (int i = 0; i < warpList.size(); i++) {
                        if (warpList.get(i).getName().equals(args[0]) && (warpList.get(i).getNumber()) == nr) {
                            warpList.remove(i);
                            i = warpList.size();
                        }
                    }
                    sender.sendMessage((nr == 0) ? (Lang.getMessage("ADDvwarp") + args[0]) : (Lang.getMessage("ADEvwarpF") + nr + Lang.getMessage("ADEvwarpE") + args[0]));
                    wasAddedOrRemoved = true;
                    return true;
                } catch (NumberFormatException NFex) { //second argument is not a number
                    return false;
                }
            }
            else { //typing too many arguments
                return false;
            }
        }
        else { //use in command prompt
            if (args.length == 0) { //empty user name
                sender.sendMessage(Lang.getMessage("CUICP"));
                return true;
            }
            if (args.length == 1) { //delete default vwarp for user by the administrator
                for (int i = 0; i < warpList.size(); i++) {
                    if (warpList.get(i).getName().equals(args[0]) && (warpList.get(i).getNumber()) == 0) {
                        warpList.remove(i);
                        i = warpList.size();
                    }
                }
                sender.sendMessage(Lang.getMessage("ADDvwarp") + args[0]);
                wasAddedOrRemoved = true;
                return true;
            }
            if (args.length == 2) { //delete extra vwarp for user by the administrator
                int nr;
                try {
                    nr = parseInt(args[1]);
                } catch (NumberFormatException NFex) {
                    sender.sendMessage(Lang.getMessage("CUICP"));
                    return true;
                }
                for (int i = 0; i < warpList.size(); i++) {
                    if (warpList.get(i).getName().equals(args[0]) && (warpList.get(i).getNumber()) == nr) {
                        warpList.remove(i);
                        i = warpList.size();
                    }
                }
                sender.sendMessage((nr == 0) ? (Lang.getMessage("ADDvwarp") + args[0]) : (Lang.getMessage("ADEvwarpF") + nr + Lang.getMessage("ADEvwarpE") + args[0]));
                wasAddedOrRemoved = true;
                return true;
            }
            else {
                sender.sendMessage(Lang.getMessage("CUICP"));
                return true;
            }
        }
    }

    private boolean vwarps(CommandSender sender, String[] args) {
        if (wasAddedOrRemoved) {
            reloadList();
        }
        if (sender instanceof Player) { //use by the player
            if (args.length == 0) {
                sender.sendMessage(Lang.getMessage("CUV"));
                return true;
            }
            if (args.length == 1) {
                int nr;
                try {
                    nr = parseInt(args[0]);
                    nr = Math.abs(nr);

                    if (nr <= 0) {
                        sender.sendMessage(Lang.getMessage("CUV"));
                        return true;
                    }
                    sender.sendMessage(Lang.getMessage("POL") + nr);
                    nr = (--nr) * 15;
                    if (nr > linkedListVWarps.size()) {
                        return true;
                    }
                    for (int count = 0; count < 15; count++) {
                        if (nr + count < linkedListVWarps.size()) {
                            sender.sendMessage(linkedListVWarps.get(count + nr).toString());
                        }
                    }
                    return true;
                } catch (NumberFormatException NFex) { //second argument is not a number
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else { //use in command prompt
            if (args.length == 0) {
                for (Warps key : linkedListVWarps) {
                    sender.sendMessage(key.toString());
                }
                return true;
            }
            else {
                sender.sendMessage(Lang.getMessage("NICP"));
                return true;
            }
        }
    }

    public boolean vreload(CommandSender sender) {
        try {
            DataBaseRW.writeFile();
            DataBaseRW.readFile();
            Lang.load(this);
            Config.load(this);
            Blocks.load(this);
        } catch (InvalidConfigurationException ICex) {
            sender.sendMessage(Lang.getMessage("RI") + " - InvalidConfigurationException");
        } catch (IOException IOex) {
            sender.sendMessage(Lang.getMessage("RI") + " - IOException");
        }
        reloadList();
        sender.sendMessage(Lang.getMessage("RP"));
        return true;
    }

    public boolean vrepair(CommandSender sender) {
        if (!(sender instanceof Player) || sender.isOp()) { //use in command prompt or by the OP player
            try {
                DataBaseRW.makeBackup();
            } catch (IOException IOex) {
                sender.sendMessage(Lang.getMessage("BI") + " - IOException");
            }

            try {
                DataBaseRW.readFile();
            } catch (IOException IOex) {
                sender.sendMessage(Lang.getMessage("RI") + " - IOException");
            }

            reloadList();
            sender.sendMessage(Lang.getMessage("BP"));
        }
        else {
            sender.sendMessage(getCommand("vrepair").getPermissionMessage());
        }
        return true;
    }

    public boolean vlist(CommandSender sender) {
        if (!(sender instanceof Player) || sender.isOp()) { //use in command prompt or by the OP player
            DataBaseRW.getBackupsList(sender);
        }
        else {
            sender.sendMessage(getCommand("vlist").getPermissionMessage());
        }
        return true;
    }

    public boolean vbackuprestore(CommandSender sender, String[] dateAndTime) {
        if (!(sender instanceof Player) || sender.isOp()) { //use in command prompt or by the OP player
            if (dateAndTime.length == 2) {
                try {
                    DataBaseRW.loadBackup(sender, dateAndTime);
                    reloadList();
                } catch (IOException IOex) {
                    sender.sendMessage(Lang.getMessage("SBI") + " - IOException");
                }
            }
            else {
                return false;
            }
        }
        else {
            sender.sendMessage(getCommand("vbackuprestore").getPermissionMessage());
        }
        return true;
    }

    public boolean vupdate(CommandSender sender) {
        if (!(sender instanceof Player) || sender.hasPermission("vwarp.vupdate")) { //use in command prompt or by authorized player
            if (updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE) {
                updater = new Updater(this, 96502, this.getFile(), UpdateType.NO_VERSION_CHECK, true);
            }
        }
        else {
            sender.sendMessage(getCommand("vbackuprestore").getPermissionMessage());
        }
        return true;
    }

    private boolean canSetVWarpNumberException(Player player, int nr) {
        return nr < Config.getMaxNumber(player);
    }

    private boolean canSetVWarpWorldException(Player player) {
        return Config.isOnRightWorld(player);
    }

    private void reloadList() {
        linkedListVWarps.clear();
        boolean wasAdd;
        Warp warp;
        Warps warps;

        Iterator<Warp> itWarp = warpList.iterator();
        while (itWarp.hasNext()) {
            warp = itWarp.next();
            wasAdd = false;
            Iterator<Warps> itWarps = linkedListVWarps.iterator();
            while (itWarps.hasNext() && !wasAdd) {
                warps = itWarps.next();
                if (warps.getNick().equals(warp.getName())) {
                    LinkedList<Integer> temp = new LinkedList<>(warps.getNumbers());
                    temp.add(warp.getNumber());
                    linkedListVWarps.remove(warps);
                    linkedListVWarps.add(new Warps(warps.getNick(), temp));
                    wasAdd = true;
                }
            }
            if (!wasAdd) {
                LinkedList<Integer> LL = new LinkedList<>();
                LL.add(warp.getNumber());
                linkedListVWarps.add(new Warps(warp.getName(), LL));
            }
        }
        wasAddedOrRemoved = false;
        Collections.sort(linkedListVWarps);
    }

    private void dustTeleportTask(final Location loc, final int counter, final int maxTicks) {
        Random rand = new Random();
        getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            for (int i = 0; i < 40; i++) {
                double x = rand.nextInt(40) - 20;
                x /= 100;
                double y = rand.nextInt(200);
                y /= 100;
                double z = rand.nextInt(40) - 20;
                z /= 100;
                Location effect = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z);
                (effect.getWorld()).playEffect(effect, Effect.PORTAL, 10);
            }
            if (counter <= maxTicks) {
                dustTeleportTask(loc, counter + 1, maxTicks);
            }
        }, 1);
    }

    private void dustStartTask(final Location loc, final int counter, final int maxTicks) {
        Random rand = new Random();
        getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            for (int i = 0; i < 40; i++) {
                double x = rand.nextInt(80) - 40;
                x /= 100;
                double z = rand.nextInt(80) - 40;
                z /= 100;
                Location effect = new Location(loc.getWorld(), loc.getX() + x, loc.getY(), loc.getZ() + z);
                (effect.getWorld()).playEffect(effect, Effect.PORTAL, 10);
            }
            if (counter <= maxTicks) {
                dustStartTask(loc, counter + 1, maxTicks);
            }
        }, 1);
    }
}
