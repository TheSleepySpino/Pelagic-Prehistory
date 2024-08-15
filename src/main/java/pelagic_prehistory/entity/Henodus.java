package pelagic_prehistory.entity;

import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Henodus extends PathfinderMob implements IAnimatable {

    // GECKOLIB //

    protected AnimationFactory instanceCache = GeckoLibUtil.createFactory(this);
    protected static final AnimationBuilder ANIM_IDLE = new AnimationBuilder().addAnimation("idle");
    protected static final AnimationBuilder ANIM_WALK = new AnimationBuilder().addAnimation("walk");
    protected static final AnimationBuilder ANIM_SWIM = new AnimationBuilder().addAnimation("swim");

    public Henodus(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 30, 20, 0.3F, 0.3F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 15);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    //// METHODS ////


    protected boolean isBodyInWater;

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }


    @Override
    protected void defineSynchedData() {super.defineSynchedData();}

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D, 10) {
            @Override
            public boolean canUse() {
                return !this.mob.isInWater() && super.canUse();
            }
        });
        this.goalSelector.addGoal(3, new RandomSwimmingGoal(this, 1.0D, 10) {
            @Override
            public boolean canUse() {
                return super.canUse() && isInWater();
            }
        });
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(9, new AvoidEntityGoal<>(this, Guardian.class, 15.0F, 1.0D, 1.0D));
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean requiresCustomPersistence() {return true;}

    @Override
    protected AmphibiousPathNavigation createNavigation(Level level) {return new AmphibiousPathNavigation(this, level);}

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.09375F;
    }

    @Override
    public double getFluidJumpThreshold() {
        return 1.1D;
    }

    @Override
    protected boolean isAffectedByFluids() {
        return !this.getEyeInFluidType().isAir();
    }

    @Override
    public int getMaxHeadXRot() {
        return 25;
    }

    @Override
    public int getMaxHeadYRot() {
        return 25;
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


    private PlayState handleAnimation(AnimationEvent<Henodus> event) {
        final boolean inWater = isBodyInWater;
        final boolean isWalking = getDeltaMovement().horizontalDistanceSqr() > 1.5000003E-7F;
        if (inWater) {
            event.getController().setAnimation(ANIM_SWIM);
        } else if (isWalking) {
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


}
