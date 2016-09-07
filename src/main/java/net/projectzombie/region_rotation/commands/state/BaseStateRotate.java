package net.projectzombie.region_rotation.commands.state;

import net.projectzombie.region_rotation.commands.RRText;
import net.projectzombie.region_rotation.modules.StateController;

import static net.projectzombie.region_rotation.commands.RRText.*;

/**
 * Created by jb on 9/4/16.
 */
public class BaseStateRotate extends StateExecution
{
    private static BaseStateRotate CMD = new BaseStateRotate();
    static protected BaseStateRotate cmd() {
        return CMD;
    }

    private BaseStateRotate()
    {
        super(ROTATE_BASESTATE_CMD, ROTATE_BASESTATE_PERM, 3, ROTATE_BASESTATE_HELP);
    }

    static private String _RotateStateDNE(final String regionDNE)
    {
        StringBuilder stb = new StringBuilder();
        stb.append("Cannot rotate BaseState: ");
        stb.append(RRText.formatRegionName(regionDNE));
        stb.append(' ');
        stb.append("does not exist.");
        return stb.toString();
    }

    static private String _RotateStateSuccess(final String altRegionName,
                                              final String baseStateName)
    {
        final StringBuilder stb = new StringBuilder();
        stb.append("Successfully rotated ");
        stb.append(RRText.formatRegionName(altRegionName));
        stb.append(" into ");
        stb.append(RRText.formatBaseStateName(baseStateName));
        stb.append(".");
        return stb.toString();
    }

    static private String _RotateStateFailure()
    {
        return "An error occurred when rotating BaseState.";
    }

    @Override
    protected String execute(final String args[],
                             final StateController controller)
    {
        final String baseStateName = args[1];
        final String altRegionName = args[2];
        final boolean success;

        boolean rotateAir = true;
        boolean broadcast = false;

        if (args.length >= 4) {
            rotateAir = Boolean.valueOf(args[3]);
        }
        if (args.length == 5) {
            broadcast = Boolean.valueOf(args[4]);
        }

        if (!controller.baseStateExists(baseStateName)) {
            return _RotateStateDNE(baseStateName);
        } else if (!controller.altStateExists(baseStateName, altRegionName)) {
            return _RotateStateDNE(altRegionName);
        } else {
            success = controller
                    .rotateBaseStateBroadcast(baseStateName, altRegionName, rotateAir, broadcast);
            if (success) {
                return _RotateStateSuccess(altRegionName, baseStateName);
            } else {
                return _RotateStateFailure();
            }
        }
    }
}
