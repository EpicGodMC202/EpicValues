package me.epicgodmc.epicvalues.objects;

import me.epicgodmc.epicvalues.EpicValues;
import me.epicgodmc.epicvalues.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class ValuePlayer {

    private EpicValues plugin = EpicValues.getInstance();
    private MessageManager mm = plugin.mm;

    UUID uuid;
    HashMap<String, Integer> valueMap = new HashMap<>();

    public ValuePlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public void setValue(String identifier, int value) {
        Player player = Bukkit.getPlayer(uuid);
        if (player.hasPermission("values.collect.*") || player.hasPermission("values.collect."+identifier)) {
            valueMap.put(identifier, value);
        }
    }

    public boolean hasValue(String identifier) {
        return valueMap.containsKey(identifier);
    }

    public int getValue(String identifier) {
        return valueMap.get(identifier);
    }

    public void incrementValue(String identifier, int amt) {
        int initial = getValue(identifier);
        setValue(identifier, initial + amt);
    }

    public void decrementValue(String identifier, int amt) {
        int initial = getValue(identifier);
        setValue(identifier, initial - amt);
    }

    public UUID getUuid() {
        return uuid;
    }

    public HashMap<String, Integer> getValueMapHash() {
        return valueMap;
    }

    public ArrayList<String> getValueInfo(int page) {
        int index = page * 5;
        int initIndex = index;
        ArrayList<String> infoPage = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        keys.addAll(valueMap.keySet());

        try{
            infoPage.add(mm.formatInfo(mm.getMessageFromPath("valueInfo.header"), Bukkit.getPlayer(this.uuid).getName(), page, page + 1));
            for (int i = 0; i < keys.size(); i++) {
                if (initIndex+5 ==i) break;
                infoPage.add(mm.getMessageFromPath("valueInfo.format")
                        .replace("%identifier%", keys.get(index))
                        .replace("%value%", getValue(keys.get(index))+""));
                index++;

            }
            infoPage.add(mm.formatInfo(mm.getMessageFromPath("valueInfo.footer"), Bukkit.getPlayer(this.uuid).getName(), page, page + 1));
        }catch (IndexOutOfBoundsException e)
        {
            return null;
        }

        return infoPage;
    }

}
