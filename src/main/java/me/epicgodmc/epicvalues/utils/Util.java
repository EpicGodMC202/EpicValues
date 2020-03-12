package me.epicgodmc.epicvalues.utils;

import me.epicgodmc.epicvalues.EpicValues;
import me.epicgodmc.epicvalues.GlobalValues;
import me.epicgodmc.epicvalues.objects.ValuePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class Util {

    private EpicValues plugin = EpicValues.getInstance();
    private MessageManager mm = plugin.mm;

    public int getBaseValue(String identifier) {
        return plugin.getConfig().getInt("baseValues." + identifier);
    }

    public boolean existsAsBaseValue(String identifier) {
        return plugin.getConfig().isSet("baseValues." + identifier);
    }

    public int parseValue(String valueStr) {
        int output;

        try {
            output = Integer.parseInt(valueStr);

        } catch (NumberFormatException e) {
            System.out.println("[EpicValues] an error occurred while parsing a value, Cause: " + e.getMessage());
            return -1;
        }

        return output;
    }

    public int parseValue(CommandSender requester, String valueStr) {
        int output;

        try {
            output = Integer.parseInt(valueStr);

        } catch (NumberFormatException e) {
            requester.sendMessage(mm.getMessage("notANumber"));
            return -1;
        }

        return output;
    }

    public void runCommands(Player player, List<String> commands, int value, int valueChecked, String identifier) {
        for (String cmd : commands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd
                    .replace("%value%", value + "")
                    .replace("%valuechecked%", valueChecked + "")
                    .replace("%identifier%", identifier)
                    .replace("%player%", player.getName()));
        }
    }

    public void runCommands(List<String> commands, int value, int valueChecked, String identifier) {
        for (String cmd : commands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd
                    .replace("%value%", value + "")
                    .replace("%valuechecked%", valueChecked + "")
                    .replace("%identifier%", identifier));
        }
    }

    public boolean conditionMet(String identifier) {
        GlobalValues globalValues = GlobalValues.getInstance();
        int valueSpecified = plugin.getConfig().getInt("valueTriggers.global." + identifier + ".value");
        String comparator = "=";

        if (plugin.getConfig().isSet("valueTriggers.global." + identifier + ".type")) {
            comparator = plugin.getConfig().getString("valueTriggers.global." + identifier + ".type");
        }

        if (comparator != null) {

            switch (comparator) {
                case "=":
                    if (globalValues.containsValue(identifier)) {
                        return globalValues.getValue(identifier) == valueSpecified;
                    } else return false;
                case "<":
                    if (globalValues.containsValue(identifier)) {
                        return globalValues.getValue(identifier) < valueSpecified;
                    } else return false;
                case ">":
                    if (globalValues.containsValue(identifier)) {
                        return globalValues.getValue(identifier) > valueSpecified;
                    } else return false;
                case ">=":
                    if (globalValues.containsValue(identifier)) {
                        return globalValues.getValue(identifier) >= valueSpecified;
                    } else return false;
                case "<=":
                    if (globalValues.containsValue(identifier)) {
                        return globalValues.getValue(identifier) <= valueSpecified;
                    } else return false;

                default:
                    return false;
            }
        } else return false;
    }

    public boolean conditionMet(ValuePlayer valuePlayer, String identifier) {
        String comparator = "=";
        int valueSpecified = plugin.getConfig().getInt("valueTriggers.player." + identifier + ".value");

        if (plugin.getConfig().isSet("valueTriggers.player." + identifier + ".type")) {
            comparator = plugin.getConfig().getString("valueTriggers.global." + identifier + ".type");
        }

        if (comparator != null) {

            switch (comparator) {
                case "=":
                    if (valuePlayer.hasValue(identifier)) {
                        return valuePlayer.getValue(identifier) == valueSpecified;
                    } else return false;
                case "<":
                    if (valuePlayer.hasValue(identifier)) {
                        return valuePlayer.getValue(identifier) < valueSpecified;
                    } else return false;
                case ">":
                    if (valuePlayer.hasValue(identifier)) {
                        return valuePlayer.getValue(identifier) > valueSpecified;
                    } else return false;
                case ">=":
                    if (valuePlayer.hasValue(identifier)) {
                        return valuePlayer.getValue(identifier) >= valueSpecified;
                    } else return false;
                case "<=":
                    if (valuePlayer.hasValue(identifier)) {
                        return valuePlayer.getValue(identifier) <= valueSpecified;
                    } else return false;

                default:
                    return false;
            }
        } else return false;
    }

    public HashMap<String, Integer> parseValues(List<String> serializedValues)
    {
        HashMap<String, Integer> parsed = new HashMap<>();

        for (String str : serializedValues)
        {
            String[] split = str.split(":");
            String id = split[0];
            int value = parseValue(split[1]);

            parsed.put(id, value);
        }

        return parsed;
    }

}
