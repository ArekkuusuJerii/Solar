/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.entity;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.common.network.PacketHandler;
import arekkuusu.solar.common.network.QQuartzEffectMessage;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 21/09/2017.
 * It's distributed as part of Solar.
 */
public class EntityQuantumQuartzItem extends EntityFastItem {

	private Vec3d vec;

	public EntityQuantumQuartzItem(EntityItem item) {
		super(item);
		vec = item.getPositionVector();
		setNoDespawn();
	}

	public EntityQuantumQuartzItem(World worldIn) {
		super(worldIn);
		setNoDespawn();
	}

	@Override
	public void updateLogic() {
		super.updateLogic();
		if(!world.isRemote && rand.nextInt(100) == 0) {
			Vector3 from = new Vector3(posX, posY, posZ);
			Vector3 to = Vector3.getRandomVec(4).add(from);

			BlockPos pos = to.toBlockPos();
			if(!getPosition().equals(pos) && world.isAirBlock(pos) && vec.distanceTo(to.toVec3d()) <= 15) {
				setPositionAndUpdate(to.x, to.y, to.z);
				playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, 1F, 1F);
				//Send teleport effect to clients
				QQuartzEffectMessage effect = new QQuartzEffectMessage(from.add(0, 0.2D, 0), to.add(0, 0.2D, 0));
				PacketHandler.sendToAllAround(effect, PacketHandler.fromWorldPos(world, pos, 25));
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if(vec != null) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setDouble("vec_x", vec.x);
			tag.setDouble("vec_y", vec.y);
			tag.setDouble("vec_z", vec.z);

			compound.setTag("vec", tag);
		}
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if(compound.hasKey("vec")) {
			NBTTagCompound tag = compound.getCompoundTag("vec");
			vec = new Vec3d(tag.getDouble("vec_x"), tag.getDouble("vec_y"), tag.getDouble("vec_z"));
		}
		super.readFromNBT(compound);
	}
}
