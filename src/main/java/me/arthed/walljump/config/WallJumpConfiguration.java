package me.arthed.walljump.config;

import me.arthed.walljump.WallJump;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WallJumpConfiguration extends YamlConfiguration {

    private final File configFile;
    private Map<String, Object> data;

    public WallJumpConfiguration(String fileName) {
        super();

        WallJump plugin = WallJump.getInstance();
        configFile = new File(plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);
        }
        reload();

        InputStream defaultConfigInputStream = WallJump.class.getResourceAsStream("/" + fileName);
        InputStreamReader defaultConfigReader = new InputStreamReader(defaultConfigInputStream);
        setDefaults(YamlConfiguration.loadConfiguration(defaultConfigReader));
    }

    public void reload() {
        try {
            load(configFile);
            data = new HashMap<>();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            save(configFile);
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public Material getMaterial(String path) {
        if(data.containsKey(path))
            return (Material)data.get(path);

        Material result = Material.valueOf(getString(path));
        data.put(path, result);
        return result;
    }

    public List<Material> getMaterialList(String path) {
        if(data.containsKey(path))
            return (List<Material>)data.get(path);

        List<Material> result = new ArrayList<>();
        for(String materialName : getStringList(path)) {
            try {
                result.add(Material.valueOf(materialName));
            } catch(IllegalArgumentException ignore) {}
        }
        data.put(path, result);
        return result;
    }

    public World getWorld(String path) {
        if(data.containsKey(path))
            return (World)data.get(path);

        World result = Bukkit.getWorld(getString(path));
        data.put(path, result);
        return result;
    }

    public List<World> getWorldList(String path) {
        if(data.containsKey(path))
            return (List<World>)data.get(path);

        List<World> result = new ArrayList<>();
        for(String worldName : getStringList(path)) {
            World world = Bukkit.getWorld(worldName);
            if(world != null)
                result.add(world);
        }
        data.put(path, result);
        return result;
    }

}
