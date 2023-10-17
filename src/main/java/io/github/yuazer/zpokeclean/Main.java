package io.github.yuazer.zpokeclean;

import io.github.yuazer.zpokeclean.cleanmode.TimeModule;
import io.github.yuazer.zpokeclean.commands.MainCommand;
import io.github.yuazer.zpokeclean.globaltime.TimeRunning;
import io.github.yuazer.zpokeclean.utils.YamlUtils;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main extends JavaPlugin {
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public static List<TimeModule> modules = new ArrayList<>();
    private Set<String> TimeMsg = getConfig().getConfigurationSection("ClearMessage").getKeys(false);

    public Set<String> getTimeMsg() {
        return TimeMsg;
    }

    public void setTimeMsg(Set<String> timeMsg) {
        TimeMsg = timeMsg;
    }

    private static int all = 0;

    public static int getAll() {
        return all;
    }

    public static void setAll(int all) {
        Main.all = all;
    }

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginCommand("zpokeclean").setExecutor(new MainCommand());
        saveDefaultConfig();
        TimeRunning timeRunning = new TimeRunning();
        timeRunning.runTaskTimerAsynchronously(this, 0L, 20L);
        worldCleanTask();
        runAloForOne();
        logLoaded(this);
    }

    @Override
    public void onDisable() {
        logDisable(this);
        TimeMsg.clear();
        modules.clear();
    }

    public void runAloForOne() {
        if (YamlUtils.getConfigBoolean("AllForOne")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getServer().broadcastMessage(YamlUtils.getConfigMessage("Message.cleanMessageForAll")
                            .replace("%total%", String.valueOf(Main.getAll())
                            ));
                    Main.setAll(0);
                }
            }.runTaskTimerAsynchronously(this, 10L, YamlUtils.getConfigInt("GlobalSetting.TimeSetting") * 60L * 20);
        }
    }

    public void worldCleanTask() {
        for (String world : YamlUtils.getConfigStringList("EnableWorld")) {
            try {
                World w = bkToNmsWorld(Bukkit.getServer().getWorld(world));
                if (w != null) {
                    TimeModule timeModule = new TimeModule(w);
                    timeModule.runTaskTimerAsynchronously(this, 0L, YamlUtils.getConfigInt("GlobalSetting.TimeSetting") * 60L * 20);
                    modules.add(timeModule);
                }
            } catch (NullPointerException e) {
                System.out.println("§c配置文件存在服务器没有的世界!:" + world+ " 请检查配置文件的配置!");
            }
        }
    }
    public static void logLoaded(JavaPlugin plugin) {
        Bukkit.getLogger().info(String.format("§e[§b%s§e] §f已加载", plugin.getName()));
        Bukkit.getLogger().info("§b作者:§eZ菌");
        Bukkit.getLogger().info("§b版本:§e" + plugin.getDescription().getVersion());
    }

    public static void logDisable(JavaPlugin plugin) {
        Bukkit.getLogger().info(String.format("§e[§b%s§e] §c已卸载", plugin.getName()));
    }
    public static World bkToNmsWorld(org.bukkit.World world) {
        return ((CraftWorld)world).getHandle();
    }
}
