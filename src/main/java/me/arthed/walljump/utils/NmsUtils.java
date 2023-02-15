package me.arthed.walljump.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import me.arthed.walljump.utils.BukkitUtils.Version;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public class NmsUtils {

    public static Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
    }

    public static Class<?> getNmsClass(String pre1_17ClassName, String _1_17Path) throws ClassNotFoundException {
        if(BukkitUtils.isVersionBefore(Version.V1_16))
            return getNmsClass(pre1_17ClassName);
        return Class.forName(_1_17Path);
    }

    public static Sound getBreakSoundForBlock(Block block) {
        try {
            if (BukkitUtils.isVersionBefore(Version.V1_8))
                return getSoundForBlock(block, "getBreakSound");
            return getSoundForBlock(block, getBreakSoundFieldName());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Sound.BLOCK_STONE_BREAK;
    }

    public static Sound getStepSoundForBlock(Block block) {
        try {
            if(BukkitUtils.isVersionBefore(Version.V1_8))
                return getSoundForBlock(block, "getStepSound");
            return getSoundForBlock(block, getStepSoundFieldName());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Sound.BLOCK_STONE_STEP;
    }

    public static Sound getPlaceSoundForBlock(Block block) {
        try {
            if (BukkitUtils.isVersionBefore(Version.V1_8))
                return getSoundForBlock(block, "getBreakSound");
            return getSoundForBlock(block, getPlaceSoundFieldName());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Sound.BLOCK_STONE_PLACE;
    }

    public static Sound getHitSoundForBlock(Block block) {
        try {
            if (BukkitUtils.isVersionBefore(Version.V1_8))
                return getSoundForBlock(block, "getStepSound");
            return getSoundForBlock(block, getHitSoundFieldName());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Sound.BLOCK_STONE_HIT;

    }

    public static Sound getFallSoundForBlock(Block block) {
        try {
            if (BukkitUtils.isVersionBefore(Version.V1_8))
                return getSoundForBlock(block, "getStepSound");
            return getSoundForBlock(block, getFallSoundFieldName());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Sound.BLOCK_STONE_FALL;
    }

    private static Sound getSoundForBlock(Block block, String fieldName) {
        try {
            Object nmsWorld = block.getWorld().getClass().getMethod("getHandle").invoke(block.getWorld());


            Object blockPosition = getNmsClass("BlockPosition", "net.minecraft.core.BlockPosition")
                    .getConstructor(double.class, double.class, double.class)
                    .newInstance(block.getX(), block.getY(), block.getZ());
            Object nmsType = nmsWorld.getClass()
                    .getMethod("getType", blockPosition.getClass())
                    .invoke(nmsWorld, blockPosition);

            Method getBlockMethod = nmsType.getClass().getMethod("getBlock");
            getBlockMethod.setAccessible(true);
            Object nmsBlock = getBlockMethod.invoke(nmsType);

            if(BukkitUtils.isVersionBefore(Version.V1_8)) {
                Object stepSound = nmsBlock.getClass().getField("stepSound").get(nmsBlock);
                String soundString = (String) stepSound.getClass().getMethod(fieldName).invoke(stepSound);
                return Sound.valueOf(soundString.toUpperCase().replace(".", "_"));
            }
            Field stepSoundField = null;
            String stepSoundFieldName = "stepSound";
            if(BukkitUtils.isVersionAfter(Version.V1_17))
                stepSoundFieldName = "aK";
            Class<?> blockSuperClass = nmsBlock.getClass();
            for(int i = 0; i < 5; i ++) {
                try {
                    stepSoundField = blockSuperClass.getDeclaredField(stepSoundFieldName);
                    break;
                } catch(NoSuchFieldException noField) {
                    blockSuperClass = blockSuperClass.getSuperclass();
                }
            }
            Objects.requireNonNull(stepSoundField).setAccessible(true);
            Object soundEffectType = stepSoundField.get(nmsBlock);

            Field sound = soundEffectType.getClass().getDeclaredField(fieldName);
            sound.setAccessible(true);
            Object nmsSound = sound.get(soundEffectType);

            String keyFieldName = "b";

            if(BukkitUtils.isVersionBefore(Version.V1_12))
                keyFieldName = "b";
            else if(BukkitUtils.isVersionBefore(Version.V1_15))
                keyFieldName = "a";

            Field keyField = nmsSound.getClass().getDeclaredField(keyFieldName);
            keyField.setAccessible(true);
            Object nmsKey = keyField.get(nmsSound);

            String getKeyMethodName = "getKey";
            if(BukkitUtils.isVersionBefore(Version.V1_11))
                getKeyMethodName = "a";

            String key = (String) nmsKey.getClass().getMethod(getKeyMethodName).invoke(nmsKey);

            return Sound.valueOf(key.replace(".", "_").toUpperCase());
        } catch (Exception Error) {
            Error.printStackTrace();
        }
        return Sound.BLOCK_STONE_PLACE;
    }

    private static String getBreakSoundFieldName() {
        String soundFieldName = "aA"; //1.17
        if(BukkitUtils.isVersionBefore(Version.V1_12))
            soundFieldName = "o";
        else if(BukkitUtils.isVersionBefore(Version.V1_13))
            soundFieldName = "q";
        else if(BukkitUtils.isVersionBefore(Version.V1_14))
            soundFieldName = "y";
        else if(BukkitUtils.isVersionBefore(Version.V1_15))
            soundFieldName = "z";
        else if(BukkitUtils.isVersionBefore(Version.V1_16)) {
            if(BukkitUtils.isPaper())
                soundFieldName = "breakSound";
            else
                soundFieldName = "X";
        }
        return soundFieldName;
    }

    private static String getStepSoundFieldName() {
        String soundFieldName = "aB"; //1.17
        if(BukkitUtils.isVersionBefore(Version.V1_12))
            soundFieldName = "p";
        else if(BukkitUtils.isVersionBefore(Version.V1_13))
            soundFieldName = "r";
        else if(BukkitUtils.isVersionBefore(Version.V1_14))
            soundFieldName = "z";
        else if(BukkitUtils.isVersionBefore(Version.V1_15))
            soundFieldName = "A";
        else if(BukkitUtils.isVersionBefore(Version.V1_16)) {
            if(BukkitUtils.isPaper())
                soundFieldName = "stepSound";
            else
                soundFieldName = "Y";
        }
        return soundFieldName;
    }

    private static String getPlaceSoundFieldName() {
        String soundFieldName = "aC"; //1.17
        if(BukkitUtils.isVersionBefore(Version.V1_12))
            soundFieldName = "q";
        else if(BukkitUtils.isVersionBefore(Version.V1_13))
            soundFieldName = "s";
        else if(BukkitUtils.isVersionBefore(Version.V1_14))
            soundFieldName = "A";
        else if(BukkitUtils.isVersionBefore(Version.V1_15))
            soundFieldName = "B";
        else if(BukkitUtils.isVersionBefore(Version.V1_16)) {
            if(BukkitUtils.isPaper())
                soundFieldName = "placeSound";
            else
                soundFieldName = "Z";
        }
        return soundFieldName;
    }

    private static String getHitSoundFieldName() {
        String soundFieldName = "aD"; //1.17
        if(BukkitUtils.isVersionBefore(Version.V1_12))
            soundFieldName = "r";
        else if(BukkitUtils.isVersionBefore(Version.V1_13))
            soundFieldName = "t";
        else if(BukkitUtils.isVersionBefore(Version.V1_14))
            soundFieldName = "B";
        else if(BukkitUtils.isVersionBefore(Version.V1_15))
            soundFieldName = "C";
        else if(BukkitUtils.isVersionBefore(Version.V1_16)) {
            if(BukkitUtils.isPaper())
                soundFieldName = "hitSound";
            else
                soundFieldName = "aa";
        }
        return soundFieldName;
    }

    private static String getFallSoundFieldName() {
        String soundFieldName = "aE"; //1.17
        if(BukkitUtils.isVersionBefore(Version.V1_12))
            soundFieldName = "s";
        else if(BukkitUtils.isVersionBefore(Version.V1_13))
            soundFieldName = "u";
        else if(BukkitUtils.isVersionBefore(Version.V1_14))
            soundFieldName = "C";
        else if(BukkitUtils.isVersionBefore(Version.V1_15))
            soundFieldName = "D";
        else if(BukkitUtils.isVersionBefore(Version.V1_16)) {
            if(BukkitUtils.isPaper())
                soundFieldName = "fallSound";
            else
                soundFieldName = "ab";
        }
        return soundFieldName;
    }


}
