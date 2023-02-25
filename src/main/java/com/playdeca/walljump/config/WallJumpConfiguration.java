package com.playdeca.walljump.config;

import com.playdeca.walljump.WallJump;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

// This class is used to load the config
public class WallJumpConfiguration extends YamlConfiguration {

    // The config file and the data
    private final File configFile;
    // The data is used to cache the values
    private Map<String, Object> data;

    public WallJumpConfiguration(String fileName) {
            // Call the super constructor
            super();

            // Get the plugin and the config file
            WallJump plugin = WallJump.getInstance();
            configFile = new File(plugin.getDataFolder(), fileName);
            // Create the config file if it doesn't exist
            if (!configFile.exists()) {
                boolean status = configFile.getParentFile().mkdirs();
                if(!status)
                    Bukkit.getLogger().warning("Failed to create the config file!");
                plugin.saveResource(fileName, false);
            }
            // Load the config
            reload();

            // Load the default config
            InputStream defaultConfigInputStream = WallJump.class.getResourceAsStream("/" + fileName);
            assert defaultConfigInputStream != null;
            InputStreamReader defaultConfigReader = new InputStreamReader(defaultConfigInputStream);
            setDefaults(YamlConfiguration.loadConfiguration(defaultConfigReader));
    }

    // This method is used to reload the config
    public void reload() {
        try {
            // Load the config
            load(configFile);
            // Clear the data
            data = new HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This method is used to save the config
    public void save() {
        try {
            save(configFile);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // This method is used to get a string from the config
    public Material getMaterial(String path) {
        try {
            if(data.containsKey(path))
                return (Material)data.get(path);

            Material result = Material.valueOf(getString(path));
            data.put(path, result);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // This method is used to get a string list from the config
    public List<Material> getMaterialList(String path) {
        try {
            if(data.containsKey(path)) {
                return (List<Material>)data.get(path);
            }

            List<Material> result = new ArrayList<>();
            for(String materialName : getStringList(path)) {
                try {
                    result.add(Material.valueOf(materialName));
                } catch(IllegalArgumentException ignore) {}
            }
            data.put(path, result);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // This method is used to get a world from the config
    public World getWorld(String path) {
        try {
            if(data.containsKey(path))
                return (World)data.get(path);

            World result = Bukkit.getWorld(Objects.requireNonNull(getString(path)));
            data.put(path, result);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // This method is used to get a world list from the config
    public List<World> getWorldList(String path) {
        try {
            if(data.containsKey(path)) {
                return (List<World>)data.get(path);
            }

            List<World> result = new ArrayList<>();
            for(String worldName : getStringList(path)) {
                World world = Bukkit.getWorld(worldName);
                if(world != null)
                    result.add(world);
            }
            data.put(path, result);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}