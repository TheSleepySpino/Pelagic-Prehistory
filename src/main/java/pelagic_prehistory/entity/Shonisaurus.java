package pelagic_prehistory.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Shonisaurus extends WaterAnimal implements IAnimatable {

    // GECKOLIB //
    protected AnimationFactory instanceCache = GeckoLibUtil.createFactory(this);
    protected static final AnimationBuilder ANIM_SWIM = new AnimationBuilder().addAnimation("swim");
    protected static final AnimationBuilder ANIM_SWIM_FAST = new AnimationBuilder().addAnimation("swim_fast");

    public Shonisaurus(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 30, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 15);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.4D)
                .add(Attributes.ATTACK_DAMAGE, 9.0D);
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
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 0.5D, 80));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    public void tick() {
        super.tick();
        if(isInWater() && !isEyeInFluidType(ForgeMod.WATER_TYPE.get())) {
            setDeltaMovement(getDeltaMovement().add(0, 0.04D, 0));
        }
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(BlockPos pPos) {
                return level.isWaterAt(pPos.above()) && super.isStableDestination(pPos);
            }
        };
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.55F;
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().inflate(2.0F, 0.5F, 2.0F);
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

    private PlayState handleAnimation(AnimationEvent<Shonisaurus> event) {
        if(getDeltaMovement().lengthSqr() > 2.5000003E-7F) {
            event.getController().setAnimation(ANIM_SWIM_FAST);
        } else {
            event.getController().setAnimation(ANIM_SWIM);
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
