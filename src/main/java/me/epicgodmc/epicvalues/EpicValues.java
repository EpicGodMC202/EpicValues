package me.epicgodmc.epicvalues;

import me.epicgodmc.epicvalues.commands.CmdRoot;
import me.epicgodmc.epicvalues.objects.ValuePlayer;
import me.epicgodmc.epicvalues.threads.TriggerInterval;
import me.epicgodmc.epicvalues.utils.FileManager;
import me.epicgodmc.epicvalues.utils.MessageManager;
import me.epicgodmc.epicvalues.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class EpicValues extends JavaPlugin
{

    public PluginManager pm = this.getServer().getPluginManager();

    private static EpicValues instance;
    public static EpicValues getInstance()
    {
        return instance;
    }


    public MessageManager mm;
    public CmdRoot cmdRoot;
    public Util util;
    public FileManager fileManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        registerInstances();
        cmdRoot.setup();

        new TriggerInterval();
    }

    @Override
    public void onDisable() {
        saveRemainingData();
        instance = null;
    }

    private void registerInstances()
    {
        util = new Util();
        mm = new MessageManager();
        cmdRoot = new CmdRoot();
        fileManager = new FileManager(this);
        new ValuePlayers(this);
        new GlobalValues(this);
    }


    private void saveRemainingData()
    {
        HashMap<UUID, ValuePlayer> remainingPlayers = ValuePlayers.getInstance().getPlayerCache();
        int amt = 0;
        for (ValuePlayer valPlayer : remainingPlayers.values())
        {
            amt++;
            List<String> serializedValues = new ArrayList<>();

            for (String str : valPlayer.getValueMapHash().keySet())
            {
                serializedValues.add(str+":"+valPlayer.getValue(str));
            }


            fileManager.getDataConfig().set(valPlayer.getUuid().toString()+".username", Objects.requireNonNull(Bukkit.getOfflinePlayer(valPlayer.getUuid())).getName());
            fileManager.getDataConfig().set(valPlayer.getUuid().toString()+".values", serializedValues);
        }
        System.out.println("[EpicValues] saved values for "+amt+" remaining players");
        fileManager.saveDataConf();

        HashMap<String, Integer> globalData = GlobalValues.getInstance().getValueMapHash();
        List<String> serializedValues = new ArrayList<>();
        for (String id : globalData.keySet())
        {
            serializedValues.add(id+":"+globalData.get(id));
            fileManager.getGlobalConfig().set("globalValues", serializedValues);

        }
        fileManager.saveGlobalConf();

    }

}
