package me.epicgodmc.epicvalues;

import me.epicgodmc.epicvalues.objects.ValuePlayer;
import me.epicgodmc.epicvalues.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ValuePlayers implements Listener {

    private EpicValues plugin;
    private Util util;

    private static ValuePlayers instance;

    public static ValuePlayers getInstance() {
        return instance;
    }

    public ValuePlayers(EpicValues plugin) {
        instance = this;
        this.plugin = plugin;
        util = plugin.util;

        plugin.pm.registerEvents(this, plugin);
    }

    private HashMap<UUID, ValuePlayer> playerCache = new HashMap<>();

    public void putPlayerIfAbsent(UUID uuid, ValuePlayer valuePlayer) {
        playerCache.putIfAbsent(uuid, valuePlayer);
    }

    public ValuePlayer getByUUID(UUID uuid) {
        return playerCache.get(uuid);
    }

    public HashMap<UUID, ValuePlayer> getPlayerCache() {
        return playerCache;
    }

    @EventHandler
    public void preJoin(AsyncPlayerPreLoginEvent e) {
        if (attemptLoad(e.getUniqueId())) return;
        setupPlayer(e.getUniqueId());
    }

    @EventHandler
    public void quit(PlayerQuitEvent e)
    {
        save(e.getPlayer().getUniqueId());
    }

    public void setupPlayer(UUID uuid) {
        putPlayerIfAbsent(uuid, new ValuePlayer(uuid));
        setupBaseValues(uuid);
    }

    private void setupBaseValues(UUID uuid) {
        ValuePlayer valPlayer = getByUUID(uuid);


        for (String string : plugin.getConfig().getConfigurationSection("baseValues").getKeys(false))
        {
            int value = plugin.getConfig().getInt("baseValues."+string);
            valPlayer.setValue(string, value);
        }

    }

    private void save(UUID uuid)
    {
        System.out.println("[EpicValues] Saving values for "+uuid.toString());
        FileConfiguration conf = plugin.fileManager.getDataConfig();
        ValuePlayer valuePlayer = getByUUID(uuid);
        if (valuePlayer == null) return;

        List<String> serializedValues = new ArrayList<>();

        for (String str : valuePlayer.getValueMapHash().keySet())
        {
            serializedValues.add(str+":"+valuePlayer.getValue(str));
        }

        conf.set(uuid.toString()+".username", Bukkit.getPlayer(uuid).getName());
        conf.set(uuid.toString()+".values", serializedValues);


        plugin.fileManager.saveDataConf();
    }

    public boolean attemptLoad(UUID uuid)
    {
        System.out.println("[EpicValues] loading values for "+uuid.toString());
        FileConfiguration conf = plugin.fileManager.getDataConfig();

        if (conf.isSet(uuid.toString()))
        {
            ValuePlayer valuePlayer = new ValuePlayer(uuid);
            HashMap<String, Integer> values = util.parseValues(conf.getStringList(uuid.toString()+".values"));
            for (String id : values.keySet())
            {
                valuePlayer.setValue(id, values.get(id));
            }

            playerCache.put(uuid, valuePlayer);

            return true;
        }
        return false;
    }


}
