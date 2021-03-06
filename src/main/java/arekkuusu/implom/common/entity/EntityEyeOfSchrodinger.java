/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.entity;

import arekkuusu.implom.api.helper.NBTHelper;
import arekkuusu.implom.api.helper.RayTraceHelper;
import arekkuusu.implom.api.util.IPMMaterial;
import arekkuusu.implom.client.effect.Light;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.entity.ai.FlightMoveHelper;
import arekkuusu.implom.common.entity.ai.FlightPathNavigate;
import arekkuusu.implom.common.handler.gen.ModGen;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import net.katsstuff.teamnightclipse.mirror.client.particles.GlowTexture;
import net.katsstuff.teamnightclipse.mirror.data.MutableVector3;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 04/08/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class EntityEyeOfSchrodinger extends EntityMob {

	private static final DataParameter<Optional<UUID>> TARGET = EntityDataManager.createKey(EntityEyeOfSchrodinger.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	public static final int BLUE = 0x1EF2FF;
	public static final int RED = 0xFF1000;

	public EntityEyeOfSchrodinger(World worldIn) {
		super(worldIn);
		this.moveHelper = new FlightMoveHelper(this);
		this.setSize(0.5F, 0.5F);
		this.experienceValue = 10;
		this.isImmuneToFire = true;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(TARGET, Optional.absent());
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.onGround = false;
		this.isJumping = false;
		this.onGroundSpeedFactor = 0;
		this.prevOnGroundSpeedFactor = 0;
		//Spawn Particles
		if(world.isRemote) {
			int rgb = hasTargetedEntity() ? RED : BLUE;
			IPM.getProxy().spawnSpeck(world
					, Vector3.apply(posX, posY + 0.25D, posZ)
					, Vector3.Zero(), 10, 1.5F, rgb, Light.GLOW, GlowTexture.GLOW.getTexture());
			Entity entity = getTargetedEntity();
			if(entity != null) {
				MutableVector3 speed = Vector3.apply(posX, posY + 0.25D, posZ)
						.asMutable()
						.subtract(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ)
						.multiply(-0.1D);
				if(speed.x() > 0.15D || speed.x() < -0.15D) speed.setX(0.15);
				if(speed.y() > 0.15D || speed.y() < -0.15D) speed.setY(0.15);
				if(speed.z() > 0.15D || speed.z() < -0.15D) speed.setZ(0.15);
				IPM.getProxy().spawnSpeck(world, Vector3.apply(posX, posY + 0.25D, posZ), speed.asImmutable(), 10, 4F, RED, Light.GLOW, ResourceLibrary.SQUARE_PARTICLE);
			}
		}
	}

	@Override
	public boolean isEntityInsideOpaqueBlock() {
		if(this.noClip) {
			return false;
		} else {
			BlockPos.PooledMutableBlockPos pos = BlockPos.PooledMutableBlockPos.retain();
			for(int i = 0; i < 8; ++i) {
				int j = MathHelper.floor(this.posY + (double) (((float) ((i) % 2) - 0.5F) * 0.1F) + (double) this.getEyeHeight());
				int k = MathHelper.floor(this.posX + (double) (((float) ((i >> 1) % 2) - 0.5F) * this.width * 0.8F));
				int l = MathHelper.floor(this.posZ + (double) (((float) ((i >> 2) % 2) - 0.5F) * this.width * 0.8F));

				if(pos.getX() != k || pos.getY() != j || pos.getZ() != l) {
					pos.setPos(k, j, l);

					if(this.world.getBlockState(pos).causesSuffocation() && world.getBlockState(pos).getMaterial() != IPMMaterial.MONOLITH) {
						pos.release();
						return true;
					}
				}
			}
			pos.release();
			return false;
		}
	}

	private void setTargetedEntity(boolean hasTarget) {
		if(!hasTarget || getAttackTarget() == null) {
			dataManager.set(TARGET, Optional.absent());
		} else {
			//noinspection Guava
			dataManager.set(TARGET, Optional.of(getAttackTarget().getUniqueID()));
		}
		dataManager.setDirty(TARGET);
	}

	@Override
	public boolean canEntityBeSeen(Entity entityIn) {
		return hasTargetedEntity() ? RayTraceHelper.rayTraceBlocksExcept(world, Vector3.fromEntityCenter(this), Vector3.fromEntityCenter(entityIn), b -> b.getMaterial() == IPMMaterial.MONOLITH) == null : super.canEntityBeSeen(entityIn);
	}

	public boolean hasTargetedEntity() {
		return getTargetedEntity() != null;
	}

	@Nullable
	public Entity getTargetedEntity() {
		return NBTHelper.getEntityByUUID(Entity.class, dataManager.get(TARGET).orNull(), world).orElse(null);
	}

	@Override
	protected void initEntityAI() {
		EntityAIBase attack = new EyeAIAttack(this);
		this.tasks.addTask(1, new EyeAIRunAway(this, attack));
		this.tasks.addTask(2, attack);
		this.tasks.addTask(3, new EntityAIWanderAvoidWaterFlying(this, 1D));
		this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(5, new EntityAISwimming(this));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, Entity.class, 8));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class, 2, true, false, new AITargetSelector()));
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();
		if(!this.hasHome()) {
			this.setHomePosAndDistance(new BlockPos(this), 5);
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
	}

	@Override
	protected PathNavigate createNavigator(World worldIn) {
		return new FlightPathNavigate(this, worldIn);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return ModGen.SCHRODINGER_LOOT;
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	public void setNoGravity(boolean noGravity) {
		//Yoink!
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
	}

	@Override
	public int getMaxFallHeight() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return Collections.emptyList();
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
		//Yoink!
	}

	@Override
	public EnumHandSide getPrimaryHand() {
		return EnumHandSide.RIGHT;
	}

	@Override
	public float getEyeHeight() {
		return height / 2;
	}

	@Override
	public int getVerticalFaceSpeed() {
		return 360;
	}

	@Override
	public int getHorizontalFaceSpeed() {
		return 360;
	}

	private static class AITargetSelector implements Predicate<Entity> {

		@Override
		public boolean apply(@Nullable Entity entity) {
			return entity instanceof EntityPlayer || (entity instanceof EntityMob && !(entity instanceof EntityEyeOfSchrodinger));
		}
	}

	private static class EyeAIRunAway extends EntityAIHurtByTarget {

		private final EntityAIBase attack;

		EyeAIRunAway(EntityCreature creatureIn, EntityAIBase attack) {
			super(creatureIn, true);
			this.attack = attack;
		}

		@Override
		public void startExecuting() {
			super.startExecuting();
			runForYourLife();
		}

		private void runForYourLife() {
			if(taskOwner.getAttackTarget() != null) {
				Vec3d vec = RandomPositionGenerator.findRandomTarget(taskOwner, 5, 5);
				if(vec != null) {
					taskOwner.getNavigator().clearPath();
					taskOwner.getNavigator().tryMoveToXYZ(vec.x, vec.y, vec.z, 1D);
					attack.resetTask();
				}
			}
			taskOwner.setRevengeTarget(null);
		}

		@Override
		public boolean shouldContinueExecuting() {
			return false;
		}
	}

	private static class EyeAIAttack extends EntityAIBase {

		private final EntityEyeOfSchrodinger eye;
		private int tickCounter;

		EyeAIAttack(EntityEyeOfSchrodinger eye) {
			this.eye = eye;
			this.setMutexBits(1);
		}

		@Override
		public boolean shouldExecute() {
			EntityLivingBase living = eye.getAttackTarget();
			return living != null && living.isEntityAlive();
		}

		@Override
		@SuppressWarnings("ConstantConditions")
		public void startExecuting() {
			if(eye.getDistance(eye.getAttackTarget()) >= 5) {
				eye.getNavigator().clearPath();
				eye.getNavigator().tryMoveToEntityLiving(eye.getAttackTarget(), 0.25D);
			}
			eye.setTargetedEntity(true);
			eye.isAirBorne = true;
			tickCounter = 0;
		}

		public void resetTask() {
			eye.setTargetedEntity(false);
			eye.setAttackTarget(null);
		}

		@Override
		@SuppressWarnings("ConstantConditions")
		public void updateTask() {
			EntityLivingBase target = eye.getAttackTarget();

			if(!eye.canEntityBeSeen(target)) {
				eye.setAttackTarget(null);
			} else if(tickCounter++ >= 10) {
				float f = 1.0F;

				if(eye.world.getDifficulty() == EnumDifficulty.HARD) {
					f += 2.0F;
				}

				target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(eye, eye), f);
				target.attackEntityFrom(DamageSource.causeMobDamage(eye), (float) eye.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
				eye.setAttackTarget(null);
			}
			eye.getLookHelper().setLookPosition(target.posX, target.posY + (double) target.getEyeHeight(), target.posZ, (float) eye.getHorizontalFaceSpeed(), (float) eye.getVerticalFaceSpeed());
		}
	}
}
