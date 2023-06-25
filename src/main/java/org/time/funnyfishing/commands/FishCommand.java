package org.time.funnyfishing.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.time.funnyfishing.FishingGame;
import org.time.funnyfishing.FunnyFishing;

public class FishCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fish")) {
            if (args.length == 0||(args.length == 1&&args[0].equalsIgnoreCase("help"))) {
                sender.sendMessage("");
                sender.sendMessage("§f========== §bFunnyFishing §f==========");
                sender.sendMessage("§6/fish help §e打开此帮助");
                if (sender.isOp()) {
                    sender.sendMessage("§6/fish start [时间(s)] §e开启一定时长的钓鱼比赛");
                    sender.sendMessage("§6/fish stop §e停止本次钓鱼比赛");
                    sender.sendMessage("§6/fish pause [true/false] §e暂停本次钓鱼比赛");
                    sender.sendMessage("§6/fish reload §e重载插件(但不会终止已开启的钓鱼比赛)");
                }
                return true;
            }
            if (args.length == 1&&args[0].equalsIgnoreCase("stop")) {
                if (!sender.isOp()) {
                    sender.sendMessage("§f[§bFunnyFishing§f] §c你没有权限使用此命令！");
                    return true;
                }
                if (FunnyFishing.fishingGame == null) {
                    sender.sendMessage("§f[§bFunnyFishing§f] §c你未开启任何钓鱼比赛！");
                    return true;
                }
                FunnyFishing.fishingGame.stop();
                sender.sendMessage("§f[§bFunnyFishing§f] §a成功结束钓鱼比赛。");
                return true;
            }
            if (args.length == 2&&args[0].equalsIgnoreCase("pause")) {
                if (!sender.isOp()) {
                    sender.sendMessage("§f[§bFunnyFishing§f] §c你没有权限使用此命令！");
                    return true;
                }
                if (FunnyFishing.fishingGame == null) {
                    sender.sendMessage("§f[§bFunnyFishing§f] §c你未开启任何钓鱼比赛！");
                    return true;
                }
                boolean pause = Boolean.parseBoolean(args[1]);
                if (pause) {
                    FunnyFishing.fishingGame.setPause(true);
                    Bukkit.broadcastMessage("§f[§bFunnyFishing§f] §c钓鱼比赛暂停。");
                }
                else {
                    FunnyFishing.fishingGame.setPause(false);
                    Bukkit.broadcastMessage("§f[§bFunnyFishing§f] §c钓鱼比赛继续。");
                }
                return true;
            }
            if (args.length == 2&&args[0].equalsIgnoreCase("start")) {
                if (!sender.isOp()) {
                    sender.sendMessage("§f[§bFunnyFishing§f] §c你没有权限使用此命令！");
                    return true;
                }
                if (FunnyFishing.fishingGame != null) {
                    sender.sendMessage("§f[§bFunnyFishing§f] §c钓鱼比赛正在进行！");
                    return true;
                }
                FunnyFishing.fishingGame = new FishingGame(Integer.parseInt(args[1]));
                Bukkit.broadcastMessage("§f[§bFunnyFishing§f] §a一场钓鱼比赛已经启动！大家快来参与吧！");
                return true;
            }
            if (args.length == 1&&args[0].equalsIgnoreCase("reload")) {
                if (!sender.isOp()) {
                    sender.sendMessage("§f[§bFunnyFishing§f] §c你没有权限使用此命令！");
                    return true;
                }
                FunnyFishing.getInstance().load();
                sender.sendMessage("§f[§bFunnyFishing§f] §a成功重载。");
                return true;
            }
        }
        return false;
    }
}
