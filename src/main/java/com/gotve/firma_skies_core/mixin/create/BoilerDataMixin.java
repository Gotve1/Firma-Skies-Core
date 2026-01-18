package com.gotve.firma_skies_core.mixin.create;

import com.simibubi.create.content.fluids.tank.BoilerData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BoilerData.BoilerFluidHandler.class, remap = false)
public class BoilerDataMixin {

    @Inject(method = "isFluidValid", at = @At("HEAD"), cancellable = true)
    private void allowTfcWater(int tank, FluidStack stack, CallbackInfoReturnable<Boolean> cir) {

        boolean isWater = stack.getFluid().builtInRegistryHolder().is(FluidTags.create(new ResourceLocation("minecraft", "water")));

        if (isWater) {
            cir.setReturnValue(true);
        }
    }
}
