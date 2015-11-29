package vwarp.system;

import java.util.List;
import static org.bukkit.Bukkit.getWorld;
import org.bukkit.Location;

/**
 *
 * @author NRUB
 */
public class CheckSafety {

    private static String world; //world name of vwarp
    private static double X; //x-coordinates of vwarp
    private static double Y; //y-coordinates of vwarp
    private static double Z; //z-coordinates of vwarp
    private static int checkX; //x-coordinates for checking
    private static int checkY; //y-coordinates for checking
    private static int checkZ; //z-coordinates for checking

    private static final List<String> safe=Blocks.getSafeBlockList();
    private static final List<String> dangerous=Blocks.getDangerousBlockList();

    private static boolean isOnList(int ID, List<String> list) {
        int iterator=0;
        boolean is=false;
        String id=Integer.toString(ID);
        do {
            if (id.equals(list.get(iterator))) {
                is=true;
            }
            iterator++;
        } while (iterator<list.size()&&!is);
        return is;
    }

    private static boolean blockIsSafe(Location loc) { //return true if block is on safe-list in file "blocks.yml"
        return isOnList(loc.getBlock().getTypeId(), safe);
    }

    private static boolean blockIsDangerous(Location loc) { //return truee if block is on dangerous-list in file "blocks.yml"
        return isOnList(loc.getBlock().getTypeId(), dangerous);
    }

    private static boolean canBeRebornHere() { //return true if player will have enough space for reborn
        Location loc=new Location(getWorld(world), X-1, Y, Z-1);
        int baseX=loc.getBlockX();
        int baseY=loc.getBlockY();
        int baseZ=loc.getBlockZ();

        int underTwoID=new Location(loc.getWorld(), baseX, baseY-2, baseZ).getBlock().getTypeId(); //cannot be air and must be safe
        int underOneID=new Location(loc.getWorld(), baseX, baseY-1, baseZ).getBlock().getTypeId(); //must be obsidian
        int zeroID=new Location(loc.getWorld(), baseX, baseY, baseZ).getBlock().getTypeId(); //must be air
        int headID=new Location(loc.getWorld(), baseX, baseY+1, baseZ).getBlock().getTypeId(); //must be air
        int upHeadID=new Location(loc.getWorld(), baseX, baseY+2, baseZ).getBlock().getTypeId(); //must be air

        return (isOnList(underTwoID, safe)&&underTwoID!=0&&underOneID==49&&zeroID==0&&headID==0&&upHeadID==0);
    }

    private static boolean isDangerous() { //return true if vwarp is dangerous (near are blocks from dangerous-list)
        for (int y=0; y<5; y++) { //checking each level
            for (int x=0; x<5; x++) {
                for (int z=0; z<5; z++) {
                    if (x!=2||z!=2) {
                        if (blockIsDangerous(new Location(getWorld(world), checkX+x, checkY+y, checkZ+z))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean isSafe() { //return true if vwarp is safe (near are only blocks from safe-list)
        for (int y=0; y<5; y++) { //checking each level
            for (int x=0; x<5; x++) {
                for (int z=0; z<5; z++) {
                    if (x!=2||z!=2) {
                        if (!blockIsSafe(new Location(getWorld(world), checkX+x, checkY+y, checkZ+z))) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static String checkSafety(Warp destination) {
        world=destination.getWorld();
        X=destination.getX()+0.5;
        Y=destination.getY();
        Z=destination.getZ()+0.5;

        Location loc=new Location(getWorld(world), X, Y, Z);
        checkX=loc.getBlockX()-3;
        checkY=loc.getBlockY()-2;
        checkZ=loc.getBlockZ()-3;

        if (!canBeRebornHere()||isDangerous()) { //SELECTED_VWARP_IS_DANGEROUS
            return "SVID";
        }
        if (canBeRebornHere()&&!isSafe()) { //SELECTED_VWARP_MIGHT_BE_NOT_SAFETY
            return "SVMBNS";
        }
        return "";
    }

    public static boolean insecure(Warp isBeingChecked) {
        world=isBeingChecked.getWorld();
        X=isBeingChecked.getX()+0.5;
        Y=isBeingChecked.getY();
        Z=isBeingChecked.getZ()+0.5;

        Location loc=new Location(getWorld(world), X, Y, Z);
        checkX=loc.getBlockX()-3;
        checkY=loc.getBlockY()-2;
        checkZ=loc.getBlockZ()-3;

        return !canBeRebornHere()||isDangerous();
    }
}
