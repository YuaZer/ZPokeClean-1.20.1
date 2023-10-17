package io.github.yuazer.zpokeclean.cleanmode;


import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import io.github.yuazer.zpokeclean.Main;
import io.github.yuazer.zpokeclean.globaltime.TimeRunning;
import io.github.yuazer.zpokeclean.utils.YamlUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;

import org.bukkit.scheduler.BukkitRunnable;

public class TimeModule extends BukkitRunnable {
    private World world;
    public TimeModule(World world) {
        this.world = world;
    }

    @Override
    public void run() {
        int count = 0;
        for (Entity entity : world.getMinecraftWorld().getWorld().getNMSEntities()) {
            if (entity instanceof PixelmonEntity entityPixelmon) {
                if (entityPixelmon.hasOwner() || entityPixelmon.battleController != null) {
                    continue;
                }
                if (YamlUtils.getConfigStringList("SafePokemon").contains(entityPixelmon.getPokemonName())) {
                    continue;
                }
                if (!YamlUtils.getConfigBoolean("GlobalSetting.shiny") && entityPixelmon.getPokemon().isShiny()) {
                    continue;
                }
                if (!YamlUtils.getConfigBoolean("GlobalSetting.lengendary") && entityPixelmon.isLegendary()) {
                    continue;
                }
                if (!YamlUtils.getConfigBoolean("GlobalSetting.ultrabeast") && entityPixelmon.getPokemon().isUltraBeast()) {
                    continue;
                }
                if (!YamlUtils.getConfigBoolean("GlobalSetting.boss") && entityPixelmon.isBossPokemon()) {
                    continue;
                }
                if (!YamlUtils.getConfigBoolean("GlobalSetting.unNormal") && entityPixelmon.canDespawn) {
                    continue;
                }
                entityPixelmon.unloadEntity();
                count++;
            }
        }
        TimeRunning.setGlobalTime(0);
        if (!YamlUtils.getConfigBoolean("AllForOne")) {
            Bukkit.getServer().broadcastMessage(YamlUtils.getConfigMessage("Message.cleanMessage")
                    .replace("%total%", String.valueOf(count))
                    .replace("%world%", world.getWorld().getName()));
        } else {
            Main.setAll(Main.getAll() + count);
        }
    }
}
