package net.projectzombie.region_rotation.commands;

import net.projectzombie.consistentchatapi.PluginChat;
import net.projectzombie.region_rotation.modules.StateController;
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

    static public String formatBaseStateName(final String baseStateName)
    {
        return CHAT.toK1(baseStateName);
    }

    static public String formatAltStateName(final String altStateName)
    {
        return CHAT.toK1(altStateName);
    }

    static public String formatWorldName(final String worldName)
    {
        return CHAT.toK2(worldName);
    }

    static public String formatControllerName(final String contrName)
    {
        return CHAT.toK1(contrName);
    }

    static public String formatTitle(final String title) {
        return CHAT.toK1(title);
    }

    static public String formatRegionName(final String regionName)
    {
        return CHAT.toK2(regionName);
    }

    static public void formatToSender(final CommandSender sender,
                                         final String toSender)
    {
        final StringBuilder stb = new StringBuilder();
        stb.append(CHAT.getTag());
        stb.append(CHAT.toPT(toSender));
        sender.sendMessage(stb.toString());
    }

    static public final String CONTROLLER_NAME_REGEX_MATCH = "^[a-zA-Z0-9]*$";
    static public final String COMMAND_ROOT = "rr";
    static public final String PERM_ROOT = "RR";

    static public String formatControllerCommand(final String txt)
    {
        return "/" + COMMAND_ROOT + " " + txt;
    }

    static protected String formatStateCommand(final String txt)
    {
        return "/" + COMMAND_ROOT + " <c> " + txt;
    }

    static protected String formatStateCommand(final StateController controller, final String txt)
    {
        return "/" + COMMAND_ROOT + " " + CHAT.toK1(controller.getName()) + " " + txt;
    }

    static public String formatPermission(final String perm)
    {
        return PERM_ROOT + "." + perm;
    }

    static public String formatPermission(final StateController controller, final String perm)
    {
        return PERM_ROOT + "." + controller.getName() + "." + perm;
    }

    static public final String

    ADD_BASESTATE_CMD           = "create",
    ADD_ALT_BASESTATE_CMD       = "addalt",
    REMOVE_BASESTATE_CMD        = "destroy",
    REMOVE_ALTSTATE_CMD         = "removealt",
    RESET_BASESTATE_CMD         = "reset",
    ROTATE_BASESTATE_CMD        = "rotate",
    INFO_BASESTATE_CMD          = "info",
    LIST_BASESTATE_CMD          = "list",
    CHANGE_BROADCAST_CMD        = "cbroadcast",

    CREATE_CONTROLLER_CMD       = "create",
    DESTROY_CONTROLLER_CMD      = "destroy",
    LIST_CONTROLLER_CMD         = "list",

    ADD_BASESTATE_PERM          = "create",
    ADD_ALT_BASESTATE_PERM      = "addalt",
    REMOVE_BASESTATE_PERM       = "destroy",
    REMOVE_ALTSTATE_PERM        = "removealt",
    RESET_BASESTATE_PERM        = "reset",
    ROTATE_BASESTATE_PERM       = "rotate",
    INFO_BASESTATE_PERM         = "info",
    LIST_BASESTATE_PERM         = "list",
    CHANGE_BROADCAST_PERM       = "cbroadcast",

    CREATE_CONTROLLER_PERM      = "controller.create",
    DESTROY_CONTROLLER_PERM     = "controller.destroy",
    LIST_CONTROLLER_PERM        = "controller.list",

    STATE_COMMAND_LIST          = "<create:addalt:destroy:removealt:reset:rotate:info:list:cbroadcast>",
    CONTROLLER_COMMAND_LIST     = "<" + formatControllerName("cName") + ":create:destroy:list>",

    ADD_BASESTATE_HELP          = "<cName> create <rName> <world> <backup rName> <backup world>",
    ADD_ALT_BASESTATE_HELP      = "<cName> addalt <rName> <alt rName> <alt world>",
    REMOVE_BASESTATE_HELP       = "<cName> destroy <rName>",
    REMOVE_ALTSTATE_HELP        = "<cName> removealt <rName> <alt rName>>",
    RESET_BASESTATE_HELP        = "<cName> reset <rName> <broadcast = F>",
    ROTATE_BASESTATE_HELP       = "<cName> rotate <rName> <alt rName> <rotate air = T> <broadcast = F>",
    INFO_BASESTATE_HELP         = "<cName> info <rName>",
    LIST_BASESTATE_HELP         = "<cName> list ",
    CHANGE_BROADCAST_HELP       = "<cName> cbroadcast <rName> <world> <stateName or backup> <BMsg...>",

    CREATE_CONTROLLER_HELP      = "create <cName>",
    DESTROY_CONTROLLER_HELP     = "destroy <cName>",
    LIST_CONTROLLER_HELP        = "list <cName>";

    static public String PERMISSIONS[] = {
        ADD_BASESTATE_PERM,
        ADD_ALT_BASESTATE_PERM,
        REMOVE_BASESTATE_PERM,
        REMOVE_ALTSTATE_PERM,
        RESET_BASESTATE_PERM,
        ROTATE_BASESTATE_PERM,
        INFO_BASESTATE_PERM,
        LIST_BASESTATE_PERM,
        CHANGE_BROADCAST_PERM,
        CREATE_CONTROLLER_PERM,
        DESTROY_CONTROLLER_PERM,
        LIST_CONTROLLER_PERM
    };

}
