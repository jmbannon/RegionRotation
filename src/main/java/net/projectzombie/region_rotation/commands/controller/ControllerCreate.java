package net.projectzombie.region_rotation.commands.controller;

import net.projectzombie.region_rotation.commands.RRText;
import net.projectzombie.region_rotation.modules.StateControllers;

import static net.projectzombie.region_rotation.commands.RRText.*;

/**
 * Created by jb on 9/4/16.
 */
public class ControllerCreate extends ControllerExecution
{
    private static ControllerCreate CMD = new ControllerCreate();
    static protected ControllerCreate cmd() {
        return CMD;
    }

    private ControllerCreate() {
        super(CREATE_CONTROLLER_CMD, CREATE_CONTROLLER_PERM, 2, CREATE_CONTROLLER_HELP);
    }

    static private String _ControllerCreateSuccess(final String name) {
        return "Successfully created " + RRText.formatControllerName(name) + " as a controller";
    }

    static private String _ControllerCreateFailure() {
        return "An error occurred trying to create the controller";
    }

    static private String _InvalidName() {
        return "Controller name must be alphanumeric.";
    }

    @Override
    protected String execute(final String args[]) {
        final String controllerName = args[1];
        final boolean success;

        if (controllerName.matches(CONTROLLER_NAME_REGEX_MATCH)) {
            success = StateControllers.create(controllerName);
            if (success) {
                return _ControllerCreateSuccess(controllerName);
            } else {
               return  _ControllerCreateFailure();
            }
        } else {
            return _InvalidName();
        }
    }
}
