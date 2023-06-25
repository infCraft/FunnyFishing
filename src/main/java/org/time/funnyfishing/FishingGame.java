package org.time.funnyfishing;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.time.funnyfishing.utils.ConfigUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FishingGame {
    private int period; // 总时长
    private int count; // 已经过去的时间
    private List<String> players;
    private List<Double> scores;
    private boolean pause; // 是否在暂停当中
    private BukkitRunnable runnable; // runnable
    private BossBar bar; // 屏幕上方的进度条Oh year!

    public FishingGame(int time) {
        // basic initialize
        this.period = time;
        count = 0;
        players = new ArrayList<>();
        scores = new ArrayList<>();
        pause = false;

        // init runnable and bar
        bar = Bukkit.createBossBar("§b钓鱼比赛", BarColor.YELLOW, BarStyle.SOLID);
        bar.setProgress(1);
        for (Player p: Bukkit.getOnlinePlayers()) bar.addPlayer(p); // 添加到所有玩家
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (count >= period) { // 结束
                    stop();
                    return;
                }
                if (pause) return; // 如果是暂停就啥也别干
                if (count % ConfigUtils.timings == 0) { // 到时间显示一遍排行榜
                    showScore();
                }

                // 调整bar相关参数
                int remain = period-count;
                bar.setProgress((remain)*1.0/period); // 设定bar的值
                for (Player p: Bukkit.getOnlinePlayers()) if (!bar.getPlayers().contains(p)) bar.addPlayer(p); // 新加进来的玩家也可以看到bar
                if (remain % 60 == 0) { // 分钟
                    bar.setTitle("§b钓鱼比赛 §f(剩余 §e"+(remain/60)+" §f分钟)");
                }
                else {
                    if (remain/60 == 0) {
                        bar.setTitle("§b钓鱼比赛 §f(剩余 §e"+(remain%60)+" §f秒)");
                    }
                    else {
                        bar.setTitle("§b钓鱼比赛 §f(剩余 §e"+(remain/60)+" §f分 §e"+(remain%60)+" §f秒)");
                    }
                }

                count++;
            }
        };
        runnable.runTaskTimer(FunnyFishing.getInstance(), 0L, 20L);
    }


    /**
     * 钓鱼比赛是否在暂停状态
     * @return true 在暂停
     * false 在正常运行
     */
    public boolean isPause() {
        return pause;
    }

    /**
     * 添加新的玩家数据，用于第一次钓鱼
     * @param player 玩家名称
     * @param score 玩家分数
     */
    public synchronized void addPlayer(String player, double score) {
        players.add(player);
        scores.add(score);
    }

    /**
     * 检测是否存在该玩家的数据
     * @param player 玩家名称
     * @return true 存在 false 不存在
     */
    public boolean containsPlayer(String player) {
        return players.contains(player);
    }

    /**
     * 修改玩家数据，加分
     * 推荐使用这个方法，进行了安全检查
     * @param player 玩家名称
     * @param score 加的分数
     */
    public synchronized void addPlayerScore(String player, double score) {
        if (!containsPlayer(player)) addPlayer(player, 0);
        int index = players.indexOf(player);
        scores.set(index, scores.get(index)+score);
    }

    /**
     * 设置是否暂停
     * @param pause 暂停
     */
    public void setPause(boolean pause) {
        this.pause = pause;
    }

    /**
     * 强制结束钓鱼比赛
     */
    public void stop() {
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§c钓鱼比赛结束！");
        Bukkit.broadcastMessage("§c钓鱼比赛结束！");
        Bukkit.broadcastMessage("§c钓鱼比赛结束！");
        Bukkit.broadcastMessage("");
        showScore();
        bar.removeAll();
        runnable.cancel();
        FunnyFishing.fishingGame = null;
    }

    // 排个序
    // 冒泡
    private void sort() {
        for (int i=1;i<scores.size();i++) {
            for (int j=0;j<scores.size()-i;j++) {
                if (scores.get(j)<scores.get(j+1)) {
                    // 交换分数
                    double score = scores.get(j);
                    scores.set(j, scores.get(j+1));
                    scores.set(j+1, score);
                    // 交换id
                    String p = players.get(j);
                    players.set(j, players.get(j+1));
                    players.set(j+1, p);
                }
            }
        }
    }

    private void showScore() {
        sort();
        Bukkit.broadcastMessage("§f========== §6排行榜 §f==========");
        for (int i=0;i<scores.size();i++) {
            if (i == 0) {
                Bukkit.broadcastMessage("§c1. "+players.get(i)+" §7- §e"+new DecimalFormat("0.0#").format(scores.get(i)));
                continue;
            }
            else if (i == 1) {
                Bukkit.broadcastMessage("§b2. "+players.get(i)+" §7- §e"+new DecimalFormat("0.0#").format(scores.get(i)));
                continue;
            }
            else if (i == 2) {
                Bukkit.broadcastMessage("§a3. "+players.get(i)+" §7- §e"+new DecimalFormat("0.0#").format(scores.get(i)));
                continue;
            }
            Bukkit.broadcastMessage("§f"+(i+1)+". "+players.get(i)+" §7- §e"+new DecimalFormat("0.0#").format(scores.get(i)));
            continue;
        }
    }
}
