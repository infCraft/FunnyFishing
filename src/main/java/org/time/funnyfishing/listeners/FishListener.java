package org.time.funnyfishing.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.time.funnyfishing.FunnyFishing;
import org.time.funnyfishing.utils.ConfigUtils;

import java.text.DecimalFormat;
import java.util.Random;

public class FishListener implements Listener {

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (FunnyFishing.fishingGame == null) return;
        if (FunnyFishing.fishingGame.isPause()) return;
        if (e.getState() != PlayerFishEvent.State.CAUGHT_FISH) return; // 钓上鱼来才算哦
        if (e.getCaught() == null) return;
        Player p = e.getPlayer();
        ItemStack item = ((Item) e.getCaught()).getItemStack();
        double score = 0;
        // 加分操作
        for (String material: ConfigUtils.items.keySet()) {
            if (material.equalsIgnoreCase(item.getType().toString())) { // 匹配成功
                score = ConfigUtils.items.get(material);
                FunnyFishing.fishingGame.addPlayerScore(p.getName(), score);
                p.sendMessage("§f钓到 §e"+item.getType()+" §f+ §a"+new DecimalFormat("0.0#").format(score) +"分");
                break;
            }
        }

        // 随机事件
        Random random = new Random();
        if (random.nextDouble()<ConfigUtils.single_event_rate) { // 单人事件
            switch (random.nextInt(11)) {
                case 1:
                    p.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 1200, 0));
                    p.sendMessage("§f[§bFunnyFishing§f] §f你触发了效果 §b幸运I(60秒)");
                    break;
                case 2:
                    p.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 1200, 0));
                    p.sendMessage("§f[§bFunnyFishing§f] §f你触发了效果 §7霉运I(60秒)");
                    break;
                case 3:
                    p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 200, 4));
                    p.sendMessage("§f[§bFunnyFishing§f] §f你触发了效果 §f漂浮V(10秒)");
                    break;
                case 4:
                    p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 0));
                    p.sendMessage("§f[§bFunnyFishing§f] §f你触发了效果 §2反胃I(10秒)");
                    break;
                case 5:
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0));
                    p.sendMessage("§f[§bFunnyFishing§f] §f你触发了效果 §8失明I(10秒)");
                    break;
                case 6:
                    p.getWorld().strikeLightning(p.getLocation());
                    p.sendMessage("§f[§bFunnyFishing§f] §f你被雷劈了！");
                    break;
                case 7:
                    p.setFireTicks(200);
                    p.sendMessage("§f[§bFunnyFishing§f] §f你触发了效果 §6灼烧(10秒)");
                    break;
                case 8:
                    p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.PRIMED_TNT);
                    p.sendMessage("§f[§bFunnyFishing§f] §f你钓到了TNT！");
                    break;
                case 9:
                    FunnyFishing.fishingGame.addPlayerScore(p.getName(), score);
                    p.sendMessage("§f[§bFunnyFishing§f] §a双倍分数！");
                    break;
                case 10:
                    FunnyFishing.fishingGame.addPlayerScore(p.getName(), score*2);
                    p.sendMessage("§f[§bFunnyFishing§f] §a三倍分数！");
                    break;
                case 0:
                    FunnyFishing.fishingGame.addPlayerScore(p.getName(), score*4);
                    p.sendMessage("§f[§bFunnyFishing§f] §a五倍分数！");
                    break;
            }
        }
        if (random.nextDouble()<ConfigUtils.multiple_event_rate) { // 多人事件
            switch (random.nextInt(6)) {
                case 1:
                    for (Player p2: Bukkit.getOnlinePlayers()) p2.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 600, 0));
                    Bukkit.broadcastMessage("§f[§bFunnyFishing§f] §f玩家 §e"+p.getName()+" §f让全员触发了效果 §b幸运I(30秒)");
                    break;
                case 2:
                    for (Player p2: Bukkit.getOnlinePlayers()) p2.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 600, 0));
                    Bukkit.broadcastMessage("§f[§bFunnyFishing§f] §f玩家 §e"+p.getName()+" §f让全员触发了效果 §7霉运I(30秒)");
                    break;
                case 3:
                    for (Player p2: Bukkit.getOnlinePlayers()) p2.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 300, 4));
                    Bukkit.broadcastMessage("§f[§bFunnyFishing§f] §f玩家 §e"+p.getName()+" §f让全员触发了效果 §f漂浮V(5秒)");
                    break;
                case 4:
                    for (Player p2: Bukkit.getOnlinePlayers()) FunnyFishing.fishingGame.addPlayerScore(p2.getName(), score);
                    Bukkit.broadcastMessage("§f[§bFunnyFishing§f] §f玩家 §e"+p.getName()+" §f让全员 §a+"+new DecimalFormat("0.0#").format(score)+"分");
                    break;
                case 5:
                    for (Player p2: Bukkit.getOnlinePlayers()) FunnyFishing.fishingGame.addPlayerScore(p2.getName(), score*2);
                    Bukkit.broadcastMessage("§f[§bFunnyFishing§f] §f玩家 §e"+p.getName()+" §f让全员 §a+"+new DecimalFormat("0.0#").format(score*2)+"分");
                    break;
                case 0:
                    for (Player p2: Bukkit.getOnlinePlayers()) FunnyFishing.fishingGame.addPlayerScore(p2.getName(), -score);
                    Bukkit.broadcastMessage("§f[§bFunnyFishing§f] §f玩家 §e"+p.getName()+" §f让全员 §c-"+new DecimalFormat("0.0#").format(score)+"分");
                    break;
            }
        }
    }
}
