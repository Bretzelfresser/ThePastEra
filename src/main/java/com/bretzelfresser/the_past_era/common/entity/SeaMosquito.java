package com.bretzelfresser.the_past_era.common.entity;

import com.bretzelfresser.the_past_era.core.init.ItemInit;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class SeaMosquito extends AbstractSchoolingFish implements IAnimatable {

    public static final String CONTROLLER_NAME = "controller";

    public static AttributeSupplier.Builder createAttributes(){
        return AbstractSchoolingFish.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.4d);
    }


    protected AnimationFactory factory = GeckoLibUtil.createFactory(this);


    public SeaMosquito(EntityType<? extends AbstractSchoolingFish> p_27523_, Level p_27524_) {
        super(p_27523_, p_27524_);
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.SALMON_FLOP;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.TROPICAL_FISH_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.TROPICAL_FISH_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource p_30039_) {
        return SoundEvents.TROPICAL_FISH_HURT;
    }


    @Override
    public ItemStack getBucketItemStack() {
        return ItemInit.SEA_MOSQUITO_BUCKET.get().getDefaultInstance();
    }

    protected PlayState predicate(AnimationEvent<Goobar> event){
        //System.out.println(!this.isInWater() + "|" + this.onGround + "|" + this.verticalCollision);
        if(!this.isInWater()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("flop", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        double lengthSq = new Vec3(xOld - this.getX(), this.yOld - this.getY(), this.zOld - this.getZ()).lengthSqr();
        if (lengthSq > 0.00001){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("swim", ILoopType.EDefaultLoopTypes.LOOP));
        }else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        }


        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, CONTROLLER_NAME, 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
