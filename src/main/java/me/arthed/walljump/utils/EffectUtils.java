package me.arthed.walljump.utils;

import me.arthed.walljump.enums.WallFace;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.arthed.walljump.utils.BukkitUtils.Version;

public class EffectUtils {

    public static void spawnSlidingParticles(Player player, int count, WallFace facing) {
        if(BukkitUtils.isVersionBefore(Version.V1_8))
            return;
        Object data;
        Location location = player.getLocation();
        Block block = location.clone().add(facing.xOffset, facing.yOffset, facing.zOffset).getBlock();
        if(BukkitUtils.isVersionBefore(Version.V1_12))
            data = block.getType().getNewData(block.getData());
        else //1.13+
            data = block.getBlockData();
        player.getWorld().spawnParticle(
                Particle.BLOCK_DUST,
                location.clone().add(facing.xOffset*0.3, facing.yOffset*0.3-0.3, facing.zOffset*0.3),
                count,
                0.2f,
                0.2f,
                0.2f,
                data);
    }

//    public static void playWallJumpSound(Player player, WallFace facing, float volume, float pitch) {
//        player.getWorld().playSound(
//                player.getLocation(),
//                NmsUtils.getStepSoundForBlock(
//                        LocationUtils.getBlockPlayerIsStuckOn(player, facing)),
//                volume,
//                pitch
//        );
//    }

}
