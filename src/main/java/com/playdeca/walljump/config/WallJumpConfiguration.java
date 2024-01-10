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

    private final File configFile;
    // The data is used to cache the values
    private Map<String, Object> data;

    public WallJumpConfiguration(String fileName) {
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

    public void reload() {
        try {
            // Load the config
            load(configFile);
            // Clear the data
            data = new HashMap<>();
        } catch (Exception e) {
            Bukkit.getLogger().warning("An error occurred while reloading the config.");
        }
    }

    public void save() {
        try {
            save(configFile);
            Bukkit.getLogger().info("Config saved!");
        } catch(Exception e) {
            Bukkit.getLogger().warning("An error occurred while saving the config.");
        }
    }

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
            Bukkit.getLogger().warning("An error occurred while getting a material list from the config.");
            return null;
        }
    }

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
            Bukkit.getLogger().warning("An error occurred while getting a world list from the config.");
            return null;
        }
    }
}
