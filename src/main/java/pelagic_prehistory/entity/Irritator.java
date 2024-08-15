package pelagic_prehistory.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;
import pelagic_prehistory.PPRegistry;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;

public class Irritator extends PathfinderMob implements NeutralMob, IAnimatable {

    // NEUTRAL MOB //
    private static final UniformInt ANGER_RANGE = TimeUtil.rangeOfSeconds(20, 39);
    private int angerTime;
    private UUID angerTarget;

    // GECKOLIB //
    protected AnimationFactory instanceCache = GeckoLibUtil.createFactory(this);
    protected static final AnimationBuilder ANIM_IDLE = new AnimationBuilder().addAnimation("idle");
    protected static final AnimationBuilder ANIM_WALK = new AnimationBuilder().addAnimation("walk");
    protected static final AnimationBuilder ANIM_SWIM = new AnimationBuilder().addAnimation("swim");

    // OTHER //
    protected boolean isBodyInWater;

    public Irritator(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 30, 20, 0.2F, 0.8F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 15);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 0.8D);
    }

    public static boolean checkIrritatorSpawnRules(EntityType<? extends PathfinderMob> entity, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return Animal.checkMobSpawnRules(entity, pLevel, pSpawnType, pPos, pRandom) && isShallowWater(pLevel, pPos);
    }

    static boolean isShallowWater(final LevelReader level, final BlockPos pos) {
        // check water block
        if(!level.isWaterAt(pos)) {
            return false;
        }
        // check air above
        if(!level.getBlockState(pos.above()).isAir()) {
            return false;
        }
        // check solid below
        final BlockPos posBelow = pos.below();
        if(!level.getBlockState(posBelow).isSolidRender(level, posBelow)) {
            return false;
        }
        // all checks passed
        return true;
    }

    //// METHODS ////

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0F, false));
        this.goalSelector.addGoal(2, new Irritator.MoveToShallowWaterGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D, 10) {
            @Override
            public boolean canUse() {
                return !this.mob.isInWater() && super.canUse();
            }
        });
        this.goalSelector.addGoal(3, new RandomSwimmingGoal(this, 2.5D, 10) {
            @Override
            public boolean canUse() {
                return super.canUse() && isInWater();
            }
        });
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Drowned.class, true, false));
        this.targetSelector.addGoal(7, new ResetUniversalAngerTargetGoal<>(this, false));

    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(!level.isClientSide()) {
            this.updatePersistentAnger((ServerLevel) this.level, true);
        }
    }
    @Override
    public void tick() {
        super.tick();
        updateFluidOnBody();
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    protected AmphibiousPathNavigation createNavigation(Level level) {return new AmphibiousPathNavigation(this, level);}

    @Override
    public float getWalkTargetValue(BlockPos pPos, LevelReader pLevel) {
        final BlockPos posBelow = pPos.below();
        final BlockState blockState = pLevel.getBlockState(posBelow);
        final BlockPathTypes pathType = blockState.getBlockPathType(pLevel, posBelow, this);
        if(pathType == BlockPathTypes.WATER_BORDER || pathType == BlockPathTypes.WATER
                || blockState.is(Blocks.GRASS_BLOCK) || isShallowWater(pLevel, pPos)) {
            return 8.0F;
        }
        return super.getWalkTargetValue(pPos, pLevel);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.95F;
    }

    @Override
    public double getFluidJumpThreshold() {
        return 1.0D;
    }

    @Override
    protected boolean isAffectedByFluids() {
        return !this.getEyeInFluidType().isAir();
    }

    @Override
    protected float getWaterSlowDown() {
        return super.getWaterSlowDown();
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().inflate(0.5F, 0.0F, 0.5F);
    }

    private void updateFluidOnBody() {
        double bodyY = this.getY() + getDimensions(getPose()).height * 0.5D;
        BlockPos blockpos = new BlockPos(this.getX(), bodyY, this.getZ());
        FluidState fluidstate = this.level.getFluidState(blockpos);
        double fluidHeight = (float)blockpos.getY() + fluidstate.getHeight(this.level, blockpos);
        this.isBodyInWater = !fluidstate.isEmpty() && fluidHeight > bodyY;
    }

    //// NEUTRAL MOB ////

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ANGER_RANGE.sample(this.random));
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.angerTime = time;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.angerTime;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.angerTarget = target;
    }

    @Override
    public UUID getPersistentAngerTarget() {
        return this.angerTarget;
    }

    //// SOUNDS ////

    @Override
    public int getAmbientSoundInterval() {
        return 160;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return PPRegistry.SoundReg.IRRITATOR_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return PPRegistry.SoundReg.IRRITATOR_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return PPRegistry.SoundReg.IRRITATOR_DEATH.get();
    }

    //// NBT ////

    @Override
    public void readAdditionalSaveData(final CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        readPersistentAngerSaveData(this.level, tag);
    }

    @Override
    public void addAdditionalSaveData(final CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        addPersistentAngerSaveData(tag);
    }

    //// GECKOLIB ////

    private PlayState handleAnimation(AnimationEvent<Irritator> event) {
        final boolean inWater = isBodyInWater;
        final boolean isWalking = getDeltaMovement().horizontalDistanceSqr() > 1.5000003E-7F;
        if(inWater) {
            event.getController().setAnimation(ANIM_SWIM);
        } else if(isWalking) {
            event.getController().setAnimation(ANIM_WALK);
        } else {
            event.getController().setAnimation(ANIM_IDLE);
        }
        event.getController().transitionLengthTicks = 6.0D;
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 2F, this::handleAnimation));
    }

    @Override
    public AnimationFactory getFactory() {
        return instanceCache;
    }

    //// GOALS ////

    private static class MoveToShallowWaterGoal extends MoveToBlockGoal {

        public MoveToShallowWaterGoal(PathfinderMob pMob, final double speedModifier) {
            super(pMob, speedModifier, 8, 3);
            this.verticalSearchStart = -1;
        }

        @Override
        public boolean canUse() {
            return this.mob.isOnGround() && !this.mob.level.isWaterAt(this.mob.blockPosition()) && super.canUse();
        }

        @Override
        public void start() {
            super.start();
        }

        @Override
        protected boolean isValidTarget(final LevelReader level, final BlockPos pos) {
            return isShallowWater(level, pos.above(1));
        }
    }
}
