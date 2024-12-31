package com.playdeca.walljump.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.Associables;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.playdeca.walljump.WallJump;
import com.sk89q.worldguard.domains.Association;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;

import java.lang.reflect.*;
import java.util.logging.Level;
import org.bukkit.World;

public class WorldGuardHandler {

    private static final String WALL_JUMP_FLAG = "wall-jump";
    private final Plugin owningPlugin = WallJump.getPlugin(WallJump.class);
    private WorldGuardPlugin worldGuardPlugin;
    private Object worldGuard;
    private Object regionContainer;
    private Method regionContainerGetMethod;
    private Method worldAdaptMethod;
    private Method regionManagerGetMethod;
    private Constructor<?> vectorConstructor;
    private boolean initialized = false;
    public static StateFlag ALLOW_WALL_JUMP;

    public WorldGuardHandler(Plugin plugin) {
        if (plugin instanceof WorldGuardPlugin worldGuardPlugin1) {
            this.worldGuardPlugin = worldGuardPlugin1;
            initializeWorldGuard();
            try {
                registerFlag();
            } catch (Throwable ex) {
                owningPlugin.getLogger().log(Level.WARNING, "Failed to register WorldGuard custom flags", ex);
            }
        }
    }

    private void initializeWorldGuard() {
        try {
            Class<?> worldGuardClass = Class.forName("com.sk89q.worldguard.WorldGuard");
            Method getInstanceMethod = worldGuardClass.getMethod("getInstance");
            worldGuard = getInstanceMethod.invoke(null);
            owningPlugin.getLogger().info("Found WorldGuard 7+");
        } catch (Exception ex) {
            owningPlugin.getLogger().info("Found WorldGuard <7");
        }
    }

    private void registerFlag() {
        if (worldGuard == null) return;
        try {
            FlagRegistry registry = (FlagRegistry) invokeMethod(worldGuard, "getFlagRegistry");
            StateFlag flag = new StateFlag(WALL_JUMP_FLAG, WallJump.getInstance().getConfig().getBoolean("worldGuardFlagDefault"));
            registry.register(flag);
            ALLOW_WALL_JUMP = flag;
        } catch (FlagConflictException e) {
            ALLOW_WALL_JUMP = (StateFlag) getExistingFlag("wall-jump");
        } catch (Exception ex) {
            owningPlugin.getLogger().log(Level.WARNING, "Failed to register custom flag", ex);
        }
    }

    private Flag<?> getExistingFlag(String flagName) {
        try {
            FlagRegistry registry = (FlagRegistry) invokeMethod(worldGuard, "getFlagRegistry");
            return registry.get(flagName);
        } catch (Exception e) {
            owningPlugin.getLogger().log(Level.WARNING, "Error fetching existing flag", e);
            return null;
        }
    }

    private Object invokeMethod(Object target, String methodName, Object... args) {
        try {
            Method method = target.getClass().getMethod(methodName, getParameterTypes(args));
            return method.invoke(target, args);
        } catch (IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
            owningPlugin.getLogger().log(Level.WARNING, "Error invoking method " + methodName, ex);
            return null;
        }
    }

    private Class<?>[] getParameterTypes(Object[] args) {
        if (args == null) return new Class<?>[0];
        Class<?>[] paramTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            paramTypes[i] = args[i].getClass();
        }
        return paramTypes;
    }

    public boolean canWallJump(Player player) {
        try {
            Location location = player.getLocation();
            if (worldGuardPlugin == null) return true;
            ApplicableRegionSet regionSet = getRegionSet(location);
            return regionSet == null || regionSet.queryState(getAssociable(player), ALLOW_WALL_JUMP) != StateFlag.State.DENY;
        } catch (Exception e) {
            Bukkit.getLogger().warning("Error checking if player can wall jump");
            return true;
        }
    }

    private RegionAssociable getAssociable(Player player) {
        try {
            if (player == null) return Associables.constant(Association.NON_MEMBER);
            return worldGuardPlugin.wrapPlayer(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("Error getting Region associable");
            return null;
        }
    }

    private void initialize() {
        if (initialized) return;
        initialized = true;
        try {
            setupReflectionMethods();
        } catch (Exception ex) {
            owningPlugin.getLogger().log(Level.WARNING, "Failed to initialize WorldGuard integration", ex);
        }
    }

    private void setupReflectionMethods() throws Exception {
        Class<?> worldEditWorldClass = Class.forName("com.sk89q.worldedit.world.World");
        Class<?> worldEditAdapterClass = Class.forName("com.sk89q.worldedit.bukkit.BukkitAdapter");
        worldAdaptMethod = worldEditAdapterClass.getMethod("adapt", World.class);

        regionContainerGetMethod = regionContainer.getClass().getMethod("get", worldEditWorldClass);

        Class<?> vectorClass = Class.forName("com.sk89q.worldedit.Vector");
        vectorConstructor = vectorClass.getConstructor(Double.TYPE, Double.TYPE, Double.TYPE);
        regionManagerGetMethod = RegionManager.class.getMethod("getApplicableRegions", vectorClass);
    }

    private RegionManager getRegionManager(World world) {
        initialize();
        if (regionContainer == null || regionContainerGetMethod == null) return null;
        try {
            Object worldEditWorld = worldAdaptMethod != null ? worldAdaptMethod.invoke(null, world) : world;
            return (RegionManager) regionContainerGetMethod.invoke(regionContainer, worldEditWorld);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            owningPlugin.getLogger().log(Level.WARNING, "An error occurred looking up a WorldGuard RegionManager", ex);
            return null;
        }
    }

    private ApplicableRegionSet getRegionSet(Location location) {
        RegionManager regionManager = getRegionManager(location.getWorld());
        if (regionManager == null) return null;
        try {
            Object vector = vectorConstructor.newInstance(location.getX(), location.getY(), location.getZ());
            return (ApplicableRegionSet) regionManagerGetMethod.invoke(regionManager, vector);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException ex) {
            owningPlugin.getLogger().log(Level.WARNING, "An error occurred looking up a WorldGuard ApplicableRegionSet", ex);
            return null;
        }
    }
}
