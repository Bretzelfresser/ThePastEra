package com.bretzelfresser.the_past_era.common.entity;

import com.bretzelfresser.the_past_era.ThePastEra;
import com.bretzelfresser.the_past_era.core.init.ItemInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Arrays;

public class Goobar extends AbstractSchoolingFish implements IAnimatable {

    public static final String VARIANT_NAME = "goobar_variant", CONTROLLER = "controller";

    public static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(Goobar.class, EntityDataSerializers.INT);


    public static AttributeSupplier.Builder createAttributes(){
        return AbstractSchoolingFish.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.4d);
    }

    protected AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public Goobar(EntityType<? extends AbstractSchoolingFish> p_27523_, Level p_27524_) {
        super(p_27523_, p_27524_);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData group, @Nullable CompoundTag nbt) {
        group =  super.finalizeSpawn(level, difficulty, reason, group, nbt);
        if (reason == MobSpawnType.BUCKET && nbt != null && nbt.contains(VARIANT_NAME)){
            this.entityData.set(TYPE, nbt.getInt(VARIANT_NAME));
        }
        this.navigation.stop();
        return group;
    }

    @Override
    public void saveToBucketTag(ItemStack p_30049_) {
        super.saveToBucketTag(p_30049_);
        CompoundTag compoundtag = p_30049_.getOrCreateTag();
        compoundtag.putInt(VARIANT_NAME, this.entityData.get(TYPE));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt(VARIANT_NAME, this.entityData.get(TYPE));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if(nbt.contains(VARIANT_NAME))
            this.entityData.set(TYPE, nbt.getInt(VARIANT_NAME));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TYPE, getInitlaType().ordinal());
    }

    public Type getGoobarType(){
        return Type.values()[this.entityData.get(TYPE)];
    }

    protected PlayState predicate(AnimationEvent<Goobar> event){
        //System.out.println(!this.isInWater() + "|" + this.onGround + "|" + this.verticalCollision);
        if(!this.isInWater()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("flop", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        double lengthSq = new Vec3(xOld - this.getX(), this.yOld - this.getY(), this.zOld - this.getZ()).lengthSqr();
        if (lengthSq > 0.00001 && lengthSq < 0.2){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("swim", ILoopType.EDefaultLoopTypes.LOOP));
        }else if (lengthSq >= 0.2){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("swim_fast", ILoopType.EDefaultLoopTypes.LOOP));
        }else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        }


        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, CONTROLLER, 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.TROPICAL_FISH_FLOP;
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
        return ItemInit.GOOBAR_BUCKET.get().getDefaultInstance();
    }

    protected Type getInitlaType(){
        int totalWeight = Arrays.stream(Type.values()).mapToInt(Type::getWeight).sum();
        int randomWeight = random.nextInt(totalWeight) + 1;
        int cumulativeWeight = 0;

        for (Type type : Type.values()) {
            cumulativeWeight += type.getWeight();
            if (randomWeight <= cumulativeWeight) {
                return type;
            }
        }

        // Fallback in case the weights are not properly defined
        return Type.values()[random.nextInt(Type.values().length)];
    }


    public enum Type{
        CLOWN(ThePastEra.modLoc("textures/entity/clown_goobar.png"), 5),
        MALESTICATED(ThePastEra.modLoc("textures/entity/malesticed_goobar.png"), 1),
        ROYAL(ThePastEra.modLoc("textures/entity/royal_goobar.png"), 5);


        private final ResourceLocation texture;
        private final int weight;

        Type(ResourceLocation texture, int weight) {
            this.texture = texture;
            this.weight = weight;
        }

        public ResourceLocation getTexture() {
            return texture;
        }

        public int getWeight() {
            return weight;
        }
    }
}
