package me.epicgodmc.epicvalues.utils;

import me.epicgodmc.epicvalues.EpicValues;
import org.bukkit.ChatColor;

import java.util.List;

public class MessageManager
{

    private EpicValues plugin = EpicValues.getInstance();


    public String prefix = plugin.getConfig().getString("pluginPrefix");

    public List<String> getUsage()
    {
        return plugin.getConfig().getStringList("messages.usage");
    }

    public String getMessageFromPath(String path)
    {
        return applyCC(plugin.getConfig().getString(path));
    }

    public String getMessage(String key)
    {
        return applyCC(prefix+plugin.getConfig().getString("messages."+key));
    }

    public String applyCC(String input)
    {
        return ChatColor.translateAlternateColorCodes('&', input);
    }


    public String formatInfo(String input, String player, int page, int nextPage)
    {
        return input.replace("%player%", player)
                .replace("%pageNumber%", page+"")
                .replace("%nextPageNumber%", nextPage+"");

    }

}
