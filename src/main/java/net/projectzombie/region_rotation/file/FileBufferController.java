package net.projectzombie.region_rotation.file;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * Used keep track of the FileBuffers and fetch them, by their ID. To get ID use FileBuffer's
 *  static method. FileBuffers are what is used to read/write things to disc.
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

    /**
     * Used to get a non central FileBuffer.
     * @param world The world associated with the FileBuffer being get.
     * @param fileName Name of file, should be something like blank.yml.
     * @return FileBuffer being fetched.
     */
    public FileBuffer getFile(final World world, final String fileName)
    {
        String fileID = FileBuffer.buildID(world, fileName);
        if (!fileBuffers.containsKey(fileID))
            fileBuffers.put(fileID, new FileBuffer(PLUGIN, fileName));
        return fileBuffers.get(fileID);
    }

    /**
     * Used for when you want a central file.
     * @param fileName of file should be something like blank.yml.
     * @return FileBuffer being fetched.
     */
    public FileBuffer getFile(final String fileName)
    {
        String fileID = FileBuffer.buildID(fileName);
        if (!fileBuffers.containsKey(fileID))
            fileBuffers.put(fileID, new FileBuffer(PLUGIN, FilePath.fileName()));
        return fileBuffers.get(fileID);
    }
}
