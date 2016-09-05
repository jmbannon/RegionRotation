package net.projectzombie.region_rotation.commands.state;

import net.projectzombie.region_rotation.modules.StateController;

import java.util.ArrayList;

import static net.projectzombie.region_rotation.commands.RRText.*;

/**
 * Created by jb on 9/4/16.
 */
public class BaseStateList extends StateExecution
{
    private static BaseStateList CMD = new BaseStateList();
    static protected BaseStateList cmd() {
        return CMD;
    }

    private BaseStateList()
    {
        super(LIST_BASESTATE_CMD, LIST_BASESTATE_PERM, 1, LIST_BASESTATE_HELP);
    }

    static private String _NoBaseStates() {
        return "No BaseStates exist";
    }

    @Override
    protected String execute(final String args[],
                             final StateController controller)
    {
        final StringBuilder stb = new StringBuilder();
        final ArrayList<String> baseStateNames = controller.getBaseStateDisplayNames();

        if (baseStateNames != null && baseStateNames.size() >= 1) {
            stb.append("Base States:\n");
            baseStateNames.forEach(name -> {
                stb.append(" - ");
                stb.append(name);
                stb.append('\n');
            });
            return stb.deleteCharAt(stb.length() - 1).toString();
        } else {
            return _NoBaseStates();
        }
    }
}
