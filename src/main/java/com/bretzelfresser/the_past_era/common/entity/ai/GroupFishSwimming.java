package com.bretzelfresser.the_past_era.common.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class GroupFishSwimming extends Goal {
    protected final AbstractFish creature;
    protected final double speed;
    protected double[] weights = new double[]{1, 1, 1, 1, 1, 1};
    protected double playerWeight = 1;
    protected boolean shouldAvoidPlayer = false;
    protected boolean mustUpdate;
    protected final double distSenseOthers, distSenseBlocks;
    protected final Predicate<AbstractFish> selector;


    /**
     *
     * @param creatureIn        the creature this is applied to
     * @param speedIn           the speed addition this goal has compared to the normal swim speed
     * @param senseDist the distance in which this entity can sense others and blocks
     * @param weights         the weights of the different rules:
     *                        0: the normal motion
     *                        1: the average direction in which the swarm is swimming
     *                        2: avoiding others when to near them
     *                        3: avoiding sensed blocks when near them
     *                        4: the random noise to let it move random around
     *                        5: moving towards the middle pos of all sensed entities
     */
    public GroupFishSwimming(AbstractFish creatureIn, double speedIn, double senseDist, double... weights) {
        this(creatureIn, speedIn, senseDist, senseDist, e -> e.getType() == creatureIn.getType(), weights);
    }

    /**
     *
     * @param creatureIn        the creature this is applied to
     * @param speed           the speed addition this goal has compared to the normal swim speed
     * @param distSenseOthers the distance in which this entity can sense others
     * @param senseBlocks th distance in which this entity can sense non water blocks
     * @param weights         the weights of the different rules:
     *                        0: the normal motion
     *                        1: the average direction in which the swarm is swimming
     *                        2: avoiding others when to near them
     *                        3: avoiding sensed blocks when near them
     *                        4: the random noise to let it move random around
     *                        5: moving towards the middle pos of all sensed entities
     */
    public GroupFishSwimming(AbstractFish creatureIn, double speed, double distSenseOthers, double senseBlocks, double... weights) {
        this(creatureIn, speed, distSenseOthers, senseBlocks, e -> e.getType() == creatureIn.getType(), weights);
    }

    /**
     * @param creature        the creature this is applied to
     * @param speed           the speed addition this goal has compared to the normal swim speed
     * @param distSenseOthers the distance in which this entity can sense others
     * @param distSenseBlocks th distance in which this entity can sense non water blocks
     * @param selector        the selector which fish should be able to swim with this fish
     * @param weights         the weights of the different rules:
     *                        0: the normal motion
     *                        1: the average direction in which the swarm is swimming
     *                        2: avoiding others when to near them
     *                        3: avoiding sensed blocks when near them
     *                        4: the random noise to let it move random around
     *                        5: moving towards the middle pos of all sensed entities
     */
    public GroupFishSwimming(AbstractFish creature, double speed, double distSenseOthers, double distSenseBlocks, Predicate<AbstractFish> selector, double... weights) {
        this.creature = creature;
        this.speed = speed;
        this.distSenseOthers = distSenseOthers;
        this.distSenseBlocks = distSenseBlocks;
        this.selector = selector;
        if (weights.length != 6 && weights.length != 0)
            throw new IllegalArgumentException("there always has to be 6 weights not: " + weights.length);
        if (weights.length != 0)
            this.weights = weights;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * this defines if the fish should try to avoid the player.
     * @param weightAvoidingPlayer the weight how fast/strong the entity should avoid the player
     * @return
     */
    public GroupFishSwimming setAvoidingPlayer(double weightAvoidingPlayer){
        this.shouldAvoidPlayer = true;
        this.playerWeight = weightAvoidingPlayer;
        return this;
    }
    /**
     * this defines if the fish should try to avoid the player.
     */
    public GroupFishSwimming setAvoidingPlayer(){
        return setAvoidingPlayer(1);
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    @Override
    public boolean canUse() {
        if (this.creature.isVehicle()) {
            return false;
        } else {
            if (!this.mustUpdate) {
                if (this.creature.getNoActionTime() >= 100) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void tick() {
        //swim with others

        //this will keep the fish swimming
        double swimSPeed = this.creature.getAttributeValue(Attributes.MOVEMENT_SPEED) * this.speed;
        Vec3 motion = this.creature.getDeltaMovement();
        if (Double.isNaN(motion.x) || Double.isNaN(motion.y) || Double.isNaN(motion.z))
            motion = Vec3.ZERO;

        //add a bit of random nise in the movement
        Vec3 noise = new Vec3(this.creature.getRandom().nextInt(2) - 1, this.creature.getRandom().nextInt(2) - 1, this.creature.getRandom().nextInt(2) - 1);

        motion = motion.scale(80 * this.weights[0]);
        motion = motion.add(getAverageDir().scale(.75d * this.weights[1]));
        motion = motion.add(getAvoidGetToNearOthers().scale(.4d * this.weights[2]));
        motion = motion.add(getAvoidBlocks().scale(.25d * this.weights[3]));
        motion = motion.add(noise.scale(this.weights[4]));
        motion = motion.add(getCohesion().scale(this.weights[5]));

        if (shouldAvoidPlayer){
            motion = motion.add(getAvoidPlayer().scale(playerWeight));
        }

        if (motion.lengthSqr() > swimSPeed * swimSPeed) {
            motion = motion.normalize().scale(swimSPeed);
        }

        this.creature.setDeltaMovement(motion);

        float yaw = (float) Math.toDegrees(Math.atan2(-motion.x, motion.z));
        float pitch = (float) Math.toDegrees(Math.asin(motion.y / motion.length()));

        this.creature.yRotO = yaw;
        this.creature.setYRot(yaw);


    }


    public Vec3 getAvoidGetToNearOthers() {
        List<AbstractFish> entities = this.creature.level.getEntitiesOfClass(AbstractFish.class, this.creature.getBoundingBox().inflate(this.distSenseOthers), this.selector);
        Vec3 result = Vec3.ZERO;
        for (AbstractFish fish : entities) {
            if (fish != this.creature) {
                double dist = fish.distanceTo(this.creature);
                Vec3 diff = this.creature.position().subtract(fish.position()).normalize().scale(1 / dist);
                result = result.add(diff);
            }
        }

        return result;
    }

    public Vec3 getAverageDir() {
        List<AbstractFish> entities = this.creature.level.getEntitiesOfClass(AbstractFish.class, this.creature.getBoundingBox().inflate(this.distSenseOthers), this.selector);
        Vec3 result = Vec3.ZERO;
        for (AbstractFish fish : entities) {
            if (fish != this.creature) {
                double dist = 1 / fish.distanceTo(this.creature);
                result = result.add(fish.getDeltaMovement().normalize().scale(dist));
            }
        }

        return result;
    }

    public Vec3 getAvoidPlayer(){
        List<Player> players = this.creature.level.getEntitiesOfClass(Player.class, this.creature.getBoundingBox().inflate(this.distSenseOthers), EntitySelector.NO_CREATIVE_OR_SPECTATOR);
        Vec3 result = Vec3.ZERO;
        for (Player player : players){
            Vec3 awayFromPlayer = this.creature.position().subtract(player.position());
            result = result.add(awayFromPlayer);
        }
        return result;
    }

    public Vec3 getAvoidBlocks() {
        Vec3 result = new Vec3(0, 0, 0);
        for (int x = (int) Math.floor(-distSenseBlocks); x <= (int) Math.ceil(distSenseBlocks); x++) {
            for (int y = (int) Math.floor(-distSenseBlocks); y <= (int) Math.ceil(distSenseBlocks); y++) {
                for (int z = (int) Math.floor(-distSenseBlocks); z <= (int) Math.ceil(distSenseBlocks); z++) {
                    Vec3 looking = Vec3.atCenterOf(this.creature.blockPosition().offset(x, y, z));
                    if (!this.creature.level.getFluidState(new BlockPos(looking)).is(FluidTags.WATER)) {
                        double distSquared = this.creature.position().distanceTo(looking);
                        Vec3 avoiding = this.creature.position().subtract(looking).normalize().scale(1 / distSquared);
                        result = result.add(avoiding);
                    }
                }
            }
        }
        return result;
    }

    public Vec3 getCohesion() {
        List<AbstractFish> entities = this.creature.level.getEntitiesOfClass(AbstractFish.class, this.creature.getBoundingBox().inflate(this.distSenseOthers), this.selector);
        Vec3 result = Vec3.ZERO;
        int count = 0;
        for (AbstractFish fish : entities) {
            result = result.add(fish.position());
            count++;
        }

        if (count > 0) {
            result = result.scale(1 / (double) count);
            result = result.subtract(this.creature.position()).normalize();
        }

        return result;

    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return !this.creature.isVehicle();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.creature.getNavigation().stop();
    }
}
