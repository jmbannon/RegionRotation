package net.projectzombie.region_rotation.file;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by maxgr on 7/15/2016.
 */
public class FileBuffer
{
    public FileConfiguration file;
    public JavaPlugin plugin;
    public String fileName;
    public String fileFolder;

    /**
     * Description: Constructor for the UtilUGFiles, sets up file dependencies.
     * @param plugin = Plugin managing the files, a JavaPlugin.
     * @param fileName = Name of file, sould be name.yml
     */
    public FileBuffer(JavaPlugin plugin, String fileName)
    {
        this.plugin = plugin;
        this.fileName = fileName;
        this.file = null;

    }

    public final String getFileFolder()         { return fileFolder; }
    public final JavaPlugin getPlugin()         { return plugin; }
    public final String getFileName()           { return fileName; }
    public final FileConfiguration getFile()    { return file; }

    /**
     * Main way to check if the fileFolder is correct for that world.
     * **IMPORTANT** To insure right file, use before every write and read. O(1) runtime.
     * @param world The world the file belongs to.
     */
    public void safeLoadFile(World world)
    {
        String newFileFolder = formatFileFolder(world);
        if (!isFileLoaded() || !isFileFolderSame(newFileFolder))
            discLoadFile(newFileFolder);
    }

    /**
     * Main way to check if the fileFolder is correct for that world.
     * **IMPORTANT** To insure right file, use before every write and read. O(1) runtime.
     * @param worldUID The UUID of the world the file belongs to.
     */
    public void safeLoadFile(UUID worldUID)
    {
        String newFileFolder = formatFileFolder(worldUID);
        if (!isFileLoaded() || !isFileFolderSame(newFileFolder))
            discLoadFile(newFileFolder);
    }

    /** @Return If the file is not null. */
    private boolean isFileLoaded()
    { return this.file != null; }

    /** @Return If the file folder is same as one in field. */
    private boolean isFileFolderSame(String cFileFolder)
    { return this.fileFolder.equals(cFileFolder); }

    /** Used to update the referenced file from disc, will create if there is none. */
    private boolean discLoadFile(String cFileFolder)
    {
        this.fileFolder = cFileFolder;
        this.file = YamlConfiguration.loadConfiguration(
                new File(plugin.getDataFolder() + cFileFolder, fileName));
        return saveFiles();
    }

    /**
     * Saves file to disc.
     * @Return If the save was successful.
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

    /** @Return If the path going to not be null. */
    public boolean isSafePath(String path)
    { return file.get(path) != null; }

    /** @Return The correct format for the file folder, going by world. */
    private String formatFileFolder(World world)
    { return formatFileFolder(world.getUID()); }

    /** @Return The correct format for the file folder, going by world. */
    private String formatFileFolder(UUID worldUID)
    { return "/" + worldUID + "/"; }

    /** Way to make sure the file you want is the same as the one had. */
    public static String buildID(World world, String fileName)
    { return "/" + world.getUID() + "/" + fileName; }

}
