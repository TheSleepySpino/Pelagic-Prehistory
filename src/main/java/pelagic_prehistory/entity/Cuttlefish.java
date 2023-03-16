package pelagic_prehistory.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Cuttlefish extends WaterAnimal implements GeoAnimatable {

    // GECKOLIB //
    protected AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    protected static final RawAnimation ANIM_IDLE = RawAnimation.begin().thenPlay("idle");
    protected static final RawAnimation ANIM_SWIM = RawAnimation.begin().thenPlay("swim");

    public Cuttlefish(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
        this.lookControl = new Cuttlefish.NoResetLookControl(this);
        //this.moveControl = new SmoothSwimmingMoveControl(this, 20, 5, 0.02F, 0.1F, true);
        //this.lookControl = new SmoothSwimmingLookControl(this, 90);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.24D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

    //// METHODS ////

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(3, new Cuttlefish.RandomMovementGoal(this));
        this.goalSelector.addGoal(4, new FleeGoal(this));
        //this.goalSelector.addGoal(8, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.0D, 1.0D));
        //this.goalSelector.addGoal(9, new AvoidEntityGoal<>(this, ElderGuardian.class, 16.0F, 1.0D, 1.0D));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(this.isInWaterOrBubble()) {
            if (!this.level.isClientSide()) {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.90F));
            }
        } else {
            if (!this.level.isClientSide()) {
                double d1 = this.getDeltaMovement().y;
                if (this.hasEffect(MobEffects.LEVITATION)) {
                    d1 = 0.05D * (double)(this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1);
                } else if (!this.isNoGravity()) {
                    d1 -= 0.08D;
                }

                this.setDeltaMovement(0.0D, d1 * (double)0.98F, 0.0D);
            }
        }
    }

    @Override
    public LookControl getLookControl() {
        return super.getLookControl();
    }

    @Override
    public void tick() {
        super.tick();
    }

    public void travel(Vec3 pTravelVector) {
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(BlockPos pPos) {
                return /*!level.getBlockState(pPos).isAir()
                        && !level.getBlockState(pPos.above()).isAir()
                        && */super.isStableDestination(pPos)
                        && !level.getBlockState(pPos.above()).isAir()/*
                        && super.isStableDestination(pPos.below())*/;
            }
        };
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.85F;
    }

    @Override
    public int getHeadRotSpeed() {
        return 90;
    }

    @Override
    public int getMaxHeadYRot() {
        return 180;
    }

    @Override
    public int getMaxHeadXRot() {
        return 180;
    }

    //// SOUNDS ////

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SQUID_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SQUID_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SQUID_DEATH;
    }

    protected SoundEvent getSquirtSound() {
        return SoundEvents.SQUID_SQUIRT;
    }

    //// INK ////

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (super.hurt(pSource, pAmount)) {
            if (this.getLastHurtByMob() != null) {
                this.spawnInk(getLastHurtByMob());
            }
            return true;
        }
        return false;
    }

    private void spawnInk(LivingEntity lastHurtByMob) {
        if(this.level.isClientSide()) {
            return;
        }
        // play ink sound
        this.playSound(this.getSquirtSound(), this.getSoundVolume(), this.getVoicePitch());
        // determine position and direction of ink
        final Vec3 position = this.getEyePosition();
        final Vec3 direction = this.position().vectorTo(lastHurtByMob.position()).normalize();
        final double variance = 0.25D;
        final double speed = 0.15D;
        // send ink particles to clients
        for(int i = 0, n = 30; i < n; i++) {
            Vec3 pos = position.add((this.getRandom().nextDouble() - 0.5D) * 2.0D * variance, (this.getRandom().nextDouble() - 0.5D) * 2.0D * variance, (this.getRandom().nextDouble() - 0.5D) * 2.0D * variance);
            Vec3 motion = direction;//direction.add((this.getRandom().nextDouble() - 0.5D) * 2.0D * variance, (this.getRandom().nextDouble() - 0.5D) * 2.0D * variance, (this.getRandom().nextDouble() - 0.5D) * 2.0D * variance);
            ((ServerLevel)this.level).sendParticles(this.getInkParticle(), pos.x(), pos.y(), pos.z(), 0, motion.x(), motion.y(), motion.z(), speed);
        }
        // add blindness effect
        if(this.position().closerThan(lastHurtByMob.getEyePosition(), 2.5D)) {
            lastHurtByMob.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, false));
        }
    }

    protected ParticleOptions getInkParticle() {
        return ParticleTypes.SQUID_INK;
    }

    
    //// NBT ////

    @Override
    public void readAdditionalSaveData(final CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }

    @Override
    public void addAdditionalSaveData(final CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    //// GECKOLIB ////

    private PlayState handleAnimation(AnimationState<Cuttlefish> event) {
        if(getDeltaMovement().lengthSqr() > 2.5000003E-7F) {
            event.getController().setAnimation(ANIM_SWIM);
        } else {
            event.getController().setAnimation(ANIM_IDLE);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::handleAnimation));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return instanceCache;
    }

    @Override
    public double getTick(Object object) {
        return tickCount;
    }

    //// LOOK CONTROL ////

    private static class NoResetLookControl extends LookControl {

        public NoResetLookControl(Mob pMob) {
            super(pMob);
        }

        @Override
        protected boolean resetXRotOnTick() {
            return false;
        }

        @Override
        public void tick() {
            this.lookAtCooldown = 1;
            super.tick();
        }
    }
    
    //// GOALS ////

    private static class FleeGoal extends Goal {
        
        private static final float CUTTLEFISH_FLEE_SPEED = 3.0F;
        private static final float CUTTLEFISH_FLEE_MIN_DISTANCE = 5.0F;
        private static final float CUTTLEFISH_FLEE_MAX_DISTANCE = 10.0F;
        
        private final Cuttlefish entity;
        private int fleeTicks;

        private FleeGoal(Cuttlefish entity) {
            this.entity = entity;
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = entity.getLastHurtByMob();
            if (entity.isInWater() && livingentity != null) {
                return entity.position().closerThan(livingentity.position(), 10.0D);
            } else {
                return false;
            }
        }

        @Override
        public void start() {
            this.fleeTicks = 0;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            ++this.fleeTicks;
            LivingEntity livingentity = entity.getLastHurtByMob();
            if (livingentity != null) {
                Vec3 vec3 = new Vec3(entity.getX() - livingentity.getX(), entity.getY() - livingentity.getY(), entity.getZ() - livingentity.getZ());
                BlockState blockstate = entity.level.getBlockState(new BlockPos(entity.getX() + vec3.x, entity.getY() + vec3.y, entity.getZ() + vec3.z));
                FluidState fluidstate = entity.level.getFluidState(new BlockPos(entity.getX() + vec3.x, entity.getY() + vec3.y, entity.getZ() + vec3.z));
                if (fluidstate.is(FluidTags.WATER) || blockstate.isAir()) {
                    double d0 = vec3.length();
                    if (d0 > 0.0D) {
                        vec3.normalize();
                        double d1 = CUTTLEFISH_FLEE_SPEED;
                        if (d0 > CUTTLEFISH_FLEE_MIN_DISTANCE) {
                            d1 -= (d0 - CUTTLEFISH_FLEE_MIN_DISTANCE) / CUTTLEFISH_FLEE_MIN_DISTANCE;
                        }

                        if (d1 > 0.0D) {
                            vec3 = vec3.scale(d1);
                        }
                    }

                    if (blockstate.isAir()) {
                        vec3 = vec3.subtract(0.0D, vec3.y, 0.0D);
                    }

                    entity.setDeltaMovement(vec3.scale(1.0D / (CUTTLEFISH_FLEE_MAX_DISTANCE * 2.0D)));
                }

                if (this.fleeTicks % 10 == 5) {
                    entity.level.addParticle(ParticleTypes.BUBBLE, entity.getX(), entity.getY(), entity.getZ(), 0.0D, 0.0D, 0.0D);
                }

            }
        }
    }

    private static class RandomMovementGoal extends Goal {
        private static final int MOVEMENT_COOLDOWN = 50;
        private final Cuttlefish entity;

        public RandomMovementGoal(Cuttlefish entity) {
            this.entity = entity;
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            int i = this.entity.getNoActionTime();
            if (i > 100) {
                this.entity.setDeltaMovement(0.0F, 0.0F, 0.0F);
            } else if (this.entity.getRandom().nextInt(reducedTickDelay(MOVEMENT_COOLDOWN)) == 0 /*|| !this.entity.wasTouchingWater*/ || this.entity.getDeltaMovement().lengthSqr() < 2.5000003E-7F) {
                final float speed = 0.24F;
                Vec3 movement = getDesiredMovement().scale(speed);
                this.entity.setDeltaMovement(movement.x(), movement.y(), movement.z());
                this.entity.getLookControl().setLookAt(this.entity.getEyePosition().add(this.entity.getDeltaMovement().normalize().reverse().scale(8.0F)));
            }
        }

        protected Vec3 getDesiredMovement() {
            float dx = 0.0F;
            float dy = 0.0F;
            float dz = 0.0F;
            float scale = 1.5F;
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
            for(int attempts = 6; attempts > 0; attempts--) {
                float angle = this.entity.getRandom().nextFloat() * ((float)Math.PI * 2F);
                dx = Mth.cos(angle);
                dy = -0.2125F + this.entity.getRandom().nextFloat();
                dz = Mth.sin(angle);
                pos.setWithOffset(this.entity.blockPosition(), (int)(dx * scale), (int)(dy * scale), (int)(dz * scale));
                if(entity.getNavigation().isStableDestination(pos)) {
                    break;
                }
            }
            return new Vec3(dx, dy, dz);
        }
    }
}
