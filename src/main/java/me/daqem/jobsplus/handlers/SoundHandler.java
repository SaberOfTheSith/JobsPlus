package me.daqem.jobsplus.handlers;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class SoundHandler {

    public static void playLevelUpSound(Player player, float volume, float pitch) {
        player.level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.AMBIENT, volume, pitch);
    }

    public static void playEXPOrbPickupSound(Player player, float volume, float pitch) {
        player.level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.AMBIENT, volume, pitch);
    }
}
