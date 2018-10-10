package vwarp.system;

import java.util.List;
import org.bukkit.Bukkit;
import static org.bukkit.Bukkit.getWorld;
import org.bukkit.Location;

/**
 *
 * @author NRUB
 */
public class CheckSafety {

    public static Messages checkSafety(Warp destination) {
        String world = destination.getWorld();
        double X = destination.getX() + 0.5;
        double Y = destination.getY();
        double Z = destination.getZ() + 0.5;

        Location loc = new Location(getWorld(world), X, Y, Z);
        double checkX = loc.getBlockX() - 3;
        double checkY = loc.getBlockY() - 2;
        double checkZ = loc.getBlockZ() - 3;

        if (!canBeRebornHere(world, X, Y, Z) || isDangerous(world, checkX, checkY, checkZ)) { //SELECTED_VWARP_IS_DANGEROUS
            return Messages.SVID;
        }
        if (canBeRebornHere(world, X, Y, Z) && !isSafe(world, checkX, checkY, checkZ)) { //SELECTED_VWARP_MIGHT_NOT_BE_SAFE
            return Messages.SVMNBS;
        }
        return null;
    }

    public static boolean insecure(Warp isBeingChecked) {
        String world = isBeingChecked.getWorld();
        double X = isBeingChecked.getX() + 0.5;
        double Y = isBeingChecked.getY();
        double Z = isBeingChecked.getZ() + 0.5;

        Location loc = new Location(getWorld(world), X, Y, Z);
        double checkX = loc.getBlockX() - 3;
        double checkY = loc.getBlockY() - 2;
        double checkZ = loc.getBlockZ() - 3;

        return !canBeRebornHere(world, X, Y, Z) || isDangerous(world, checkX, checkY, checkZ);
    }

    public static void validate(Warp warp) {
        if (Bukkit.getWorld(warp.getWorld()) == null) {
            throw new NullPointerException();
        }
    }

    private static boolean isOnList(int ID, List<String> list) {
        int iterator = 0;
        boolean is = false;
        String id = Integer.toString(ID);
        do {
            if (id.equals(list.get(iterator))) {
                is = true;
            }
            iterator++;
        } while (iterator < list.size() && !is);
        return is;
    }

    private static boolean blockIsSafe(Location loc) { //return true if block is on safe-list in file "blocks.yml"
        return isOnList(loc.getBlock().getTypeId(), Blocks.getSafeBlockList());
    }

    private static boolean blockIsDangerous(Location loc) { //return truee if block is on dangerous-list in file "blocks.yml"
        return isOnList(loc.getBlock().getTypeId(), Blocks.getDangerousBlockList());
    }

    private static boolean canBeRebornHere(String world, double X, double Y, double Z) { //return true if player will have enough space for reborn
        try {
            Location loc = new Location(getWorld(world), X - 1, Y, Z - 1);
            int baseX = loc.getBlockX();
            int baseY = loc.getBlockY();
            int baseZ = loc.getBlockZ();

            int underTwoID = new Location(loc.getWorld(), baseX, baseY - 2, baseZ).getBlock().getTypeId(); //cannot be air and must be safe
            int underOneID = new Location(loc.getWorld(), baseX, baseY - 1, baseZ).getBlock().getTypeId(); //must be obsidian
            int zeroID = new Location(loc.getWorld(), baseX, baseY, baseZ).getBlock().getTypeId(); //must be air
            int headID = new Location(loc.getWorld(), baseX, baseY + 1, baseZ).getBlock().getTypeId(); //must be air
            int upHeadID = new Location(loc.getWorld(), baseX, baseY + 2, baseZ).getBlock().getTypeId(); //must be air

            return (isOnList(underTwoID, Blocks.getSafeBlockList()) && underTwoID != 0 && underOneID == 49 && zeroID == 0 && headID == 0 && upHeadID == 0);
        } catch (NullPointerException NPex) {
            return false;
        }
    }

    private static boolean isDangerous(String world, double checkX, double checkY, double checkZ) { //return true if vwarp is dangerous (near are blocks from dangerous-list)
        for (int y = 0; y < 5; y++) { //checking each level
            for (int x = 0; x < 5; x++) {
                for (int z = 0; z < 5; z++) {
                    if (x != 2 || z != 2) {
                        if (blockIsDangerous(new Location(getWorld(world), checkX + x, checkY + y, checkZ + z))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean isSafe(String world, double checkX, double checkY, double checkZ) { //return true if vwarp is safe (near are only blocks from safe-list)
        for (int y = 0; y < 5; y++) { //checking each level
            for (int x = 0; x < 5; x++) {
                for (int z = 0; z < 5; z++) {
                    if (x != 2 || z != 2) {
                        if (!blockIsSafe(new Location(getWorld(world), checkX + x, checkY + y, checkZ + z))) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
