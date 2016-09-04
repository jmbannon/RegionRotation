package net.projectzombie.region_rotation.commands;

import net.projectzombie.region_rotation.modules.StateController;
import org.bukkit.command.CommandSender;

import static net.projectzombie.region_rotation.commands.RRText.*;

/**
 * Created by jb on 9/4/16.
 */
public class AltStateRemove extends CommandExecution
{
    private static AltStateRemove CMD = new AltStateRemove();
    static protected AltStateRemove cmd() {
        return CMD;
    }

    private AltStateRemove()
    {
        super(REMOVE_ALTSTATE_CMD, REMOVE_ALTSTATE_PERM, 3, REMOVE_ALTSTATE_HELP);
    }

    static private String _RemoveAltStateRegionDNE(final String regionNameDNE)
    {
        StringBuilder stb = new StringBuilder();
        stb.append("Cannot remove AltState: ");
        stb.append(RRText.formatRegionName(regionNameDNE));
        stb.append(' ');
        stb.append("does not exist.");
        return stb.toString();
    }

    static private String _RemoveAltStateSuccess(final String altRegionName,
                                                 final String baseStateName)
    {
        final StringBuilder stb = new StringBuilder();
        stb.append("Successfully removed ");
        stb.append(RRText.formatAltStateName(altRegionName));
        stb.append(" from ");
        stb.append(RRText.formatBaseStateName(baseStateName));
        stb.append(".");
        return stb.toString();
    }

    static private String _RemoveAltStateFailure()
    {
        return "An error occurred when removing AltState to BaseState.";
    }

    @Override
    protected String execute(final String args[],
                             final CommandSender sender)
    {
        final String baseStateName = args[1];
        final String altRegionName = args[2];

        final boolean success;

        if (!StateController.instance().baseStateExists(baseStateName)) {
            return _RemoveAltStateRegionDNE(baseStateName);
        } else if (!StateController.instance().altStateExists(baseStateName, altRegionName)) {
            return _RemoveAltStateRegionDNE(altRegionName);
        } else {
            success = StateController.instance().removeAltState(baseStateName, altRegionName);
            if (success) {
                return _RemoveAltStateSuccess(altRegionName, baseStateName);
            } else {
                return _RemoveAltStateFailure();
            }
        }
    }
}
