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

import java.util.ArrayList;

public class ValueInfo extends SubCommand
{

    private EpicValues plugin = EpicValues.getInstance();
    private MessageManager mm = plugin.mm;
    private Util util = plugin.util;


    // /EpicValues <[playername], [global] info <page>>

    @Override
    public void onCommand(CommandSender sender, String[] args, Player playerSpecified) {
        int page;
        if (args.length == 1)
        {
            page = util.parseValue(sender, args[0]);
        }else page = 0;

        if (playerSpecified != null)
        {
            ValuePlayer valPlayer = ValuePlayers.getInstance().getByUUID(playerSpecified.getUniqueId());
            if (valPlayer == null) {
                ValuePlayers.getInstance().setupPlayer(playerSpecified.getUniqueId());
                valPlayer = ValuePlayers.getInstance().getByUUID(playerSpecified.getUniqueId());

            }
            if (sender.getName().equalsIgnoreCase(playerSpecified.getName()))
            {
                if (sender.hasPermission("values.command.info.self")){
                    ArrayList<String> pageInfo = valPlayer.getValueInfo(page);
                    if (pageInfo == null)
                    {
                        sender.sendMessage(mm.getMessage("pageNotFound"));
                        return;
                    }

                    pageInfo.forEach(sender::sendMessage);
                }else{
                    sender.sendMessage(mm.getMessage("noPermissions"));
                }
            }

            else if (sender.hasPermission("values.command.info."+playerSpecified.getName()) || sender.hasPermission("values.command.info.*")) {
                ArrayList<String> pageInfo = valPlayer.getValueInfo(page);
                if (pageInfo == null)
                {
                    sender.sendMessage(mm.getMessage("pageNotFound"));
                    return;
                }
                pageInfo.forEach(sender::sendMessage);
            }else{
                sender.sendMessage(mm.getMessage("noPermissions"));
            }
        }else{
            if (sender.hasPermission("values.command.info.global"))
            {
                GlobalValues.getInstance().getValueInfo(page).forEach(sender::sendMessage);
            }else{
                sender.sendMessage(mm.getMessage("noPermissions"));
            }

        }
    }

    @Override
    public String name() {
        return plugin.cmdRoot.info;
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
