package me.epicgodmc.epicvalues.utils;

import me.epicgodmc.epicvalues.EpicValues;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private EpicValues plugin;

    public FileManager(EpicValues plugin) {
        this.plugin = plugin;

        createFiles();
    }

    private File globalFile;
    private FileConfiguration globalConfig;

    private File dataFile;
    private FileConfiguration dataConfig;


    private void createFiles() {
        dataFile = new File(plugin.getDataFolder(), "PlayerData.yml");
        globalFile = new File(plugin.getDataFolder(), "GlobalData.yml");

        if (!globalFile.exists())
        {
            globalFile.getParentFile().mkdirs();
            plugin.saveResource("GlobalData.yml", false);
        }
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            plugin.saveResource("PlayerData.yml", false);
        }

        dataConfig = new YamlConfiguration();
        globalConfig = new YamlConfiguration();

        try {

            globalConfig.load(globalFile);
            dataConfig.load(dataFile);

        } catch (InvalidConfigurationException | IOException e) {
            Bukkit.getConsoleSender().sendMessage("[EpicValues] Failed to create Files!");
            e.printStackTrace();
        }
    }

    public FileConfiguration getGlobalConfig()
    {
        return this.globalConfig;
    }
    public void saveGlobalConf()
    {
        try{
            globalConfig.save(globalFile);
        }catch (IOException e)
        {
            Bukkit.getConsoleSender().sendMessage("[EpicValues] failed to save GlobalData.yml");
            e.printStackTrace();
        }

    }

    public FileConfiguration getDataConfig() {
        return this.dataConfig;
    }

    public void saveDataConf()
    {
        try{
            dataConfig.save(dataFile);
        }catch (IOException e)
        {
            Bukkit.getConsoleSender().sendMessage("[EpicValues] failed to save PlayerData.yml");
            e.printStackTrace();
        }

    }


}
