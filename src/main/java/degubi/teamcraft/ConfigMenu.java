package degubi.teamcraft;

import net.minecraftforge.common.config.*;

@Config(modid = "tcm")
public final class ConfigMenu {
    private ConfigMenu(){}

    @Config.Comment("Command of the 1st custom bind.\nFormat: /command;/command \nE.g.: /time set day;/weather clear")
    public static String customBind1 = "";

    @Config.Comment("Command of the 2st custom bind.\nFormat: /command;/command \nE.g.: /time set day;/weather clear")
    public static String customBind2 = "";

    @Config.Comment("Command of the 3st custom bind.\nFormat: /command;/command \nE.g.: /time set day;/weather clear")
    public static String customBind3 = "";

    @Config.Comment("Command of the 4st custom bind.\nFormat: /command;/command \nE.g.: /time set day;/weather clear")
    public static String customBind4 = "";

    @Config.Comment("Command of the 5st custom bind.\nFormat: /command;/command \nE.g.: /time set day;/weather clear")
    public static String customBind5 = "";

    @Config.Comment("Enable Ingame FPS counter?")
    public static boolean enableFPSCounter = false;

    @Config.Comment("Enable Ingame Ping counter?")
    public static boolean enablePingCounter = false;

    @Config.Comment("Enable zooming With C")
    public static boolean enableZoom = false;
}