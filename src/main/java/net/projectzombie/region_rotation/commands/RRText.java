package net.projectzombie.region_rotation.commands;

import net.projectzombie.consistentchatapi.PluginChat;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Used to access lots of static command specifics.
 * @author Gephery
 */
public class RRText
{

    private RRText() { /* Does nothing. */ }

    static protected final PluginChat CHAT = new PluginChat("Region Rotation", ChatColor.GREEN,
                                                          ChatColor.GRAY, ChatColor.RED,
                                                          ChatColor.YELLOW);

    static protected String formatBaseStateName(final String baseStateName)
    {
        return CHAT.toK1(baseStateName);
    }

    static protected String formatAltStateName(final String altStateName)
    {
        return CHAT.toK1(altStateName);
    }

    static protected String formatWorldName(final String worldName)
    {
        return CHAT.toK2(worldName);
    }

    static protected String formatRegionName(final String regionName)
    {
        return CHAT.toK2(regionName);
    }

    static protected void formatToSender(final CommandSender sender,
                                         final String toSender)
    {
        final StringBuilder stb = new StringBuilder();
        stb.append(CHAT.getTag());
        stb.append(CHAT.toPT(toSender));
        sender.sendMessage(stb.toString());
    }

    static public final String COMMAND_ROOT = "rr";

    static protected String formatCommand(final String txt)
    { return "/" + COMMAND_ROOT + " " + txt; }

    static protected final String

    ADD_BASESTATE_CMD           = "add",
    ADD_ALT_BASESTATE_CMD       = "addalt",
    REMOVE_BASESTATE_CMD        = "remove",
    REMOVE_ALTSTATE_CMD         = "removealt",
    RESET_BASESTATE_CMD         = "reset",
    ROTATE_BASESTATE_CMD        = "rotate",
    INFO_BASESTATE_CMD          = "info",
    LIST_BASESTATE_CMD          = "list",

    ADD_BASESTATE_PERM          = "RR.add",
    ADD_ALT_BASESTATE_PERM      = "RR.addalt",
    REMOVE_BASESTATE_PERM       = "RR.remove",
    REMOVE_ALTSTATE_PERM        = "RR.removealt",
    RESET_BASESTATE_PERM        = "RR.reset",
    ROTATE_BASESTATE_PERM       = "RR.rotate",
    INFO_BASESTATE_PERM         = "RR.info",
    LIST_BASESTATE_PERM         = "RR.list",

    COMMAND_LIST                = formatCommand("<add:addalt:remove:removealt:reset:rotate:info:list>"),

    ADD_BASESTATE_HELP          = formatCommand("add <rName> <world> <backup rName> <backup world>"),
    ADD_ALT_BASESTATE_HELP      = formatCommand("addalt <rName> <alt rName> <alt world>"),
    REMOVE_BASESTATE_HELP       = formatCommand("remove <rName>"),
    REMOVE_ALTSTATE_HELP        = formatCommand("removealt <rName> <alt rName>>"),
    RESET_BASESTATE_HELP        = formatCommand("reset <rName> <broadcast = F>"),
    ROTATE_BASESTATE_HELP       = formatCommand("rotate <rName> <alt rName> <rotate air = T> <broadcast = F>"),
    INFO_BASESTATE_HELP         = formatCommand("info <rName>"),
    LIST_BASESTATE_HELP         = formatCommand("list");

    static public String PERMISSIONS[] = {
        ADD_BASESTATE_PERM,
        ADD_ALT_BASESTATE_PERM,
        REMOVE_BASESTATE_PERM,
        REMOVE_ALTSTATE_PERM,
        RESET_BASESTATE_PERM,
        ROTATE_BASESTATE_PERM,
        INFO_BASESTATE_PERM,
        LIST_BASESTATE_PERM
    };

}
