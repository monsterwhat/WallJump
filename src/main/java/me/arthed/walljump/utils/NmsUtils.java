package me.arthed.walljump.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import me.arthed.walljump.utils.BukkitUtils.Version;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
// This class is used to get the sound of a block
// It is used to get the sound of a block when it is broken, placed, stepped on, hit or fell on
public class NmsUtils {

    // This method returns the NMS class for the given class name, using the current server version.
    public static Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException {
        // Get the server package name and split it into parts using the "." delimiter.
        String[] packageParts = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
        // Get the third part of the package name, which represents the server version (e.g. "v1_17_R1").
        String version = packageParts[packageParts.length - 1];
        // Construct the full class name by concatenating the NMS package name, the server version, and the class name.
        String className = "net.minecraft.server." + version + "." + nmsClassName;
        // Load and return the NMS class using the fully qualified class name.
        return Class.forName(className);
    }

    // This method returns the NMS class for the given class name, depending on the server version.
    // If the server version is 1.16 or earlier, it uses the pre-1.17 class name to get the NMS class.
    // If the server version is 1.17 or later, it uses the new class path to get the NMS class.
    public static Class<?> getNmsClass(String pre1_17ClassName, String _1_17Path) throws ClassNotFoundException {
        if(BukkitUtils.isVersionBefore(Version.V1_16)) {
            // If the server version is 1.16 or earlier, use the pre-1.17 class name to get the NMS class.
            return getNmsClass(pre1_17ClassName);
        } else {
            // If the server version is 1.17 or later, use the new class path to get the NMS class.
            return Class.forName(_1_17Path);
        }
    }

    // This method returns a sound to play when a block is broken.
    // It takes a Block object as input and returns a Sound object.
    public static Sound getBreakSoundForBlock(Block block) {
        try {
            // Check if the server version is before 1.8
            if (BukkitUtils.isVersionBefore(Version.V1_8)) {
                // If the server version is before 1.8, get the break sound for the block
                // using the method name "getBreakSound"
                return getSoundForBlock(block, "getBreakSound");
            } else {
                // If the server version is 1.8 or later, get the break sound for the block
                // using the name returned by the getBreakSoundFieldName() method.
                return getSoundForBlock(block, getBreakSoundFieldName());
            }
        } catch (Exception e) {
            // If an exception is thrown while getting the sound, print the stack trace to the console.
            e.printStackTrace();
        }
        // If no sound can be determined, return the default block break sound.
        return Sound.BLOCK_STONE_BREAK;
    }

    // This method returns the sound to play when a player walks on a block.
    // It takes a Block object as input and returns a Sound object.
    public static Sound getStepSoundForBlock(Block block) {
        try {
            // Check if the server version is before 1.8
            if (BukkitUtils.isVersionBefore(Version.V1_8)) {
                // If the server version is before 1.8, get the step sound for the block
                // using the method name "getStepSound"
                return getSoundForBlock(block, "getStepSound");
            } else {
                // If the server version is 1.8 or later, get the step sound for the block
                // using the name returned by the getStepSoundFieldName() method.
                return getSoundForBlock(block, getStepSoundFieldName());
            }
        } catch (Exception e) {
            // If an exception is thrown while getting the sound, print the stack trace to the console.
            e.printStackTrace();
        }
        // If no sound can be determined, return the default block step sound.
        return Sound.BLOCK_STONE_STEP;
    }

    // This method returns the sound to play when a player places a block.
    // It takes a Block object as input and returns a Sound object.
    public static Sound getPlaceSoundForBlock(Block block) {
        try {
            // Check if the server version is before 1.8
            if (BukkitUtils.isVersionBefore(Version.V1_8)) {
                // If the server version is before 1.8, get the break sound for the block
                // to use as the place sound.
                return getSoundForBlock(block, "getBreakSound");
            } else {
                // If the server version is 1.8 or later, get the place sound for the block
                // using the name returned by the getPlaceSoundFieldName() method.
                return getSoundForBlock(block, getPlaceSoundFieldName());
            }
        } catch (Exception e) {
            // If an exception is thrown while getting the sound, print the stack trace to the console.
            e.printStackTrace();
        }
        // If no sound can be determined, return the default place sound for a stone block.
        return Sound.BLOCK_STONE_PLACE;
    }

    // This method returns the sound to play when a player hits a block.
    // It takes a Block object as input and returns a Sound object.
    public static Sound getHitSoundForBlock(Block block) {
        try {
            // Check if the server version is before 1.8
            if (BukkitUtils.isVersionBefore(Version.V1_8)) {
                // If the server version is before 1.8, get the step sound for the block
                // to use as the hit sound.
                return getSoundForBlock(block, "getStepSound");
            } else {
                // If the server version is 1.8 or later, get the hit sound for the block
                // using the name returned by the getHitSoundFieldName() method.
                return getSoundForBlock(block, getHitSoundFieldName());
            }
        } catch (Exception e) {
            // If an exception is thrown while getting the sound, print the stack trace to the console.
            e.printStackTrace();
        }
        // If no sound can be determined, return the default hit sound for a stone block.
        return Sound.BLOCK_STONE_HIT;
    }

    // This method returns the sound to play when a player falls on a block.
    // It takes a Block object as input and returns a Sound object.
    public static Sound getFallSoundForBlock(Block block) {
        try {
            // Check if the server version is before 1.8
            if (BukkitUtils.isVersionBefore(Version.V1_8)) {
                // If the server version is before 1.8, get the step sound for the block
                // to use as the fall sound.
                return getSoundForBlock(block, "getStepSound");
            } else {
                // If the server version is 1.8 or later, get the fall sound for the block
                // using the name returned by the getFallSoundFieldName() method.
                return getSoundForBlock(block, getFallSoundFieldName());
            }
        } catch (Exception e) {
            // If an exception is thrown while getting the sound, print the stack trace to the console.
            e.printStackTrace();
        }
        // If no sound can be determined, return the default fall sound for a stone block.
        return Sound.BLOCK_STONE_FALL;
    }

    private static Sound getSoundForBlock(Block block, String fieldName) {
        try {
            // Get the NMS World object from the Bukkit world
            Object nmsWorld = block.getWorld().getClass().getMethod("getHandle").invoke(block.getWorld());
            // Create a new BlockPosition object with the block's coordinates
            Object blockPosition = getNmsClass("BlockPosition", "net.minecraft.core.BlockPosition")
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
            if(BukkitUtils.isVersionBefore(Version.V1_8)) {
                // For versions before 1.8, use the stepSound field on the NMS Block object
                Object stepSound = nmsBlock.getClass().getField("stepSound").get(nmsBlock);
                String soundString = (String) stepSound.getClass().getMethod(fieldName).invoke(stepSound);
                return Sound.valueOf(soundString.toUpperCase().replace(".", "_"));
            }
            // For versions after 1.8, use the stepSound field on the NMS BlockType object
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
            Object nmsKey = keyField.get(nmsSound);

            // Get the key (namespace:name) of the sound
            String getKeyMethodName = "getKey";
            if(BukkitUtils.isVersionBefore(Version.V1_11))
                getKeyMethodName = "a";

            String key = (String) nmsKey.getClass().getMethod(getKeyMethodName).invoke(nmsKey);

            // Convert the key to a Bukkit Sound object and return it
            return Sound.valueOf(key.replace(".", "_").toUpperCase());
        } catch (Exception Error) {
            Error.printStackTrace();
        }
        // If an error occurs, return the default sound for a stone block
        return Sound.BLOCK_STONE_PLACE;
    }

    private static String getBreakSoundFieldName() {
        String soundFieldName = "aA"; // Default value for Minecraft 1.17
        // Check the Minecraft version and set the appropriate field name
        if (BukkitUtils.isVersionBefore(Version.V1_12))
            soundFieldName = "o";
        else if (BukkitUtils.isVersionBefore(Version.V1_13))
            soundFieldName = "q";
        else if (BukkitUtils.isVersionBefore(Version.V1_14))
            soundFieldName = "y";
        else if (BukkitUtils.isVersionBefore(Version.V1_15))
            soundFieldName = "z";
        else if (BukkitUtils.isVersionBefore(Version.V1_16)) {
            // For Minecraft versions before 1.16, check if the server is running Paper and set the field name accordingly
            if (BukkitUtils.isPaper())
                soundFieldName = "breakSound";
            else
                soundFieldName = "X";
        }
        return soundFieldName;
    }

    /**
     * Returns the name of the field containing the step sound for a block in the current version of Minecraft.
     * The field name is different for each version, so this method checks the current version of Minecraft and returns
     * the appropriate field name.
     * @return The name of the step sound field for the current version of Minecraft.
     */
    private static String getStepSoundFieldName() {
        // Default to the 1.17 sound field name
        String soundFieldName = "aB";
        // Check the version of Minecraft and set the sound field name accordingly
        if(BukkitUtils.isVersionBefore(Version.V1_12))
            soundFieldName = "p";
        else if(BukkitUtils.isVersionBefore(Version.V1_13))
            soundFieldName = "r";
        else if(BukkitUtils.isVersionBefore(Version.V1_14))
            soundFieldName = "z";
        else if(BukkitUtils.isVersionBefore(Version.V1_15))
            soundFieldName = "A";
        else if(BukkitUtils.isVersionBefore(Version.V1_16)) {
            // If using Paper, use the stepSound field name; otherwise, use Y
            if(BukkitUtils.isPaper())
                soundFieldName = "stepSound";
            else
                soundFieldName = "Y";
        }
        return soundFieldName;
    }

    private static String getPlaceSoundFieldName() {
        String soundFieldName = "aC"; // Default sound field name for version 1.17
        // Check the current version and set the appropriate sound field name
        if(BukkitUtils.isVersionBefore(Version.V1_12))
            soundFieldName = "q";
        else if(BukkitUtils.isVersionBefore(Version.V1_13))
            soundFieldName = "s";
        else if(BukkitUtils.isVersionBefore(Version.V1_14))
            soundFieldName = "A";
        else if(BukkitUtils.isVersionBefore(Version.V1_15))
            soundFieldName = "B";
        else if(BukkitUtils.isVersionBefore(Version.V1_16)) {
            // For versions earlier than 1.16, check if Paper server is being used
            if(BukkitUtils.isPaper())
                soundFieldName = "placeSound";
            else
                soundFieldName = "Z";
        }
        // Return the sound field name
        return soundFieldName;
    }

    /**
     * Returns the name of the field containing the sound effect name for block hits.
     * The value returned depends on the current version of Bukkit/Paper.
     * @return The name of the sound field.
     */
    private static String getHitSoundFieldName() {
        String soundFieldName = "aD"; // sound field name for Minecraft 1.17 and later
        if(BukkitUtils.isVersionBefore(Version.V1_12))
            soundFieldName = "r"; // sound field name for Minecraft 1.11 and earlier
        else if(BukkitUtils.isVersionBefore(Version.V1_13))
            soundFieldName = "t"; // sound field name for Minecraft 1.12
        else if(BukkitUtils.isVersionBefore(Version.V1_14))
            soundFieldName = "B"; // sound field name for Minecraft 1.13
        else if(BukkitUtils.isVersionBefore(Version.V1_15))
            soundFieldName = "C"; // sound field name for Minecraft 1.14
        else if(BukkitUtils.isVersionBefore(Version.V1_16)) {
            if(BukkitUtils.isPaper())
                soundFieldName = "hitSound"; // sound field name for Paper 1.15 and earlier
            else
                soundFieldName = "aa"; // sound field name for Minecraft 1.15 and later
        }
        return soundFieldName;
    }

    private static String getFallSoundFieldName() {
        String soundFieldName = "aE"; // Set default field name for Minecraft 1.17
        // Check the Minecraft version and set the field name accordingly
        if(BukkitUtils.isVersionBefore(Version.V1_12))
            soundFieldName = "s";
        else if(BukkitUtils.isVersionBefore(Version.V1_13))
            soundFieldName = "u";
        else if(BukkitUtils.isVersionBefore(Version.V1_14))
            soundFieldName = "C";
        else if(BukkitUtils.isVersionBefore(Version.V1_15))
            soundFieldName = "D";
        else if(BukkitUtils.isVersionBefore(Version.V1_16)) {
            // Check if the server is running Paper and set the field name accordingly
            if(BukkitUtils.isPaper())
                soundFieldName = "fallSound";
            else
                soundFieldName = "ab";
        }
        return soundFieldName; // Return the field name
    }
}
