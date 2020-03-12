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

public class ResetValue extends SubCommand
{

    private EpicValues plugin = EpicValues.getInstance();
    private MessageManager mm = plugin.mm;
    private Util util = plugin.util;

    // /ev <[playername], [global]> reset <identifier>

    @Override
    public void onCommand(CommandSender sender, String[] args, Player playerSpecified) {
        if (args.length == 0)
        {
            sender.sendMessage(mm.getMessage("provideIdentifier"));
            return;
        }
        String identifier = args[0];
        if (sender.hasPermission("values.command.reset.*") || sender.hasPermission("values.command.reset."+identifier)) {
            if (playerSpecified != null) {
                ValuePlayer valPlayer = ValuePlayers.getInstance().getByUUID(playerSpecified.getUniqueId());
                if (valPlayer == null) {
                    ValuePlayers.getInstance().setupPlayer(playerSpecified.getUniqueId());
                    valPlayer = ValuePlayers.getInstance().getByUUID(playerSpecified.getUniqueId());
                }
                if (valPlayer.hasValue(identifier)) {
                    if (util.existsAsBaseValue(identifier)) {
                        int baseVal = util.getBaseValue(identifier);
                        valPlayer.setValue(identifier, baseVal);
                        sender.sendMessage(mm.getMessage("valueReset")
                                .replace("%identifier%", identifier)
                                .replace("%value%", baseVal + "")
                                .replace("%player%", playerSpecified.getName()));
                    } else {
                        valPlayer.setValue(identifier, 0);
                        sender.sendMessage(mm.getMessage("valueReset")
                                .replace("%identifier%", identifier)
                                .replace("%value%", "0")
                                .replace("%player%", playerSpecified.getName()));
                    }
                } else {
                    sender.sendMessage(mm.getMessage("valueNotFound"));
                }
            } else {
                if (!GlobalValues.getInstance().containsValue(identifier)) {
                    GlobalValues.getInstance().setValue(identifier, 0);
                    sender.sendMessage(mm.getMessage("valueReset")
                            .replace("%identifier%", identifier)
                            .replace("%value%", "0")
                            .replace("%player%", playerSpecified.getName()));

                } else sender.sendMessage(mm.getMessage("valueNotFound"));

            }
        }else{
            sender.sendMessage(mm.getMessage("noPermission"));
        }
    }

    @Override
    public String name() {
        return plugin.cmdRoot.reset;
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
