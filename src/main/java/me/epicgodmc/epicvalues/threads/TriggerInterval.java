package me.epicgodmc.epicvalues.threads;

import me.epicgodmc.epicvalues.EpicValues;
import me.epicgodmc.epicvalues.GlobalValues;
import me.epicgodmc.epicvalues.ValuePlayers;
import me.epicgodmc.epicvalues.objects.ValuePlayer;
import me.epicgodmc.epicvalues.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class TriggerInterval {

    private EpicValues plugin = EpicValues.getInstance();
    private ValuePlayers valuePlayers = ValuePlayers.getInstance();
    private Util util = plugin.util;

    public TriggerInterval() {
        int interval = plugin.getConfig().getInt("valueTriggers.checkInterval");
        int formatted = interval * 20;

        new BukkitRunnable() {
            @Override
            public void run() {
                checkPlayerTriggers();
                checkGlobalTriggers();
            }
        }.runTaskTimer(plugin, formatted, formatted);
    }

    private void checkGlobalTriggers()
    {
        for (String identifier : plugin.getConfig().getConfigurationSection("valueTriggers.global").getKeys(false))
        {
            if (util.conditionMet(identifier))
            {

                util.runCommands(plugin.getConfig().getStringList("valueTriggers.global."+identifier+".commands"),
                        GlobalValues.getInstance().getValue(identifier),
                        plugin.getConfig().getInt("valueTriggers.global."+identifier+".value"),
                        identifier);
            }

        }

    }

    private void checkPlayerTriggers() {
        for (String identifier : plugin.getConfig().getConfigurationSection("valueTriggers.player").getKeys(false)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ValuePlayer valuePlayer = valuePlayers.getByUUID(player.getUniqueId());
                if (valuePlayer == null) {
                    valuePlayers.setupPlayer(player.getUniqueId());
                    valuePlayer = valuePlayers.getByUUID(player.getUniqueId());
                    if (util.conditionMet(valuePlayer, identifier)) {
                        util.runCommands(player, plugin.getConfig().getStringList("valueTriggers.player."+identifier+".commands"),
                                valuePlayer.getValue(identifier),
                                plugin.getConfig().getInt("valueTriggers.player."+identifier+".value"),
                                identifier);
                    }

                }
            }
        }
    }

}
