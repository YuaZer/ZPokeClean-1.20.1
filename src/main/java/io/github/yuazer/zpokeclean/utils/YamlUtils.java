package io.github.yuazer.zpokeclean.utils;

import io.github.yuazer.zpokeclean.Main;

import java.util.List;

public class YamlUtils {
    public static String getConfigMessage(String path) {
        try {
            return Main.getInstance().getConfig().getString(path).replace("&", "§");
        } catch (NullPointerException e) {
            return "";
        }
    }

    public static boolean getConfigBoolean(String path) {
        return Main.getInstance().getConfig().getBoolean(path);
    }

    public static int getConfigInt(String path) {
        try {
            return Main.getInstance().getConfig().getInt(path);
        } catch (NullPointerException var3) {
            return 0;
        }
    }

    public static List<String> getConfigStringList(String path) {
        return Main.getInstance().getConfig().getStringList(path);
    }
}
