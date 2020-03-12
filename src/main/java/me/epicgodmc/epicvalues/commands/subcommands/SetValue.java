package me.epicgodmc.epicvalues.commands.subcommands;

import me.epicgodmc.epicvalues.EpicValues;
import me.epicgodmc.epicvalues.GlobalValues;
import me.epicgodmc.epicvalues.ValuePlayers;
import me.epicgodmc.epicvalues.objects.SubCommand;
import me.epicgodmc.epicvalues.objects.ValuePlayer;
import me.epicgodmc.epicvalues.utils.MessageManager;
import me.epicgodmc.epicvalues.utils.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetValue extends SubCommand {

    private EpicValues plugin = EpicValues.getInstance();
    private MessageManager mm = plugin.mm;
    private Util util = plugin.util;

    @Override
    public void onCommand(CommandSender sender, String[] args, Player playerSpecified) {
        if (args.length != 2) {
            sender.sendMessage(mm.getMessage("invalidArgLength"));
            return;
        }
        String identifier = args[0];
        if (sender.hasPermission("values.command.set.*") || sender.hasPermission("values.command.set." + identifier)) {
            int value = util.parseValue(sender, args[1]);
            if (playerSpecified != null) {
                ValuePlayer valPlayer = ValuePlayers.getInstance().getByUUID(playerSpecified.getUniqueId());
                if (valPlayer == null) {
                    ValuePlayers.getInstance().setupPlayer(playerSpecified.getUniqueId());
                    valPlayer = ValuePlayers.getInstance().getByUUID(playerSpecified.getUniqueId());
                }

                if (value != -1) {
                    valPlayer.setValue(identifier, value);
                    sender.sendMessage(mm.getMessage("valueSet")
                            .replace("%identifier%", identifier)
                            .replace("%value%", String.valueOf(value))
                            .replace("%player%", playerSpecified.getName()));

                }
            } else {
                GlobalValues.getInstance().setValue(identifier, value);
                sender.sendMessage(mm.getMessage("valueSet")
                        .replace("%identifier%", identifier)
                        .replace("%value%", String.valueOf(value))
                        .replace("%player%", "GLOBAL"));
            }
        }else{
            sender.sendMessage(mm.getMessage("noPermissions"));
        }
    }

    @Override
    public String name() {
        return plugin.cmdRoot.set;
    }

    @Override
    public String info() {
        return "";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
