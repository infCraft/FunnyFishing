package org.time.funnyfishing;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.time.funnyfishing.commands.FishCommand;
import org.time.funnyfishing.listeners.FishListener;
import org.time.funnyfishing.utils.ConfigUtils;

public class FunnyFishing extends JavaPlugin {

    private static FunnyFishing instance;
    public static FunnyFishing getInstance() {
        return instance;
    }

    public static FishingGame fishingGame; // 用于存储fishinggame

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginCommand("fish").setExecutor(new FishCommand());
        Bukkit.getPluginManager().registerEvents(new FishListener(), this);
        load();
    }

    public void load() {
        ConfigUtils.loadConfig("config.yml");
    }
}
