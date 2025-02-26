package com.teampotato.comparties.mixin.nomagic;


import dev.matthe815.mmoparties.forge.api.PartyHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.teampotato.comparties.Comparties.PARTY;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void onHurt(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity pet = comparties$getThis();
        if (pet instanceof OwnableEntity targetPet && targetPet.getOwner() instanceof ServerPlayer petOwner) {
            Entity directSourceEntity = pSource.getDirectEntity();

            if (
                    (directSourceEntity instanceof ServerPlayer sourcePlayer && PartyHelper.Server.GetRelation(petOwner, sourcePlayer) == PARTY) ||
                            (directSourceEntity instanceof AreaEffectCloud sourceCloud && sourceCloud.getOwner() instanceof ServerPlayer ownerPlayer && PartyHelper.Server.GetRelation(petOwner, ownerPlayer) == PARTY) ||
                            (directSourceEntity instanceof Projectile projectile && projectile.getOwner() instanceof ServerPlayer projectileOwner && PartyHelper.Server.GetRelation(petOwner, projectileOwner) == PARTY) ||
                            (directSourceEntity instanceof ThrownPotion sourcePotion && sourcePotion.getOwner() instanceof ServerPlayer potionOwner && PartyHelper.Server.GetRelation(petOwner, potionOwner) == PARTY)
            ) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    @Unique
    private LivingEntity comparties$getThis() {
        return (LivingEntity) (Object) this;
    }


}
