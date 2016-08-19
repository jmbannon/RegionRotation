package net.projectzombie.region_rotation.file;

import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * FileBuffers read in a FileConfiguration and store all the necessary workings to write and save
 *  to that file.
 *  @author Gephery
 */
public class FileBuffer
{
    public YamlConfiguration file;
    private JavaPlugin plugin;
    private String fileName;
    private String fileFolder;

    /**
     * Description: Constructor for the UtilUGFiles, sets up file dependencies.
     * @param plugin = JavaPlugin managing the files, a JavaPlugin.
     * @param fileName = Name of file, sould be name.yml
     */
    public FileBuffer(final JavaPlugin plugin, final String fileName)
    {
        this.plugin = plugin;
        this.fileName = fileName;
        this.file = null;

    }

    public final String getFileFolder()         { return fileFolder; }
    public final JavaPlugin getJavaPlugin()         { return plugin; }
    public final String getFileName()           { return fileName; }
    public final YamlConfiguration getFile()    { return file; }

    /**
     * Main way to check if the fileFolder is correct for that world.
     * **IMPORTANT** To insure right file, use before every write and read. O(1) runtime.
     * @param world The world the file belongs to.
     * @return If the load was a success. 
     */
    public boolean safeLoadFile(World world)
    {
        return safeLoadFile(world.getUID()); 
    }

    /**
     * Main way to check if the fileFolder is correct for that world.
     * **IMPORTANT** To insure right file, use before every write and read. O(1) runtime.
     * @param worldUID The UUID of the world the file belongs to.
     * @return If the load was a success. 
     */
    public boolean safeLoadFile(UUID worldUID)
    {
        String newFileFolder = formatFileFolder(worldUID);
        if (!isFileLoaded() || !isFileFolderSame(newFileFolder))
        {
            return discLoadFile(newFileFolder);
        }
        return true; 
    }

    /** @return If the file is not null. */
    private boolean isFileLoaded()
    { return file != null; }

    /** @return If the file folder is same as one in field. */
    private boolean isFileFolderSame(String cFileFolder)
    { return fileFolder.equals(cFileFolder); }

    /** Used to update the referenced file from disc, will create if there is none. */
    private boolean discLoadFile(String cFileFolder)
    {
        fileFolder = cFileFolder;
        file = YamlConfiguration.loadConfiguration(
                new File(plugin.getDataFolder() + cFileFolder, fileName));
        return saveFiles();
    }

    /**
     * Saves file to disc.
     * @return If the save was successful.
     */
    public boolean saveFiles()
    {
        try
        {
            this.file.save(new File(plugin.getDataFolder() + fileFolder, fileName));
            return true;
        } catch (IOException exc)
        {
            exc.printStackTrace();
            return false;
        }
    }

    /** @return If the path going to not be null. */
    public boolean isSafePath(String path)
    { return file.get(path) != null; }

    /** @return The correct formatForChat for the file folder, going by world. */
    private String formatFileFolder(World world)
    { return formatFileFolder(world.getUID()); }

    /** @return The correct formatForChat for the file folder, going by world. */
    private String formatFileFolder(UUID worldUID)
    { return "/" + worldUID + "/"; }

    /** Way to make sure the file you want is the same as the one had. */
    public static String buildID(World world, String fileName)
    { return "/" + world.getUID() + "/" + fileName; }

}
