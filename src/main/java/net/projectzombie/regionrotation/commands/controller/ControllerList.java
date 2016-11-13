package net.projectzombie.regionrotation.commands.controller;

import net.projectzombie.regionrotation.commands.RRText;
import net.projectzombie.regionrotation.modules.StateControllers;

import java.util.ArrayList;

import static net.projectzombie.regionrotation.commands.RRText.*;

/**
 * Created by jb on 9/5/16.
 */
public class ControllerList extends ControllerExecution
{
    private static ControllerList CMD = new ControllerList();
    static protected ControllerList cmd() {
        return CMD;
    }

    private ControllerList() {
        super(LIST_CONTROLLER_CMD, LIST_CONTROLLER_PERM, 1, LIST_CONTROLLER_HELP);
    }

    @Override
    protected String execute(final String args[]) {
        final StringBuilder stb = new StringBuilder();
        final ArrayList<String> names = StateControllers.list();
        stb.append("State Controllers:\n");

        if (!names.isEmpty()) {
            names.forEach(name -> {
                stb.append(" - ");
                stb.append(RRText.formatControllerName(name));
                stb.append('\n');
            });
            return stb.deleteCharAt(stb.length() - 1).toString();
        } else {
            return "No controllers exist.";
        }
    }
}
