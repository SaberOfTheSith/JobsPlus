package me.daqem.jobsplus.utils;

import me.daqem.jobsplus.capability.ModCapabilityImpl;
import me.daqem.jobsplus.utils.enums.CapType;
import me.daqem.jobsplus.utils.enums.Jobs;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class JobGetters {
    
    public static boolean getJobIsEnabled(Player player, Jobs job) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> {
                    if(handler.getAlchemist()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case BUILDER -> {
                    if(handler.getBuilder()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case BUTCHER -> {
                    if(handler.getButcher()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case CRAFTSMAN -> {
                    if(handler.getCraftsman()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case DIGGER -> {
                    if(handler.getDigger()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case ENCHANTER -> {
                    if(handler.getEnchanter()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case FARMER -> {
                    if(handler.getFarmer()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case FISHERMAN -> {
                    if(handler.getFisherman()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case HUNTER -> {
                    if(handler.getHunter()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case LUMBERJACK -> {
                    if(handler.getLumberjack()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case MINER -> {
                    if(handler.getMiner()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
                case SMITH -> {
                    if(handler.getSmith()[CapType.LEVEL.get()] != 0) atomicBoolean.set(true);
                }
            }
        });
        return atomicBoolean.get();
    }

    public static int getJobLevel(Player player, Jobs job) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> atomicInteger.set(handler.getAlchemist()[CapType.LEVEL.get()]);
                case BUILDER -> atomicInteger.set(handler.getBuilder()[CapType.LEVEL.get()]);
                case BUTCHER -> atomicInteger.set(handler.getButcher()[CapType.LEVEL.get()]);
                case CRAFTSMAN -> atomicInteger.set(handler.getCraftsman()[CapType.LEVEL.get()]);
                case DIGGER -> atomicInteger.set(handler.getDigger()[CapType.LEVEL.get()]);
                case ENCHANTER -> atomicInteger.set(handler.getEnchanter()[CapType.LEVEL.get()]);
                case FARMER -> atomicInteger.set(handler.getFarmer()[CapType.LEVEL.get()]);
                case FISHERMAN -> atomicInteger.set(handler.getFisherman()[CapType.LEVEL.get()]);
                case HUNTER -> atomicInteger.set(handler.getHunter()[CapType.LEVEL.get()]);
                case LUMBERJACK -> atomicInteger.set(handler.getLumberjack()[CapType.LEVEL.get()]);
                case MINER -> atomicInteger.set(handler.getMiner()[CapType.LEVEL.get()]);
                case SMITH -> atomicInteger.set(handler.getSmith()[CapType.LEVEL.get()]);
            }
        });
        return atomicInteger.get();
    }

    public static int getJobEXP(Player player, Jobs job) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        player.getCapability(ModCapabilityImpl.MOD_CAPABILITY).ifPresent(handler -> {
            switch (job) {
                case ALCHEMIST -> atomicInteger.set(handler.getAlchemist()[CapType.EXP.get()]);
                case BUILDER -> atomicInteger.set(handler.getBuilder()[CapType.EXP.get()]);
                case BUTCHER -> atomicInteger.set(handler.getButcher()[CapType.EXP.get()]);
                case CRAFTSMAN -> atomicInteger.set(handler.getCraftsman()[CapType.EXP.get()]);
                case DIGGER -> atomicInteger.set(handler.getDigger()[CapType.EXP.get()]);
                case ENCHANTER -> atomicInteger.set(handler.getEnchanter()[CapType.EXP.get()]);
                case FARMER -> atomicInteger.set(handler.getFarmer()[CapType.EXP.get()]);
                case FISHERMAN -> atomicInteger.set(handler.getFisherman()[CapType.EXP.get()]);
                case HUNTER -> atomicInteger.set(handler.getHunter()[CapType.EXP.get()]);
                case LUMBERJACK -> atomicInteger.set(handler.getLumberjack()[CapType.EXP.get()]);
                case MINER -> atomicInteger.set(handler.getMiner()[CapType.EXP.get()]);
                case SMITH -> atomicInteger.set(handler.getSmith()[CapType.EXP.get()]);
            }
        });
        return atomicInteger.get();
    }

    public static int getAmountOfEnabledJobs(Player player) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for(Jobs job : Jobs.values()) {
            if (getJobIsEnabled(player, job)) {
                atomicInteger.set(atomicInteger.get() + 1);
            }
        }
        return atomicInteger.get();
    }
}