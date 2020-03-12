package me.epicgodmc.epicvalues;

import me.epicgodmc.epicvalues.utils.FileManager;
import me.epicgodmc.epicvalues.utils.MessageManager;
import me.epicgodmc.epicvalues.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GlobalValues
{

    private EpicValues plugin;
    private MessageManager mm;
    private FileManager fileManager;
    private Util util;


    private static GlobalValues instance;
    public static GlobalValues getInstance()
    {
        return instance;
    }

    public GlobalValues(EpicValues plugin)
    {
        instance = this;
        this.plugin = plugin;
        mm = plugin.mm;
        util = plugin.util;
        fileManager = plugin.fileManager;

        loadValues();
    }

    private HashMap<String, Integer> globalValueMap = new HashMap<>();

    public void setValue(String identifier, int value)
    {
        globalValueMap.put(identifier, value);
    }

    public boolean containsValue(String identifier)
    {
        return globalValueMap.containsKey(identifier);
    }

    public int getValue(String identifier)
    {
        return globalValueMap.get(identifier);
    }

    public void incrementValue(String identifier, int amt)
    {
        int initial = getValue(identifier);
        setValue(identifier, initial+amt);
    }

    public void decrementValue(String identifier, int amt)
    {
        int initial = getValue(identifier);
        setValue(identifier, initial-amt);
    }

    public HashMap<String, Integer> getValueMapHash()
    {
        return globalValueMap;
    }

    public ArrayList<String> getValueInfo(int page) {
        int index = page * 5;
        int initIndex = index;
        ArrayList<String> infoPage = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        keys.addAll(globalValueMap.keySet());

        try{
            infoPage.add(mm.formatInfo(mm.getMessageFromPath("valueInfo.header"), "GLOBAL", page, page + 1));
            for (int i = 0; i < keys.size(); i++) {
                if (initIndex+5 ==i) break;
                infoPage.add(mm.getMessageFromPath("valueInfo.format")
                        .replace("%identifier%", keys.get(index))
                        .replace("%value%", getValue(keys.get(index))+""));
                index++;

            }
            infoPage.add(mm.formatInfo(mm.getMessageFromPath("valueInfo.footer"), "GLOBAL", page, page + 1));
        }catch (IndexOutOfBoundsException e)
        {
            return null;
        }

        return infoPage;
    }


    private void loadValues()
    {
        List<String> serializedValues = fileManager.getGlobalConfig().getStringList("globalValues");
        this.globalValueMap = util.parseValues(serializedValues);


    }
}
