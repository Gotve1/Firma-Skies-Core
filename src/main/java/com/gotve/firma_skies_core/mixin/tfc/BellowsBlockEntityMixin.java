package com.gotve.firma_skies_core.mixin.tfc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.blockentities.BellowsBlockEntity;
import net.dries007.tfc.common.blocks.devices.BellowsBlock;
import net.dries007.tfc.common.blocks.devices.IBellowsConsumer;

@Mixin(net.dries007.tfc.common.blockentities.BellowsBlockEntity.class)
public abstract class BellowsBlockEntityMixin {

    @Shadow(remap = false)
    private void afterPush() {}

    @Unique
    private @Nullable Boolean tfc$encasedFanClassCache = null;


    // replace tfc's doPush with mine
    @Inject(method = "doPush()V", at = @At("TAIL"), cancellable = true, remap = false)
    private void tfc$doPushEnhanced(CallbackInfo ci) {
        Level level = ((BellowsBlockEntity)(Object)this).getLevel();
        BlockPos worldPosition = ((BellowsBlockEntity)(Object)this).getBlockPos();


        if (level.isClientSide) {
            return;
        }

        BellowsBlockEntity bellows = (BellowsBlockEntity) (Object) this;
        boolean foundAnyReceivers = false;
        boolean foundAnyAllowingReceivers = false;

        final Direction direction = bellows.getBlockState().getValue(BellowsBlock.FACING);
        for (IBellowsConsumer.Offset offset : IBellowsConsumer.offsets()) {
            final BlockPos airPosition = worldPosition.above(offset.up())
                    .relative(direction, offset.out())
                    .relative(direction.getClockWise(), offset.side());
            final BlockState state = level.getBlockState(airPosition);
            if (state.getBlock() instanceof IBellowsConsumer consumer) {
                foundAnyReceivers = true;
                if (consumer.canAcceptAir(level, airPosition, state)) {
                    foundAnyAllowingReceivers = true;
                    consumer.intakeAir(level, airPosition, state, BellowsBlockEntity.BELLOWS_AIR);
                }
            } else if (tfc$isEncasedFan(state)) {
                tfc$handleEncasedFanIntake(level, airPosition, state);
                foundAnyReceivers = true;
                foundAnyAllowingReceivers = true;
            }
        }

        if (!foundAnyReceivers || foundAnyAllowingReceivers) {
            tfc$invokeAfterPush(bellows);
        }

        ci.cancel();
    }

    @Unique
    private boolean tfc$isEncasedFan(BlockState state) {
        try {
            if (tfc$encasedFanClassCache == null) {
                Class.forName("com.simibubi.create.content.kinetics.fan.EncasedFanBlock");
                tfc$encasedFanClassCache = true;
            }
            return tfc$encasedFanClassCache && state.getBlock().getClass().getName()
                    .equals("com.simibubi.create.content.kinetics.fan.EncasedFanBlock");
        } catch (ClassNotFoundException e) {
            tfc$encasedFanClassCache = false;
            return false;
        }
    }

    @Unique
    private void tfc$handleEncasedFanIntake(Level level, BlockPos pos, BlockState state) {
        try {
            Object blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {
                var method = blockEntity.getClass().getDeclaredMethod("tfc$intakeAir", int.class);
                method.setAccessible(true);
                method.invoke(blockEntity, BellowsBlockEntity.BELLOWS_AIR);
            }
        } catch (Exception e) {
            // crash if no create installed
        }
    }

    @Unique
    private void tfc$invokeAfterPush(BellowsBlockEntity bellows) {
        try {
            var method = BellowsBlockEntity.class.getDeclaredMethod("afterPush");
            method.setAccessible(true);
            method.invoke(bellows);
        } catch (Exception e) {
            // crash if no create installed
        }
    }
}