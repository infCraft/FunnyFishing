package org.time.funnyfishing.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.time.funnyfishing.FunnyFishing;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigUtils {

    public static int timings;
    public static Map<String,Double> items;
    public static double single_event_rate;
    public static double multiple_event_rate;

    /**
     * 加载配置文件config.yml并将读取的数据保存在ConfigUtils的静态变量当中
     * @param file 文件名称
     */
    public static void loadConfig(String file) {
        File f = new File(FunnyFishing.getInstance().getDataFolder(), file);
        if (!f.exists()) {
            FunnyFishing.getInstance().saveResource(file, true);
            FunnyFishing.getInstance().getLogger().info("§e未检测到 config.yml，正在重新创建...");
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(f);
        timings = config.getInt("Timings");

        // 将items里面的物品添加到map中
        Map<String,Double> map = new HashMap<>();
        for (String key: config.getConfigurationSection("Items").getKeys(false)) {
            map.put(key.toUpperCase(), config.getDouble("Items."+key));
        }
        items = map;

        single_event_rate = config.getDouble("Event.Single");
        multiple_event_rate = config.getDouble("Event.Multiple");
        FunnyFishing.getInstance().getLogger().info("§a成功加载 config.yml");
    }
}
