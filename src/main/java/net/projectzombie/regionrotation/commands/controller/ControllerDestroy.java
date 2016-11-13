package net.projectzombie.regionrotation.commands.controller;

import net.projectzombie.regionrotation.commands.RRText;
import net.projectzombie.regionrotation.modules.StateControllers;

import static net.projectzombie.regionrotation.commands.RRText.*;

/**
 * Created by jb on 9/5/16.
 */
public class ControllerDestroy extends ControllerExecution
{
    private static ControllerDestroy CMD = new ControllerDestroy();
    static protected ControllerDestroy cmd() {
        return CMD;
    }

    private ControllerDestroy() {
        super(DESTROY_CONTROLLER_CMD, DESTROY_CONTROLLER_PERM, 2, DESTROY_CONTROLLER_HELP);
    }

    static private String _ControllerDestroySuccess() {
        return "Successfully removed controller";
    }

    static private String _ControllerDestroyFailure() {
        return "An error occurred trying to destroy the controller";
    }

    static private String _InvalidName(final String name) {
        return "Controller with name " + RRText.formatControllerName(name) + " does not exist.";
    }

    @Override
    protected String execute(final String args[]) {
        final String controllerName = args[1];
        final boolean success;

        if (StateControllers.contains(controllerName)) {
            success = StateControllers.destroy(controllerName);
            if (success) {
                return _ControllerDestroySuccess();
            } else {
                return  _ControllerDestroyFailure();
            }
        } else {
            return _InvalidName(controllerName);
        }
    }
}
