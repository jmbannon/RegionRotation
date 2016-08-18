package net.projectzombie.region_rotation.file;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * Used keep track of the FileBuffers and fetch them.
 * @author Gephery
 */
public class FileBufferController
{
    private static FileBufferController FILE_CONTROLLER_SINGLETON;
    private static JavaPlugin PLUGIN;

    private final HashMap<String, FileBuffer> fileBuffers;

    /** Initializes the FileBufferController. Must be called first in Main. */
    static public void init(final JavaPlugin plugin)
    {
        FILE_CONTROLLER_SINGLETON = new FileBufferController(plugin);
    }

    /** @return Initialized FileBufferController. */
    static public FileBufferController instance()
    { return FILE_CONTROLLER_SINGLETON; }

    /** @return Commanding plugin */
    public static JavaPlugin plugin()
    { return PLUGIN; }

    /**
     * Constructor for the Controller.
     * @param plugin The plugin using the fileBuffers.
     */
    private FileBufferController(final JavaPlugin plugin)
    {
        PLUGIN = plugin;
        this.fileBuffers = new HashMap<>();
    }

    /** @return FileBuffer for that world. */
    public FileBuffer getFile(final World world)
    {
        String fileID = FileBuffer.buildID(world, FilePath.fileName());
        if (!fileBuffers.containsKey(fileID))
            fileBuffers.put(fileID, new FileBuffer(PLUGIN, FilePath.fileName()));
        return fileBuffers.get(fileID);
    }
}
