package net.projectzombie.region_rotation.controller;

import net.projectzombie.consistentchatapi.PluginChat;
import net.projectzombie.region_rotation.modules.BaseState;
import org.bukkit.ChatColor;

/**
 * Used to access lots of static command specifics.
 * @author Gephery
 */
public class BaseStateText
{
    private static final PluginChat chat = new PluginChat("Region Rotation", ChatColor.GREEN,
                                                          ChatColor.WHITE, ChatColor.BOLD,
                                                          ChatColor.ITALIC);
    private static final String tag = chat.getTag();

    public static final String COMMAND_ROOT = "rr";

    static protected String

    COMMAND_LIST                = formatForHelp("<add:addalt:remove:reset:rotate>"),

    ADD_BASESTATE_CMD           = "add",
    ADD_ALT_BASESTATE_CMD       = "addalt",
    REMOVE_BASESTATE_CMD        = "remove",
    RESET_BASESTATE_CMD         = "reset",
    ROTATE_BASESTATE_CMD        = "rotate",
    INFO_CMD                    = "info",

    ADD_BASESTATE_PERM          = "RR.add",
    ADD_ALT_BASESTATE_PERM      = "RR.addalt",
    REMOVE_BASESTATE_PERM       = "RR.remove",
    RESET_BASESTATE_PERM        = "RR.reset",
    ROTATE_BASESTATE_PERM       = "RR.rotate",
    INFO_PERM                   = "RR.info",

    ADD_BASESTATE_HELP          = formatForHelp("add <rName> <world> <backup rName> <backup world"),
    ADD_ALT_BASESTATE_HELP      = formatForHelp("addalt <rName> <alt rName> <alt world>"),
    REMOVE_BASESTATE_HELP       = formatForHelp("remove <rName>"),
    RESET_BASESTATE_HELP        = formatForHelp("reset <rName> <broadcast>"),
    ROTATE_BASESTATE_HELP       = formatForHelp("rotate <rName> <alt rName> <rotate air> <broadcast>"),
    INFO_HELP                   = formatForHelp("info <rName>"),

    ADD_BASESTATE_SUCCESS       = "Your BaseState has been set up.",
    ADD_BASESTATE_FAIL          = "Your BaseState could not be created.",
    ADD_ALT_BASESTATE_SUCCESS   = "Your Alt has been added.",
    ADD_ALT_BASESTATE_FAIL      = "Your Alt could not be added.",
    REMOVE_BASESTATE_SUCCESS    = "BaseState was deleted.",
    REMOVE_BASESTATE_FAIL       = "BaseState could not be deleted.",
    RESET_BASESTATE_SUCCESS     = "BaseState was successfully reset.",
    RESET_BASESTATE_FAIL        = "BaseState reset failed",
    ROTATE_BASESTATE_SUCCESS    = "BaseState rotated",
    ROTATE_BASESTATE_FAIL       = "BaseState rotate failed";

    /** Used to add the tag and allow difference in failed and success message.
     * txtS will be used if success is true and txtF will be used if success is false.
     */
    public static String formatForChat(final String txtS, final String txtF, final boolean success)
    { return tag + (success ? txtS : txtF); }

    public static String formatForHelp(final String txt)
    { return "/" + COMMAND_ROOT + " " + txt; }

    public static String addBaseState(final BaseState baseState, final boolean success)
    { return formatForChat(ADD_BASESTATE_SUCCESS, ADD_BASESTATE_HELP, success); }

    public static String addAltState(final boolean success)
    { return formatForChat(ADD_ALT_BASESTATE_SUCCESS, ADD_ALT_BASESTATE_HELP, success); }

    public static String removeBaseState(final boolean success)
    { return formatForChat(REMOVE_BASESTATE_SUCCESS, REMOVE_BASESTATE_HELP, success); }

    public static String resetBaseState(final boolean success)
    { return formatForChat(RESET_BASESTATE_SUCCESS, RESET_BASESTATE_HELP, success); }

    public static String rotateBaseState(final boolean success)
    { return formatForChat(ROTATE_BASESTATE_SUCCESS, ROTATE_BASESTATE_HELP, success); }

    public static String info(final boolean success, final BaseState baseState)
    { return formatForChat(baseState != null ? baseState.toString() : "", INFO_HELP, success); }

    public static String commandHelp()
    { return COMMAND_LIST; }
}
