package net.projectzombie.region_rotation.commands.state;

import net.projectzombie.region_rotation.commands.RRText;
import net.projectzombie.region_rotation.modules.StateController;

import static net.projectzombie.region_rotation.commands.RRText.*;

/**
 * Created by jb on 9/4/16.
 */
public class BaseStateInfo extends StateExecution
{
    private static BaseStateInfo CMD = new BaseStateInfo();
    static protected BaseStateInfo cmd() {
        return CMD;
    }

    private BaseStateInfo()
    {
        super(INFO_BASESTATE_CMD, INFO_BASESTATE_PERM, 2, INFO_BASESTATE_HELP);
    }

    static private String _RemoveBaseStateRegionDNE(final String regionNameDNE)
    {
        StringBuilder stb = new StringBuilder();
        stb.append("Cannot view BaseState: ");
        stb.append(RRText.formatRegionName(regionNameDNE));
        stb.append(' ');
        stb.append("does not exist.");
        return stb.toString();
    }

    @Override
    protected String execute(final String args[],
                             final StateController controller)
    {
        final String baseStateName = args[1];

        if (!controller.baseStateExists(baseStateName)) {
            return _RemoveBaseStateRegionDNE(baseStateName);
        } else {
            return controller.getBaseStateInfo(baseStateName);
        }
    }
}
