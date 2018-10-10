package vwarp.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import vwarp.VWarp;
import vwarp.system.Lang;
import vwarp.system.Messages;
import vwarp.utils.Updater;

/**
 *
 * @author NRUB
 */
public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent join) {
        Player player = join.getPlayer();
        if (VWarp.getUpdater().getResult() == Updater.UpdateResult.UPDATE_AVAILABLE && player.hasPermission("vwarp.vupdate")) {
            player.sendMessage("[vWarp] " + Lang.getMessage(Messages.UAA) + VWarp.getUpdater().getLatestName() + Lang.getMessage(Messages.UAB) + "http://dev.bukkit.org/bukkit-plugins/vwarp/" + Lang.getMessage(Messages.UAC));
        }
    }
}
