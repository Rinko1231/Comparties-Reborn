package com.teampotato.comparties.mixin;


import dev.matthe815.mmoparties.common.events.EventCommon;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EventCommon.class, remap = false)
public abstract class MixinEventCommon {
    @Inject(method = "OnPlayerHurt", at = @At("HEAD"), cancellable = true)
    private static void onPlayerHurt2(LivingEntity entity, Entity damageSource, CallbackInfoReturnable<Boolean> cir) {
        cir.cancel();
    }
}
