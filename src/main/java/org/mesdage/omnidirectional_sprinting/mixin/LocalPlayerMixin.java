package org.mesdage.omnidirectional_sprinting.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    @Shadow
    public Input input;

    @Shadow
    public abstract boolean isUnderWater();

    @ModifyReturnValue(method = "hasEnoughImpulseToStartSprinting", at = @At("RETURN"))
    private boolean omni(boolean original, @Local double d0) {
        if (original) return true;
        if (isUnderWater()) return input.forwardImpulse < -Mth.EPSILON || input.leftImpulse > Mth.EPSILON || input.leftImpulse < -Mth.EPSILON;
        return input.forwardImpulse < -d0 || input.leftImpulse > d0 || input.leftImpulse < -d0;
    }

    @ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/Input;hasForwardImpulse()Z"))
    private boolean omni(boolean original) {
        return original || input.forwardImpulse < -Mth.EPSILON || input.leftImpulse > Mth.EPSILON || input.leftImpulse < -Mth.EPSILON;
    }
}
