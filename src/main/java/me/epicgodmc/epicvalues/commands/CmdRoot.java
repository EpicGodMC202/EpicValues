package me.epicgodmc.epicvalues.commands;

import me.epicgodmc.epicvalues.EpicValues;
import me.epicgodmc.epicvalues.commands.subcommands.*;
import me.epicgodmc.epicvalues.objects.SubCommand;
import me.epicgodmc.epicvalues.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class CmdRoot implements CommandExecutor
{

    private ArrayList<SubCommand> commands = new ArrayList<>();
    private EpicValues plugin = EpicValues.getInstance();
    private MessageManager mm = plugin.mm;


    public CmdRoot()
    {

    }


    public String main = "epicvalues";
    //SubCmds
    public String set = "set";
    public String add = "add";
    public String subtract = "sub";
    public String reset = "reset";
    public String info = "info";

    //

    public void setup(){
        plugin.getCommand(main).setExecutor(this);
        this.commands.add(new SetValue());
        this.commands.add(new ValueInfo());
        this.commands.add(new AddValue());
        this.commands.add(new DecrementValue());
        this.commands.add(new ResetValue());




    }
// - '&6&l/EpicValues &c<&6[username], [GLOBAL]&c> &c<&6[set], [add], [sub], [reset], [info]&c> &c<&6[ValueIdentifier]&c> &c<&6Value&c>'

    // /EpicValues EpicGodMC set yeet 0
    // /EpicValues global set yeet 0

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length < 2)
        {
            sender.sendMessage(mm.getMessage("cmdNotRecognized"));
            return true;
        }
        if (cmd.getName().equalsIgnoreCase(main))
        {
                if (args.length == 0) {
                    mm.getUsage().forEach((e) -> sender.sendMessage(mm.applyCC(e)));
                    return true;
                }
                Player targetted = Bukkit.getPlayer(args[0]);
                SubCommand target;
                if (targetted != null || args[0].equalsIgnoreCase("global"))
                {
                    target = this.get(args[1]);
                }else{
                    sender.sendMessage(mm.getMessage("playerNotFound"));
                    return true;
                }




                if (target == null) {
                    sender.sendMessage(mm.getMessage("cmdNotRecognized"));
                    return true;
                }

                ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));
                argList.remove(0);
                argList.remove(0);

                String[] arguments = argList.toArray(new String[0]);

                try {
                    target.onCommand(sender, arguments, targetted);
                } catch (Exception e) {
                    e.printStackTrace();
                    sender.sendMessage(mm.getMessage("error")); //code #0001
                    sender.sendMessage(mm.applyCC("Error Code: #0001"));
                    return true;
                }
        }
        return true;
    }


    private SubCommand get(String name) {

        for (SubCommand sCmd : this.commands) {
            if (sCmd.name().equalsIgnoreCase(name)) {
                return sCmd;
            }

            String[] aliases;
            int length = (aliases = sCmd.aliases()).length;

            for (int var5 = 0; var5 < length; ++var5) {
                String alias = aliases[var5];
                if (name.equalsIgnoreCase(alias)) {
                    return sCmd;
                }
            }
        }
        return null;
    }

}
