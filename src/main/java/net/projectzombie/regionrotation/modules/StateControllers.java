package net.projectzombie.regionrotation.modules;

import net.projectzombie.regionrotation.main.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static net.projectzombie.regionrotation.commands.RRText.CONTROLLER_NAME_REGEX_MATCH;

/**
 * Created by jb on 9/4/16.
 */
public class StateControllers
{
    static private final String CONTROLLER_FILE_EXTENSION = ".yml";
    static private final String DEFAULT_CONTROLLER = "default";
    static private final String DEFAULT_FILE_NAME = DEFAULT_CONTROLLER + CONTROLLER_FILE_EXTENSION;

    static private final HashMap<String, StateController> CONTROLLERS = new HashMap<>();

    private StateControllers() { /* Do nothing. */ }

    /** Instantiates all StateControllers and their respective BaseStates. */
    static public void onEnable()
    {
        final File dir = Main.plugin().getDataFolder();
        String fileName;
        String controllerName;
        StateController controller;

        if (!dir.isDirectory()) {
            dir.mkdir();
        }

        for (File file : dir.listFiles()) {
            fileName = file.getName();

            if (fileName.endsWith(".yml")) {
                controllerName = fileName.replace(".yml", "");
                controller = new StateController(controllerName, new StateBuffer(dir, fileName));
                if (controller.isValid()) {
                    CONTROLLERS.put(controller.getName(), controller);
                }
            }
        }

        if (CONTROLLERS.isEmpty()) {
            controller = new StateController(DEFAULT_CONTROLLER, new StateBuffer(dir, DEFAULT_FILE_NAME));
            CONTROLLERS.put(controller.getName(), controller);
        }
    }

    /** Saves all BaseStates for every StateController. To be called on server disable. */
    static public void onDisable() {
        CONTROLLERS.values().forEach(controller -> controller.saveBaseStates());
    }

    /**
     * Retrieves StateController from the Controllers list.
     * @param controllerName Name of StateController.
     * @return StateController if it exists. Null otherwise.
     */
    static public StateController get(final String controllerName) {
        return CONTROLLERS.get(controllerName);
    }

    /**
     * Attempts to create a StateController to the Controllers list.
     * If successful, the StateController will create its file
     * in /plugins/RegionRotation/'controllerName'.yml
     *
     * @param controllerName Name of StateController. Must be alphanumeric and not already exist.
     * @return True: controller was added successfully. False otherwise.
     */
    static public boolean create(final String controllerName) {
        if (controllerName.matches(CONTROLLER_NAME_REGEX_MATCH) && !CONTROLLERS.containsValue(controllerName)) {
            final File dir = Main.plugin().getDataFolder();
            final String fileName = controllerName + CONTROLLER_FILE_EXTENSION;
            final StateController contr = new StateController(controllerName, new StateBuffer(dir, fileName));
            if (contr.isValid()) {
                CONTROLLERS.put(contr.getName(), contr);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes StateController from the Controllers list.
     * @param controllerName Name of the StateController to destroy.
     * @return True if the StateController was removed successfully. False otherwise.
     */
    static public boolean destroy(final String controllerName) {
        final StateController toDestroy = CONTROLLERS.remove(controllerName);
        if (toDestroy != null) {
            return toDestroy.destroy();
        } else {
            return true;
        }
    }

    /**
     * @param controllerName StateController name to check if it exists.
     * @return True if the specified StateController exists. False otherwise.
     */
    static public boolean contains(final String controllerName) {
        return CONTROLLERS.containsKey(controllerName);
    }

    static public ArrayList<String> list() {
        final ArrayList<String> names = new ArrayList<>();
        CONTROLLERS.values().forEach(contr -> names.add(contr.getName()));
        return names;
    }


}
