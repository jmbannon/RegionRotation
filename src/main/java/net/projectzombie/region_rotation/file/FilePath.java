package net.projectzombie.region_rotation.file;

import net.projectzombie.region_rotation.modules.BaseState;

import java.util.UUID;

/**
 * Collection of useful file paths in the BaseState configurations.
 * @author Gephery
 */
public class FilePath
{
    public static String fileName = "region_rotation.yml";
    private static final String BASE_STATE_ROOT = "region";

    public static String fileName() { return fileName; }

    public static String baseStates()
    { return BASE_STATE_ROOT; }

    public static String baseState(String regionName, UUID worldUID)
    { return BASE_STATE_ROOT + "." + BaseState.toString(regionName, worldUID); }

    public static String baseState(BaseState baseState)
    { return BASE_STATE_ROOT + "." +
                baseState(baseState.getRegionName(), baseState.getWorldUID()); }

    public static String altStates(String regionName, UUID worldUID)
    { return baseState(regionName, worldUID) + ".alts"; }

    public static String altStates(BaseState baseState)
    { return altStates(baseState.getRegionName(), baseState.getWorldUID()); }

    public static String backupState(String regionName, UUID worldUID)
    { return baseState(regionName, worldUID) + ".backup"; }

    public static String backupState(BaseState baseState)
    { return backupState(baseState.getRegionName(), baseState.getWorldUID()); }

    public static String currentState(String regionName, UUID worldUID)
    { return baseState(regionName, worldUID) + ".current"; }

    public static String currentState(BaseState baseState)
    { return currentState(baseState.getRegionName(), baseState.getWorldUID()); }
}