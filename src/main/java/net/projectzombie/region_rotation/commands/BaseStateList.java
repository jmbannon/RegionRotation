package net.projectzombie.region_rotation.commands;

import net.projectzombie.region_rotation.modules.StateController;
import org.bukkit.command.CommandSender;

import java.util.Set;

import static net.projectzombie.region_rotation.commands.RRText.*;

/**
 * Created by jb on 9/4/16.
 */
public class BaseStateList extends CommandExecution
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
                             final CommandSender sender) {
        final StringBuilder stb = new StringBuilder();
        final Set<String> baseStateNames = StateController.instance().getBaseStateNames();

        if (baseStateNames != null && baseStateNames.size() >= 1) {
            for (String baseStateN : baseStateNames) {
                stb.append(baseStateN);
                stb.append('\n');
            }
            return stb.deleteCharAt(stb.length() - 1).toString();
        } else {
            return _NoBaseStates();
        }
    }
}
