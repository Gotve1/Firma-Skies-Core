package com.gotve.firma_skies_core.mixin.create;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.world.level.block.entity.BlockEntity;

@Mixin(value = com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity.class)
public abstract class EncasedFanBlockEntityMixin extends BlockEntity {

    @Unique
    private int tfc$airTicks = 0;

    public EncasedFanBlockEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Unique
    public void tfc$intakeAir(int amount) {
        this.tfc$airTicks += amount;
        if (this.tfc$airTicks > 600) {
            this.tfc$airTicks = 600;
        }
    }

    @Unique
    public int tfc$getAirTicks() {
        return this.tfc$airTicks;
    }

    @Unique
    public void tfc$decrementAirTicks() {
        if (this.tfc$airTicks > 0) {
            this.tfc$airTicks--;
        }
    }
}