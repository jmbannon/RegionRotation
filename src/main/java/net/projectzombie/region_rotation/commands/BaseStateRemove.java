package net.projectzombie.region_rotation.commands;

import net.projectzombie.region_rotation.modules.StateController;
import org.bukkit.command.CommandSender;

import static net.projectzombie.region_rotation.commands.RRText.*;

/**
 * Created by jb on 9/4/16.
 */
public class BaseStateRemove extends CommandExecution
{
    private static BaseStateRemove CMD = new BaseStateRemove();
    static protected BaseStateRemove cmd() {
        return CMD;
    }

    private BaseStateRemove()
    {
        super(REMOVE_BASESTATE_CMD, REMOVE_BASESTATE_PERM, 2, REMOVE_BASESTATE_HELP);
    }

    static private String _RemoveBaseStateRegionDNE(final String regionNameDNE)
    {
        StringBuilder stb = new StringBuilder();
        stb.append("Cannot remove BaseState: ");
        stb.append(RRText.formatRegionName(regionNameDNE));
        stb.append(' ');
        stb.append("does not exist.");
        return stb.toString();
    }

    static private String _RemoveBaseStateSuccess(final String baseStateName)
    {
        final StringBuilder stb = new StringBuilder();
        stb.append("Successfully removed ");
        stb.append(RRText.formatRegionName(baseStateName));
        stb.append(".");
        return stb.toString();
    }

    static private String _RemoveBaseStateFailure()
    {
        return "An error occurred when removing BaseState.";
    }

    @Override
    protected String execute(final String args[],
                             final CommandSender sender)
    {
        final String baseStateName = args[1];

        final boolean success;

        if (!StateController.instance().baseStateExists(baseStateName)) {
            return _RemoveBaseStateRegionDNE(baseStateName);
        } else {
            success = StateController.instance().removeBaseState(baseStateName);
            if (success) {
                return _RemoveBaseStateSuccess(baseStateName);
            } else {
                return _RemoveBaseStateFailure();
            }
        }
    }
}
