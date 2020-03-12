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

public class DecrementValue extends SubCommand
{

    private EpicValues plugin = EpicValues.getInstance();
    private MessageManager mm = plugin.mm;
    private Util util = plugin.util;



    @Override
    public void onCommand(CommandSender sender, String[] args, Player playerSpecified) {
        if (args.length != 2)
        {
            sender.sendMessage(mm.getMessage("invalidArgLength"));
            return;
        }
        String identifier = args[0];
        if (sender.hasPermission("values.command.sub.*") || sender.hasPermission("values.command.sub."+identifier)) {
            int value = util.parseValue(sender, args[1]);
            if (playerSpecified != null) {
                ValuePlayer valPlayer = ValuePlayers.getInstance().getByUUID(playerSpecified.getUniqueId());
                if (valPlayer == null) {
                    ValuePlayers.getInstance().setupPlayer(playerSpecified.getUniqueId());
                    valPlayer = ValuePlayers.getInstance().getByUUID(playerSpecified.getUniqueId());
                }

                if (value != -1) {
                    if (!valPlayer.hasValue(identifier)) {
                        valPlayer.setValue(identifier, 0);
                    }
                    valPlayer.decrementValue(identifier, value);
                    sender.sendMessage(mm.getMessage("valueDecrement")
                            .replace("%identifier%", identifier)
                            .replace("%value%", String.valueOf(valPlayer.getValue(identifier)))
                            .replace("%player%", playerSpecified.getName()));

                }
            } else {
                if (!GlobalValues.getInstance().containsValue(identifier)) {
                    GlobalValues.getInstance().setValue(identifier, 0);
                }
                GlobalValues.getInstance().decrementValue(identifier, value);
                sender.sendMessage(mm.getMessage("valueDecrement")
                        .replace("%identifier%", identifier)
                        .replace("%value%", String.valueOf(GlobalValues.getInstance().getValue(identifier)))
                        .replace("%player%", "GLOBAL"));
            }
        }else{
            sender.sendMessage(mm.getMessage("noPermissions"));
        }
    }

    @Override
    public String name() {
        return plugin.cmdRoot.subtract;
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
