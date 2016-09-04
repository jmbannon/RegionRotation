package net.projectzombie.region_rotation.commands;

import net.projectzombie.region_rotation.modules.StateController;
import org.bukkit.command.CommandSender;

import static net.projectzombie.region_rotation.commands.RRText.*;

/**
 * Created by jb on 9/4/16.
 */
public class BaseStateReset extends CommandExecution
{
    private static BaseStateReset CMD = new BaseStateReset();
    static protected BaseStateReset cmd() {
        return CMD;
    }

    private BaseStateReset()
    {
        super(RESET_BASESTATE_CMD, RESET_BASESTATE_PERM, 2, RESET_BASESTATE_HELP);
    }

    static private String _ResetBaseStateRegionDNE(final String regionNameDNE)
    {
        StringBuilder stb = new StringBuilder();
        stb.append("Cannot reset BaseState: ");
        stb.append(RRText.formatRegionName(regionNameDNE));
        stb.append(' ');
        stb.append("does not exist.");
        return stb.toString();
    }

    static private String _ResetBaseStateSuccess(final String baseStateName)
    {
        final StringBuilder stb = new StringBuilder();
        stb.append("Successfully reset ");
        stb.append(RRText.formatRegionName(baseStateName));
        stb.append(".");
        return stb.toString();
    }

    static private String _ResetBaseStateFailure()
    {
        return "An error occurred when resetting BaseState.";
    }

    @Override
    protected String execute(final String args[],
                             final CommandSender sender)
    {
        final String baseStateName = args[1];
        final boolean broadcast;
        final boolean success;

        if (args.length == 3) {
            broadcast = Boolean.valueOf(args[2]);
        } else {
            broadcast = false;
        }

        if (!StateController.instance().baseStateExists(baseStateName)) {
            return _ResetBaseStateRegionDNE(baseStateName);
        } else {
            success = StateController.instance().resetBaseState(baseStateName, broadcast);
            if (success) {
                return _ResetBaseStateSuccess(baseStateName);
            } else {
                return _ResetBaseStateFailure();
            }
        }
    }
}
