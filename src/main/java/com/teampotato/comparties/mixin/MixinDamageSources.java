package com.teampotato.comparties.mixin;

import dev.matthe815.mmoparties.forge.api.PartyHelper;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.scores.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.teampotato.comparties.Comparties.PARTY;

@Mixin(value = DamageSources.class, remap = false)
public abstract class MixinDamageSources {

    @Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
    private static void onHurt(Entity target, float baseAmount, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (target instanceof ServerPlayer targetPlayer) {
            Entity directSourceEntity = damageSource.getDirectEntity();
            if (directSourceEntity instanceof ServerPlayer directSourcePlayer && PartyHelper.Server.GetRelation(targetPlayer, directSourcePlayer) == PARTY) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    /**
     * @author Rinko
     * @reason Tweak
     */
    @Overwrite
    public static boolean isFriendlyFireBetween(Entity attacker, Entity target) {
                if (attacker != null && target != null) {
                    if (attacker instanceof ServerPlayer player)
                    {
                        if (target instanceof TamableAnimal pet && pet.getOwner() instanceof ServerPlayer petOwner)
                            return PartyHelper.Server.GetRelation(petOwner, player) == PARTY;
                        else if (target instanceof ServerPlayer targetPlayer)
                            return PartyHelper.Server.GetRelation(targetPlayer, player) == PARTY;
                    }
                    else
                        if (attacker.isPassengerOfSameVehicle(target)) {
                        return true;
                    } else {
                        Team team = attacker.getTeam();
                        if (team == null) {
                            return attacker.isAlliedTo(target);
                        } else {

                            return team.isAlliedTo(target.getTeam()) && !team.isAllowFriendlyFire();
                        }
                    }
                }
                else {
                    return false;
                }

        return false;
    }
/*
    @Unique
    private static boolean ba_painting$friendlyFire(Entity attacker, Entity target)
    {
        if (attacker instanceof ServerPlayer player)
        {
            if (target instanceof TamableAnimal pet && pet.getOwner() instanceof ServerPlayer petOwner)
                return PartyHelper.Server.GetRelation(petOwner, player) == PARTY;
            else if (target instanceof ServerPlayer targetPlayer)
                return PartyHelper.Server.GetRelation(targetPlayer, player) == PARTY;
        }
        return false;
    }*/
}


