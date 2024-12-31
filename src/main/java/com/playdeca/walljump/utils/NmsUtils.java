package com.playdeca.walljump.utils;

import com.playdeca.walljump.utils.BukkitUtils.Version;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class NmsUtils {

    // This method returns the NMS class for the given class name, depending on the server version.
    // If the server version is 1.16 or earlier, it uses the pre-1.17 class name to get the NMS class.
    // If the server version is 1.17 or later, it uses the new class path to get the NMS class.
    public static Class<?> getNmsClass(String _1_17Path) throws ClassNotFoundException {
            // Use the new class path to get the NMS class.
            return Class.forName(_1_17Path);
    }

    public static Sound getStepSoundForBlock(Block block) {
        try {
                // Get the step sound for the block
                return getSoundForBlock(block, getStepSoundFieldName());

        } catch (Exception e) {
            Bukkit.getLogger().warning("An error occurred while getting the step sound for a block.");
            // If no sound can be determined, return the default block step sound.
            return Sound.BLOCK_STONE_STEP;
        }
    }

    private static Sound getSoundForBlock(Block block, String fieldName) {
        try {
            // Get the NMS World object from the Bukkit world
            Object nmsWorld = block.getWorld().getClass().getMethod("getHandle").invoke(block.getWorld());
            // Create a new BlockPosition object with the block's coordinates
            Object blockPosition = getNmsClass("net.minecraft.core.BlockPosition")
                    .getConstructor(double.class, double.class, double.class)
                    .newInstance(block.getX(), block.getY(), block.getZ());
            // Get the NMS BlockType object for the block
            Object nmsType = nmsWorld.getClass()
                    .getMethod("getType", blockPosition.getClass())
                    .invoke(nmsWorld, blockPosition);
            // Get the NMS Block object for the block
            Method getBlockMethod = nmsType.getClass().getMethod("getBlock");
            getBlockMethod.setAccessible(true);
            Object nmsBlock = getBlockMethod.invoke(nmsType);

            // Get the StepSound object for the block's material
            Field stepSoundField = null;
            String stepSoundFieldName = "stepSound";
            if(BukkitUtils.isVersionAfter(Version.V1_17))
                stepSoundFieldName = "aK";

            // The stepSound field may be located in a superclass of the NMS BlockType object, so we need to loop through the superclasses to find it
            Class<?> blockSuperClass = nmsBlock.getClass();
            for(int i = 0; i < 5; i ++) {
                try {
                    stepSoundField = blockSuperClass.getDeclaredField(stepSoundFieldName);
                    break;
                } catch(NoSuchFieldException noField) {
                    blockSuperClass = blockSuperClass.getSuperclass();
                }
            }

            // Make the stepSound field accessible and get its value (an instance of SoundEffectType)
            Objects.requireNonNull(stepSoundField).setAccessible(true);
            Object nmsKey = getNmsKey(fieldName, stepSoundField, nmsBlock);

            // Get the key (namespace:name) of the sound
            String getKeyMethodName = "getKey";
            if(BukkitUtils.isVersionBefore(Version.V1_11))
                getKeyMethodName = "a";

            String key = (String) nmsKey.getClass().getMethod(getKeyMethodName).invoke(nmsKey);

            // Convert the key to a Bukkit Sound object and return it
            return Sound.valueOf(key.replace(".", "_").toUpperCase());
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchFieldException | NoSuchMethodException | SecurityException | InvocationTargetException Error) {
            Bukkit.getLogger().warning("An error occurred while getting the sound for a block.");
            // If an error occurs, return the default sound for a stone block
            return Sound.BLOCK_STONE_PLACE;
        }
    }

    private static Object getNmsKey(String fieldName, Field stepSoundField, Object nmsBlock) throws IllegalAccessException, NoSuchFieldException {
        Object soundEffectType = stepSoundField.get(nmsBlock);

        // Get the SoundEffect object for the sound we want to play (e.g. "break_sound" or "step_sound")
        Field sound = soundEffectType.getClass().getDeclaredField(fieldName);
        sound.setAccessible(true);
        Object nmsSound = sound.get(soundEffectType);

        // Get the MinecraftKey object for the sound (which contains the sound's namespace and name)
        String keyFieldName = "b";

        if(BukkitUtils.isVersionBefore(Version.V1_12))
            keyFieldName = "b";
        else if(BukkitUtils.isVersionBefore(Version.V1_15))
            keyFieldName = "a";

        Field keyField = nmsSound.getClass().getDeclaredField(keyFieldName);
        keyField.setAccessible(true);
        return keyField.get(nmsSound);
    }

    /**
     * Returns the name of the field containing the step sound for a block in the current version of Minecraft.
     * The field name is different for each version, so this method checks the current version of Minecraft and returns
     * the appropriate field name.
     * @return The name of the step sound field for the current version of Minecraft.
     */
    private static String getStepSoundFieldName() {
        try{
            // Default to the 1.17 sound field name
            String soundFieldName = "aB";
            if(BukkitUtils.isVersionBefore(Version.V1_16)) {
                // If using Paper, use the stepSound field name; otherwise, use Y
                if(BukkitUtils.isPaper())
                soundFieldName = "stepSound";
                else{
                    soundFieldName = "Y";
                    }
                }
            return soundFieldName;
        }catch(Exception e){
            Bukkit.getLogger().warning("An error occurred while getting the step sound field name.");
            return "Y";
        }
    }
}
