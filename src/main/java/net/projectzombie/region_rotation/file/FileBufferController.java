package net.projectzombie.region_rotation.file;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * Used keep track of the FileBuffers and fetch them.
 * @author Gephery
 */
public class FileBufferController
{
    private static FileBufferController FILE_CONTROLLER_SINGLETON = null;
    private static Plugin PLUGIN;

    private final HashMap<String, FileBuffer> fileBuffers;

    /** Initializes the FileBufferController. Must be called first in Main. */
    static public void init(final Plugin plugin)
    {
        FILE_CONTROLLER_SINGLETON = new FileBufferController(plugin);
    }

    /** @return Initialized FileBufferController. */
    static public FileBufferController instance()
    { return FILE_CONTROLLER_SINGLETON; }

    /** @return Commanding plugin */
    public static Plugin plugin()
    { return PLUGIN; }

    /**
     * Constructor for the Controller.
     * @param plugin The plugin using the fileBuffers.
     */
    private FileBufferController(final Plugin plugin)
    {
        PLUGIN = plugin;
        this.fileBuffers = new HashMap<>();
    }

    /** @return FileBuffer for that world. */
    public FileBuffer getFile(World world)
    {
        String fileID = FileBuffer.buildID(world, FilePath.fileName());
        if (!fileBuffers.containsKey(fileID))
            fileBuffers.put(fileID, new FileBuffer((JavaPlugin) PLUGIN, FilePath.fileName()));
        return fileBuffers.get(fileID);
    }
}
