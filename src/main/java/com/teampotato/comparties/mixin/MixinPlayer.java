package com.teampotato.comparties.mixin;

import dev.matthe815.mmoparties.forge.api.PartyHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.teampotato.comparties.Comparties.PARTY;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {

    protected MixinPlayer(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void onHurt(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        Player player = comparties$getThis();
        if (player instanceof ServerPlayer targetPlayer) {
            Entity directSourceEntity = pSource.getDirectEntity();
            if (
                    (directSourceEntity instanceof ServerPlayer sourcePlayer && PartyHelper.Server.GetRelation(targetPlayer, sourcePlayer) == PARTY) ||
                    (directSourceEntity instanceof ThrownPotion sourcePotion && sourcePotion.getOwner() instanceof ServerPlayer ownerPlayer && PartyHelper.Server.GetRelation(targetPlayer, ownerPlayer) == PARTY) ||
                    (directSourceEntity instanceof Projectile projectile && projectile.getOwner() instanceof ServerPlayer projectileOwner && PartyHelper.Server.GetRelation(targetPlayer, projectileOwner) == PARTY) ||
                     (directSourceEntity instanceof AreaEffectCloud sourceCloud && sourceCloud.getOwner() instanceof ServerPlayer cloudOwner && PartyHelper.Server.GetRelation(targetPlayer, cloudOwner) == PARTY)
            ) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    @Unique
    private Player comparties$getThis() {
        return (Player) (Object)this;
    }
}
